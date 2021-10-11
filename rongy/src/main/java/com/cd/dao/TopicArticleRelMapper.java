package com.cd.dao;

import com.cd.entity.TopicArticleRel;
import com.cd.entity.TopicCls;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TopicArticleRelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicArticleRel record);

    int insertSelective(TopicArticleRel record);

    TopicArticleRel selectByPrimaryKey(Integer id);

    int count(Integer id);

    int updateByPrimaryKeySelective(TopicArticleRel record);

    int updateByPrimaryKey(TopicArticleRel record);

    TopicCls selectByInfoIdAndBigType(@Param("infoId") Integer infoId, @Param("bigType") String bigType);

    List<TopicArticleRel> selectByTopicClsId(Integer topicClsId);

    int deleteByTopicId(Integer topicClsId);

    int deleteByTopicIdAndInfoId(@Param("topicClsId") Integer topicClsId, @Param("infoId") Integer infoId);
}