package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.MarketGoodsInfo;
import com.cd.entity.dto.ArticleDTO;
import com.cd.entity.dto.ArticleQueryCondition;
import com.cd.entity.dto.TopicAritclesQueryCondition;
import com.cd.entity.dto.TopicClsDTO;
import com.cd.entity.vo.DynamicListVOs;
import com.cd.service.ArticleService;
import com.cd.service.TopicClsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;

import com.cd.entity.TopicCls;

import java.util.List;


@RestController
@RequestMapping("/topic")
public class TopicClsController {

    @Autowired
    private TopicClsService topicClsService;
    @Autowired
    private ArticleService articleService;

    @GetMapping("/getBigTypes")
    public RestResult getBigTypes(){
        try {
            return ResultUtil.createSuccessResult(topicClsService.getBigTypes());
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @PostMapping("/publishTopic")
    public RestResult publishTopic(HttpServletRequest request, @RequestBody TopicClsDTO topicClsDTO){
        try {
            topicClsService.create(topicClsDTO);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @DeleteMapping("/topic/{userNo}/delete/{id}")
    public RestResult del(@PathVariable("userNo") String userNo, @PathVariable("id") int id) {
        boolean flag = topicClsService.del(userNo, id);
        return flag ? ResultUtil.createSuccessResult() : ResultUtil.createFailedResult();
    }


    @PostMapping("/newArticleWithTopic/{topicClsId}")
    public RestResult newArticleWithTopic(HttpServletRequest request, @PathVariable("topicClsId") String topicClsId,  @RequestBody ArticleDTO articleDTO) {
        try {
            topicClsService.newArticleWithTopic(articleDTO, topicClsId);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @PostMapping("/newGoodsWithTopic/{topicClsId}")
    public RestResult newGoodsWithTopic(HttpServletRequest request, @PathVariable("topicClsId") String topicClsId, @RequestBody MarketGoodsInfo marketGoodsInfo) {
        try {
            topicClsService.newGoodsWithTopic(marketGoodsInfo, topicClsId);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @PostMapping("/update")
    public RestResult update(HttpServletRequest request, @RequestBody TopicCls topicCls) {
        return ResultUtil.createSuccessResult();
    }


    @GetMapping("/viewCountAdd/{topicId}")
    public RestResult viewCountAdd(@PathVariable("topicId") Integer topicId) {
        int i = topicClsService.viewCountAdd(topicId);

        return i == 1 ? ResultUtil.createSuccessResult() : ResultUtil.createFailedResult();
    }

    @GetMapping("/queryTopics")
    public RestResult queryTopics(){
        try {
            return ResultUtil.createSuccessResult(topicClsService.queryTopics());
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @GetMapping("/queryTopicById/{userNo}/{topicId}")
    public RestResult queryTopicById(@PathVariable("userNo") String userNo, @PathVariable("topicId") Integer topicId) {
        try {
            return ResultUtil.createSuccessResult(topicClsService.getTopicById(topicId, userNo));
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @GetMapping("/queryArticlesByTopicId")
    public RestResult queryArticlesByTopicId(TopicAritclesQueryCondition topicQC) {
         return topicClsService.queryArticlesByTopicId(topicQC);
    }


    @GetMapping("/getTopicsByUserNo/{userNo}/{smallType}")
    public RestResult getByUserNo(@PathVariable("userNo") String userNo, @PathVariable("smallType") String smallType) {
        try {
            List<TopicCls> list = topicClsService.getByUserNo(userNo, smallType);
            return  ResultUtil.createSuccessResult(list);
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }

    @GetMapping("/getTopicsByCondition")
    public RestResult getTopicsByCondition(TopicCls topicCls){
        try {
            List<TopicCls> list = topicClsService.getTopicsByCondition(topicCls);
            return  ResultUtil.createSuccessResult(list);
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }


}
