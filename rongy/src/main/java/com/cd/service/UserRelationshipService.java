package com.cd.service;

import com.cd.common.RestResult;
import com.cd.entity.UserRelationship;
import com.cd.entity.vo.UserRelationshipVO;

import java.util.List;


public interface UserRelationshipService {

    /**
     * 根据用户id和关注人id进行删除（取消关注）
     * @param userId
     * @param followedUserId
     * @return
     */
    RestResult unFollow(String userId, String followedUserId);

    /**
     * 添加（关注）
     * @param record
     * @return
     */
    RestResult follow(UserRelationship record);

    List<UserRelationshipVO> queryFollow(String queryType, String userNo, String fromDate);

    RestResult getBothRelationshipByUserNo(String self, String other);

    List<UserRelationshipVO> queryOther(String userNO, String type, String searchUserNo);
}
