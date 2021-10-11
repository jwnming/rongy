package com.cd.dao;

import com.cd.entity.UserCircleRel;

public interface UserCircleRelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCircleRel record);

    int insertSelective(UserCircleRel record);

    UserCircleRel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCircleRel record);

    int updateByPrimaryKey(UserCircleRel record);
}