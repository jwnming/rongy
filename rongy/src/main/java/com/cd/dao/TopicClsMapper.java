package com.cd.dao;

import com.cd.entity.TopicCls;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TopicClsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicCls record);

    int insertSelective(TopicCls record);

    TopicCls selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicCls record);

    int updateByPrimaryKey(TopicCls record);

    // 帖子浏览量加1
    int viewCountAdd(Integer topicId);

    List<TopicCls> queryTopics();

    List<TopicCls> selectByUserNo(@Param("userNo") String userNo, @Param("smallType") String smallType);

    List<TopicCls> getTopicsByCondition(TopicCls topicCls);
}