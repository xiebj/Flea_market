package com.example.administrator.flea_market.msg_widget.model;

import android.content.Context;

import com.example.administrator.flea_market.home_widget.MyApplication;

/**
 * Created by Administrator on 2017/4/5.
 */
public abstract class BaseModel {

    public int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;

    public static final int DEFAULT_LIMIT=20;

    public Context getContext(){
        return MyApplication.INSTANCE();
    }
}
