package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.TeamActivityPromote;
import com.cd.service.TeamActivityPromoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/teamPromote")
public class TeamActivityPromoteController {

    @Autowired
    private TeamActivityPromoteService teamActivityPromoteService;

    @PostMapping("/publishActivity")
    public RestResult publishActivity(HttpServletRequest request, @RequestBody TeamActivityPromote teamActivityPromote){
        try {
            teamActivityPromoteService.create(teamActivityPromote);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @PostMapping("/signUpActivity")
    public RestResult signUpActivity(HttpServletRequest request, @RequestBody TeamActivityPromote teamActivityPromote){
        try {
            teamActivityPromoteService.signUpActivity(teamActivityPromote);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @PostMapping("/cancelSignUpActivity")
    public RestResult cancelSignUpActivity(HttpServletRequest request, @RequestBody TeamActivityPromote teamActivityPromote){
        try {
            teamActivityPromoteService.cancelSignUpActivity(teamActivityPromote);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @GetMapping("/qryActivityDetail/{id}")
    public RestResult qryActivityDetail(@PathVariable("id") Integer id){
        try {
            return ResultUtil.createSuccessResult(teamActivityPromoteService.qryActivityDetail(id));
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @DeleteMapping("/{userNo}/delete/{id}")
    public RestResult del(@PathVariable("userNo") String userNo, @PathVariable("id") int id) {
        try {
            teamActivityPromoteService.del(userNo, id);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }




}
