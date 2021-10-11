package com.cd.dao;

import com.cd.entity.UserRelationship;
import com.cd.entity.vo.UserRelationshipVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRelationshipMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRelationship record);

    int insertSelective(UserRelationship record);

    UserRelationship selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRelationship record);

    int updateByPrimaryKey(UserRelationship record);

    /**
     * 根据用户id和关注人id进行删除（取消关注）
     * @param userId
     * @param followedUserId
     * @return
     */
    int deleteByBothId(@Param("userId") String userId, @Param("followedUserId") String followedUserId);

    List<UserRelationshipVO> selectFollowerVOByUserNoAndFromDate(@Param("userNo") String userNo, @Param("fromDate") String fromDate);

    List<UserRelationshipVO> selectFollowingVOByUserNo(String userNo);

    List<UserRelationship> selectFollowerByUserNo(String userNo);

    List<UserRelationship> selectFollowingByUserNo(String userNo);

    /**
     * 根据用户id和关注人id进行查询
     * @param userId
     * @param followedUserId
     * @return
     */
    UserRelationship selectByBothId(@Param("userId") String userId, @Param("followedUserId") String followedUserId);

    List<UserRelationship> selectByUserNo(String userNo);

}