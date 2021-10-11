package com.cd.service.impl;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.dao.ArticleMapper;
import com.cd.dao.CommentMapper;
import com.cd.dao.MarketGoodsInfoMapper;
import com.cd.dao.UserInfoMapper;
import com.cd.entity.Article;
import com.cd.entity.Comment;
import com.cd.entity.MarketGoodsInfo;
import com.cd.entity.UserInfo;
import com.cd.entity.dto.CommentDTO;
import com.cd.entity.vo.MyCommentVO;
import com.cd.enums.ErrorCodeEnum;
import com.cd.enums.RongyDictEnum;
import com.cd.exception.BusiException;
import com.cd.service.CommentService;
import com.cd.utils.HttpUtil;
import com.cd.utils.MiniappNotificationUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private MarketGoodsInfoMapper marketGoodsInfoMapper;

    @Autowired
    private MiniappNotificationUtil miniapp;

    /**
     * 根据微信号查询我的消息
     * @param wechatName
     * @return
     */
    @Override
    public RestResult getByWechatName(String wechatName) {
        logger.info("根据微信号查询我的消息开始，微信号[{}]", wechatName);
        RestResult restResult = null;
        List<Comment> comments = null;
        try {
            comments = commentMapper.selectByWechatName(wechatName);
            restResult = ResultUtil.createSuccessResult(comments);
        } catch (Exception e) {
            logger.error("根据微信号查询我的消息失败,微信号[{}]", wechatName, e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    /**
     * 根据用户id查询我的消息
     * @param userNo
     * @return
     */
    @Override
    public RestResult getByUserNo(String userNo, int pageNum, int pageSize) {

        PageInfo<Comment> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            commentMapper.selectByUserNo(userNo);
        });

        MyCommentVO myCommentVO = new MyCommentVO();
        myCommentVO.setComments(pageInfo.getList());
        myCommentVO.setCurPage(pageNum);
        myCommentVO.setTotalPage(pageInfo.getPages());

        return ResultUtil.createSuccessResult(myCommentVO);
    }

    /**
     * 点赞或取消点赞
     * @param articleId
     * @param beCommenteeUserNo
     * @param commentatorUserNo
     * @return
     */
    @Override
    public RestResult likeOrUnlikeByUserNo(String articleId, String beCommenteeUserNo, String commentatorUserNo) {
        RestResult restResult = null;
        try {
            Comment comment = commentMapper.selectIsLike(articleId, commentatorUserNo);
            if (comment == null) {  // 如果为查询到点赞，则添加数据
                Article article = articleMapper.selectByPrimaryKey(Integer.parseInt(articleId));
                if (article == null) throw new BusiException(ErrorCodeEnum.ARTICLE_NOT_EXISTS);

                Comment record = new Comment();
                record.setArticleId(articleId);
                record.setContent("");
                if (StringUtils.isEmpty(beCommenteeUserNo)) {
                    record.setBecommenteeId(article.getCreatedBy());
                } else {
                    record.setBecommenteeId(beCommenteeUserNo);
                }
                record.setCommentatorId(commentatorUserNo);
                record.setType("0");
                record.setLevel("0");
                if (StringUtils.equals(record.getCommentatorId(), record.getBecommenteeId())) {
                    record.setIsRead(RongyDictEnum.COMMENT_IS_READ$YES.getCode());
                } else {
                    record.setIsRead(RongyDictEnum.COMMENT_IS_READ$NO.getCode());
                }
                record.setCreatedTime(new Date());
                record.setUpdatedTime(new Date());
                commentMapper.insert(record);

                UserInfo beCommented = userInfoMapper.selectByUserNo(beCommenteeUserNo);
                UserInfo commenter = userInfoMapper.selectByUserNo(commentatorUserNo);
                if (!StringUtils.equals(commenter.getUserNo(), beCommented.getUserNo())) {
                    miniapp.onLike(beCommented.getOpenId(), commenter.getName(), article.getContent(), commentMapper.selectLikeCountByArticleId(articleId));
                }
            } else {
                commentMapper.deleteByPrimaryKey(comment.getId());
            }
            restResult = ResultUtil.createSuccessResult();
        } catch (Exception e) {
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    @Override
    public RestResult marketInfoLikeOrUnlikeByUserNo(String articleId, String beCommenteeUserNo, String commentatorUserNo) {
        RestResult restResult = null;
        try {
            Comment comment = commentMapper.selectIsLike("A-" + articleId, commentatorUserNo);
            if (comment == null) {  // 如果为查询到点赞，则添加数据
                MarketGoodsInfo marketGoodsInfo = marketGoodsInfoMapper.selectByPrimaryKey(Integer.parseInt(articleId));
                if (marketGoodsInfo == null) throw new BusiException(ErrorCodeEnum.ARTICLE_NOT_EXISTS);

                Comment record = new Comment();
                record.setArticleId("A-" + articleId);
                record.setContent("");
                if (StringUtils.isEmpty(beCommenteeUserNo)) {
                    record.setBecommenteeId(marketGoodsInfo.getCreatedBy());
                } else {
                    record.setBecommenteeId(beCommenteeUserNo);
                }
                record.setCommentatorId(commentatorUserNo);
                record.setType("0");
                record.setLevel("0");
                if (StringUtils.equals(record.getCommentatorId(), record.getBecommenteeId())) {
                    record.setIsRead(RongyDictEnum.COMMENT_IS_READ$YES.getCode());
                } else {
                    record.setIsRead(RongyDictEnum.COMMENT_IS_READ$NO.getCode());
                }
                record.setCreatedTime(new Date());
                record.setUpdatedTime(new Date());
                commentMapper.insert(record);

                UserInfo beCommented = userInfoMapper.selectByUserNo(beCommenteeUserNo);
                UserInfo commenter = userInfoMapper.selectByUserNo(commentatorUserNo);
                if (!StringUtils.equals(commenter.getUserNo(), beCommented.getUserNo())) {
                    miniapp.onLike(beCommented.getOpenId(), commenter.getName(), marketGoodsInfo.getContent(), commentMapper.selectLikeCountByArticleId("A-" + articleId));
                }
            } else {
                commentMapper.deleteByPrimaryKey(comment.getId());
            }
            restResult = ResultUtil.createSuccessResult();
        } catch (Exception e) {
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    @Override
    public int add(CommentDTO commentDto) {
        validType(commentDto.getType());
        validLevel(commentDto.getLevel());

        if (StringUtils.equals(commentDto.getType(), RongyDictEnum.COMMENT_TYPE$TEXT.getCode()) &&
                StringUtils.isEmpty(commentDto.getContent())) {
            throw new BusiException(ErrorCodeEnum.CONTENT_SHOULD_NOT_BE_EMPTY);
        }

        UserInfo becommentee = userInfoMapper.selectByUserNo(commentDto.getBecommenteeId());
        if (StringUtils.equals(commentDto.getType(), RongyDictEnum.COMMENT_TYPE$TEXT.getCode())
                && StringUtils.equals(commentDto.getLevel(), RongyDictEnum.COMMENT_LEVEL$2.getCode())
                && becommentee == null) {
            throw new BusiException(ErrorCodeEnum.BECOMMENTEE_NOT_EXIST);
        }

        UserInfo commentator = userInfoMapper.selectByUserNo(commentDto.getCommentatorId());
        if (commentator == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(commentator.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        Article article = articleMapper.selectByPrimaryKey(commentDto.getArticleId());
        if (article == null) throw new BusiException(ErrorCodeEnum.ARTICLE_NOT_EXISTS);

        Comment comment = transferDtoToComment(article, commentDto);

        commentMapper.insertSelective(comment);

        try {
            updateArticleIsHot(article, 1);
        } catch (Exception e) {
            logger.error("更新文章【{}】为热门异常：", article.getTitle(), e);
        }

        // 直接评论才发通知
        if (!StringUtils.equals(becommentee.getUserNo(), commentator.getUserNo())) {
            miniapp.onComment(becommentee.getOpenId(), commentator.getName(), article.getContent(), commentDto.getContent());
        }

        return comment.getId();
    }

    @Override
    public int addMarketInfoComment(CommentDTO commentDto) {
        validType(commentDto.getType());
        validLevel(commentDto.getLevel());

        if (StringUtils.equals(commentDto.getType(), RongyDictEnum.COMMENT_TYPE$TEXT.getCode()) &&
                StringUtils.isEmpty(commentDto.getContent())) {
            throw new BusiException(ErrorCodeEnum.CONTENT_SHOULD_NOT_BE_EMPTY);
        }

        UserInfo becommentee = userInfoMapper.selectByUserNo(commentDto.getBecommenteeId());
        if (StringUtils.equals(commentDto.getType(), RongyDictEnum.COMMENT_TYPE$TEXT.getCode())
                && StringUtils.equals(commentDto.getLevel(), RongyDictEnum.COMMENT_LEVEL$2.getCode())
                && becommentee == null) {
            throw new BusiException(ErrorCodeEnum.BECOMMENTEE_NOT_EXIST);
        }

        UserInfo commentator = userInfoMapper.selectByUserNo(commentDto.getCommentatorId());
        if (commentator == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(commentator.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        MarketGoodsInfo marketGoodsInfo = marketGoodsInfoMapper.selectByPrimaryKey(commentDto.getArticleId());
        if (marketGoodsInfo == null) throw new BusiException(ErrorCodeEnum.ARTICLE_NOT_EXISTS);

        Comment comment = transferDtoToMarketInfoComment(marketGoodsInfo, commentDto);

        commentMapper.insertSelective(comment);

        // 直接评论才发通知
        if (!StringUtils.equals(becommentee.getUserNo(), commentator.getUserNo())) {
            miniapp.onComment(becommentee.getOpenId(), commentator.getName(), marketGoodsInfo.getContent(), commentDto.getContent());
        }

        return comment.getId();
    }

    @Override
    public void del(String userNo, int commentId) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        Comment comment = commentMapper.selectByPrimaryKey(commentId);

        Article article  = articleMapper.selectByPrimaryKey(Integer.valueOf(comment.getArticleId()));

        if (!StringUtils.equals(comment.getCommentatorId(), userNo)
                && !StringUtils.equals(article.getCreatedBy(), userNo)) {
            throw new BusiException(ErrorCodeEnum.COMMENT_USER_NOT_MATCH);
        }

        commentMapper.deleteByPrimaryKey(commentId);

        try {
            updateArticleIsHot(article, -1);
        } catch (Exception e) {
            logger.error("更新文章【{}】取消为热门异常：", article.getTitle(), e);
        }
    }

    @Override
    public void delMarketInfoComment(String userNo, int commentId) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        Comment comment = commentMapper.selectByPrimaryKey(commentId);

        MarketGoodsInfo marketGoodsInfo = marketGoodsInfoMapper.selectByPrimaryKey(Integer.valueOf(comment.getArticleId().split("-")[1]));

        if (!StringUtils.equals(comment.getCommentatorId(), userNo)
                && !StringUtils.equals(marketGoodsInfo.getCreatedBy(), userNo)) {
            throw new BusiException(ErrorCodeEnum.COMMENT_USER_NOT_MATCH);
        }

        commentMapper.deleteByPrimaryKey(commentId);
    }

    private Comment transferDtoToMarketInfoComment(MarketGoodsInfo marketGoodsInfo, CommentDTO commentDto) {
        Comment comment = new Comment();

        if (HttpUtil.msgSecCheck(miniapp.getAccessToken(), commentDto.getContent())) {
            throw new BusiException(ErrorCodeEnum.COMMENT_CONTENT_CONTAINS_SENSITIVE);
        }

        comment.setArticleId("A-" + String.valueOf(commentDto.getArticleId()));
        comment.setContent(commentDto.getContent());
        comment.setType(commentDto.getType());
        if (StringUtils.isEmpty(commentDto.getBecommenteeId())) {
            comment.setBecommenteeId(marketGoodsInfo.getCreatedBy());
        } else {
            comment.setBecommenteeId(commentDto.getBecommenteeId());
        }
        comment.setCommentatorId(commentDto.getCommentatorId());
        comment.setLevel(commentDto.getLevel());
        if (StringUtils.equals(comment.getCommentatorId(), comment.getBecommenteeId())) {
            comment.setIsRead(RongyDictEnum.COMMENT_IS_READ$YES.getCode());
        } else {
            comment.setIsRead(RongyDictEnum.COMMENT_IS_READ$NO.getCode());
        }
        comment.setCreatedTime(new Date());

        comment.setTargetId(commentDto.getTargetId());

        return comment;
    }

    private Comment transferDtoToComment(Article article, CommentDTO commentDto) {
        Comment comment = new Comment();

        if (HttpUtil.msgSecCheck(miniapp.getAccessToken(), commentDto.getContent())) {
            throw new BusiException(ErrorCodeEnum.COMMENT_CONTENT_CONTAINS_SENSITIVE);
        }

        comment.setArticleId(String.valueOf(commentDto.getArticleId()));
        comment.setContent(commentDto.getContent());
        comment.setType(commentDto.getType());
        if (StringUtils.isEmpty(commentDto.getBecommenteeId())) {
            comment.setBecommenteeId(article.getCreatedBy());
        } else {
            comment.setBecommenteeId(commentDto.getBecommenteeId());
        }
        comment.setCommentatorId(commentDto.getCommentatorId());
        comment.setLevel(commentDto.getLevel());
        if (StringUtils.equals(comment.getCommentatorId(), comment.getBecommenteeId())) {
            comment.setIsRead(RongyDictEnum.COMMENT_IS_READ$YES.getCode());
        } else {
            comment.setIsRead(RongyDictEnum.COMMENT_IS_READ$NO.getCode());
        }
        comment.setCreatedTime(new Date());

        comment.setTargetId(commentDto.getTargetId());

        return comment;
    }

    private void validType(String type) {
        if (!StringUtils.equals(type, RongyDictEnum.COMMENT_TYPE$LIKE.getCode())
                && !StringUtils.equals(type, RongyDictEnum.COMMENT_TYPE$TEXT.getCode())) {
            throw new BusiException(ErrorCodeEnum.COMMENT_TYPE_INVALID);
        }
    }

    private void validLevel(String level) {
        if (!StringUtils.equals(level, RongyDictEnum.COMMENT_LEVEL$1.getCode())
                && !StringUtils.equals(level, RongyDictEnum.COMMENT_LEVEL$2.getCode())) {
            throw new BusiException(ErrorCodeEnum.COMMENT_LEVEL_INVALID);
        }
    }

    private void updateArticleIsHot(Article article, int delta) {
        int nowCount = commentMapper.selectLikeCountByArticleId(article.getId().toString()) + delta;

        if (nowCount >= 15 && StringUtils.equals(article.getIsHot(), RongyDictEnum.ARTICLE_IS_HOT$NO.getCode())) {
            Article forUpdate = new Article();
            forUpdate.setId(article.getId());
            forUpdate.setIsHot(RongyDictEnum.ARTICLE_IS_HOT$YES.getCode());
            articleMapper.updateByPrimaryKeySelective(forUpdate);
        } else if (nowCount < 15 && StringUtils.equals(article.getIsHot(), RongyDictEnum.ARTICLE_IS_HOT$YES.getCode())) {
            Article forUpdate = new Article();
            forUpdate.setId(article.getId());
            forUpdate.setIsHot(RongyDictEnum.ARTICLE_IS_HOT$NO.getCode());
            articleMapper.updateByPrimaryKeySelective(forUpdate);
        }
    }


}
