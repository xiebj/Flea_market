package com.example.administrator.flea_market.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/3.
 */
public class MyGoods extends BmobObject {
    private Integer type;//所属大类：1为书籍、2为物品、3为技能
    private String title;//发布帖子的标题
    private Integer number;//数量，如果type为3技能类的话 number默认为0
    private String book_for_school;//若type不为1书籍类的话，则默认为null
    private String good_type;//物品的分类，若为1则置为书籍类，若为3则置为技能类
    private String rank;//若非书籍类则置为null
    private String description;//物品描述
    private Integer price;//售价
    private Integer old_price;//原价,若为3则置为0
    private String phone;//手机号码
    private String place;//交易地址
    private List<String> urls;//图片url存放数组
    private MyUser author;//该发布帖的作者

    public void setType(Integer integer) {
        this.type = integer;
    }

    public Integer getType() {
        return this.type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return this.number;
    }

    public void setBook_for_school(String book_for_school) {
        this.book_for_school = book_for_school;
    }

    public String getBook_for_school() {
        return this.book_for_school;
    }

    public void setGood_type(String good_type) {
        this.good_type = good_type;
    }

    public String getGood_type() {
        return this.good_type;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return this.rank;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return this.price;
    }

    public void setOld_price(Integer old_price) {
        this.old_price = old_price;
    }

    public Integer getOld_price() {
        return this.old_price;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return this.place;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return this.urls;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public MyUser getAuthor() {
        return this.author;
    }
}
