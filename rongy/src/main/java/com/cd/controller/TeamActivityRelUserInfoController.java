package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.service.TeamActivityRelUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/teamActTypeRelUserInfo")
public class TeamActivityRelUserInfoController {
    @Autowired
    private TeamActivityRelUserInfoService teamActivityRelUserInfoService;

    @GetMapping("/queryByUserNo")
    public RestResult queryByUserNo(HttpServletRequest request,@RequestParam("userNo") String userNo){
        try {
            return ResultUtil.createSuccessResult(teamActivityRelUserInfoService.queryTeamActTypeByUserNo(userNo));
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

}
