package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.service.TeamActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/teamActType")
public class TeamActivityTypeController {
    @Autowired
    private TeamActivityTypeService teamActivityTypeService;

    @GetMapping("/queryAll")
    public RestResult queryAll(HttpServletRequest request){
        try {
            return ResultUtil.createSuccessResult(teamActivityTypeService.queryAllTeamActType());
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @GetMapping("/queryTeamDetail/{typeNo}")
    public RestResult queryTeamDetail(HttpServletRequest request, @PathVariable("typeNo") String typeNo, @RequestParam("userNo") String userNo){
        try {
            return ResultUtil.createSuccessResult(teamActivityTypeService.queryTeamDetail(typeNo, userNo));
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }
}
