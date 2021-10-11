package com.cd.service;

import com.cd.common.RestResult;
import com.cd.entity.dto.ArticleDTO;
import com.cd.entity.dto.ArticleQueryCondition;
import com.cd.entity.vo.DynamicListVO;
import com.cd.entity.vo.DynamicListVOs;

import java.util.List;

public interface ArticleService {

    void create(ArticleDTO articleDTO);

    DynamicListVOs queryArticles(ArticleQueryCondition articleQC);

    RestResult getByContentLike(String userNo, String contentLike);

    DynamicListVO selectById(int id, String userNo);

    List<DynamicListVO> listByUserNo(String userNo, String searchUserNo);

    List<DynamicListVO> listUserLiked(String userNo);

    boolean del(String userNo, int id);

    int viewCountAdd(Integer articleId);

    DynamicListVOs queryArticlesByTopicId(ArticleQueryCondition articleQC);

}