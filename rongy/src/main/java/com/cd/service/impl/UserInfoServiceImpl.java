package com.cd.service.impl;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.dao.*;
import com.cd.entity.*;
import com.cd.entity.dto.UserLoginDTO;
import com.cd.entity.vo.*;
import com.cd.enums.ErrorCodeEnum;
import com.cd.enums.RongyDictEnum;
import com.cd.exception.BusiException;
import com.cd.service.UserInfoService;
import com.cd.utils.SecurityUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.webkit.dom.CSSPageRuleImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Filter;

import static cn.hutool.db.sql.Direction.DESC;


@Service
public class UserInfoServiceImpl implements UserInfoService {
    private Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserRelationshipMapper userRelationshipMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MarketGoodsInfoMapper marketGoodsInfoMapper;

    @Autowired
    private RoleMapper roleMapper;


    /**
     * 根据用户编号查询用户信息
     * @param userNo
     * @return
     */
    @Override
    public RestResult getByUserNo(String userNo) {
        logger.info("根据用户编号查询用户信息开始，用户编号[{}]", userNo);
        RestResult restResult = null;
        UserInfo userInfo = null;
        try {
            userInfo = userInfoMapper.selectByUserNo(userNo);
            userInfo.setTelephone(SecurityUtil.phoneNumDecrypt(userInfo.getTelephone()));

            List<UserRelationship> userRelationships = userRelationshipMapper.selectByUserNo(userNo);

            int followers = 0;
            int followings = 0;
            if (userRelationships != null) {
                for (UserRelationship ur : userRelationships) {
                    if (StringUtils.equals(ur.getFollowedUserId(), userNo)) {
                        followings++;
                    } else {
                        followers++;
                    }
                }
            }

            // 发帖量+发布物品量
            int posts = articleMapper.countByUserNo(userNo);
            List<MarketGoodsInfo> marketGoodsInfos = marketGoodsInfoMapper.selectByUserNo(userNo);
            if (marketGoodsInfos != null) {
                posts = posts + marketGoodsInfos.size();
            }
            MyOperationVO myOperation = new MyOperationVO(posts, followings, followers);

            int unReadCount = commentMapper.countReadByUserNo(userNo);

            UserInfoVO userInfoVO = new UserInfoVO(userInfo, myOperation, unReadCount);

            restResult = ResultUtil.createSuccessResult(userInfoVO);
        } catch (Exception e) {
            logger.error("根据用户编号[{}]查询用户信息失败", userNo, e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * 根据用户编号查询用户信息
     * @param userNo
     * @return
     */
    @Override
    public RestResult getByOtherUserNo(String userNo) {
        logger.info("根据他人编号查询用户信息开始，用户编号[{}]", userNo);
        RestResult restResult = null;
        UserInfo userInfo = null;
        try {
            userInfo = userInfoMapper.selectByUserNo(userNo);
            if (userInfo != null) {
                userInfo.setTelephone(SecurityUtil.phoneNumDecrypt(userInfo.getTelephone()));
            }


            List<UserRelationship> userRelationships = userRelationshipMapper.selectByUserNo(userNo);

            int followers = 0;
            int followings = 0;
            for (UserRelationship ur : userRelationships) {
                if (StringUtils.equals(ur.getFollowedUserId(), userNo)) {
                    followings++;
                } else {
                    followers++;
                }
            }

            int posts = articleMapper.countByOtherUserNo(userNo);

            MyOperationVO myOperation = new MyOperationVO(posts, followings, followers);

            int unReadCount = commentMapper.countReadByUserNo(userNo);

            UserInfoVO userInfoVO = new UserInfoVO(userInfo, myOperation, unReadCount);

            restResult = ResultUtil.createSuccessResult(userInfoVO);
        } catch (Exception e) {
            logger.error("根据用户编号[{}]查询用户信息失败", userNo, e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }



    /**
     * 根据用户id修改用户信息
     * @param userInfo
     * @return
     */
    @Override
    public RestResult updateById(UserInfo userInfo) {
        logger.info("更新用户信息开始，用户id：{}", userInfo.getId());
        // 手动控制事务
        RestResult restResult = null;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(userInfo.getTelephone()));
            int i = userInfoMapper.updateByPrimaryKeySelective(userInfo);
            if (i != 1) {
                logger.error("用户信息更新失败！用户信息：{}", userInfo);
                restResult = ResultUtil.createFailedResult();
            } else {
                logger.info("更新用户信息成功，用户信息：{}", userInfo);
                restResult = ResultUtil.createSuccessResult(i);
            }
            txManager.commit(status);
        } catch (Throwable e) {
            logger.error("用户信息出现异常！", e);
            txManager.rollback(status);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * 根据用户编号修改用户信息
     * @param userInfo
     * @return
     */
    @Override
    public RestResult updateByUserNo(UserInfo userInfo) {
        logger.info("更新用户信息开始，用户编号：{}", userInfo.getUserNo());
        // 手动控制事务
        RestResult restResult = null;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(userInfo.getTelephone()));
            int i = userInfoMapper.updateByUserNoSelective(userInfo);
            if (i != 1) {
                logger.error("用户信息更新失败！用户信息：{}", userInfo);
                restResult = ResultUtil.createFailedResult();
            } else {
                logger.info("更新用户信息成功，用户信息：{}", userInfo);
                restResult = ResultUtil.createSuccessResult(i);
            }
            txManager.commit(status);
        } catch (Throwable e) {
            logger.error("用户信息出现异常！", e);
            txManager.rollback(status);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * 根据圈子id修改用户信息，若为零，默认查询所有
     * @param circleId
     * @return
     */
    @Override
    public RestResult getByCircleId(Integer circleId) {
        RestResult restResult = null;
        List<UserInfo> userInfos = null;
        logger.info("根据圈子id查询用户信息开始，圈子id：{}", circleId);
        try {
            if (circleId == 0) {
                logger.info("圈子id为0，查询所有用户");
                userInfos = userInfoMapper.selectAll();
            } else {
                userInfos = userInfoMapper.selectByCircleId(circleId);
            }
            if (userInfos != null) {
                for (int i = 0; i < userInfos.size(); i++) {
                    userInfos.get(i).setTelephone(SecurityUtil.phoneNumDecrypt(userInfos.get(i).getTelephone()));
                }
            }
            restResult = ResultUtil.createSuccessResult(userInfos);
        } catch (Exception e) {
            logger.error("根据圈子id查询用户信息异常", e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * 根据参数模糊匹配用户名字进行查询
     * @param nameLike
     * @return
     */
    @Override
    public RestResult getByNameLike(String userNo, String nameLike) {
        logger.info("根据参数模糊匹配用户名字，参数为[{}]", nameLike);
        RestResult restResult = null;
        List<UserInfo> userInfos = null;
        try {
            userInfos = userInfoMapper.selectByNameLike(nameLike);
            // 查询我的信息
            UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
            // 判断与我的关系
            for (int i = 0; i < userInfos.size(); i++) {
                // 判断他对我的关注
                UserRelationship userRelationship1 = userRelationshipMapper.selectByBothId(userInfo.getUserNo(), userInfos.get(i).getUserNo());
                if (userRelationship1 != null) {
                    userInfos.get(i).setStatusToMe("1");
                } else {
                    userInfos.get(i).setStatusToMe("0");
                }

                // 判断我对他的关注
                UserRelationship userRelationship2 = userRelationshipMapper.selectByBothId(userInfos.get(i).getUserNo(), userInfo.getUserNo());
                if (userRelationship2 != null) {
                    userInfos.get(i).setStatus("1");
                } else {
                    userInfos.get(i).setStatus("0");
                }

            }
            if (userInfos != null) {
                for (int i = 0; i < userInfos.size(); i++) {
                    userInfos.get(i).setTelephone(SecurityUtil.phoneNumDecrypt(userInfos.get(i).getTelephone()));
                }
            }
            restResult = ResultUtil.createSuccessResult(userInfos);
        } catch (Exception e) {
            logger.error("根据用户编号[{}]查询用户信息失败", nameLike, e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    @Override
    public UserLoginVO userLogin(UserLoginDTO userLogin) {
        /** 1. 根据 openId 获取用户*/
        UserInfo userInfo = userInfoMapper.selectByOpenId(userLogin.getOpenId());

        /** 2. 根据 openId 获取不到就使用 phone + name 匹配*/
        if (userInfo == null) {
            userInfo = userInfoMapper.selectByNameAndPhone(userLogin.getName(), SecurityUtil.phoneNumEncrypt(userLogin.getUserNo()));
        }

        // 没登录过 或 账号密码错误
        if (userInfo == null) return new UserLoginVO(null, RongyDictEnum.USER_LOGIN_FLAG$FALSE.getCode());

        // 登陆过 或 账号密码正确
        UserInfo forUpdate = new UserInfo();

        // 更新最新的微信信息
        forUpdate.setId(userInfo.getId());
        forUpdate.setOpenId(userLogin.getOpenId());
        forUpdate.setUserNo(userLogin.getOpenId());
        if (StringUtils.isNotEmpty(userLogin.getAvatar())) {
            forUpdate.setImageUrl(userLogin.getAvatar());
        }

        if (StringUtils.isNotEmpty(userLogin.getWxName())) {
            forUpdate.setWechatName(userLogin.getWxName());
        }

        if (StringUtils.isNotEmpty(userLogin.getSex())) {
            forUpdate.setSex(userLogin.getSex());
        }

        userInfoMapper.updateByPrimaryKeySelective(forUpdate);

        // 返回全量信息
        userInfo = userInfoMapper.selectByOpenId(userLogin.getOpenId());
        userInfo.setTelephone(SecurityUtil.phoneNumDecrypt(userInfo.getTelephone()));

        return new UserLoginVO(userInfo, RongyDictEnum.USER_LOGIN_FLAG$TRUE.getCode());
    }

    @Override
    public void readAll(String userNo) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);

        if (userInfo == null) {
            throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);
        }
        userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(userInfo.getTelephone()));
        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) {
            throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);
        }

        commentMapper.readAll(userNo);
    }

    @Override
    public UserInfo getByNameAndPhone(String name, String phone) {
        return userInfoMapper.selectByNameAndPhone(name, SecurityUtil.phoneNumEncrypt(phone));
    }

    @Override
    public List<UserInfo> getAllUser(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum == 0 ? 10 : pageNum, pageSize == 0 ? 1 : pageSize);

        List<UserInfo> userInfos = userInfoMapper.selectAll();
        PageInfo<UserInfo> userInfoPageInfo = new PageInfo<>(userInfos);
        return userInfoPageInfo.getList();
    }

    @Override
    public RankingListVOs getRankingList(String userNo) {
        logger.info("查询排行榜开始，当前用户编号[{}]", userNo);
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) {
            return null;
        }

        List<UserInfo> userInfos = userInfoMapper.selectAll();  // 获得所有的用户信息
        List<RankingList> rankingList = articleMapper.selectAritclesAndUserNoList();    // 获取发帖排行

        List<RankingListVO> recordList = new ArrayList<>();
        int currentRanking = 1;    // 排名
        int userCurrentRanking = 0; // 当前用户排名
        if (rankingList != null) {
            for (int i = 0; i < rankingList.size(); i++) {
                UserInfo getUserInfo = getAndRemoveFromList(userInfos, rankingList.get(i).getUserNo().trim());
                if (getUserInfo == null) {
                    continue;
                }
                if (StringUtils.equals(getUserInfo.getUserNo().trim(), userInfo.getUserNo().trim())) {
                    userCurrentRanking = currentRanking;
                }
                recordList.add(new RankingListVO(getUserInfo, (rankingList.get(i).getArticleCount()) * 10, currentRanking));
                currentRanking++;
            }
            for (int i = 0; i < userInfos.size(); i++) {
                if (StringUtils.equals(userInfos.get(i).getUserNo().trim(), userInfo.getUserNo().trim())) {
                    userCurrentRanking = currentRanking;
                }
                recordList.add(new RankingListVO(userInfos.get(i), 0, currentRanking));
                currentRanking++;
            }
        }

        // 个人发帖量
        RankingListVO record = new RankingListVO();
        RankingList select = articleMapper.selectAritclesAndUserNoListByUserNo(userNo);


        if (select != null) {
            record.setArticleCount(select.getArticleCount() * 10);
            record.setUserInfo(userInfo);
            record.setCurrentRanking(userCurrentRanking);
        } else {
            record.setArticleCount(0);
            record.setUserInfo(userInfo);
            record.setCurrentRanking(userCurrentRanking);
        }

        RankingListVOs rankingListVOs = new RankingListVOs();
        rankingListVOs.setCurrentUser(record);
        rankingListVOs.setRankingList(recordList);
        return rankingListVOs;
    }


    private UserInfo getAndRemoveFromList(List<UserInfo> userInfos, String userNo) {
        for (int i = 0; i < userInfos.size(); i++) {
            if (StringUtils.equals(userInfos.get(i).getUserNo().trim(), userNo.trim())) {
                UserInfo userInfo = userInfos.get(i);
                userInfos.remove(i);
                return userInfo;
            }
        }
        return null;
    }


    // 批量导入
    @Override
    public void insertByExcel(MultipartFile file) throws Exception {
        if (file == null) {
            throw new RuntimeException("文件未上传成功！");
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("文件不能为空！");
        }
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        if (!prefix.toLowerCase().contains("xls") && !prefix.toLowerCase().contains("xlsx")) {
            throw new RuntimeException("文件格式异常，请上传Excel文件格式！");
        }
        // 防止生成的临时文件重复-建议使用UUID
        final File excelFile = File.createTempFile(System.currentTimeMillis() + "", prefix);
        file.transferTo(excelFile);
        //由于2003和2007的版本所使用的接口不一样，所以这里统一用Workbook做兼容
        boolean isExcel2003 = prefix.toLowerCase().endsWith("xls") ? true : false;
        Workbook workbook = null;
        if (isExcel2003) {
            workbook = new HSSFWorkbook(new FileInputStream(excelFile));
        } else {
            workbook = new XSSFWorkbook(new FileInputStream(excelFile));
        }
        //Excel表中的内容
        List<Map<String, String>> list = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        //这里重1开始，跳过了标题，直接从第二行开始解析
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            //设置行格式和验证start 这里最好做成一个方法，免得代码多处复制
            if (row.getCell(0) != null) {
                row.getCell(0).setCellType(CellType.STRING);
            }
            if (row.getCell(1) != null) {
                row.getCell(1).setCellType(CellType.STRING);
            }
            if (row.getCell(2) != null) {
                row.getCell(2).setCellType(CellType.STRING);
            }
            String userName = row.getCell(0).getStringCellValue(); //姓名
            if (StringUtils.isEmpty(userName)) {
                throw new RuntimeException("第" + i + "行，第1列不能为空!");
            }
            String telephone = row.getCell(1).getStringCellValue();//手机号
            if (StringUtils.isEmpty(telephone)) {
                throw new RuntimeException("第" + i + "行，第2列不能为空!");
            }
            String sex = row.getCell(2).getStringCellValue();// 性别
            if (StringUtils.isEmpty(sex)) {
                throw new RuntimeException("第" + i + "行，第3列不能为空!");
            }
            //设置行格式和验证end
            // 组装列表
            Map<String, String> map = new HashMap<>();
            map.put("userName", userName);
            map.put("telephone", telephone);
            map.put("sex", sex);
            list.add(map);
        }
        //删除临时转换的文件
        if (excelFile.exists()) {
            excelFile.delete();
        }

        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            //信息导入
            for (int i = 0; i < list.size(); i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserNo("blank");
                userInfo.setName(list.get(i).get("userName"));
                userInfo.setWechatName("");
                userInfo.setSex(list.get(i).get("sex"));
                userInfo.setBirthday("");
                userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(list.get(i).get("telephone")));
                userInfo.setIntroduction("暂无简介");
                userInfo.setImageUrl("");
                userInfo.setStatus("1");
                userInfo.setAddress("");
                userInfo.setCreatedTime(new Date());
                userInfo.setUpdatedTime(new Date());
                userInfo.setOpenId("");
                userInfo.setRoleId("0");
                userInfoMapper.insertSelective(userInfo);
            }
            txManager.commit(status);
        } catch (Throwable e) {
            txManager.rollback(status);
            logger.error("导入用户信息失败，文件名：{}", fileName);
        }
    }

    @Override
    public UserInfo getUserInfo(String userNo) {
        return userInfoMapper.selectByUserNo(userNo);
    }

    @Override
    public void addSingle(UserInfo userInfo) {
        userInfo.setCreatedTime(new Date());
        userInfoMapper.insertSelective(userInfo);
    }

    @Override
    public Role roleValid(String userNo, String roleType) {
        Role role = roleMapper.selectByUserNoAndRoleId(userNo, roleType);
        return role;
    }

    @Override
    public UserInfo userInfoValidate(String userNo) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);
        return userInfo;
    }
}
