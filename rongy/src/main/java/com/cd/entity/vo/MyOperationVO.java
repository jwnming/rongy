package com.cd.entity.vo;

public class MyOperationVO {

    private int posts;
    private int followings;
    private int followers;

    public MyOperationVO(int posts, int followings, int followers) {
        this.posts = posts;
        this.followings = followings;
        this.followers = followers;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
