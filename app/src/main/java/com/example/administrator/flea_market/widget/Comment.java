package com.example.administrator.flea_market.widget;

/**
 * Created by Administrator on 2017/2/26.
 */
public class Comment {

    String name; //评论者
    String content; //评论内容
    String comment_time; //评论时间
    private String avatar; // 用户头像URL
    public Comment(){

    }

    public Comment(String avatar, String name, String comment_time, String content){
        this.avatar = avatar;
        this.name = name;
        this.comment_time = comment_time;
        this.content = content;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getCommentTime() {
        return comment_time;
    }

    public void setCommentTime(String comment_time) {
        this.comment_time = comment_time;
    }
}
