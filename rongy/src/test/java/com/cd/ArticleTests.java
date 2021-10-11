package com.cd;

import com.cd.common.RestResult;
import com.cd.dao.ArticleMapper;
import com.cd.entity.Article;
import com.cd.entity.UserInfo;
import com.cd.service.ArticleService;
import com.cd.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
class ArticleTests {
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleMapper articleMapper;

    @Test
    void testInsert() {
       /* Article article = new Article();
        article.setId(8888);
        article.setTypeNo("123");
        article.setTitle("123");
        article.setImageUrl("123");
        article.setVideoUrl("123");
        article.setStatus("1");
        article.setCreatedBy("1");
        article.setIsHot("1");
        article.setContent("123");
        article.setIsSticky("1");
        article.setCreatedTime(new Date());*/
        //articleMapper.insertSelective(article);
        Article article = articleMapper.selectByPrimaryKey(297);
        System.out.println(article);
    }

    // 更新
    @Test
    void contextLoads1() {

        RestResult restResult = articleService.getByContentLike("100", "53");
        System.out.println(restResult.getHeader().getCode());

    }

    @Test
    void testViewAdd() {
        articleService.viewCountAdd(229);
    }
}
