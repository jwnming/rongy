package com.cd.controller.admin;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.UserInfo;
import com.cd.entity.dto.UserLoginDTO;
import com.cd.entity.vo.UserLoginVO;
import com.cd.service.UserInfoService;
import com.cd.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RequestMapping("/admin/userInfo")
@RestController
public class AdminUserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 批量导入用户信息
     * @return
     */
    @PostMapping("/upload")
    public RestResult getUserInfo(HttpServletRequest request, MultipartFile userInfoFile) {
        try {
            userInfoService.insertByExcel(userInfoFile);
        } catch (Exception e) {
            return ResultUtil.createFailedResult();
        }
        return ResultUtil.createSuccessResult();
    }

    /**
     * 单条录入用户信息
     * @return
     */
    @PostMapping("/addSingle")
    public RestResult addSingle(HttpServletRequest request, UserInfo userInfo) {
        try {
            userInfoService.addSingle(userInfo);
        } catch (Exception e) {
            return ResultUtil.createFailedResult();
        }
        return ResultUtil.createSuccessResult();
    }

    /**
     * 根据用户编号获取用户信息
     * @param userNo
     * @return
     */
    @GetMapping("/getUserInfo/{userNo}")
    public RestResult getUserInfo(@PathVariable("userNo") String userNo) {

        return ResultUtil.createSuccessResult(userInfoService.getUserInfo(userNo));
    }


    /**
     * 根据用户编号获取用户信息
     * @param
     * @return
     */
    @GetMapping("/getAll/{pageNum}/{pageSize}")
    public RestResult getAll(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {

        //  userInfoService.getAllUser(pageNum, pageSize);
        return null;
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


}
