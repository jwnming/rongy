package com.cd;

import com.cd.common.RestResult;
import com.cd.dao.CommentMapper;
import com.cd.entity.Comment;
import com.cd.entity.UserInfo;
import com.cd.service.CommentService;
import com.cd.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class CommentTests {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentMapper commentMapper;

    // 根据微信号查询
    @Test
    void contextLoads1() {
        RestResult byWechatName = commentService.getByWechatName("蓉言蓉语");
        System.out.println(byWechatName.getHeader().getCode());
        System.out.println(((List<Comment>)byWechatName.getData()).toString());

    }

    // 根据用户id查询
    @Test
    void contextLoads2() {
//        RestResult restResult = commentService.getByUserId(100);
//        System.out.println(restResult.getHeader().getCode());
//        System.out.println(((List<Comment>)restResult.getData()).toString());

    }

    @Test
    void contextLoads3() {
        Comment comment = commentMapper.selectByArticleIdAndUserId("100", "101");
        System.out.println(comment);

    }
    @Test
    void contextLoads4() {
        // Integer i = commentMapper.selectLikeCountByArticleId("100");
        List<Comment> comments = commentMapper.selectByArticleId("100");
        System.out.println(comments);

    }

    @Test
    void contextLoads5() {
        commentService.likeOrUnlikeByUserNo("123", "123", "123");
    }
}
