package com.example.administrator.flea_market.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.flea_market.bean.MyUser;
import com.example.administrator.flea_market.new_widget.new_book;
import com.example.administrator.flea_market.new_widget.new_goods;
import com.example.administrator.flea_market.new_widget.new_skill;
import com.example.administrator.flea_market.R;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/2/10.
 */
public class new_fragment extends Fragment {
    public new_fragment() {
        // Required empty public constructor
    }
    private MyUser myUser;
    private String object_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View new_layout = inflater.inflate(R.layout.new_fragment, container, false);
        MyUser myUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        object_id = myUser.getObjectId();
        final View new_book = new_layout.findViewById(R.id.new_book);
        new_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), new_book.class);
                intent.putExtra("object_id", object_id);
                intent.putExtra("type", "book");
                getActivity().startActivity(intent);
            }
        });
        View new_goods = new_layout.findViewById(R.id.new_goods);
        new_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), new_goods.class);
                intent.putExtra("object_id", object_id);
                intent.putExtra("type", "goods");
                getActivity().startActivity(intent);
            }
        });
        View new_skill = new_layout.findViewById(R.id.new_skill);
        new_skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), new_skill.class);
                intent.putExtra("object_id", object_id);
                intent.putExtra("type", "skill");
                getActivity().startActivity(intent);
            }
        });
        //加粗中文字体
        TextView tv1 = (TextView)new_layout.findViewById(R.id.solid1);
        TextPaint tp1 = tv1.getPaint();
        tp1.setFakeBoldText(true);
        TextView tv2 = (TextView)new_layout.findViewById(R.id.solid2);
        TextPaint tp2 = tv2.getPaint();
        tp2.setFakeBoldText(true);
        TextView tv3 = (TextView)new_layout.findViewById(R.id.solid3);
        TextPaint tp3 = tv3.getPaint();
        tp3.setFakeBoldText(true);
        return new_layout;
    }
}
