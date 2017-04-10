package com.example.administrator.flea_market.msg_widget.model;

import com.example.administrator.flea_market.bean.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;
/**
 * Created by Administrator on 2017/4/5.
 */
public abstract class QueryUserListener extends BmobListener<MyUser> {

    public abstract void done(MyUser s, BmobException e);
    @Override
    protected void postDone(MyUser o, BmobException e) {
        done(o, e);
    }
}
