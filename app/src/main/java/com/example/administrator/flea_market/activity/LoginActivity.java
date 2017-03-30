package com.example.administrator.flea_market.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.bean.MyUser;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity implements View.OnClickListener {
    private ProgressBar progressBar;
    private EditText username;
    private EditText passwordtext;
    private String user;
    private String password;
    private Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        username = (EditText) findViewById(R.id.username);
        passwordtext = (EditText) findViewById(R.id.password);
        login_btn = (Button) findViewById(R.id.login_button);
        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        user = username.getText().toString();
        password = passwordtext.getText().toString();
        //与后台交互验证密码是否正确
        boolean error = false;
        if (TextUtils.isEmpty(user)) {
            onUsernameEmpty();
            error = true;
        }
        if (TextUtils.isEmpty(password)) {
            onPasswordEmpty();
            error = true;
        }
        if (!error) {
            onNotempty(user, password);
        }
    }

    public void onUsernameEmpty() {
        username.setError(Html.fromHtml("<font color=#ff0000>Username cannot be empty</font>"));
    }

    public void onPasswordEmpty() {
        passwordtext.setError(Html.fromHtml("<font color=#ff0000>Password cannot be empty</font>"));
    }

    public void onNotempty(String user, String password) {
        progressBar.setVisibility(View.VISIBLE);
        final MyUser myUser = new MyUser();
        myUser.setUsername(user);
        myUser.setPassword(password);
        myUser.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                    query.getObject(myUser.getObjectId(), new QueryListener<MyUser>() {

                        @Override
                        public void done(MyUser object, BmobException e) {
                            if(e==null){
                                if (object.getName() == null) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String object_id = myUser.getObjectId();
                                    Intent intent = new Intent();
                                    intent.putExtra("object_id", object_id);
                                    intent.setClass(LoginActivity.this, EditUser.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String object_id = myUser.getObjectId();
                                    Intent intent = new Intent();
                                    intent.putExtra("object_id", object_id);
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }

                    });

                } else {
                    passwordtext.setError(Html.fromHtml("<font color=#ff0000>username or password is wrong</font>"));
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
    //重写返回函数确认退出
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        LoginActivity.this.finish();
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
}
