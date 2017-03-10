package com.example.administrator.flea_market.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.activity.detial_info;
import com.example.administrator.flea_market.home_widget.ItemEntity;
import com.example.administrator.flea_market.home_widget.ListItemAdapter;
import com.example.administrator.flea_market.widget.RefreshListView;
import com.example.administrator.flea_market.widget.RefreshListViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class home_fragment extends Fragment implements RefreshListViewListener, RadioGroup.OnCheckedChangeListener {
    private static final long DURATION_TIME = 2000;
    private RefreshListView mListView;
    private SimpleAdapter mAdapter;
    private List<String> datas;
    /**
     * Item数据实体集合
     */
    private ArrayList<ItemEntity> itemEntities;
    boolean hasRefreshDatas = true;
    boolean hasMoreDatas = true;
    private Random random = new Random();
    Handler handler = new Handler();

    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton0;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private RadioButton mRadioButton3;
    private RadioButton mRadioButton4;
    private RadioButton mRadioButton5;
    private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件

    public home_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View home_layout = inflater.inflate(R.layout.home_fragment, container, false);
        mListView = (RefreshListView) home_layout.findViewById(R.id.refresh_listview);

        //点击单一物品跳转到物品详情界面
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), detial_info.class);
                startActivity(intent);
            }
        });

        mListView.setOnRefreshListViewListener(this);
        /*
        mAdapter = new SimpleAdapter(getActivity(), this.getItem(), R.layout.listview_item,
                                new String[] {"blief","name", "price", "description", "place"},
                                new int[] {R.id.title, R.id.name, R.id.price, R.id.description, R.id.place});
        */
        //mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1);
        initData();

        mListView.setAdapter(new ListItemAdapter(getActivity(), itemEntities));
        //mAdapter.addAll(datas);

        mRadioGroup = (RadioGroup) home_layout.findViewById(R.id.radioGroup);
        mRadioButton0 = (RadioButton) home_layout.findViewById(R.id.btn0);
        mRadioButton1 = (RadioButton) home_layout.findViewById(R.id.btn1);
        mRadioButton2 = (RadioButton) home_layout.findViewById(R.id.btn2);
        mRadioButton3 = (RadioButton) home_layout.findViewById(R.id.btn3);
        mRadioButton4 = (RadioButton) home_layout.findViewById(R.id.btn4);
        mRadioButton5 = (RadioButton) home_layout.findViewById(R.id.btn5);
        mHorizontalScrollView = (HorizontalScrollView) home_layout.findViewById(R.id.horizontalScrollview);
        mRadioGroup.setOnCheckedChangeListener(this);

        return home_layout;
    }

    /*
     * Function     :    获取所有的列表内容
     * Author       :    博客园-依旧淡然
     */
    /*
    public ArrayList<HashMap<String, Object>> getItem() {
        ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < 50; i++) {
            map.put("blief", "出售五月天门票一张");
            map.put("name", "厚蛋烧先生");
            map.put("price", "100元");
            map.put("description", "今年刚买的五月天的门票，但是有事去不了，故100元转让。");
            map.put("place", "东校区");
            item.add(map);
        }
        return item;
    }
    */

    /**
     * 初始模拟有数据
     */
    private void initData() {
        itemEntities = new ArrayList<ItemEntity>();
        // 1.无图片
        ItemEntity entity1 = new ItemEntity(
                "http://img.my.csdn.net/uploads/201410/19/1413698871_3655.jpg", "出售五月天门票一张", "100元", "厚蛋烧先生", "今年刚买的门票，但现在有事去不了，100元转", "东校区", null);
        itemEntities.add(entity1);
        // 2.1张图片
        ArrayList<String> urls_1 = new ArrayList<String>();
        urls_1.add("http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg");
        ItemEntity entity2 = new ItemEntity(
                "http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg", "出售五月天门票2张", "200元", "娘子", "今年刚买的门票，但现在有事去不了，100元转", "东校区", urls_1);
        itemEntities.add(entity2);
        // 3.3张图片
        ArrayList<String> urls_2 = new ArrayList<String>();
        urls_2.add("http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg");
        urls_2.add("http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg");
        urls_2.add("http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg");
        ItemEntity entity3 = new ItemEntity(
                "http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg", "出售五月天门票3张", "300元", "厚蛋烧先生", "今年刚买的门票，但现在有事去不了，100元转", "东校区", urls_2);
        itemEntities.add(entity3);

        ArrayList<String> urls_3 = new ArrayList<String>();
        urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698837_7507.jpg");
        urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg");
        urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg");
        urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg");
        urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg");
        urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698839_2302.jpg");
        ItemEntity entity4 = new ItemEntity(
                "http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg", "出售五月天门票3张", "300元", "厚蛋烧先生", "今年刚买的门票，但现在有事去不了，100元转", "东校区", urls_3);
        itemEntities.add(entity4);
        /*
        datas = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            datas.add("Content: " + random.nextInt(100));
        }
        */
    }

    @Override
    public void onLoad() {
        if (hasMoreDatas) {
            getLoadMoreData_HasDatas();
        } else {
            getLoadMoreData_HasNoDatas();
        }
    }

    @Override
    public void onRefresh() {
        if (hasRefreshDatas) {
            getRefreshMoreData_HasDatas();
        } else {
            getRefreshData_HasNoDatas();
        }
    }

    public void getLoadMoreData_HasDatas() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                itemEntities.clear();
                for (int i = 0; i < 10; i++) {
                    ArrayList<String> urls_2 = new ArrayList<String>();
                    urls_2.add("http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg");
                    urls_2.add("http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg");
                    urls_2.add("http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg");
                    ItemEntity entity3 = new ItemEntity(
                            "http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg", "出售五月天门票3张", "300元", "厚蛋烧先生", "今年刚买的门票，但现在有事去不了，100元转", "东校区", urls_2);
                    itemEntities.add(entity3);
                }
                //mAdapter.addAll(datas);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.stopLoadMore();
                    }
                });

            }
        }, DURATION_TIME);
    }

    public void getLoadMoreData_HasNoDatas() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.stopLoadMoreForNoDatas();
                    }
                });

            }
        }, DURATION_TIME);
    }


    public void getRefreshMoreData_HasDatas() {
        //下拉刷新有数据，说明上拉加载更多可能有数据，因此激活上拉加载
        mListView.setLoadMoreEnable(true);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mAdapter.clear();
                itemEntities.clear();
                for (int i = 0; i < 20; i++) {
                    ArrayList<String> urls_2 = new ArrayList<String>();
                    urls_2.add("http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg");
                    urls_2.add("http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg");
                    urls_2.add("http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg");
                    ItemEntity entity3 = new ItemEntity(
                            "http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg", "出售五月天门票3张", "300元", "厚蛋烧先生", "今年刚买的门票，但现在有事去不了，100元转", "东校区", urls_2);
                    itemEntities.add(entity3);
                }
                //mAdapter.addAll(datas);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.stopRefresh();
                    }
                });

            }
        }, DURATION_TIME);
    }

    public void getRefreshData_HasNoDatas() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.stopRefresh();
                    }
                });

            }
        }, DURATION_TIME);
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //待完善
        if (checkedId == R.id.btn1) {

        }
    }
}
