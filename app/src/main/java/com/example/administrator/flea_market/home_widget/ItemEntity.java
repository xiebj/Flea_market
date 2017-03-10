package com.example.administrator.flea_market.home_widget;

/**
 * Created by Administrator on 2017/2/15.
 */

import java.util.ArrayList;

public class ItemEntity {
    private String avatar; // 用户头像URL
    private String title; // 标题
    private String price; // 价格
    private String name; //用户名
    private String content; //内容
    private String place; //所在校区
    private ArrayList<String> imageUrls; // 图片的URL集合

    public ItemEntity(String avatar, String title, String price, String name, String content, String place, ArrayList<String> imageUrls) {
        super();
        this.avatar = avatar;
        this.title = title;
        this.place = place;
        this.name = name;
        this.price = price;
        this.content = content;
        this.imageUrls = imageUrls;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public String toString() {
        return "ItemEntity [avatar=" + avatar + ", title=" + title + ", content=" + content + ", imageUrls=" + imageUrls + "]";
    }

}

