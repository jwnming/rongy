package com.cd.service.impl;

import cn.hutool.json.JSONUtil;
import com.cd.common.BigTypeEnum;
import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.dao.*;
import com.cd.entity.*;
import com.cd.entity.dto.*;
import com.cd.entity.vo.DynamicListVO;
import com.cd.entity.vo.DynamicListVOs;
import com.cd.entity.vo.GoodsListVO;
import com.cd.entity.vo.GoodsListVOs;
import com.cd.enums.ErrorCodeEnum;
import com.cd.enums.RongyDictEnum;
import com.cd.exception.BusiException;
import com.cd.service.CategoryService;
import com.cd.service.TopicClsService;
import com.cd.utils.CommonUtil;
import com.cd.utils.HttpUtil;
import com.cd.utils.MiniappNotificationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.cd.service.ArticleService;
import com.cd.service.MarketGoodsInfoService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import sun.swing.PrintingStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class TopicClsServiceImpl implements TopicClsService {

    @Autowired
    private CategoryService categoryService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Autowired
    private MiniappNotificationUtil miniapp;
    @Resource
    private TopicClsMapper topicClsMapper;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private MarketGoodsInfoService marketGoodsInfoService;
    @Resource
    private TopicArticleRelMapper topicArticleRelMapper;
    private Logger logger = LoggerFactory.getLogger(TopicClsServiceImpl.class);

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private MarketGoodsInfoMapper marketGoodsInfoMapper;
    @Autowired
    private PlatformTransactionManager txManager;




    @Override
    public String getBigTypes() throws Exception {
        return CommonUtil.toJson(BigTypeEnum.values());
    }

    @Override
    public void create(TopicClsDTO topicClsDTO) {
        CommonUtil.isBlank("主题标题", topicClsDTO.getTitle());
        CommonUtil.isBlank("主题类型", topicClsDTO.getBigType());
        if(StringUtils.equals(topicClsDTO.getBigType(), BigTypeEnum.BIGTYPE$0.getKey())){
            CommonUtil.isBlank("发布动态类型", topicClsDTO.getSmallType());
        }
        CommonUtil.isBlank("主题描述", topicClsDTO.getContent());

        // valid user
        UserInfo userInfo = userInfoMapper.selectByUserNo(topicClsDTO.getUserNo());
        if (userInfo == null)
            throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode()))
            throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        if(StringUtils.equals(userInfo.getRoleId(), RongyDictEnum.CATEGORY_AUTH$ADMIN.getCode())){
            throw new BusiException(ErrorCodeEnum.USER_NOT_ADMIN);
        }

        boolean containsSensitive = HttpUtil.msgSecCheck(miniapp.getAccessToken(), topicClsDTO.getContent());

        if (containsSensitive) {
            throw new BusiException(ErrorCodeEnum.ARTICLE_CONTENT_CONTAINS_SENSITIVE);
        }

        TopicCls topicCls = new TopicCls();
        topicCls.setBigType(topicClsDTO.getBigType());
        topicCls.setContent(topicClsDTO.getContent());
        topicCls.setTitle(topicClsDTO.getTitle());
        topicCls.setVadioUrl("[]");
        topicCls.setImageUrl(JSONUtil.toJsonStr(topicClsDTO.getImageUrl()));
        topicCls.setCreatedTime(new Date());
        topicCls.setSmallType(topicClsDTO.getSmallType());
        topicCls.setCreateBy(userInfo.getUserNo());
        topicClsMapper.insertSelective(topicCls);
    }

    @Override
    public boolean del(String userNo, int id) {
        // valid user
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null)
            throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode()))
            throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);

        if(StringUtils.equals(userInfo.getRoleId(), RongyDictEnum.CATEGORY_AUTH$ADMIN.getCode())){
            throw new BusiException(ErrorCodeEnum.USER_NOT_ADMIN);
        }

        TopicCls topicCls = topicClsMapper.selectByPrimaryKey(id);
        if(topicCls == null){
            throw new BusiException(ErrorCodeEnum.TOPICNOTFOUND3000);
        }

        topicArticleRelMapper.deleteByTopicId(topicCls.getId());

        int flag = topicClsMapper.deleteByPrimaryKey(id);

        if(flag == 1)
            return true;
        return false;
    }

    @Override
    public void newArticleWithTopic(ArticleDTO articleDTO, String topicClsId) {
        // valid topicCls
        TopicCls topicCls = topicClsMapper.selectByPrimaryKey(Integer.parseInt(topicClsId));
        if (topicCls == null) throw new BusiException(ErrorCodeEnum.TOPICNOTFOUND3000);
        if(!StringUtils.equals(topicCls.getBigType(), BigTypeEnum.BIGTYPE$0.getKey())){
            throw new BusiException(ErrorCodeEnum.TOPICNOTFOUND3001);
        }
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try{
            articleService.create(articleDTO);
            if(!StringUtils.equals(topicCls.getSmallType(), articleDTO.getTypeNo())){
                throw new BusiException(ErrorCodeEnum.TOPICNOTFOUND3003);
            }
            TopicArticleRel topicArticleRel = new TopicArticleRel();
            topicArticleRel.setTopicClsId(Integer.parseInt(topicClsId));
            topicArticleRel.setInfoId(articleDTO.getId());
            topicArticleRelMapper.insert(topicArticleRel);
            txManager.commit(status);
        }catch(Throwable e){
            logger.error("出现异常:", e);
            txManager.rollback(status);
            throw e;
        }

    }

    @Override
    public void newGoodsWithTopic(MarketGoodsInfo marketGoodsInfo, String topicClsId) {
        // valid topicCls
        TopicCls topicCls = topicClsMapper.selectByPrimaryKey(Integer.parseInt(topicClsId));
        if (topicCls == null) throw new BusiException(ErrorCodeEnum.TOPICNOTFOUND3000);
        if(!StringUtils.equals(topicCls.getBigType(), BigTypeEnum.BIGTYPE$1.getKey())){
            throw new BusiException(ErrorCodeEnum.TOPICNOTFOUND3002);
        }

        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            marketGoodsInfoService.create(marketGoodsInfo);
            if(!StringUtils.equals(topicCls.getSmallType(), String.valueOf(marketGoodsInfo.getCategoryId()))){
                throw new BusiException(ErrorCodeEnum.TOPICNOTFOUND3003);
            }
            TopicArticleRel topicArticleRel = new TopicArticleRel();
            topicArticleRel.setTopicClsId(Integer.parseInt(topicClsId));
            topicArticleRel.setInfoId(marketGoodsInfo.getId());
            topicArticleRelMapper.insertSelective(topicArticleRel);
            txManager.commit(status);
        }catch(Throwable e){
            logger.error("出现异常:", e);
            txManager.rollback(status);
            throw e;
        }
    }


    @Override
    public int viewCountAdd(Integer topicId) {
        int i = topicClsMapper.viewCountAdd(topicId);
        if (i != 1) {
            logger.error("访问量增加失败，topicId:{}", topicId);
        }
        return i;
    }

    @Override
    public List<TopicCls> queryTopics() {
        List<TopicCls> topicCls = topicClsMapper.queryTopics();
        for(TopicCls topicCls1 : topicCls){
            Integer id = topicCls1.getId();
            Integer count = topicArticleRelMapper.count(id);
            topicCls1.setJoinCount(count);
        }
        return topicCls;
    }


    @Override
    public TopicDTO getTopicById(Integer topicId, String userNo) {
        try {
            logger.info("根据id查询主题详情开始");
            TopicDTO topicDTO = new TopicDTO();
            // 查询主题信息
            TopicCls topicCls = topicClsMapper.selectByPrimaryKey(topicId);
            if (topicCls != null) {
                topicCls.setImageUrls(JSONUtil.toList(JSONUtil.parseArray(topicCls.getImageUrl()), String.class));
            }
            topicDTO.setTopicCls(topicCls);

            // 获取主题下的跟帖列表
            List<TopicArticleRel> topicArticleRels = topicArticleRelMapper.selectByTopicClsId(topicCls.getId());
            if (topicArticleRels == null) {
                topicArticleRels = new ArrayList<>();
            }

            if ((topicCls.getBigType()).equals(BigTypeEnum.BIGTYPE$0.getKey())) {   // 动态  永远进入这个分支
            // if ((topicCls.getBigType()).equals(BigTypeEnum.BIGTYPE$0.getKey())) {   // 动态
                logger.info("主题为动态");
                ArticleQueryCondition articleQC = new ArticleQueryCondition();
                articleQC.setTopicId(String.valueOf(topicId));
                List<Article> articles = articleMapper.selectByTopciId(articleQC);
                if (articles == null) articles = new ArrayList<>();
                Set<String> set = new HashSet<>();
                if (articles != null) {
                    articles.forEach(x -> {
                        set.add(x.getCreatedBy());
                    });
                }

                List<UserInfo> userInfos = new ArrayList<>();
                set.forEach(x -> {
                    UserInfo userInfo = userInfoMapper.selectByUserNo(x);
                    userInfos.add(userInfo);
                });

                topicDTO.setArticleCount(topicArticleRels.size());
                topicDTO.setUserInfos(userInfos);

            } else if ((topicCls.getBigType()).equals(BigTypeEnum.BIGTYPE$1.getKey())) { // 跳蚤市场
                logger.info("主题为跳蚤市场");
                GoodsInfoQueryCondition goodsQC = new GoodsInfoQueryCondition();
                goodsQC.setTopicId(String.valueOf(topicId));

                List<MarketGoodsInfo> marketGoodsInfos = marketGoodsInfoMapper.selectByTopciId(goodsQC);

                if (marketGoodsInfos == null) marketGoodsInfos = new ArrayList<>();
                Set<String> set = new HashSet<>();
                if (marketGoodsInfos != null) {
                    marketGoodsInfos.forEach(x -> {
                        set.add(x.getCreatedBy());
                    });
                }
                List<UserInfo> userInfos = new ArrayList<>();
                set.forEach(x -> {
                    UserInfo userInfo = userInfoMapper.selectByUserNo(x);
                    userInfos.add(userInfo);
                });

                topicDTO.setArticleCount(topicArticleRels.size());
                topicDTO.setUserInfos(userInfos);

            }

            return topicDTO;
        } catch (Exception e) {
            logger.error("根据id查询主题详情出现异常", e);

            return null;
        }
    }

    @Override
    public RestResult queryArticlesByTopicId(TopicAritclesQueryCondition topicQC) {
        logger.info("根据主题id查询跟帖列表");
        TopicCls topicCls = topicClsMapper.selectByPrimaryKey(Integer.valueOf(topicQC.getTopicId()));

        try {
            if ((topicCls.getBigType()).equals(BigTypeEnum.BIGTYPE$0.getKey())) {   // 动态
                ArticleQueryCondition articleQC = new ArticleQueryCondition();
                articleQC.setTopicId(topicQC.getTopicId());
                articleQC.setUserNo(topicQC.getUserNo());
                articleQC.setPageSize(topicQC.getPageSize());
                articleQC.setPageNum(topicQC.getPageNum());
                DynamicListVOs vos = articleService.queryArticlesByTopicId(articleQC);
                return ResultUtil.createSuccessResult(vos);
            } else if ((topicCls.getBigType()).equals(BigTypeEnum.BIGTYPE$1.getKey())) { // 跳蚤市场
                GoodsInfoQueryCondition goodsInfoQC= new GoodsInfoQueryCondition();
                goodsInfoQC.setTopicId(topicQC.getTopicId());
                goodsInfoQC.setUserNo(topicQC.getUserNo());
                goodsInfoQC.setPageSize(topicQC.getPageSize());
                goodsInfoQC.setPageNum(topicQC.getPageNum());
                GoodsListVOs goodsListVOs = marketGoodsInfoService.queryGoodsInfosByTopicId(goodsInfoQC);
                return ResultUtil.createSuccessResult(goodsListVOs);


            }
        } catch (Exception e) {
            logger.error("根据主题id查询跟帖列表异常", e);
        }

        return ResultUtil.createSuccessResult();
    }

    @Override
    public List<TopicCls> getByUserNo(String userNo, String smallType) {
        if (smallType == null || smallType.equals("0")) {
            smallType = null;
        }
        List<TopicCls> topicClss = topicClsMapper.selectByUserNo(userNo, smallType);
        List<TopicCls> returnList = new ArrayList<>();
        if (topicClss != null && !topicClss.isEmpty()) {
            for(TopicCls topicCls : topicClss) {
                TopicDTO topicDTO = getTopicById(topicCls.getId(), null);
                if (topicDTO == null) {
                    continue;
                }
                topicCls.setImageUrls(JSONUtil.toList(JSONUtil.parseArray(topicCls.getImageUrl()), String.class));
                topicCls.setJoinCount(topicDTO.getArticleCount());
                returnList.add(topicCls);
            }
        }
        return returnList;
    }

    @Override
    public List<TopicCls> getTopicsByCondition(TopicCls topicCls) {
        List<TopicCls> topicCls1 = topicClsMapper.getTopicsByCondition(topicCls);
        List<TopicCls> returnList = new ArrayList<>();
        if(topicCls1 != null && !topicCls1.isEmpty()){
            if(StringUtils.equals("1", topicCls.getIsFilterJoined())){
                topicCls1 = topicCls1.stream().filter(this::filterJoined).collect(Collectors.toList());
            }else if(StringUtils.equals("0", topicCls.getIsFilterJoined())){
                topicCls1 = topicCls1.stream().filter(this::filterUnJoined).collect(Collectors.toList());
            }

            if (topicCls1 != null) {
                for(TopicCls topicClsitem : topicCls1){
                    topicClsitem.setImageUrls(JSONUtil.toList(JSONUtil.parseArray(topicClsitem.getImageUrl()), String.class));
                    TopicDTO topicDTO = getTopicById(topicClsitem.getId(), null);
                    if (topicDTO == null) {
                        continue;
                    }

                    topicClsitem.setJoinCount(topicDTO.getArticleCount());
                    returnList.add(topicClsitem);
                }
            }
        }
        return returnList;
    }

    private boolean filterUnJoined(TopicCls topicCls){
        List<TopicArticleRel> topicArticleRels = topicArticleRelMapper.selectByTopicClsId(topicCls.getId());
        if(topicArticleRels == null || topicArticleRels.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    private boolean filterJoined(TopicCls topicCls){
        List<TopicArticleRel> topicArticleRels = topicArticleRelMapper.selectByTopicClsId(topicCls.getId());
        if(topicArticleRels == null || topicArticleRels.isEmpty()){
            return true;
        }else{
            return false;
        }
    }


}
