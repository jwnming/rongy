package com.cd.dao;

import com.cd.entity.TeamActivityPromote;
import com.cd.entity.TeamActivityRelUserInfo;
import com.cd.entity.TeamActivityType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeamActivityRelUserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TeamActivityRelUserInfo record);

    int insertSelective(TeamActivityRelUserInfo record);

    TeamActivityRelUserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TeamActivityRelUserInfo record);

    int updateByPrimaryKey(TeamActivityRelUserInfo record);

    List<TeamActivityPromote> queryTeamActPromoteByUserNo(String userNo);

    TeamActivityRelUserInfo selectById(TeamActivityRelUserInfo record);

    List<TeamActivityType> queryTeamActTypeByUserNo(String userNo);

    int isParticipateIn(@Param("userNo") String userNo, @Param("teamActProId")Integer teamActProId);

}