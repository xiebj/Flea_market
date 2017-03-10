package com.example.administrator.flea_market.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.example.administrator.flea_market.home_widget.NoScrollGridView;
import com.example.administrator.flea_market.widget.Comment;
import com.example.administrator.flea_market.widget.Comment_Adapter;
import com.example.administrator.flea_market.widget.Detial_GridAdapter;

import java.util.ArrayList;
import java.util.List;

public class detial_info extends Activity implements View.OnClickListener{
    private NoScrollGridView detial_gridview;
    private ListView comment_list;
    private Comment_Adapter adapterComment;
    private List<Comment> data;
    private ImageView comment;
    private TextView hide_down;
    private TextView comment_board;
    private EditText comment_content;
    private Button comment_send;

    private LinearLayout rl_enroll;
    private LinearLayout click_comment;
    private RelativeLayout rl_comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial_info);
        // 初始化评论列表
        comment_list = (ListView) findViewById(R.id.comment_list);
        // 初始化数据
        data = new ArrayList<>();
        for (int i = 0; i<3; i++) {
            String content = "感觉这货还不错呀~~~";
            String comment_time = "两天前";
            String name = "谢柏基";
            String avatar = "http://img.my.csdn.net/uploads/201410/19/1413698837_7507.jpg";
            Comment temp = new Comment(avatar, name, comment_time, content);
            data.add(temp);
        }
        // 初始化适配器
        adapterComment = new Comment_Adapter(getApplicationContext(), data);
        // 为评论列表设置适配器
        comment_list.setAdapter(adapterComment);
        setListViewHeightBasedOnChildren(comment_list);
        detial_gridview = (NoScrollGridView) findViewById(R.id.detial_gridview);
        // 3.3张图片
        ArrayList<String> urls = new ArrayList<String>();
        urls.add("http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg");
        urls.add("http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg");
        urls.add("http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg");
        detial_gridview.setAdapter(new Detial_GridAdapter(this, urls));

        comment = (ImageView) findViewById(R.id.comment_pic);
        hide_down = (TextView) findViewById(R.id.hide_down);
        comment_board = (TextView) findViewById(R.id.comment_board);
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_send = (Button) findViewById(R.id.comment_send);

        rl_enroll = (LinearLayout) findViewById(R.id.bottom4);
        click_comment = (LinearLayout) findViewById(R.id.click_comment);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);

        setListener();
    }

    /**
     * 设置监听
     */
    public void setListener(){
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
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
    public void sendComment(){
        if(comment_content.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        }else{
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
            InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
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
