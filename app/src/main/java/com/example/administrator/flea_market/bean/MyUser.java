package com.example.administrator.flea_market.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/3/3.
 */
public class MyUser extends BmobUser implements Serializable {
    //BmobUser自带账号跟密码，这里无需设置
    private String name;//用户昵称
    private BmobFile avator;//用于存放头像文件
    private Boolean sex;//性别，true为男，false为女
    private Integer school_place;//所在校区，1代表南校、2代表东校、3代表珠海、4代表北校

    public void setName(String name) {
        this.name = name;
    }

    public void setAvator(BmobFile avator) {
        this.avator = avator;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public void setSchool_place(Integer school_place) {
        this.school_place = school_place;
    }

    public String getName() {
        return this.name;
    }

    public BmobFile getAvator() {
        return this.avator;
    }

    public Boolean getSex() {
        return this.sex;
    }

    public Integer getSchool_place() {
        return this.school_place;
    }
}
