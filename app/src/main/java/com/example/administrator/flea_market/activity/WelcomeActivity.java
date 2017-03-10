package com.example.administrator.flea_market.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.bean.MyUser;
import com.example.administrator.flea_market.config.MyConfig;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class WelcomeActivity extends Activity {
    public static final int HOME =1;//主界面
    public static final int LOGIN = 2;//登录界面
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HOME:
                    //跳转到主页
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case LOGIN:
                    //跳转到登录界面
                    Intent intent2 = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    MyUser myuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化Bmob SDK
        Bmob.initialize(this, MyConfig.APP_ID);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        myuser = BmobUser.getCurrentUser(MyUser.class);
        //若资料未完善需登录进一步完善，只有登陆后完善资料者可享有功能
        if(myuser == null || myuser.getName() == null){
            mHandler.sendEmptyMessageDelayed(LOGIN,2000);
        }else{
            mHandler.sendEmptyMessageDelayed(HOME,2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
