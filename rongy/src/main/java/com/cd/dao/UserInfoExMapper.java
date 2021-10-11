package com.cd.dao;

import com.cd.entity.UserInfoEx;

import java.util.List;

public interface UserInfoExMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfoEx record);

    int insertSelective(UserInfoEx record);

    UserInfoEx selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfoEx record);

    int updateByPrimaryKey(UserInfoEx record);

    UserInfoEx selectExInfoByUserNo(String userNo);

    List<UserInfoEx> selectAllInfoEx();
}