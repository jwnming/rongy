package com.cd;

import com.cd.entity.*;
import com.cd.enums.RongyDictEnum;
import com.cd.utils.CommonUtil;
import com.cd.utils.SecurityUtil;

import java.util.Date;
import java.util.UUID;

public class BaseGenerate {

    private static String uniqueNo() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static UserInfo makeUserInfo() {
        UserInfo userInfo = new UserInfo();

        userInfo.setUserNo(uniqueNo());
        userInfo.setName("username");
        userInfo.setWechatName("wechatName");
        userInfo.setSex(RongyDictEnum.USER_INFO_SEX$FEMALE.getCode());
        userInfo.setBirthday("2000-01-01");
        userInfo.setTelephone(SecurityUtil.phoneNumEncrypt("1231234789"));
        userInfo.setIntroduction("123");
        userInfo.setImageUrl("image_url");
        userInfo.setStatus(RongyDictEnum.USER_INFO_STATUS$VALID.getCode());
        userInfo.setAddress("address");
        userInfo.setOpenId(CommonUtil.uniqueNo());
        userInfo.setRoleId("mockRole");
        userInfo.setCreatedTime(new Date());

        return userInfo;
    }

    public static UserRelationship makeUserRelationship(String userId, String followedId) {
        UserRelationship userRelationship = new UserRelationship();

        userRelationship.setUserId(userId);
        userRelationship.setFollowedUserId(followedId);
        userRelationship.setCreatedTime(new Date());

        return userRelationship;
    }

    public static Circle makeCircle(String masterUserNo) {
        Circle circle = new Circle();

        circle.setName("circleName");
        circle.setIntroduction("circleIntroduction");
        circle.setImageUrl("image_url");
        circle.setStatus(RongyDictEnum.CIRCLE_STATUS$CHECKED.getCode());
        circle.setParentId(-1);
        circle.setMasterUserId(masterUserNo);
        circle.setCreatedTime(new Date());

        return circle;
    }

    public static Role makeRole() {
        Role role = new Role();

        role.setRoleId(uniqueNo());
        role.setRoleName("role");
        role.setStatus(RongyDictEnum.ROLE_STATUS$VALID.getCode());
        role.setCreatedTime(new Date());

        return role;
    }

    public static UserCircleRel makeUserCircleRel(int circleId, int userId) {
        UserCircleRel userCircleRel = new UserCircleRel();

        userCircleRel.setCircleId(circleId);
        userCircleRel.setUserId(userId);
        userCircleRel.setStatus(RongyDictEnum.USER_CIRCLE_REL_STATUS$CHECKED.getCode());
        userCircleRel.setCreatedTime(new Date());

        return userCircleRel;
    }

    public static Topic makeTopic(int circleId, String typeNo, String userNo) {
        Topic topic = new Topic();

        topic.setCircleId(circleId);
        topic.setTitle("title");
        topic.setTypeNo(typeNo);
        topic.setContent("content content");
        topic.setImageUrl("image_url");
        topic.setVideoUrl("video_url");
        topic.setStatus(RongyDictEnum.TOPIC_STATUS$CHECKED.getCode());
        topic.setCreatedBy(userNo);
        topic.setCreatedTime(new Date());

        return topic;
    }

    public static Article makeArticle(String createdBy) {
        Article article = new Article();

        article.setTitle("article");
        article.setContent("content content content");
        article.setImageUrl("image_url");
        article.setVideoUrl("video_url");
        article.setStatus(RongyDictEnum.ARTICLE_STATUS$CHECKED.getCode());
        article.setCreatedBy(createdBy);
        article.setIsHot(RongyDictEnum.ARTICLE_IS_HOT$YES.getCode());
        article.setCreatedTime(new Date());

        return article;
    }

    public static Comment makeComment(String articleId, String type, String toId, String fromId, String level, String isRead) {
        Comment comment = new Comment();

        comment.setArticleId(articleId);
        comment.setContent("content");
        comment.setType(type);
        comment.setBecommenteeId(toId);
        comment.setCommentatorId(fromId);
        comment.setLevel(level);
        comment.setIsRead(isRead);
        comment.setCreatedTime(new Date());

        return comment;
    }

    public static Category makeCategory() {
        Category category = new Category();

        category.setNo(uniqueNo());
        category.setName("name");
        category.setAuthority(RongyDictEnum.CATEGORY_AUTH$ALL.getCode());
        category.setCreatedTime(new Date());

        return category;
    }

}
