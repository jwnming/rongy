package com.cd;

import com.cd.dao.UserInfoExMapper;
import com.cd.dao.UserInfoMapper;
import com.cd.entity.UserInfo;
import com.cd.entity.UserInfoEx;

import com.cd.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserInfoOptTests {
    @Autowired
    UserInfoExMapper userInfoExMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    // 信息核对
    @Test
    void testInsert() {
        Charset utf8 = StandardCharsets.UTF_8;

        List<UserInfo> userInfos = userInfoMapper.selectAll();
        /**
         * 文件内容格式：
         */
        File file = new File("D:/info-all.txt");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        int x = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr = "";
            while ((tempStr = reader.readLine()) != null) {
                x++;
                // 分割字符串
                String[] s = tempStr.split("\t");
                /*SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Date d1 = sdf2.parse(s[1]);*/
                UserInfoEx userInfoEx = new UserInfoEx();
                for (int i = 0; i < userInfos.size(); i++) {
                    if (StringUtils.equals(userInfos.get(i).getName().trim(), s[0].trim())) {
                        userInfoEx.setUserInfoId(userInfos.get(i).getId());
                    }
                }
                if (userInfoEx.getUserInfoId() == null) {
                    System.out.println("没找到该用户, 姓名：" + s[0]);
                    continue;
                }

            }
            System.out.println("成功：共" + x + "条");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // 用户信息导入
    @Test
    void testInsert2() {
        File file = new File("D:/user_info.txt");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        int x = 1;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            // 查询所有
            List<UserInfo> userInfos = userInfoMapper.selectAll();
            reader = new BufferedReader(new FileReader(file));
            String tempStr = "";

            int has = 0;
            int news = 0;
            while ((tempStr = reader.readLine()) != null) {
                System.out.println("处理行：" + x++);
                // 分割字符串
                String[] s = tempStr.split("\t");
                UserInfo userInfo = new UserInfo();
                userInfo.setName(s[0]);
                userInfo.setSex("0");
                userInfo.setUserNo("userNo" + x);
                String t = s[1].replace("\"", "");
                userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(t));
                //userInfo.setBirthday("");
                userInfo.setIntroduction("暂无简介");
                userInfo.setImageUrl("http://img.yizicheng.cn/rongyu/niming.gif");
                userInfo.setStatus("1");
                //userInfo.setAddress("");
                //userInfo.setOpenId("");
                userInfo.setCreatedTime(new Date());
                userInfo.setUpdatedTime(new Date());
                boolean is = false;
                for (int i = 0; i < userInfos.size(); i++) {
                    if ((userInfos.get(i).getName()).equals(s[0]) && (userInfos.get(i).getTelephone()).equals(SecurityUtil.phoneNumEncrypt(s[1]))) {
                        has++;
                        is = true;
                        break;
                    }
                }
                if (!is) {
                    news++;
                    userInfoMapper.insertSelective(userInfo);
                }


            }
            System.out.println("存在：" + has);
            System.out.println("新数据：" + news);

            txManager.commit(status);
            System.out.println("处理成功：共" + (x - 1) + "条");
        } catch (Exception e) {
            txManager.rollback(status);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // 入职时间导入
    @Test
    void testInsert00() {
        File file = new File("D:/user_work.txt");
        BufferedReader reader = null;
        int x = 1;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            // 查询所有
            List<UserInfo> userInfos = userInfoMapper.selectAll();
            List<UserInfoEx> userInfoExes = userInfoExMapper.selectAllInfoEx();
            reader = new BufferedReader(new FileReader(file));
            String tempStr = "";

            int has = 0;
            int news = 0;
            while ((tempStr = reader.readLine()) != null) {
                System.out.println("处理行：" + x++);
                // 分割字符串
                tempStr = tempStr.replace("\"", "");
                String[] s = tempStr.split("\t");

                UserInfoEx userInfoEx = new UserInfoEx();
                String entryTime = s[1];
                entryTime = entryTime.replace("年", "-");
                entryTime = entryTime.replace("月", "-");
                entryTime = entryTime.replace("日", "");


                userInfoEx.setEntryDate(entryTime);
                userInfoEx.setCreateTime(new Date());
                userInfoEx.setUpdateTime(new Date());
                userInfoEx.setDeleteFlag("0");


                boolean isExist = false;
                for (int i = 0; i < userInfos.size(); i++) {
                    UserInfo currentUs = userInfos.get(i);
                    if (currentUs.getId() > 434) {
                        if (currentUs.getName().equals(s[0]) && (currentUs.getTelephone()).equals(SecurityUtil.phoneNumEncrypt(s[2]))) {
                            news++;
                            userInfoEx.setUserInfoId(currentUs.getId());
                            userInfoExMapper.insertSelective(userInfoEx);
                            break;
                        }
                    }
                }
            }
            System.out.println("新的：" + news);

            txManager.commit(status);
            System.out.println("处理成功：共" + (x - 1) + "条");
        } catch (Exception e) {
            txManager.rollback(status);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }



    // 入职时间信息导入（旧）
    @Test
    void testInsert1() {
        List<UserInfo> userInfos = userInfoMapper.selectAll();
        /**
         * 文件内容格式：
         */
        File file = new File("D:/info.txt");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        int x = 0;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr = "";
            while ((tempStr = reader.readLine()) != null) {
                x++;
                // 分割字符串
                String[] s = tempStr.split("\t");
//                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//                Date d1 = sdf2.parse(s[1]);
                UserInfoEx userInfoEx = new UserInfoEx();
                for (int i = 0; i < userInfos.size(); i++) {
                    if (StringUtils.equals(userInfos.get(i).getName().trim(), s[0].trim())) {
                        userInfoEx.setUserInfoId(userInfos.get(i).getId());
                    }
                }
                if (userInfoEx.getUserInfoId() == null) {
                    System.out.println("没找到该用户, 姓名：" + s[0]);
                    continue;
                }
                userInfoEx.setEntryDate(s[1]);
                userInfoEx.setCreateTime(new Date());
                userInfoEx.setUpdateTime(new Date());
                userInfoEx.setDeleteFlag("0");
                userInfoExMapper.insertSelective(userInfoEx);

            }
            txManager.commit(status);
            System.out.println("成功：共" + x + "条");
        } catch (Exception e) {
            txManager.rollback(status);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // 新用户信息导入
    @Test
    void testInsert33() {
        File file = new File("D:/user_info.txt");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        int x = 1;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            // 查询所有
            List<UserInfo> userInfos = userInfoMapper.selectAll();
            reader = new BufferedReader(new FileReader(file));
            String tempStr = "";

            int has = 0;
            int news = 0;
            while ((tempStr = reader.readLine()) != null) {
                System.out.println("处理行：" + x++);
                // 分割字符串
                String[] s = tempStr.split("\t");
                UserInfo userInfo = new UserInfo();
                userInfo.setName(s[0]);
                userInfo.setSex("0");
                userInfo.setUserNo("userNo" + x);
                String t = s[1].replace("\"", "");
                userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(t));
                //userInfo.setBirthday("");
                userInfo.setIntroduction("暂无简介");
                userInfo.setImageUrl("http://img.yizicheng.cn/rongyu/niming.gif");
                userInfo.setStatus("1");
                //userInfo.setAddress("");
                //userInfo.setOpenId("");
                userInfo.setCreatedTime(new Date());
                userInfo.setUpdatedTime(new Date());
                userInfoMapper.insertSelective(userInfo);
            }
            txManager.commit(status);
            System.out.println("处理成功：共" + (x - 1) + "条");
        } catch (Exception e) {
            txManager.rollback(status);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    // 入职时间导入-薪新增
    @Test
    void testInsert23() {
        File file = new File("D:/user_work.txt");
        BufferedReader reader = null;
        int x = 1;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            List<UserInfo> userInfos = userInfoMapper.selectAll();
            // 查询所有
            reader = new BufferedReader(new FileReader(file));
            String tempStr = "";
            while ((tempStr = reader.readLine()) != null) {
                System.out.println("处理行：" + x++);
                // 分割字符串
                tempStr = tempStr.replace("\"", "");
                String[] s = tempStr.split("\t");

                UserInfoEx userInfoEx = new UserInfoEx();
                String entryTime = s[1];
                entryTime = entryTime.replace("年", "-");
                entryTime = entryTime.replace("月", "-");
                entryTime = entryTime.replace("日", "");

                userInfoEx.setEntryDate(entryTime);
                userInfoEx.setCreateTime(new Date());
                userInfoEx.setUpdateTime(new Date());
                userInfoEx.setDeleteFlag("0");

                for (int i = 0; i < userInfos.size(); i++) {
                    UserInfo currentUs = userInfos.get(i);
                    if (currentUs.getName().equals(s[0]) && (currentUs.getTelephone()).equals(SecurityUtil.phoneNumEncrypt(s[2]))) {
                        userInfoEx.setUserInfoId(currentUs.getId());
                        userInfoExMapper.insertSelective(userInfoEx);
                        break;
                    }
                }
            }
            txManager.commit(status);
            System.out.println("处理成功：共" + (x - 1) + "条");
        } catch (Exception e) {
            txManager.rollback(status);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
