package com.cd.service;

import com.cd.entity.UserInfoEx;

import java.util.List;

public interface UserInfoExService {

    UserInfoEx selectExInfoByUserNo(String userNo);

    List<UserInfoEx> selectAllInfoEx();
}
