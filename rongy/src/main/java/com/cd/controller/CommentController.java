package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.UserInfo;
import com.cd.entity.dto.CommentDTO;
import com.cd.service.CommentService;
import com.cd.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 根据微信号获取我的消息
     * @param wechatName
     * @return
     */
    @GetMapping("/getByWechatName/{wechatName}")
    public RestResult getByWechatName(@PathVariable("wechatName") String wechatName) {
        return commentService.getByWechatName(wechatName);
    }

    /**
     * 根据用户id查询我的消息
     * @param userNo
     * @return
     */
    @GetMapping("/user/{userNo}")
    public RestResult getByUserNo(@PathVariable("userNo") String userNo, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return commentService.getByUserNo(userNo, pageNum, pageSize);
    }

    @PostMapping("/comment")
    public RestResult add(HttpServletRequest request, @RequestBody CommentDTO commentDto) {
        int id = commentService.add(commentDto);

        return ResultUtil.createSuccessResult(id);
    }

    @PostMapping("/marketInfo/comment")
    public RestResult addMarketInfoComment(HttpServletRequest request, @RequestBody CommentDTO commentDto) {
        int id = commentService.addMarketInfoComment(commentDto);

        return ResultUtil.createSuccessResult(id);
    }

    /**
     * 点赞或取消点赞
     */
    @GetMapping("/like/{articleId}/{beCommenteeUserNo}/{commentatorUserNo}")
    public RestResult like(@PathVariable("articleId") String articleId,
                                      @PathVariable("beCommenteeUserNo") String beCommenteeUserNo,
                                      @PathVariable("commentatorUserNo") String commentatorUserNo) {
        return commentService.likeOrUnlikeByUserNo(articleId, beCommenteeUserNo, commentatorUserNo);
    }

    /**
     * 点赞或取消点赞
     */
    @GetMapping("/marketInfo/like/{articleId}/{beCommenteeUserNo}/{commentatorUserNo}")
    public RestResult marketInfoLikeOrUnlikeByUserNo(@PathVariable("articleId") String articleId,
                           @PathVariable("beCommenteeUserNo") String beCommenteeUserNo,
                           @PathVariable("commentatorUserNo") String commentatorUserNo) {
        return commentService.marketInfoLikeOrUnlikeByUserNo(articleId, beCommenteeUserNo, commentatorUserNo);
    }

    @DeleteMapping("/user/{userNo}/{commentId}")
    public RestResult delComment(@PathVariable("userNo") String userNo, @PathVariable("commentId") int commentId) {
        commentService.del(userNo, commentId);

        return ResultUtil.createSuccessResult();
    }

    @DeleteMapping("/marketInfo/{userNo}/{commentId}")
    public RestResult delMarketInfoComment(@PathVariable("userNo") String userNo, @PathVariable("commentId") int commentId) {

        try {
            commentService.delMarketInfoComment(userNo, commentId);
            return ResultUtil.createSuccessResult();
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }


    }
}
