package com.example.administrator.flea_market.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.activity.LoginActivity;
import com.example.administrator.flea_market.activity.WelcomeActivity;
import com.example.administrator.flea_market.activity.user_info;
import com.example.administrator.flea_market.bean.MyUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import circleimageview.CircleImageView;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by Administrator on 2017/2/10.
 */
public class person_fragment extends Fragment {
    private Button logoff;
    private TextView show_name;
    private CircleImageView person_avator;
    private LinearLayout layout;
    private MyUser myUser;
    private BroadcastReceiver broadcastReceiver;
    public person_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View person_layout = inflater.inflate(R.layout.person_fragment, container, false);
        logoff = (Button) person_layout.findViewById(R.id.logoff);
        show_name = (TextView) person_layout.findViewById(R.id.center_name);
        person_avator = (CircleImageView) person_layout.findViewById(R.id.person_avator);
        layout = (LinearLayout) person_layout.findViewById(R.id.edit_user);
        myUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        show_name.setText(myUser.getName());
        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUser.logOut(getActivity());
                //可断开连接
                BmobIM.getInstance().disConnect();
                //在此处对主activity进行广播注销，也需要声明onDestroy函数：super.onDestroy()
                getActivity().unregisterReceiver(broadcastReceiver);
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        // 使用ImageLoader加载网络图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图�?
                .cacheInMemory(true) // 内存缓存
                .cacheOnDisk(true) // sdcard缓存
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置�?低配�?
                .build();//
        ImageLoader.getInstance().displayImage(myUser.getAvator().getFileUrl(getActivity()), person_avator, options);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String object_id = myUser.getObjectId();
                Intent intent = new Intent();
                intent.putExtra("object_id", object_id);
                intent.setClass(getActivity(), user_info.class);
                startActivity(intent);
            }
        });

        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                final String object_id = myUser.getObjectId();
                BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                query.getObject(getActivity(), object_id, new GetListener<MyUser>() {
                    @Override
                    public void onSuccess(MyUser object) {
                        show_name.setText(object.getName());
                        // 使用ImageLoader加载网络图片
                        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                                .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
                                .showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图�?
                                .cacheInMemory(true) // 内存缓存
                                .cacheOnDisk(true) // sdcard缓存
                                .bitmapConfig(Bitmap.Config.RGB_565)// 设置�?低配�?
                                .build();//
                        ImageLoader.getInstance().displayImage(object.getAvator().getFileUrl(getActivity()), person_avator, options);
                    }

                    @Override
                    public void onFailure(int code, String arg0) {
                        Log.i("bmob", "失败：" + arg0);
                    }
                });
            }
        };
        //为之后修改个人资料成功后给个人中心的ui做更新
        IntentFilter filter = new IntentFilter("renew_user_info");
        getActivity().registerReceiver(broadcastReceiver, filter);

        return person_layout;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
