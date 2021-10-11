package com.cd.service.impl;

import com.cd.dao.UserInfoExMapper;
import com.cd.entity.UserInfoEx;
import com.cd.service.UserInfoExService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserInfoExServiceImpl implements UserInfoExService{
    @Autowired
    private UserInfoExMapper userInfoExMapper;
    @Override
    public UserInfoEx selectExInfoByUserNo(String userNo) {
        UserInfoEx userInfoEx = userInfoExMapper.selectExInfoByUserNo(userNo);
        if(StringUtils.isBlank(userInfoEx.getEntryDate())){
            userInfoEx.setEntryDate("2018-7-26");
        }
        return userInfoExMapper.selectExInfoByUserNo(userNo);
    }

    @Override
    public List<UserInfoEx> selectAllInfoEx() {
        return userInfoExMapper.selectAllInfoEx();
    }
}
