package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.service.ArticleService;
import com.cd.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/search")
@RestController
public class SearchController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据用户编号获取用户信息
     * @param userNo
     * @return
     */
    @GetMapping("/search/{userNo}/{searchText}/{searchType}")
    public RestResult getUserInfo(@PathVariable("userNo") String userNo, @PathVariable("searchText") String searchText,
            @PathVariable("searchType") String searchType) {
        if (StringUtils.equals("1", searchType)) {  // 1-搜索用户
            return userInfoService.getByNameLike(userNo, searchText);
        } else if (StringUtils.equals("2", searchType)) { // 2-搜帖子
            return articleService.getByContentLike(userNo, searchText);
        } else {
            return ResultUtil.createFailedResult();
        }
    }

}
