package com.cd.dao;

import com.cd.entity.TeamActivityPromote;

import java.util.List;

public interface TeamActivityPromoteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TeamActivityPromote record);

    int insertSelective(TeamActivityPromote record);

    TeamActivityPromote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TeamActivityPromote record);

    int updateByPrimaryKey(TeamActivityPromote record);

    List<TeamActivityPromote> selectUnderwayByTypeNo(String typeNo);

    List<TeamActivityPromote> selectFinishedByTypeNo(String typeNo);

    int count(String typeNo);

    List<TeamActivityPromote> selectByDoingStatus();

    List<TeamActivityPromote> selectByUserInfo(String userNo);
}