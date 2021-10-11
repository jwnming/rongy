package com.cd;

import com.cd.common.RestResult;
import com.cd.dao.UserInfoMapper;
import com.cd.entity.UserInfo;
import com.cd.service.UserInfoService;
import com.cd.utils.CommonUtil;
import com.cd.utils.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import junit.framework.TestCase;
import org.apache.commons.lang3.time.DateUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserInfoTests {
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    // 更新
    @Test
    void contextLoads1() {
        /*UserInfo byUserNo = userInfoService.getByUserNo("123456");
        System.out.println(byUserNo);*/
        UserInfo userInfo = new UserInfo();
        //userInfo.setId(100);
        userInfo.setUserNo("123456");
        userInfo.setAddress("四川成都1");
        RestResult restResult = userInfoService.updateByUserNo(userInfo);
        System.out.println(restResult.getHeader().getCode());

    }

    // 查询
    @Test
    void contextLoads2() {
//        RestResult byUserNo = userInfoService.getByUserNo("123457");
//        System.out.println(byUserNo.getHeader());
//        System.out.println(byUserNo.getData().toString());
        PageHelper.startPage(1, 10);

        List<UserInfo> userInfos = userInfoMapper.selectAll();
        PageInfo<UserInfo> userInfoPageInfo = new PageInfo<>(userInfos);
        userInfoPageInfo.getList().forEach(x -> {
            System.out.println(x.getName());
        });
    }

    // 根据圈子id查询用户
    @Test
    void contextLoads() {
        RestResult byCircleId = userInfoService.getByCircleId(1002);
        System.out.println(byCircleId.getHeader());
        System.out.println(byCircleId.getData().toString());
    }

    // 模糊查询
    @Test
    void contextLoads4() {
        RestResult byUserNo = userInfoService.getByNameLike("123456","t");
        System.out.println(byUserNo.getHeader());
        System.out.println(byUserNo.getData().toString());
    }

    // 信息导入
    @Test
    void testInsert() {
        /**
         * 文件内容格式：姓名 手机号
         */
        File file = new File("D:/user_info.txt");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

        List<UserInfo> list = new ArrayList<UserInfo>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr = "";
            int i =0;
            while ((tempStr = reader.readLine()) != null ) {
                // 分割字符串
                String[] s = tempStr.split("\t");
                UserInfo userInfo = new UserInfo();
                userInfo.setUserNo("user" + i);
                userInfo.setName(s[0]);
                userInfo.setWechatName("");
                userInfo.setSex("2");
                userInfo.setBirthday("");
                userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(s[1].replace("'", "")));
                userInfo.setIntroduction("暂无简介");
                userInfo.setImageUrl("");
                userInfo.setStatus("1");
                userInfo.setAddress("");
                userInfo.setCreatedTime(new Date());
                userInfo.setUpdatedTime(new Date());
                userInfo.setOpenId("");
                userInfo.setRoleId("0");

                i++;
                boolean isExist = false;
                for (int j = 0; j <list.size(); j++) {
                    if (list.get(j).getTelephone().equals(userInfo.getTelephone())) {
                        System.out.println("出现重复:" + userInfo.getName());
                        isExist = true;
                        continue;
                    }
                }
                if (!isExist) {
                    list.add(userInfo);
                    userInfoMapper.insertSelective(userInfo);
                }
                System.out.println("成功：" + userInfo.toString());

            }

            txManager.commit(status);
        } catch (IOException e) {
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
        System.out.println(list.size());
    }

    // 信息导入
    @Test
    void testInser07() {
        /**
         * 文件内容格式：姓名 手机号 邮箱 性别
         */
        File file = new File("D:/info.txt");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr = "";
            while ((tempStr = reader.readLine()) != null ) {
                // 分割字符串
                String[] s = tempStr.split(" ");
                UserInfo userInfo = new UserInfo();
                userInfo.setUserNo((s[2].split("@"))[0]);
                userInfo.setName(s[0]);
                userInfo.setWechatName("");
                userInfo.setSex(s[3]);
                userInfo.setBirthday("");
                userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(s[1]));
                userInfo.setIntroduction("暂无简介");
                userInfo.setImageUrl("");
                userInfo.setStatus("1");
                userInfo.setAddress("");
                userInfo.setCreatedTime(new Date());
                userInfo.setUpdatedTime(new Date());
                userInfo.setOpenId("");
                userInfo.setRoleId("0");
                userInfoMapper.insertSelective(userInfo);
                System.out.println("成功：" + userInfo.getName());
            }

            txManager.commit(status);
        } catch (IOException e) {
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



    private void  test95() {




    }












}
