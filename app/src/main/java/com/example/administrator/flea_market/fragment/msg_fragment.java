package com.example.administrator.flea_market.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.flea_market.R;

/**
 * Created by Administrator on 2017/2/10.
 */
public class msg_fragment extends Fragment {
    public msg_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.listview_item, container, false);
    }
}
