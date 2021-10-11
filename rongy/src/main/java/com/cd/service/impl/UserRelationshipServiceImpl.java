package com.cd.service.impl;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.dao.UserInfoMapper;
import com.cd.dao.UserRelationshipMapper;
import com.cd.entity.UserInfo;
import com.cd.entity.UserRelationship;
import com.cd.entity.vo.UserRelationshipVO;
import com.cd.enums.RongyDictEnum;
import com.cd.service.UserRelationshipService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRelationshipServiceImpl implements UserRelationshipService {

    private Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private UserRelationshipMapper userRelationshipMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 取消关注（删除）
     * @param userId
     * @param followedUserId
     * @return
     */
    @Override
    public RestResult unFollow(String userId, String followedUserId) {
        logger.info("取消关注开始，用户id：{}，关注人id：{}", userId, followedUserId);
        // 手动控制事务
        RestResult restResult = null;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            int i = userRelationshipMapper.deleteByBothId(userId, followedUserId);
            if (i != 1) {
                logger.error("取消关注失败，用户id：{}，关注人id：{}", userId, followedUserId);
                restResult = ResultUtil.createFailedResult();
            } else {
                logger.info("取消关注成功，用户id：{}，关注人id：{}", userId, followedUserId);
                restResult = ResultUtil.createSuccessResult(i);
            }
            txManager.commit(status);
        } catch (Throwable e) {
            logger.error("取消关注异常！", e);
            txManager.rollback(status);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * 添加关注
     * @param record
     * @return
     */
    @Override
    public RestResult follow(UserRelationship record) {
        logger.info("添加关注开始，用户id：{}，关注人id：{}", record.getUserId(), record.getFollowedUserId());
        // 手动控制事务
        RestResult restResult = null;
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            UserRelationship ur = userRelationshipMapper.selectByBothId(record.getUserId(), record.getFollowedUserId());

            if (ur != null) {
                restResult = ResultUtil.createSuccessResult();
            } else {
                int i = userRelationshipMapper.insert(record);
                if (i != 1) {
                    logger.error("添加关注失败，用户id：{}，关注人id：{}", record.getUserId(), record.getFollowedUserId());
                    restResult = ResultUtil.createFailedResult();
                } else {
                    logger.info("添加关注成功，用户id：{}，关注人id：{}", record.getUserId(), record.getFollowedUserId());
                    restResult = ResultUtil.createSuccessResult();
                }
            }
            txManager.commit(status);
        } catch (Throwable e) {
            logger.error("取消关注异常！", e);
            txManager.rollback(status);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    @Override
    public List<UserRelationshipVO> queryFollow(String queryType, String userNo, String fromDate) {
        if (StringUtils.equals(queryType, RongyDictEnum.FOLLOW_QUERY_TYPE$FOLLOWER.getCode())) {
            return getFollower(userNo, fromDate);
        } else if (StringUtils.equals(queryType, RongyDictEnum.FOLLOW_QUERY_TYPE$FOLLOWING.getCode())) {
            return getFollowing(userNo);
        } else {
            return null;
        }
    }

    // fans
    private List<UserRelationshipVO> getFollower(String userNo, String fromDate) {
        List<UserRelationshipVO> relationships =  userRelationshipMapper.selectFollowerVOByUserNoAndFromDate(userNo, fromDate);

        List<UserRelationship> followings = userRelationshipMapper.selectFollowingByUserNo(userNo);

        Set<String> followingUserNos = followings.stream().map(UserRelationship::getUserId).collect(Collectors.toSet());

        relationships.forEach(t -> {
            if (followingUserNos.contains(t.getUserNo())) {
                t.setStatus(RongyDictEnum.FOLLOW_STATUS$YES.getCode());
            } else {
                t.setStatus(RongyDictEnum.FOLLOW_STATUS$NO.getCode());
            }
        });

        return relationships;
    }

    private List<UserRelationshipVO> getFollowing(String userNo) {
        List<UserRelationshipVO> relationships =  userRelationshipMapper.selectFollowingVOByUserNo(userNo);

        List<UserRelationship> followers = userRelationshipMapper.selectFollowerByUserNo(userNo);

        Set<String> followerUserNos = followers.stream().map(UserRelationship::getFollowedUserId).collect(Collectors.toSet());

        relationships.forEach(t -> {
            if (followerUserNos.contains(t.getUserNo())) {
                t.setStatusToMe(RongyDictEnum.FOLLOW_STATUS_TO_ME$YES.getCode());
            } else {
                t.setStatusToMe(RongyDictEnum.FOLLOW_STATUS_TO_ME$NO.getCode());
            }
        });

        return relationships;
    }
    /**
     * 查询我和他的关系
     * @param userNo
     * @param otherNo
     * @return
     */
    @Override
    public RestResult getBothRelationshipByUserNo(String userNo, String otherNo) {
        logger.info("查询我和他的关注与被关注信息，我的userNo:{},他的userNo:{}", userNo, otherNo);
        RestResult restResult = null;
        try {
            // 查询我的信息
            UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
            // 判断他对我的关注
            UserRelationship userRelationship1 = userRelationshipMapper.selectByBothId(userNo, otherNo);
            if (userRelationship1 != null) {
                userInfo.setStatusToMe("1");
            } else {
                userInfo.setStatusToMe("0");
            }

            // 判断我对他的关注
            UserRelationship userRelationship2 = userRelationshipMapper.selectByBothId(otherNo,userNo);
            if (userRelationship2 != null) {
                userInfo.setStatus("1");
            } else {
                userInfo.setStatus("0");
            }
            restResult = ResultUtil.createSuccessResult(userInfo);
        } catch (Exception e) {
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    @Override
    public List<UserRelationshipVO> queryOther(String userNo, String type, String searchUserNo) {
        if (StringUtils.equals(type, RongyDictEnum.FOLLOW_QUERY_TYPE$FOLLOWER.getCode())) {
            return getOtherFollower(userNo, searchUserNo);
        } else if (StringUtils.equals(type, RongyDictEnum.FOLLOW_QUERY_TYPE$FOLLOWING.getCode())) {
            return getOtherFollowing(userNo, searchUserNo);
        } else {
            return null;
        }
    }

    private List<UserRelationshipVO> getOtherFollower(String userNo, String searchUserNo) {
        List<UserRelationshipVO> relationships =  userRelationshipMapper.selectFollowerVOByUserNoAndFromDate(userNo, null);

        return fetchRelations(relationships, searchUserNo);
    }

    public List<UserRelationshipVO> getOtherFollowing(String userNo, String searchUserNo) {
        List<UserRelationshipVO> relationships =  userRelationshipMapper.selectFollowingVOByUserNo(userNo);

        return fetchRelations(relationships, searchUserNo);
    }

    private List<UserRelationshipVO> fetchRelations(List<UserRelationshipVO> relationships, String searchUserNo) {
        List<UserRelationship> urs = userRelationshipMapper.selectByUserNo(searchUserNo);

        // 我的关注
        Set<String> followingUserNos = new HashSet<>();
        // 我的粉丝
        Set<String> followerUserNos = new HashSet<>();
        urs.forEach(ur -> {
            if (StringUtils.equals(ur.getUserId(), searchUserNo)) {
                followerUserNos.add(ur.getFollowedUserId());
            } else if (StringUtils.equals(ur.getFollowedUserId(), searchUserNo)) {
                followingUserNos.add(ur.getUserId());
            }
        });

        relationships.forEach(t -> {
            if (followerUserNos.contains(t.getUserNo())) {
                t.setStatusToMe(RongyDictEnum.FOLLOW_STATUS_TO_ME$YES.getCode());
            } else {
                t.setStatusToMe(RongyDictEnum.FOLLOW_STATUS_TO_ME$NO.getCode());
            }

            if (followingUserNos.contains(t.getUserNo())) {
                t.setStatus(RongyDictEnum.FOLLOW_STATUS$YES.getCode());
            } else {
                t.setStatus(RongyDictEnum.FOLLOW_STATUS$NO.getCode());
            }
        });

        return relationships;
    }



}
