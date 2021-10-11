package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.UserRelationship;
import com.cd.entity.vo.UserRelationshipVO;
import com.cd.service.UserRelationshipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RequestMapping("/userRelationship")
@RestController
public class UserRelationshipController {

    @Autowired
    private UserRelationshipService userRelationshipService;

    /**
     * 关注或取消关注
     * @return
     */
    @PostMapping("/follow")
    public RestResult getUserInfo(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String followedUserId = request.getParameter("followedUserId");
        String dealType = request.getParameter("dealType");
        if (StringUtils.equals(dealType, "0")) { // 删除关注
            return userRelationshipService.unFollow(userId, followedUserId);
        } else if (StringUtils.equals(dealType, "1")) { // 添加关注
            UserRelationship userRelationship = new UserRelationship();
            userRelationship.setUserId(userId);
            userRelationship.setFollowedUserId(followedUserId);
            userRelationship.setCreatedTime(new Date());
            userRelationship.setUpdatedTime(new Date());
            return userRelationshipService.follow(userRelationship);
        } else {
            return ResultUtil.createFailedResult();
        }
    }

    @GetMapping("/follow/type/{queryType}")
    public RestResult queryFollow(@PathVariable("queryType") String queryType, @RequestParam("userNo") String userNo, @RequestParam("fromDate") String fromDate) {

        List<UserRelationshipVO> relationships = userRelationshipService.queryFollow(queryType, userNo, fromDate);

        return ResultUtil.createSuccessResult(relationships);
    }

    @GetMapping("/queryBothRelationship/{self}/{other}")
    public RestResult queryBothRelationship(@PathVariable("self") String self, @PathVariable("other") String other) {
        return userRelationshipService.getBothRelationshipByUserNo(self, other);
    }

    @GetMapping("/{userNo}/{type}")
    public RestResult queryOther(@PathVariable("userNo") String userNo, @PathVariable("type") String type, @RequestParam("searchUserNo") String searchUserNo) {
        List<UserRelationshipVO> relationships = userRelationshipService.queryOther(userNo, type, searchUserNo);

        return ResultUtil.createSuccessResult(relationships);
    }
}
