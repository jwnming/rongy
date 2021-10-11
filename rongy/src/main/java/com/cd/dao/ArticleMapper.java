package com.cd.dao;

import com.cd.entity.Article;
import com.cd.entity.RankingList;
import com.cd.entity.dto.ArticleQueryCondition;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);
 	Article selectByTitle(String title);

    List<Article> selectByQC(ArticleQueryCondition articleQC);

    /**
     * 模糊匹配帖子
     * @param contentLike
     * @return
     */
    List<Article> selectByContentLike(String contentLike);

    List<Article> selectByUserNo(String userNo);

    List<Article> listByUserLiked(String userNo);

    int countByUserNo(String userNo);

    int countByOtherUserNo(String userNo);

    // 帖子浏览量加1
    int viewCountAdd(Integer articleId);

    List<Article> selectStickyArticlesByType(String typeNo);

    // 查询用户及发表的文章数量
    List<RankingList> selectAritclesAndUserNoList();

    // 查询用户及发表的文章数量
    RankingList selectAritclesAndUserNoListByUserNo(String userNo);

    // 根据主题id查询
    List<Article> selectByTopciId(ArticleQueryCondition articleQC);


}