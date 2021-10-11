package com.cd.service;


import com.cd.common.RestResult;
import com.cd.entity.MarketGoodsInfo;
import com.cd.entity.Topic;
import com.cd.entity.TopicCls;
import com.cd.entity.dto.ArticleDTO;
import com.cd.entity.dto.TopicAritclesQueryCondition;
import com.cd.entity.dto.TopicClsDTO;
import com.cd.entity.dto.TopicDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface TopicClsService {

    String getBigTypes() throws Exception;

    void create(TopicClsDTO topicClsDTO);

    boolean del(String userNo, int id);

    void newArticleWithTopic(ArticleDTO articleDTO, String topicClsId);

    void newGoodsWithTopic(MarketGoodsInfo marketGoodsInfo, String topicClsId);

    int viewCountAdd(Integer topicId);

    List<TopicCls> queryTopics();

    // 主题详情
    TopicDTO getTopicById(Integer topicId, String userNo);

    // 根据主题id查询跟帖列表
    RestResult queryArticlesByTopicId(TopicAritclesQueryCondition topicQC);

    // 获取用户参与的主题
    List<TopicCls> getByUserNo(String userNo, String smallType);

    List<TopicCls> getTopicsByCondition(TopicCls topicCls);

}
