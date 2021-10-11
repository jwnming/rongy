package com.cd.dao;

import com.cd.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);


    /**
     * 根据微信号查询我的消息
     * @param wechatName
     * @return
     */
    List<Comment> selectByWechatName(String wechatName);

    /**
     * 根据用户id查询我的消息
     * @param userNo
     * @return
     */
    List<Comment> selectByUserNo(String userNo);
	List<Comment> selectDynamicByArticleIds(List<Integer> articleIds);

/**
     * 根据帖子id和评论人id查询
     * @param articleId
     * @param commentatorId
     * @return
     */
    Comment selectByArticleIdAndUserId(@Param("articleId") String articleId, @Param("commentatorId") String commentatorId);

    /**
     * 查询喜欢的人数
     * @param articleId
     * @return
     */
    Integer selectLikeCountByArticleId(String articleId);

    /**
     * 根据帖子id查询
     * @param articleId
     * @return
     */
    List<Comment> selectByArticleId(String articleId);

    /**
     * 查询是否点赞
     * @param articleId
     * @param commentatorUserNo
     * @return
     */
    Comment selectIsLike(@Param("articleId") String articleId, @Param("commentatorUserNo") String commentatorUserNo);

    int deleteByArticleId(String id);

    int countReadByUserNo(String userNo);

    void readAll(String userNo);

    /**
     * 根据商品id查询
     * @param goodsInfoId
     * @return
     */
    List<Comment> selectByGoodsInfoId(String goodsInfoId);
}

