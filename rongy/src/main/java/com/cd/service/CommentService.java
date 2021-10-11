package com.cd.service;

import com.cd.common.RestResult;
import com.cd.entity.dto.CommentDTO;

public interface CommentService {

    /**
     * 根据微信号查询我的消息
     * @param wechatName
     * @return
     */
    RestResult getByWechatName(String wechatName);

    /**
     * 根据用户id查询我的消息
     * @param userNo
     * @return
     */
    RestResult getByUserNo(String userNo, int pageNum, int pageSize);

    /**
     * 点赞或取消点赞
     * @param articleId
     * @param beCommenteeUserNo
     * @param commentatorUserNo
     */
    RestResult likeOrUnlikeByUserNo(String articleId, String beCommenteeUserNo, String commentatorUserNo);

    RestResult marketInfoLikeOrUnlikeByUserNo(String articleId, String beCommenteeUserNo, String commentatorUserNo);

    int add(CommentDTO commentDto);

    int addMarketInfoComment(CommentDTO commentDto);

    void del(String userNo, int commentId);

    void delMarketInfoComment(String userNo, int commentId);
}
