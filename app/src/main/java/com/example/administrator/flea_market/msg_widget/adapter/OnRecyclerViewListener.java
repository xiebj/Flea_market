package com.example.administrator.flea_market.msg_widget.adapter;

/**
 * 为RecycleView添加点击事件
 * Created by Administrator on 2017/4/4.
 */
public interface OnRecyclerViewListener {
    void onItemClick(int position);
    boolean onItemLongClick(int position);
}
