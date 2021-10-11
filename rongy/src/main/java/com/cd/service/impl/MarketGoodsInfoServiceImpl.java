package com.cd.service.impl;

import cn.hutool.json.JSONUtil;
import com.cd.common.BigTypeEnum;
import com.cd.common.GoodsDictEnum;
import com.cd.dao.CommentMapper;
import com.cd.dao.MarketGoodsInfoMapper;
import com.cd.dao.TopicArticleRelMapper;
import com.cd.dao.UserInfoMapper;
import com.cd.entity.Comment;
import com.cd.entity.MarketGoodsInfo;
import com.cd.entity.TopicCls;
import com.cd.entity.UserInfo;
import com.cd.entity.dto.GoodsInfoQueryCondition;
import com.cd.entity.vo.*;
import com.cd.enums.ErrorCodeEnum;
import com.cd.enums.MarketGoodsEnum;
import com.cd.enums.RongyDictEnum;
import com.cd.exception.BusiException;
import com.cd.service.MarketGoodsInfoService;
import com.cd.utils.CommonUtil;
import com.cd.utils.HttpUtil;
import com.cd.utils.MiniappNotificationUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MarketGoodsInfoServiceImpl implements MarketGoodsInfoService {

    private Logger logger = LoggerFactory.getLogger(MarketGoodsInfoServiceImpl.class);

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private MarketGoodsInfoMapper marketGoodsInfoMapper;
    @Autowired
    private MiniappNotificationUtil miniapp;
    @Resource
    private TopicArticleRelMapper topicArticleRelMapper;

    @Override
    public GoodsListVOs queryAllGoodsInfo(GoodsInfoQueryCondition goodsInfoQC) {
        //logger.info("查询用户信息，userNo:{}", goodsInfoQC.getUserNo());
//        UserInfo userInfo = userInfoMapper.selectByUserNo(goodsInfoQC.getUserNo());
//        if (userInfo == null) {
//            throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);
//        }
        // 判断用户状态
        /*if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) {
            throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);
        }*/

        if (goodsInfoQC == null) {
            return null;
        } else {
            if (StringUtils.equals(goodsInfoQC.getNo(),"0")) {
                goodsInfoQC.setNo(null);
            }
        }

        logger.info("分页查询商品, pageNum[{}], pageSize[{}]", goodsInfoQC.getPageNum(), goodsInfoQC.getPageSize());

        PageInfo<MarketGoodsInfo> pageInfo = PageHelper.
                startPage(goodsInfoQC.getPageNum(), goodsInfoQC.getPageSize()).
                doSelectPageInfo(() -> marketGoodsInfoMapper.selectByQC(goodsInfoQC));

        List<GoodsListVO> rets = new ArrayList<>(pageInfo.getSize());

        pageInfo.getList().forEach(goodsInfo -> {
            GoodsListVO vo = getGoodInfoDetail(goodsInfo, goodsInfoQC.getUserNo());
            if (vo != null) {
                rets.add(vo);
            }
        });

        //List<DynamicListVO> stickyArticles = new LinkedList<>();

        // List<Article> originSticky = articleMapper.selectStickyArticlesByType(articleQC.getNo());

        /*originSticky.forEach(article -> {
            DynamicListVO vo = getArticleDetail(article, articleQC.getUserNo());
            if (vo != null) {
                stickyArticles.add(vo);
            }
        });*/

        GoodsListVOs goodsListVOs = new GoodsListVOs(rets, goodsInfoQC.getPageNum(), pageInfo.getPages());

        return goodsListVOs;
    }

    private GoodsListVO getGoodInfoDetail(MarketGoodsInfo goodsInfo, String userNo) {
        // 创建人
        UserInfo author = userInfoMapper.selectByUserNo(goodsInfo.getCreatedBy());
        if (author == null) {
            logger.info("商品的创建者为空，userNo", goodsInfo.getCreatedBy());
            return null;
        }

        // 所有评论
        List<Comment> comments = commentMapper.selectByArticleId("A-" + goodsInfo.getId().toString().trim());

        DynamicUserInfo dynamicUserInfo = new DynamicUserInfo(author.getImageUrl(), author.getName(), author.getSex(), author.getUserNo());
        goodsInfo.setImageUrls(JSONUtil.toList(JSONUtil.parseArray(goodsInfo.getImageUrl()), String.class));
        /*DynamicArticle dynamicArticle = new DynamicArticle(
                article.getCreatedTime(),
                article.getContent(),
                JSONUtil.toList(JSONUtil.parseArray(article.getImageUrl()), String.class),
                article.getVideoUrl(),
                article.getTypeNo(),
                article.getId()
        );*/

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
        GoodsListVO goodsListVO = new GoodsListVO(dynamicUserInfo, goodsInfo, dynamicOperation);

        TopicCls topicCls = topicArticleRelMapper.selectByInfoIdAndBigType(goodsInfo.getId(), BigTypeEnum.BIGTYPE$1.getKey());
        goodsListVO.setTopicCls(topicCls);

        return goodsListVO;
    }

    private DynamicComment transferCommentToDynamic(Comment comment) {
        return new DynamicComment(comment.getId(), comment.getCommentatorName(), comment.getBecommenteeName(), comment.getLevel(),
                comment.getCommentatorId(), comment.getBecommenteeId(), comment.getContent(), comment.getCommentatorAvatar(),
                comment.getTargetId(), comment.getCreatedTime());
    }

    @Override
    public void modifyContentStatus(Integer id, String dealStatus) {
        //查询一下该商品帖子是否存在
        MarketGoodsInfo marketGoodsInfo = marketGoodsInfoMapper.selectByPrimaryKey(id);

        if(marketGoodsInfo == null)
            throw new BusiException(ErrorCodeEnum.ARTICLE_NOT_EXISTS);

        //初始化标志
        int flag = 0;

        //已完成，更新状态
        if(StringUtils.equals(MarketGoodsEnum.TZSC_GOODS_DEAL_TYPE$FINISHED.getKey(), dealStatus)){
            //完成
            marketGoodsInfo.setStatus(MarketGoodsEnum.TZSC_GOODS_STATUS$DONE.getKey());
            marketGoodsInfo.setUpdatedTime(new Date());
            flag = marketGoodsInfoMapper.updateByPrimaryKey(marketGoodsInfo);
        }else if(StringUtils.equals(MarketGoodsEnum.TZSC_GOODS_DEAL_TYPE$DELETE.getKey(), dealStatus)){
            //删除
            marketGoodsInfo.setDeleteFlag(MarketGoodsEnum.DELETE_FLAG$YES.getKey());
            marketGoodsInfo.setUpdatedTime(new Date());
            flag = marketGoodsInfoMapper.updateByPrimaryKey(marketGoodsInfo);
        }

        if(flag != 1){
            throw new BusiException(ErrorCodeEnum.MARKETGOODSDONE2000);
        }
    }

    @Override
    public void create(MarketGoodsInfo marketGoodsInfo) {

        CommonUtil.isBlank("商品类型", marketGoodsInfo.getCategoryId().toString());
        CommonUtil.isBlank("发布类型", marketGoodsInfo.getType());
        CommonUtil.isBlank("价格", marketGoodsInfo.getPrice().toString());
        CommonUtil.isPrice(marketGoodsInfo.getPrice().toString());

        // valid user
        UserInfo userInfo = userInfoMapper.selectByUserNo(marketGoodsInfo.getUserNo());
        if (userInfo == null)
            throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode()))
            throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);


        List<String> keys = Arrays.stream(GoodsDictEnum.values())
                .map(item -> item.getCode())
                .collect(Collectors.toList());


        //校验帖子类型是否有效
        if(!keys.contains(marketGoodsInfo.getCategoryId().toString())){
            throw new BusiException(ErrorCodeEnum.MARKETGOODSDONE2001);
        }

        //校验发布类型是否有效
        if(!StringUtils.equals(marketGoodsInfo.getType(), MarketGoodsEnum.TZSC_GOODS_PUBLISH_TYPE$BUY.getKey())
                && !StringUtils.equals(marketGoodsInfo.getType(), MarketGoodsEnum.TZSC_GOODS_PUBLISH_TYPE$SELL.getKey())){
            throw new BusiException(ErrorCodeEnum.MARKETGOODSDONE2002);
        }

        boolean containsSensitive = HttpUtil.msgSecCheck(miniapp.getAccessToken(), marketGoodsInfo.getContent());

        if (containsSensitive) {
            throw new BusiException(ErrorCodeEnum.ARTICLE_CONTENT_CONTAINS_SENSITIVE);
        }

        marketGoodsInfo.setCreatedBy(marketGoodsInfo.getUserNo());
        marketGoodsInfo.setTitle(CommonUtil.uniqueNo());
        marketGoodsInfo.setImageUrl(JSONUtil.toJsonStr(marketGoodsInfo.getImageUrls()));
        marketGoodsInfo.setVideoUrl("[]");
        marketGoodsInfo.setTabs(JSONUtil.toJsonStr(marketGoodsInfo.getTabList()));
        marketGoodsInfo.setIsHot(RongyDictEnum.ARTICLE_IS_HOT$NO.getCode());
        marketGoodsInfo.setStatus(MarketGoodsEnum.TZSC_GOODS_STATUS$DOING.getKey());
        marketGoodsInfo.setCreatedTime(new Date());

        marketGoodsInfoMapper.insertSelective(marketGoodsInfo);
    }

    @Override
    public List<GoodsListVO> getListByUserNo(String userNo, String searchUserNo) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(searchUserNo);
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        List<MarketGoodsInfo> goodsInfos = marketGoodsInfoMapper.selectByUserNo(userNo);

        List<GoodsListVO> rets = new ArrayList<>(goodsInfos.size());

        goodsInfos.forEach(goodsInfo -> {
            GoodsListVO vo = getGoodInfoDetail(goodsInfo, searchUserNo);
            if (vo != null) {
                rets.add(vo);
            }
        });
        return rets;
    }

    @Override
    public GoodsListVO selectById(int goodsId, String userNo) {
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        MarketGoodsInfo marketGoodsInfo = marketGoodsInfoMapper.selectByPrimaryKey(goodsId);

        return getGoodInfoDetail(marketGoodsInfo, userNo);
    }

    @Override
    public int viewCountAdd(Integer goodsInfoId) {
        int i = marketGoodsInfoMapper.viewCountAdd(goodsInfoId);
        if (i != 1) {
            logger.error("访问量增加失败，goodsInfoId:{}", goodsInfoId);
        }
        return i;
    }

    @Override
    public GoodsListVOs queryGoodsInfosByTopicId(GoodsInfoQueryCondition goodsInfoQC) {
        logger.info("根据主题id查询商品, pageNum[{}], pageSize[{}]", goodsInfoQC.getPageNum(), goodsInfoQC.getPageSize());
        PageInfo<MarketGoodsInfo> pageInfo = PageHelper.
                startPage(goodsInfoQC.getPageNum(), goodsInfoQC.getPageSize()).
                doSelectPageInfo(() -> marketGoodsInfoMapper.selectByTopciId(goodsInfoQC));

        List<GoodsListVO> rets = new ArrayList<>(pageInfo.getSize());

        pageInfo.getList().forEach(goodsInfo -> {
            GoodsListVO vo = getGoodInfoDetail(goodsInfo, goodsInfoQC.getUserNo());
            if (vo != null) {
                rets.add(vo);
            }
        });

        GoodsListVOs goodsListVOs = new GoodsListVOs(rets, goodsInfoQC.getPageNum(), pageInfo.getList().size());

        return goodsListVOs;
    }
    
}
