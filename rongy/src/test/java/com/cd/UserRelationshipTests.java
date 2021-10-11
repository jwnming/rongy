package com.cd;

import com.cd.common.RestResult;
import com.cd.dao.UserRelationshipMapper;
import com.cd.entity.Comment;
import com.cd.entity.UserRelationship;
import com.cd.service.CommentService;
import com.cd.service.UserRelationshipService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserRelationshipTests {
    @Autowired
    UserRelationshipService userRelationshipService;
    @Autowired
    UserRelationshipMapper userRelationshipMapper;

    // 关注
    @Test
    void contextLoads1() {
        UserRelationship userRelationship = new UserRelationship();
        userRelationship.setUserId("123");
        userRelationship.setFollowedUserId("456");
        userRelationship.setCreatedTime(new Date());
        userRelationship.setUpdatedTime(new Date());

        RestResult restResult = userRelationshipService.follow(userRelationship);
        System.out.println(restResult.getHeader().getCode());
    }

    // 取消关注
    @Test
    void contextLoads2() {
        RestResult restResult = userRelationshipService.unFollow("123", "456");
        System.out.println(restResult.getHeader().getCode());
    }

    @Test
    void test1() {
        UserRelationship userRelationship = userRelationshipMapper.selectByBothId("liangz6", "yizc");
        UserRelationship userRelationship2 = userRelationshipMapper.selectByBothId("liangz6", "yizc");
        System.out.println(userRelationship);
        System.out.println(userRelationship2);
    }


}
