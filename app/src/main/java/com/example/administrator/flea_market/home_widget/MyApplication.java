package com.example.administrator.flea_market.home_widget;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.msg_widget.DemoMessageHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.Logger;

import android.app.Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;

public class MyApplication extends Application {
	private static MyApplication INSTANCE;
	public static MyApplication INSTANCE(){
		return INSTANCE;
	}
	private void setInstance(MyApplication app) {
		setBmobIMApplication(app);
	}
	private static void setBmobIMApplication(MyApplication a) {
		MyApplication.INSTANCE = a;
	}
	@Override
	public void onCreate() {
		super.onCreate();
        setInstance(this);
		//初始化
		Logger.init("smile");
		//只有主进程运行的时候才需要初始化
		if (getApplicationInfo().packageName.equals(getMyProcessName())){
			//im初始化
			BmobIM.init(this);
			//注册消息接收器
			BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
		}
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.ic_launcher) //
                .showImageOnFail(R.drawable.ic_launcher) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)//
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs()//
                .build();//
        ImageLoader.getInstance().init(config);
	}
	/**
	 * 获取当前运行的进程名
	 * @return
	 */
	public static String getMyProcessName() {
		try {
			File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
			BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
			String processName = mBufferedReader.readLine().trim();
			mBufferedReader.close();
			return processName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
