package com.cd.service.impl;

import cn.hutool.json.JSONUtil;
import com.cd.common.TeamActivityEnum;
import com.cd.dao.TeamActivityPromoteMapper;
import com.cd.dao.TeamActivityRelUserInfoMapper;
import com.cd.dao.UserInfoMapper;
import com.cd.entity.TeamActivityPromote;
import com.cd.entity.TeamActivityRelUserInfo;
import com.cd.entity.UserInfo;
import com.cd.enums.ErrorCodeEnum;
import com.cd.enums.RongyDictEnum;
import com.cd.exception.BusiException;
import com.cd.service.TeamActivityPromoteService;
import com.cd.utils.CommonUtil;
import com.cd.utils.HttpUtil;
import com.cd.utils.MiniappNotificationUtil;
import com.cd.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class TeamActivityPromoteServiceImpl implements TeamActivityPromoteService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MiniappNotificationUtil miniapp;

    @Autowired
    private TeamActivityPromoteMapper teamActivityPromoteMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private TeamActivityRelUserInfoMapper teamActivityRelUserInfoMapper;

    private static Logger logger = LoggerFactory.getLogger(TeamActivityPromoteServiceImpl.class);

    @Override
    public void create(TeamActivityPromote teamActivityPromote) {
        CommonUtil.isBlank("约团活动类型" , teamActivityPromote.getTeamTypeNo());
        CommonUtil.isBlank("活动名称" , teamActivityPromote.getActivityName());
        CommonUtil.isBlank("活动时间" , teamActivityPromote.getActivityTime());
        CommonUtil.isBlank("活动地址" , teamActivityPromote.getActivityAddress());
        CommonUtil.isBlank("最大人数限制" , teamActivityPromote.getMaxPersons().toString());
        CommonUtil.isBlank("活动描述" , teamActivityPromote.getDesContent());
        CommonUtil.isBlank("报名截止时间" , teamActivityPromote.getSignEndTime());
        if(teamActivityPromote.getMaxPersons() > 50)
            throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0016);
        // valid user
        UserInfo userInfo = userInfoMapper.selectByUserNo(teamActivityPromote.getUserNo());
        if (userInfo == null)
            throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode()))
            throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        boolean containsSensitive = HttpUtil.msgSecCheck(miniapp.getAccessToken(), teamActivityPromote.getDesContent());

        if (containsSensitive) {
            throw new BusiException(ErrorCodeEnum.ARTICLE_CONTENT_CONTAINS_SENSITIVE);
        }

        String endTime = teamActivityPromote.getActivityTime() + ":00";
        String signEndTime = teamActivityPromote.getSignEndTime() + ":00";
        Date datetime = new Date();
        SimpleDateFormat sdf  =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateD = null;
        Date dateEndTime = null;
        try {
            dateD = sdf.parse(endTime);
            dateEndTime = sdf.parse(signEndTime);
        } catch (ParseException e) {
            logger.error("转化异常", e);
        }

        Boolean flag = datetime.getTime() >= dateD.getTime();
        if(flag){
            throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0009);
        }

        Boolean endSignFlag1 = datetime.getTime() >= dateEndTime.getTime();
        Boolean endSignFlag2 = dateD.getTime() <= dateEndTime.getTime();
        if(endSignFlag1 || endSignFlag2){
            throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0011);
        }

        TeamActivityPromote teamActivityPromoteInsert = new TeamActivityPromote();
        teamActivityPromoteInsert.setTeamTypeNo(teamActivityPromote.getTeamTypeNo());
        teamActivityPromoteInsert.setActivityName(teamActivityPromote.getActivityName());
        teamActivityPromoteInsert.setActivityTime(teamActivityPromote.getActivityTime());
        teamActivityPromoteInsert.setActivityAddress(teamActivityPromote.getActivityAddress());
        teamActivityPromoteInsert.setMaxPersons(teamActivityPromote.getMaxPersons());
        teamActivityPromoteInsert.setDesContent(teamActivityPromote.getDesContent());
        teamActivityPromoteInsert.setImageUrl(JSONUtil.toJsonStr(teamActivityPromote.getImageUrls()));
        teamActivityPromoteInsert.setVadioUrl("[]");
        teamActivityPromoteInsert.setViewCount(0);
        teamActivityPromoteInsert.setSignEndTime(teamActivityPromote.getSignEndTime());
        teamActivityPromoteInsert.setPresentNum(1);
        teamActivityPromoteInsert.setActivityStatus(TeamActivityEnum.TEAMACTIVITY_STATUS$0.getKey());
        teamActivityPromoteInsert.setIsSticky("0");
        teamActivityPromoteInsert.setCreateBy(teamActivityPromote.getUserNo());
        teamActivityPromoteInsert.setCreatedTime(new Date());
        teamActivityPromoteInsert.setDeleteFlag("0");
        teamActivityPromoteInsert.setLongitude(teamActivityPromote.getLongitude());
        teamActivityPromoteInsert.setLatitude(teamActivityPromote.getLatitude());
        teamActivityPromoteInsert.setSignUrl(JSONUtil.toJsonStr(teamActivityPromote.getSignUrls()));
        teamActivityPromoteMapper.insertSelective(teamActivityPromoteInsert);
    }

    @Override
    public void signUpActivity(TeamActivityPromote teamActivityPromote) {
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

        try {
            CommonUtil.isBlank("报名该活动有误" , teamActivityPromote.getId().toString());
            CommonUtil.isBlank("用户编号有误", teamActivityPromote.getUserNo());

           TeamActivityPromote teamActivityPromoteQry = teamActivityPromoteMapper.selectByPrimaryKey(teamActivityPromote.getId());
            if(teamActivityPromoteQry == null){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0001);
            }

            UserInfo userInfo = userInfoMapper.selectByUserNo(teamActivityPromote.getUserNo());
            if (userInfo == null)
                throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

            if(StringUtils.equals(teamActivityPromoteQry.getCreateBy(), teamActivityPromote.getUserNo())){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0006);
            }

            logger.info("当前已报名人数:{},最大人数:{}", teamActivityPromoteQry.getPresentNum(), teamActivityPromoteQry.getMaxPersons());

            if(teamActivityPromoteQry.getPresentNum() == teamActivityPromoteQry.getMaxPersons()){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0008);
            }

            String endTime = teamActivityPromoteQry.getActivityTime() + ":00";
            String signEndTime = teamActivityPromoteQry.getSignEndTime() + ":00";
            Date datetime = new Date();
            SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateD = null;
            Date dateEndTime = null;
            try {
                dateD = sdf.parse(endTime);
                dateEndTime = sdf.parse(signEndTime);
            } catch (ParseException e) {
                logger.error("转化异常", e);
            }

            boolean flag1 = datetime.getTime() > dateD.getTime();
            boolean flag2 = datetime.getTime() > dateEndTime.getTime();
            if(flag1 || flag2){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0012);
            }

            TeamActivityRelUserInfo teamActivityRelUserInfoselect = new TeamActivityRelUserInfo();
            teamActivityRelUserInfoselect.setUserInfoId(userInfo.getId());
            teamActivityRelUserInfoselect.setTeamActivityId(teamActivityPromoteQry.getId());

            TeamActivityRelUserInfo teamActivityRelUserInfoQry = teamActivityRelUserInfoMapper.selectById(teamActivityRelUserInfoselect);
            if(teamActivityRelUserInfoQry != null){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0004);
            }

            List<TeamActivityPromote> teamActivityPromotes = teamActivityPromoteMapper.selectByUserInfo(userInfo.getUserNo());
            if(teamActivityPromotes != null && !teamActivityPromotes.isEmpty()){
                List<String> times = teamActivityPromotes.stream().map(TeamActivityPromote::getSignEndTime).collect(Collectors.toList());
                times.stream().forEach(
                        item -> {
                            if(StringUtils.equals(item.substring(0, 10), teamActivityPromoteQry.getSignEndTime().substring(0, 10))){
                                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0010);
                            }
                        }
                );
            }

            //插入关系表
            TeamActivityRelUserInfo teamActivityRelUserInfo = new TeamActivityRelUserInfo();
            teamActivityRelUserInfo.setTeamActivityId(teamActivityPromoteQry.getId());
            teamActivityRelUserInfo.setUserInfoId(userInfo.getId());
            teamActivityRelUserInfoMapper.insertSelective(teamActivityRelUserInfo);

            AtomicInteger count = new AtomicInteger(teamActivityPromoteQry.getPresentNum());

            TeamActivityPromote teamActivityPromote1 = new TeamActivityPromote();
            teamActivityPromote1.setId(teamActivityPromoteQry.getId());
            teamActivityPromote1.setPresentNum(count.incrementAndGet());

            teamActivityPromoteMapper.updateByPrimaryKeySelective(teamActivityPromote1);
            txManager.commit(status);
        }catch (Exception e){
            logger.error("出现异常:", e);
            txManager.rollback(status);
            throw e;
        }


    }

    @Override
    public void cancelSignUpActivity(TeamActivityPromote teamActivityPromote) {
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

        try {
            CommonUtil.isBlank("取消报名该活动有误" , teamActivityPromote.getId().toString());
            CommonUtil.isBlank("用户编号有误", teamActivityPromote.getUserNo());
            TeamActivityPromote teamActivityPromoteQry = teamActivityPromoteMapper.selectByPrimaryKey(teamActivityPromote.getId());
            if(teamActivityPromoteQry == null){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0001);
            }

            UserInfo userInfo = userInfoMapper.selectByUserNo(teamActivityPromote.getUserNo());
            if (userInfo == null)
                throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

            if(StringUtils.equals(teamActivityPromoteQry.getCreateBy(), teamActivityPromote.getUserNo())){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0007);
            }

            String endTime = teamActivityPromoteQry.getActivityTime() + ":00";
            String signEndTime = teamActivityPromoteQry.getSignEndTime() + ":00";
            Date datetime = new Date();
            SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateD = null;
            Date dateEndTime = null;
            try {
                dateD = sdf.parse(endTime);
                dateEndTime = sdf.parse(signEndTime);
            } catch (ParseException e) {
                logger.error("转化异常", e);
            }

            boolean flag1 = datetime.getTime() > dateD.getTime();
            boolean flag2 = datetime.getTime() > dateEndTime.getTime();
            if(flag1 || flag2){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0013);
            }

            logger.info("当前已报名人数:{},最大人数:{}", teamActivityPromoteQry.getPresentNum(), teamActivityPromoteQry.getMaxPersons());

            TeamActivityRelUserInfo teamActivityRelUserInfo = new TeamActivityRelUserInfo();
            teamActivityRelUserInfo.setUserInfoId(userInfo.getId());
            teamActivityRelUserInfo.setTeamActivityId(teamActivityPromoteQry.getId());

            TeamActivityRelUserInfo teamActivityRelUserInfoQry = teamActivityRelUserInfoMapper.selectById(teamActivityRelUserInfo);
            if(teamActivityRelUserInfoQry == null){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0005);
            }

            teamActivityRelUserInfoMapper.deleteByPrimaryKey(teamActivityRelUserInfoQry.getId());

            AtomicInteger count = new AtomicInteger(teamActivityPromoteQry.getPresentNum());

            TeamActivityPromote teamActivityPromoteUpdate = new TeamActivityPromote();
            teamActivityPromoteUpdate.setId(teamActivityPromoteQry.getId());
            teamActivityPromoteUpdate.setPresentNum(count.decrementAndGet());
            if(teamActivityPromoteUpdate.getPresentNum() < 1 ){
                throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0003);
            }

            teamActivityPromoteMapper.updateByPrimaryKeySelective(teamActivityPromoteUpdate);
            txManager.commit(status);
        }catch (Exception e){
            logger.error("出现异常", e);
            txManager.rollback(status);
            throw e;
        }
    }

    @Override
    public TeamActivityPromote qryActivityDetail(Integer id) {
        TeamActivityPromote teamActivityPromote = teamActivityPromoteMapper.selectByPrimaryKey(id);
        if(teamActivityPromote == null){
            throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0001);
        }
        teamActivityPromote.setImageUrls(JSONUtil.toList(JSONUtil.parseArray(teamActivityPromote.getImageUrl()), String.class));
        teamActivityPromote.setSignUrls(JSONUtil.toList(JSONUtil.parseArray(teamActivityPromote.getSignUrl()), String.class));
        UserInfo userInfo = userInfoMapper.selectByUserNo(teamActivityPromote.getCreateBy());
        if (userInfo == null)
            throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);
        teamActivityPromote.setCreateUserInfo(userInfo);
        List<UserInfo> list = userInfoMapper.selectByActivityId(id);
        if(list!=null && !list.isEmpty()){
            list.forEach(
                    item -> {
                        item.setTelephone(SecurityUtil.phoneNumDecrypt(item.getTelephone()));
                    }
            );
            teamActivityPromote.setUserInfos(list);
        }else{
            List<UserInfo> empty = new ArrayList<>();
            teamActivityPromote.setUserInfos(empty);
        }
        return teamActivityPromote;
    }

    @Override
    public void del(String userNo, Integer id) {
        TeamActivityPromote teamActivityPromote = teamActivityPromoteMapper.selectByPrimaryKey(id);
        if(teamActivityPromote == null){
            throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0001);
        }

        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null)
            throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if(!StringUtils.equals(userInfo.getUserNo(), userNo)){
            throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0014);
        }
        int count = teamActivityPromoteMapper.deleteByPrimaryKey(id);
        if(count != 1){
            throw new BusiException(ErrorCodeEnum.TEAMACTIVITY0014);
        }
    }
}
