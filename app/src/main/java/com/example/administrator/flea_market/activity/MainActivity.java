package com.example.administrator.flea_market.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.bean.MyUser;
import com.example.administrator.flea_market.config.MyConfig;
import com.example.administrator.flea_market.fragment.*;
import com.example.administrator.flea_market.msg_widget.DemoMessageHandler;
import com.example.administrator.flea_market.msg_widget.event.RefreshEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends Activity {
    public MyUser myUser;
    private RadioGroup radioGroup;
    private ImageView iv_conversation_tips;
    private Button home;
    private Button create;
    private Button msg;
    private Button center;
    //作为fragment的标记
    public static final String fragment1Tag = "fragment1";
    public static final String fragment2Tag = "fragment2";
    public static final String fragment3Tag = "fragment3";
    public static final String fragment4Tag = "fragment4";
    private Button[] mTabs;
    private Fragment home_fragment;
    private Fragment new_fragment;
    private Fragment msg_fragment;
    private Fragment person_fragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        home = (Button) findViewById(R.id.home);
        create = (Button) findViewById(R.id.create);
        msg = (Button) findViewById(R.id.message);
        center = (Button) findViewById(R.id.setting);
        iv_conversation_tips = (ImageView) findViewById(R.id.iv_conversion_tips);
        mTabs = new Button[4];
        mTabs[0] = home;
        mTabs[1] = create;
        mTabs[2] = msg;
        mTabs[3] = center;
        mTabs[0].setSelected(true);
        //连接服务器
        myUser = BmobUser.getCurrentUser(this, MyUser.class);
        BmobIM.connect(myUser.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("connect success");
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        /*
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                Toast.makeText(MainActivity.this, status.getMsg(), Toast.LENGTH_SHORT);
            }
        });
        */
        //解决leancanary提示InputMethodManager内存泄露的问题
        //IMMLeaks.fixFocusedViewLeak(getApplication());
        initTab();
    }

    private void initTab() {
        home_fragment = new home_fragment();
        new_fragment = new new_fragment();
        msg_fragment = new msg_fragment();
        person_fragment = new person_fragment();
        fragments = new Fragment[]{home_fragment, new_fragment, msg_fragment, person_fragment};
        getFragmentManager().beginTransaction()
                .add(R.id.container, home_fragment).
                add(R.id.container, new_fragment)
                .add(R.id.container, msg_fragment)
                .add(R.id.container, person_fragment)
                .hide(new_fragment).hide(msg_fragment).hide(person_fragment)
                .show(home_fragment).commit();
    }

    public void onTabSelect(View view) {
        switch (view.getId()) {
            case R.id.home:
                index = 0;
                break;
            case R.id.create:
                index = 1;
                break;
            case R.id.message:
                index = 2;
                break;
            case R.id.setting:
                index = 3;
                break;
        }
        onTabIndex(index);
    }

    private void onTabIndex(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.container, fragments[index]);
            }
            mTabs[currentTabIndex].setSelected(false);
            mTabs[index].setSelected(true);
            trx.show(fragments[index]).commit();
        }
        currentTabIndex = index;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示小红点
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }

    private void checkRedPoint() {
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        if (count > 0) {
            iv_conversation_tips.setVisibility(View.VISIBLE);
        } else {
            iv_conversation_tips.setVisibility(View.GONE);
        }
    }

    //重写返回函数确认退出
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        BmobIM.getInstance().disConnect();
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
        // super.onBackPressed();
    }

    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton mTab = (RadioButton) radioGroup.getChildAt(i);
            FragmentManager fm = getFragmentManager();
            Fragment fragment = fm.findFragmentByTag((String) mTab.getTag());
            FragmentTransaction ft = fm.beginTransaction();
            if (fragment != null) {
                if (!mTab.isChecked()) {
                    ft.hide(fragment);
                }
            }
            ft.commit();
        }
    }
    */
}
