package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.service.UserInfoExService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userInfoEx")
public class UserInfoExController {
    @Autowired
    private UserInfoExService userInfoExService;

    @GetMapping("/selectByUserNo/{userNo}")
    public RestResult selectByUserNo(@PathVariable("userNo") String UserNo){
        return ResultUtil.createSuccessResult(userInfoExService.selectExInfoByUserNo(UserNo));
    }

    @GetMapping("/selectAllInfoEx")
    public RestResult selectAllInfoEx(){
        return ResultUtil.createSuccessResult(userInfoExService.selectAllInfoEx());
    }

}
