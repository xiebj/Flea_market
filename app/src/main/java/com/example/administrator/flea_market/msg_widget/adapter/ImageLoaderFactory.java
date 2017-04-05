package com.example.administrator.flea_market.msg_widget.adapter;

/**
 * Created by Administrator on 2017/4/4.
 */
public class ImageLoaderFactory {
    private static volatile ILoader sInstance;

    private ImageLoaderFactory() {}

    public static ILoader getLoader() {
        if (sInstance == null) {
            synchronized (ImageLoaderFactory.class) {
                if (sInstance == null) {
                    sInstance = new UniversalImageLoader();
                }
            }
        }
        return sInstance;
    }
}
