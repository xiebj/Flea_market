package com.example.administrator.flea_market.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.bean.MyGoods;
import com.example.administrator.flea_market.home_widget.NoScrollGridView;
import com.example.administrator.flea_market.widget.Comment;
import com.example.administrator.flea_market.widget.Comment_Adapter;
import com.example.administrator.flea_market.widget.CustomProgressDialog;
import com.example.administrator.flea_market.widget.Detial_GridAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import circleimageview.CircleImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class detial_info extends Activity implements View.OnClickListener {
    private NoScrollGridView detial_gridview;
    private ListView comment_list;
    private Comment_Adapter adapterComment;
    private List<Comment> data;
    private ImageView comment;
    private TextView hide_down;
    private TextView comment_board;
    private EditText comment_content;
    private Button comment_send;
    private String object_id;
    private LinearLayout rl_enroll;
    private LinearLayout click_comment;
    private RelativeLayout rl_comment;
    private CircleImageView head_pic;
    private TextView author_name;
    private TextView detial_price;
    private TextView detial_description;
    private TextView price_logo;
    private ArrayList<String> urls;
    private MyGoods post;
    private CustomProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial_info);
        object_id = getIntent().getStringExtra("object_id");
        //从服务器返回数据前先显示加载动画
        dialog =new CustomProgressDialog(this, "正在加载中",R.anim.frame);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        // 初始化评论列表
        comment_list = (ListView) findViewById(R.id.comment_list);
        // 初始化数据
        data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String content = "感觉这货还不错呀~~~";
            String comment_time = "两天前";
            String name = "谢柏基";
            String avatar = "http://img.my.csdn.net/uploads/201410/19/1413698837_7507.jpg";
            Comment temp = new Comment(avatar, name, comment_time, content);
            data.add(temp);
        }
        // 初始化适配器
        adapterComment = new Comment_Adapter(getApplicationContext(), data);
        BmobQuery<MyGoods> query = new BmobQuery<MyGoods>();
        query.include("author");//缺少该关联查询则无法获取到user
        //
        query.getObject(object_id, new QueryListener<MyGoods>() {
            @Override
            public void done(MyGoods object, BmobException e) {
                if (e == null) {
                    // 为评论列表设置适配器
                    comment_list.setAdapter(adapterComment);
                    setListViewHeightBasedOnChildren(comment_list);
                    comment = (ImageView) findViewById(R.id.comment_pic);
                    hide_down = (TextView) findViewById(R.id.hide_down);
                    comment_board = (TextView) findViewById(R.id.comment_board);
                    comment_content = (EditText) findViewById(R.id.comment_content);
                    comment_send = (Button) findViewById(R.id.comment_send);

                    rl_enroll = (LinearLayout) findViewById(R.id.bottom4);
                    click_comment = (LinearLayout) findViewById(R.id.click_comment);
                    rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);

                    detial_gridview = (NoScrollGridView) findViewById(R.id.detial_gridview);
                    head_pic = (CircleImageView) findViewById(R.id.head_pic);
                    detial_description = (TextView) findViewById(R.id.detial_description);
                    detial_price = (TextView) findViewById(R.id.detial_price);
                    author_name = (TextView) findViewById(R.id.detial_name);
                    price_logo = (TextView) findViewById(R.id.price_logo);

                    detial_description.setText(object.getDescription());
                    detial_price.setText(String.valueOf(object.getPrice()));
                    author_name.setText(object.getAuthor().getName());
                    price_logo.setText("¥");
                    // 使用ImageLoader加载网络图片
                    DisplayImageOptions options = new DisplayImageOptions.Builder()//
                            .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
                            .showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图�?
                            .cacheInMemory(true) // 内存缓存
                            .cacheOnDisk(true) //
                            .build();//
                    ImageLoader.getInstance().displayImage(object.getAuthor().getAvator().getFileUrl(), head_pic, options);
                    urls = new ArrayList<String>();
                    urls = (ArrayList<String>) object.getUrls();
                    detial_gridview.setAdapter(new Detial_GridAdapter(detial_info.this, urls));
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
                dialog.dismiss();
                setListener();
            }
        });

    }

    /**
     * 设置监听
     */
    public void setListener() {
        click_comment.setOnClickListener(this);
        hide_down.setOnClickListener(this);
        comment_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_comment:
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                // 显示评论框
                rl_enroll.setVisibility(View.GONE);
                rl_comment.setVisibility(View.VISIBLE);
                comment_content.setFocusable(true);
                comment_content.requestFocus();
                break;
            case R.id.hide_down:
                // 隐藏评论框
                rl_enroll.setVisibility(View.VISIBLE);
                rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
                break;
            case R.id.comment_send:
                sendComment();
                break;
            default:
                break;
        }
    }

    /**
     * 发送评论
     */
    public void sendComment() {
        if (comment_content.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            String comment_time = "两天前";
            String name = "谢柏基";
            String avatar = "http://img.my.csdn.net/uploads/201410/19/1413698837_7507.jpg";
            // 生成评论数据
            Comment comment = new Comment();
            comment.setName("评论者" + (data.size() + 1));
            comment.setAvatar(avatar);
            comment.setCommentTime("1分钟前");
            comment.setContent(comment_content.getText().toString());
            data.add(0, comment);
            setListViewHeightBasedOnChildren(comment_list);
            adapterComment.notifyDataSetChanged();
            //adapterComment.addComment(comment);
            // 发送完，清空输入框
            comment_content.setText("");
            rl_enroll.setVisibility(View.VISIBLE);
            rl_comment.setVisibility(View.GONE);
            InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
            comment_board.setFocusable(true);
            comment_board.setFocusableInTouchMode(true);
            comment_board.requestFocus();
            comment_board.requestFocusFromTouch();
            Toast.makeText(getApplicationContext(), "评论成功！", Toast.LENGTH_SHORT).show();
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取gridView对应的Adapter
        Adapter gridAdapter = gridView.getAdapter();
        if (gridAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = gridAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = gridAdapter.getView(i, null, gridView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        //params.height = totalHeight+ (gridView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        gridView.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detial_info, menu);
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
}
