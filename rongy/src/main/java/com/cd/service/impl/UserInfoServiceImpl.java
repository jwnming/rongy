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
     * ????????????????????????????????????
     * @param userNo
     * @return
     */
    @Override
    public RestResult getByUserNo(String userNo) {
        logger.info("?????????????????????????????????????????????????????????[{}]", userNo);
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

            // ?????????+???????????????
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
            logger.error("??????????????????[{}]????????????????????????", userNo, e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * ????????????????????????????????????
     * @param userNo
     * @return
     */
    @Override
    public RestResult getByOtherUserNo(String userNo) {
        logger.info("?????????????????????????????????????????????????????????[{}]", userNo);
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
            logger.error("??????????????????[{}]????????????????????????", userNo, e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }



    /**
     * ????????????id??????????????????
     * @param userInfo
     * @return
     */
    @Override
    public RestResult updateById(UserInfo userInfo) {
        logger.info("?????????????????????????????????id???{}", userInfo.getId());
        // ??????????????????
        RestResult restResult = null;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(userInfo.getTelephone()));
            int i = userInfoMapper.updateByPrimaryKeySelective(userInfo);
            if (i != 1) {
                logger.error("??????????????????????????????????????????{}", userInfo);
                restResult = ResultUtil.createFailedResult();
            } else {
                logger.info("??????????????????????????????????????????{}", userInfo);
                restResult = ResultUtil.createSuccessResult(i);
            }
            txManager.commit(status);
        } catch (Throwable e) {
            logger.error("???????????????????????????", e);
            txManager.rollback(status);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * ????????????????????????????????????
     * @param userInfo
     * @return
     */
    @Override
    public RestResult updateByUserNo(UserInfo userInfo) {
        logger.info("??????????????????????????????????????????{}", userInfo.getUserNo());
        // ??????????????????
        RestResult restResult = null;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(userInfo.getTelephone()));
            int i = userInfoMapper.updateByUserNoSelective(userInfo);
            if (i != 1) {
                logger.error("??????????????????????????????????????????{}", userInfo);
                restResult = ResultUtil.createFailedResult();
            } else {
                logger.info("??????????????????????????????????????????{}", userInfo);
                restResult = ResultUtil.createSuccessResult(i);
            }
            txManager.commit(status);
        } catch (Throwable e) {
            logger.error("???????????????????????????", e);
            txManager.rollback(status);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * ????????????id???????????????????????????????????????????????????
     * @param circleId
     * @return
     */
    @Override
    public RestResult getByCircleId(Integer circleId) {
        RestResult restResult = null;
        List<UserInfo> userInfos = null;
        logger.info("????????????id?????????????????????????????????id???{}", circleId);
        try {
            if (circleId == 0) {
                logger.info("??????id???0?????????????????????");
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
            logger.error("????????????id????????????????????????", e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * ????????????????????????????????????????????????
     * @param nameLike
     * @return
     */
    @Override
    public RestResult getByNameLike(String userNo, String nameLike) {
        logger.info("????????????????????????????????????????????????[{}]", nameLike);
        RestResult restResult = null;
        List<UserInfo> userInfos = null;
        try {
            userInfos = userInfoMapper.selectByNameLike(nameLike);
            // ??????????????????
            UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
            // ?????????????????????
            for (int i = 0; i < userInfos.size(); i++) {
                // ????????????????????????
                UserRelationship userRelationship1 = userRelationshipMapper.selectByBothId(userInfo.getUserNo(), userInfos.get(i).getUserNo());
                if (userRelationship1 != null) {
                    userInfos.get(i).setStatusToMe("1");
                } else {
                    userInfos.get(i).setStatusToMe("0");
                }

                // ????????????????????????
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
            logger.error("??????????????????[{}]????????????????????????", nameLike, e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    @Override
    public UserLoginVO userLogin(UserLoginDTO userLogin) {
        /** 1. ?????? openId ????????????*/
        UserInfo userInfo = userInfoMapper.selectByOpenId(userLogin.getOpenId());

        /** 2. ?????? openId ????????????????????? phone + name ??????*/
        if (userInfo == null) {
            userInfo = userInfoMapper.selectByNameAndPhone(userLogin.getName(), SecurityUtil.phoneNumEncrypt(userLogin.getUserNo()));
        }

        // ???????????? ??? ??????????????????
        if (userInfo == null) return new UserLoginVO(null, RongyDictEnum.USER_LOGIN_FLAG$FALSE.getCode());

        // ????????? ??? ??????????????????
        UserInfo forUpdate = new UserInfo();

        // ???????????????????????????
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

        // ??????????????????
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
        logger.info("??????????????????????????????????????????[{}]", userNo);
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) {
            return null;
        }

        List<UserInfo> userInfos = userInfoMapper.selectAll();  // ???????????????????????????
        List<RankingList> rankingList = articleMapper.selectAritclesAndUserNoList();    // ??????????????????

        List<RankingListVO> recordList = new ArrayList<>();
        int currentRanking = 1;    // ??????
        int userCurrentRanking = 0; // ??????????????????
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

        // ???????????????
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


    // ????????????
    @Override
    public void insertByExcel(MultipartFile file) throws Exception {
        if (file == null) {
            throw new RuntimeException("????????????????????????");
        }
        //???????????????
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("?????????????????????");
        }
        // ??????????????????
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        if (!prefix.toLowerCase().contains("xls") && !prefix.toLowerCase().contains("xlsx")) {
            throw new RuntimeException("??????????????????????????????Excel???????????????");
        }
        // ?????????????????????????????????-????????????UUID
        final File excelFile = File.createTempFile(System.currentTimeMillis() + "", prefix);
        file.transferTo(excelFile);
        //??????2003???2007????????????????????????????????????????????????????????????Workbook?????????
        boolean isExcel2003 = prefix.toLowerCase().endsWith("xls") ? true : false;
        Workbook workbook = null;
        if (isExcel2003) {
            workbook = new HSSFWorkbook(new FileInputStream(excelFile));
        } else {
            workbook = new XSSFWorkbook(new FileInputStream(excelFile));
        }
        //Excel???????????????
        List<Map<String, String>> list = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        //?????????1?????????????????????????????????????????????????????????
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            //????????????????????????start ?????????????????????????????????????????????????????????
            if (row.getCell(0) != null) {
                row.getCell(0).setCellType(CellType.STRING);
            }
            if (row.getCell(1) != null) {
                row.getCell(1).setCellType(CellType.STRING);
            }
            if (row.getCell(2) != null) {
                row.getCell(2).setCellType(CellType.STRING);
            }
            String userName = row.getCell(0).getStringCellValue(); //??????
            if (StringUtils.isEmpty(userName)) {
                throw new RuntimeException("???" + i + "?????????1???????????????!");
            }
            String telephone = row.getCell(1).getStringCellValue();//?????????
            if (StringUtils.isEmpty(telephone)) {
                throw new RuntimeException("???" + i + "?????????2???????????????!");
            }
            String sex = row.getCell(2).getStringCellValue();// ??????
            if (StringUtils.isEmpty(sex)) {
                throw new RuntimeException("???" + i + "?????????3???????????????!");
            }
            //????????????????????????end
            // ????????????
            Map<String, String> map = new HashMap<>();
            map.put("userName", userName);
            map.put("telephone", telephone);
            map.put("sex", sex);
            list.add(map);
        }
        //???????????????????????????
        if (excelFile.exists()) {
            excelFile.delete();
        }

        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            //????????????
            for (int i = 0; i < list.size(); i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserNo("blank");
                userInfo.setName(list.get(i).get("userName"));
                userInfo.setWechatName("");
                userInfo.setSex(list.get(i).get("sex"));
                userInfo.setBirthday("");
                userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(list.get(i).get("telephone")));
                userInfo.setIntroduction("????????????");
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
            logger.error("???????????????????????????????????????{}", fileName);
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
