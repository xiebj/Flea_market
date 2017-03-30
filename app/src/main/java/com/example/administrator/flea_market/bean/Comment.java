package com.example.administrator.flea_market.bean;

import com.example.administrator.flea_market.bean.MyGoods;
import com.example.administrator.flea_market.bean.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/2/26.
 */
public class Comment extends BmobObject {

    private String content; //评论内容
    private String comment_time; //评论时间
    private MyUser author; //评论者
    private MyGoods post; //所属的帖子

    public Comment(){

    }

    public MyUser getAuthor() {
        return this.author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public MyGoods getPost() {
        return this.post;
    }

    public void setPost(MyGoods post) {
        this.post = post;
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
