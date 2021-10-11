package com.cd.service;

import com.cd.entity.TeamActivityType;

import java.util.List;

public interface TeamActivityRelUserInfoService {

    List<TeamActivityType> queryTeamActTypeByUserNo(String userNo);
}
