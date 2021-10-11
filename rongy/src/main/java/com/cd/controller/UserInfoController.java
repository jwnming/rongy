package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.Role;
import com.cd.entity.UserInfo;
import com.cd.entity.dto.UserLoginDTO;
import com.cd.entity.vo.RankingListVOs;
import com.cd.entity.vo.UserLoginVO;
import com.cd.service.UserInfoService;
import com.cd.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/user")
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据用户编号获取用户信息
     * @param userNo
     * @return
     */
    @GetMapping("/getByUserNo/{userNo}")
    public RestResult getUserInfo(@PathVariable("userNo") String userNo) {
        return userInfoService.getByUserNo(userNo);
    }

    /**
     * 根据他人用户编号获取用户信息
     * @param userNo
     * @return
     */
    @GetMapping("/getByOtherUserNo/{userNo}")
    public RestResult getOtherUserInfo(@PathVariable("userNo") String userNo) {
        return userInfoService.getByOtherUserNo(userNo);
    }

    /**
     * 根据用户编号修改用户信息
     * @param request
     * @return
     */
    @PostMapping("/updateByUserNo")
    public RestResult updateByUserNo(HttpServletRequest request) {
        String userNo = request.getParameter("userNo");
        String introduction = request.getParameter("introduction");
        String telephone = request.getParameter("telephone");
        System.out.println(userNo);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserNo(userNo);
        userInfo.setIntroduction(introduction);
        userInfo.setTelephone(SecurityUtil.phoneNumEncrypt(telephone));
        return userInfoService.updateByUserNo(userInfo);
    }

    /**
     * 根据圈子id查询用户信息
     * @param circleId
     * @return
     */
    @GetMapping("/getByCircleId/{circleId}")
    public RestResult getUserInfo(@PathVariable("circleId") Integer circleId) {
        return userInfoService.getByCircleId(circleId);
    }

    @GetMapping("/login")
    public RestResult login(UserLoginDTO userLogin) {
        UserLoginVO vo = userInfoService.userLogin(userLogin);

        return ResultUtil.createSuccessResult(vo);
    }

    @PostMapping("/{userNo}/read")
    public RestResult readAll(HttpServletRequest request, @PathVariable("userNo") String userNo) {
        userInfoService.readAll(userNo);

        return ResultUtil.createSuccessResult();
    }

    @GetMapping("/exist")
    public RestResult isExist(@RequestParam("name") String name, @RequestParam("phone") String phone) {
        UserInfo userInfo = userInfoService.getByNameAndPhone(name, phone);

        return userInfo == null ? ResultUtil.createFailedResult() : ResultUtil.createSuccessResult();
    }

    @GetMapping("/getRankingList/{userNo}")
    public RestResult getRankingList(@PathVariable("userNo") String userNo) {
        RankingListVOs rankingList = userInfoService.getRankingList(userNo);

        return ResultUtil.createSuccessResult(rankingList);
    }

    @GetMapping("/role/{userNo}/{roleType}")
    public RestResult roleValid(@PathVariable("userNo") String userNo,
                                     @PathVariable("roleType") String roleType) {
        Role role = userInfoService.roleValid(userNo, roleType);

        return role == null ? ResultUtil.createSuccessResult() : ResultUtil.createSuccessResult(role);
    }




}
