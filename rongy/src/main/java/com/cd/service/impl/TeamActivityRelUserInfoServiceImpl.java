package com.cd.service.impl;

import com.cd.dao.TeamActivityPromoteMapper;
import com.cd.dao.TeamActivityRelUserInfoMapper;
import com.cd.entity.TeamActivityType;
import com.cd.service.TeamActivityRelUserInfoService;
import com.cd.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TeamActivityRelUserInfoServiceImpl implements TeamActivityRelUserInfoService {
    @Autowired
    private UserInfoService userInfoService;
    @Resource
    private TeamActivityRelUserInfoMapper teamActivityRelUserInfoMapper;
    @Resource
    private TeamActivityPromoteMapper teamActivityPromoteMapper;

    @Override
    public List<TeamActivityType> queryTeamActTypeByUserNo(String userNo) {
        userInfoService.userInfoValidate(userNo);

        List<TeamActivityType> teamActivityTypes = teamActivityRelUserInfoMapper.queryTeamActTypeByUserNo(userNo);
        for(TeamActivityType teamActivityType : teamActivityTypes){
            teamActivityType.setPublishNum(teamActivityPromoteMapper.count(String.valueOf(teamActivityType.getTypeNo())));
        }
        return teamActivityTypes;
    }
}
