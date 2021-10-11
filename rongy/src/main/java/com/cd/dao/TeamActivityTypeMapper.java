package com.cd.dao;

import com.cd.entity.TeamActivityType;
import com.cd.entity.vo.TeamVO;

import java.util.List;

public interface TeamActivityTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TeamActivityType record);

    int insertSelective(TeamActivityType record);

    TeamActivityType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TeamActivityType record);

    int updateByPrimaryKey(TeamActivityType record);

    List<TeamActivityType> queryAll();

    TeamVO selectByTypeNo(String typeNo);

    List<TeamActivityType> selectBydoingStatus();
}