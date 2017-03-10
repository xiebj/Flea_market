package com.example.administrator.flea_market.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.bean.MyUser;
import com.example.administrator.flea_market.config.MyConfig;
import com.example.administrator.flea_market.fragment.*;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class MainActivity extends Activity {
    public MyUser myUser;
    private RadioGroup radioGroup;
    //作为fragment的标记
    public static final String fragment1Tag = "fragment1";
    public static final String fragment2Tag = "fragment2";
    public static final String fragment3Tag = "fragment3";
    public static final String fragment4Tag = "fragment4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myUser = BmobUser.getCurrentUser(MyUser.class);
        radioGroup = (RadioGroup) findViewById(R.id.activity_group_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //在进行tab切换的过程中，使用show()和hide()来处理，这样可以保存Fragment的状态
            //Fragment文件单独存在，各自页面的内容各自去实现完成，有自己的生命周期，便于后期维护
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment home_fragment = fm.findFragmentByTag(fragment1Tag);
                Fragment new_fragment = fm.findFragmentByTag(fragment2Tag);
                Fragment msg_fragment = fm.findFragmentByTag(fragment3Tag);
                Fragment person_fragment = fm.findFragmentByTag(fragment4Tag);
                if (home_fragment != null) {
                    ft.hide(home_fragment);
                }
                if (new_fragment != null) {
                    ft.hide(new_fragment);
                }
                if (msg_fragment != null) {
                    ft.hide(msg_fragment);
                }
                if (person_fragment != null) {
                    ft.hide(person_fragment);
                }
                switch (checkedId) {
                    case R.id.home:
                        if (home_fragment == null) {
                            home_fragment = new home_fragment();
                            ft.add(R.id.container, home_fragment, fragment1Tag);
                        } else {
                            ft.show(home_fragment);
                        }
                        break;
                    case R.id.create:
                        if (new_fragment == null) {
                            new_fragment = new new_fragment();
                            ft.add(R.id.container, new_fragment, fragment2Tag);
                        } else {
                            ft.show(new_fragment);
                        }
                        break;
                    case R.id.message:
                        if (msg_fragment == null) {
                            msg_fragment = new msg_fragment();
                            ft.add(R.id.container, msg_fragment, fragment3Tag);
                        } else {
                            ft.show(msg_fragment);
                        }
                        break;
                    case R.id.setting:
                        if (person_fragment == null) {
                            person_fragment = new person_fragment();
                            ft.add(R.id.container, person_fragment, fragment4Tag);
                        } else {
                            ft.show(person_fragment);
                        }
                        break;
                    default:
                        break;
                }
                ft.commit();
            }
        });
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new home_fragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment1Tag).commit();
        }
    }
    public MyUser getMyUser () {
        return myUser;
    }

    //重写返回函数确认退出
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
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
