package com.cd.service.impl;

import cn.hutool.json.JSONUtil;
import com.cd.common.BigTypeEnum;
import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.dao.*;
import com.cd.entity.*;
import com.cd.entity.dto.ArticleDTO;
import com.cd.entity.dto.ArticleQueryCondition;
import com.cd.entity.vo.*;
import com.cd.enums.ErrorCodeEnum;
import com.cd.enums.RongyDictEnum;
import com.cd.exception.BusiException;
import com.cd.service.ArticleService;
import com.cd.utils.CommonUtil;
import com.cd.utils.HttpUtil;
import com.cd.utils.MiniappNotificationUtil;
import com.cd.utils.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleMapper articleMapper;
   	@Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private MiniappNotificationUtil miniapp;
    @Resource
    private TopicArticleRelMapper topicArticleRelMapper;
    @Resource
    private TopicClsMapper topicClsMapper;



    @Override
    public void create(ArticleDTO articleDTO) {
        articleDTO.setTitle(CommonUtil.uniqueNo());

        // valid user
        UserInfo userInfo = userInfoMapper.selectByUserNo(articleDTO.getUserNo());
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        // valid type
        Category category = categoryMapper.selectByTypeNoAndRoleId(articleDTO.getTypeNo(), userInfo.getRoleId());

        if (category == null) throw new BusiException(ErrorCodeEnum.CATEGORY_CAN_NOT_USE_OR_NOT_EXIST);

        Article article = transferDto(articleDTO);

        boolean containsSensitive = !StringUtils.isEmpty(articleDTO.getContent()) &&
                HttpUtil.msgSecCheck(miniapp.getAccessToken(), articleDTO.getContent());

        if (containsSensitive) {
            throw new BusiException(ErrorCodeEnum.ARTICLE_CONTENT_CONTAINS_SENSITIVE);
        }

        articleMapper.insertSelective(article);
        articleDTO.setId(article.getId());
        logger.info("create article {} success", articleDTO.getTitle());
    }

    private Article transferDto(ArticleDTO articleDTO) {
        Article article = new Article();

        article.setTypeNo(articleDTO.getTypeNo());
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setImageUrl(JSONUtil.toJsonStr(articleDTO.getImageUrl()));
        article.setVideoUrl("[]");
        article.setStatus(RongyDictEnum.ARTICLE_STATUS$CHECKED.getCode());
        article.setCreatedBy(articleDTO.getUserNo());
        article.setIsHot(RongyDictEnum.ARTICLE_IS_HOT$NO.getCode());
        article.setCreatedTime(new Date());
        article.setIsAnonymous(articleDTO.getIsAnonymous());
        return article;
    }

    @Override
    public DynamicListVOs queryArticles(ArticleQueryCondition articleQC) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(articleQC.getUserNo());
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        logger.info("select articles by page, pageNum[{}], pageSize[{}]", articleQC.getPageNum(), articleQC.getPageSize());

        PageInfo<Article> pageInfo = PageHelper.
                startPage(articleQC.getPageNum(), articleQC.getPageSize()).
                doSelectPageInfo(() -> articleMapper.selectByQC(articleQC));

        List<DynamicListVO> rets = new ArrayList<>(pageInfo.getSize());

        pageInfo.getList().forEach(article -> {
            DynamicListVO vo = getArticleDetail(article, articleQC.getUserNo());
            if (vo != null) {
                rets.add(vo);
            }
        });

        List<DynamicListVO> stickyArticles = new LinkedList<>();

        List<Article> originSticky = articleMapper.selectStickyArticlesByType(articleQC.getNo());

        originSticky.forEach(article -> {
            DynamicListVO vo = getArticleDetail(article, articleQC.getUserNo());
            if (vo != null) {
                stickyArticles.add(vo);
            }
        });

        DynamicListVOs dynamicListVOs = new DynamicListVOs(rets, stickyArticles, articleQC.getPageNum(), pageInfo.getPages());


        return dynamicListVOs;
    }

    private DynamicListVO getArticleDetail(Article article, String userNo) {
        // 创建人
        UserInfo author = userInfoMapper.selectByUserNo(article.getCreatedBy());
        if (author == null) {
            logger.info("article {}'s author == null", article.getTitle());

            return null;
        }

        // 所有评论
        List<Comment> comments = commentMapper.selectByArticleId(article.getId().toString());

        DynamicUserInfo dynamicUserInfo = new DynamicUserInfo(author.getImageUrl(), author.getName(), author.getSex(), author.getUserNo());

        DynamicArticle dynamicArticle = new DynamicArticle(
                article.getCreatedTime(),
                article.getContent(),
                JSONUtil.toList(JSONUtil.parseArray(article.getImageUrl()), String.class),
                article.getVideoUrl(),
                article.getTypeNo(),
                article.getId()
        );
        dynamicArticle.setViewCount(article.getViewCount());
        dynamicArticle.setIsAnonymous(article.getIsAnonymous());

        int isLike = 0;
        int likeCount = 0;
        List<DynamicComment> dynamicComments = new LinkedList<>();
        List<DynamicComment> dynamicLikes = new LinkedList<>();

        for (Comment comment : comments) {
            // 为点赞
            if (StringUtils.equals(RongyDictEnum.COMMENT_TYPE$LIKE.getCode(), comment.getType())) {
                // 为本人点赞
                if (StringUtils.equals(comment.getCommentatorId(), userNo)) {
                    isLike = 1;
                }

                likeCount++;

                dynamicLikes.add(transferCommentToDynamic(comment));
            } else { // 为评论
                dynamicComments.add(transferCommentToDynamic(comment));
            }
        }

        DynamicOperation dynamicOperation = new DynamicOperation(isLike, likeCount, dynamicComments, dynamicLikes);
        DynamicListVO dynamicListVO = new DynamicListVO(dynamicUserInfo, dynamicArticle, dynamicOperation);

        TopicCls topicCls = topicArticleRelMapper.selectByInfoIdAndBigType(article.getId(), BigTypeEnum.BIGTYPE$0.getKey());
        dynamicListVO.setTopicCls(topicCls);

        return dynamicListVO;
    }

    private DynamicComment transferCommentToDynamic(Comment comment) {
        return new DynamicComment(comment.getId(), comment.getCommentatorName(), comment.getBecommenteeName(), comment.getLevel(),
                comment.getCommentatorId(), comment.getBecommenteeId(), comment.getContent(), comment.getCommentatorAvatar(),
                comment.getTargetId(), comment.getCreatedTime());
    }
    /**
     * 根据内容模糊查询
     * @return
     */
    @Override
    public RestResult getByContentLike(String userNo, String contentLike) {
        logger.info("根据参数模糊匹配帖子内容，查询参数[{}]", contentLike);
        RestResult restResult = null;
        List<Article> articles = null;
        try {
            articles = articleMapper.selectByContentLike(contentLike);

            List<DynamicListVO> rets = new ArrayList<>(articles.size());

            articles.forEach(article -> {
                DynamicListVO vo = getArticleDetail(article, userNo);
                if (vo != null) {
                    rets.add(vo);
                }
            });

//            UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
//            if (userInfo == null) {
//                return ResultUtil.createFailedResult();
//            }
//            // 查询帖子相关的信息
//            for (int i = 0; i < articles.size(); i++) {
//                // 判断该帖子是不是该userNo下点赞的
//                Comment comment = commentMapper.selectByArticleIdAndUserId(articles.get(i).getId().toString(), userInfo.getId().toString());
//                if (comment != null && StringUtils.equals(comment.getType() , "0")) {
//                    articles.get(i).setIsLike("1");
//                } else {
//                    articles.get(i).setIsLike("1");
//                }
//
//                // 查询点赞的人数
//                Integer likeCount = commentMapper.selectLikeCountByArticleId(articles.get(i).getId().toString());
//                articles.get(i).setLike(likeCount);
//
//                // 查询该帖子的评论列表
//                List<Comment> comments = commentMapper.selectByArticleId(articles.get(i).getId().toString());
//                articles.get(i).setComment(comments);
//
//                // 查询该帖子创建者
//                UserInfo createUser = userInfoMapper.selectByUserNo(articles.get(i).getCreatedBy());
//                articles.get(i).setUserInfo(createUser);
//            }
            restResult = ResultUtil.createSuccessResult(rets);
        } catch (Exception e) {
            logger.error("根据参数[{}]模糊匹配帖子异常", contentLike, e);
            restResult = ResultUtil.createFailedResult();
        }
        return restResult;
    }

    @Override
    public DynamicListVO selectById(int id, String userNo) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        Article article = articleMapper.selectByPrimaryKey(id);

        return getArticleDetail(article, userNo);
    }

    @Override
    public List<DynamicListVO> listByUserNo(String userNo, String searchUserNo) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(searchUserNo);
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        List<Article> articles = articleMapper.selectByUserNo(userNo);
        // 判断是否是查看的他人的
        if (!StringUtils.equals(userNo, searchUserNo)) {
            List<Article> tmpList = new ArrayList<>();
            articles.forEach(x -> {
                if (!StringUtils.equals(x.getIsAnonymous(), "1")) {
                    tmpList.add(x);
                }
            });
            articles = tmpList;
        }

        List<DynamicListVO> rets = new ArrayList<>(articles.size());

        articles.forEach(article -> {
            DynamicListVO vo = getArticleDetail(article, searchUserNo);
            if (vo != null) {
                rets.add(vo);
            }
        });

        return rets;
    }

    @Override
    public List<DynamicListVO> listUserLiked(String userNo) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        List<Article> articles = articleMapper.listByUserLiked(userNo);

        List<DynamicListVO> rets = new ArrayList<>(articles.size());

        articles.forEach(article -> {
            DynamicListVO vo = getArticleDetail(article, userNo);
            if (vo != null) {
                rets.add(vo);
            }
        });

        return rets;
    }

    @Override
    public boolean del(String userNo, int id) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        Article article = articleMapper.selectByPrimaryKey(id);
        if (article == null) throw new BusiException(ErrorCodeEnum.ARTICLE_NOT_EXISTS);

        if (!StringUtils.equals(userNo, article.getCreatedBy())) {
            throw new BusiException(ErrorCodeEnum.ARTICLE_USER_NOT_MATCH);
        }

        boolean retFlag = true;

        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            TopicCls topicClsQC = new TopicCls();
            topicClsQC.setBigType(BigTypeEnum.BIGTYPE$0.getKey());
            topicClsQC.setSmallType(article.getTypeNo());
            List<TopicCls> topicClsList = topicClsMapper.getTopicsByCondition(topicClsQC);
            for (TopicCls topicCls : topicClsList) {
                topicArticleRelMapper.deleteByTopicIdAndInfoId(topicCls.getId(), id);
            }
            int affectedRows = articleMapper.deleteByPrimaryKey(id);

            if (affectedRows != 1) {
                retFlag = false;
            } else {
                commentMapper.deleteByArticleId(String.valueOf(id));
            }
            txManager.commit(status);
        } catch (Throwable e) {
            logger.error("删除帖子异常！", e);
            txManager.rollback(status);
        }

        return retFlag;
    }

    @Override
    public int viewCountAdd(Integer articleId) {
        int i = articleMapper.viewCountAdd(articleId);
        if (i != 1) {
            logger.error("访问量增加失败，articleId:{}", articleId);
        }
        return i;
    }


    @Override
    public DynamicListVOs queryArticlesByTopicId(ArticleQueryCondition articleQC) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(articleQC.getUserNo());
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        logger.info("select articles by page, pageNum[{}], pageSize[{}]", articleQC.getPageNum(), articleQC.getPageSize());

        PageInfo<Article> pageInfo = PageHelper.
                startPage(articleQC.getPageNum(), articleQC.getPageSize()).
                doSelectPageInfo(() -> articleMapper.selectByTopciId(articleQC));

        List<DynamicListVO> rets = new ArrayList<>(pageInfo.getSize());

        pageInfo.getList().forEach(article -> {
            DynamicListVO vo = getArticleDetail(article, articleQC.getUserNo());
            if (vo != null) {
                rets.add(vo);
            }
        });

        List<DynamicListVO> stickyArticles = new LinkedList<>();

        /*List<Article> originSticky = articleMapper.selectStickyArticlesByType(articleQC.getNo());

        originSticky.forEach(article -> {
            DynamicListVO vo = getArticleDetail(article, articleQC.getUserNo());
            if (vo != null) {
                stickyArticles.add(vo);
            }
        });*/

        DynamicListVOs dynamicListVOs = new DynamicListVOs(rets, stickyArticles, articleQC.getPageNum(), pageInfo.getList().size());


        return dynamicListVOs;
    }
}