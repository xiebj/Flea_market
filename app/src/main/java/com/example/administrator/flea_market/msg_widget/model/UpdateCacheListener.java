package com.example.administrator.flea_market.msg_widget.model;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;

/**
 * Created by Administrator on 2017/4/5.
 */
public abstract class UpdateCacheListener extends BmobListener {
    public abstract void done(BmobException e);


    protected void postDone(Object o, BmobException e) {
        done(e);
    }
}
