package com.example.administrator.flea_market.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.flea_market.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/2/26.
 */
public class Comment_Adapter extends BaseAdapter {
    Context context;
    List<Comment> data;

    public Comment_Adapter(Context c, List<Comment> data){
        this.context = c;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        // 重用convertView
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
            holder.comment_name = (TextView) convertView.findViewById(R.id.comment_name);
            holder.comment_content = (TextView) convertView.findViewById(R.id.comment_detial);
            holder.comment_time = (TextView) convertView.findViewById(R.id.comment_time);
            holder.iv_avatar = (CircleImageView) convertView.findViewById(R.id.comment_circleImageView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        holder.comment_name.setText(data.get(i).getName());
        holder.comment_content.setText(data.get(i).getContent());
        holder.comment_time.setText(data.get(i).getCommentTime());
        // 使用ImageLoader加载网络图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图�?
                .cacheInMemory(true) // 内存缓存
                .cacheOnDisk(true) // sdcard缓存
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置�?低配�?
                .build();//
        ImageLoader.getInstance().displayImage(data.get(i).getAvatar(), holder.iv_avatar, options);
        return convertView;
    }

    /**
     * 添加一条评论,刷新列表
     * @param comment
     */
    public void addComment(Comment comment){
        data.add(comment);
        notifyDataSetChanged();
    }

    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder{
        private CircleImageView iv_avatar;
        TextView comment_name;
        TextView comment_content;
        TextView comment_time;
    }
}
