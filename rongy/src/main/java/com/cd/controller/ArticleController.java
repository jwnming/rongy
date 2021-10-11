package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.dto.ArticleDTO;
import com.cd.entity.dto.ArticleQueryCondition;
import com.cd.entity.vo.DynamicListVO;
import com.cd.entity.vo.DynamicListVOs;
import com.cd.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/article")
    public RestResult newArticle(HttpServletRequest request, @RequestBody ArticleDTO articleDTO) {
        articleService.create(articleDTO);

        return ResultUtil.createSuccessResult();
    }

    @GetMapping("/articles")
    public RestResult list(ArticleQueryCondition articleQC) {
        DynamicListVOs vos = articleService.queryArticles(articleQC);

        return ResultUtil.createSuccessResult(vos);
    }

    @GetMapping("/article/{id}")
    public RestResult get(@PathVariable("id") int id, @RequestParam("userNo") String userNo) {
        DynamicListVO vo = articleService.selectById(id, userNo);

        return ResultUtil.createSuccessResult(vo);
    }

    @GetMapping("/user/{userNo}/articles")
    public RestResult listByUserNo(@PathVariable("userNo") String userNo, @RequestParam("searchUserNo") String searchUserNo) {
        List<DynamicListVO> vos = articleService.listByUserNo(userNo, searchUserNo);

        return ResultUtil.createSuccessResult(vos);
    }

    @GetMapping("/user/{userNo}/liked/articles")
    public RestResult listUserLinkedArticles(@PathVariable("userNo") String userNo) {
        List<DynamicListVO> vos = articleService.listUserLiked(userNo);

        return ResultUtil.createSuccessResult(vos);
    }

    @DeleteMapping("/user/{userNo}/article/{id}")
    public RestResult del(@PathVariable("userNo") String userNo, @PathVariable("id") int id) {
        boolean flag = articleService.del(userNo, id);

        return flag ? ResultUtil.createSuccessResult() : ResultUtil.createFailedResult();
    }

    @GetMapping("/article/viewCountAdd/{articleId}")
    public RestResult viewCountAdd(@PathVariable("articleId") Integer articleId) {
        int i = articleService.viewCountAdd(articleId);

        return i == 1 ? ResultUtil.createSuccessResult() : ResultUtil.createFailedResult();
    }



}
