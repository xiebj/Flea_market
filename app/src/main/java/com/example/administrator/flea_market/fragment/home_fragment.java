package com.example.administrator.flea_market.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.activity.detial_info;
import com.example.administrator.flea_market.bean.MyGoods;
import com.example.administrator.flea_market.bean.MyUser;
import com.example.administrator.flea_market.home_widget.ItemEntity;
import com.example.administrator.flea_market.home_widget.ListItemAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.example.administrator.flea_market.widget.CustomProgressDialog;
import com.example.administrator.flea_market.widget.RefreshListView;
import com.example.administrator.flea_market.widget.RefreshListViewListener;

public class home_fragment extends Fragment implements RefreshListViewListener, RadioGroup.OnCheckedChangeListener {
    private ArrayList<String> urls = new ArrayList<String>();
    /**
     * Item数据实体集合
     */
    private ArrayList<ItemEntity> itemEntities;
    private ListItemAdapter adapter;
    private ItemEntity temp;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton0;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private RadioButton mRadioButton3;
    private RadioButton mRadioButton4;
    private RadioButton mRadioButton5;
    private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件

    private RefreshListView mListView;
    private CustomProgressDialog dialog;
    private static final int STATE_REFRESH = 0;// 下拉刷新 want to refresh data
    private static final int STATE_MORE = 1;// 加载更多 has more data
    private int limit = 10;        // 每页的数据是10条
    private int curPage = 0;    // 当前页的编号，从0开始
    private String lastTime;    //当前展示页面最后一个item发表时间的数据
    private static final long DURATION_TIME = 2000;

    public home_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View home_layout = inflater.inflate(R.layout.home_fragment, container, false);
        temp = new ItemEntity();
        mListView = (RefreshListView) home_layout.findViewById(R.id.refresh_listview);
        //mPullToRefreshView = (PullToRefreshListView) home_layout.findViewById(R.id.list);
        mListView.setOnRefreshListViewListener(this);
        //点击单一物品跳转到物品详情界面
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取当前item的物品id，传递到物品详情页以展示,这里位置需要减一，因为headerview占用了一个位置
                temp = (ItemEntity) adapter.getItem(i - 1);
                String id = temp.getObject_id();
                Intent intent = new Intent(getActivity(), detial_info.class);
                intent.putExtra("object_id", id);
                startActivity(intent);
            }
        });

        mRadioGroup = (RadioGroup) home_layout.findViewById(R.id.radioGroup);
        mRadioButton0 = (RadioButton) home_layout.findViewById(R.id.btn0);
        mRadioButton1 = (RadioButton) home_layout.findViewById(R.id.btn1);
        mRadioButton2 = (RadioButton) home_layout.findViewById(R.id.btn2);
        mRadioButton3 = (RadioButton) home_layout.findViewById(R.id.btn3);
        mRadioButton4 = (RadioButton) home_layout.findViewById(R.id.btn4);
        mRadioButton5 = (RadioButton) home_layout.findViewById(R.id.btn5);
        mHorizontalScrollView = (HorizontalScrollView) home_layout.findViewById(R.id.horizontalScrollview);
        mRadioGroup.setOnCheckedChangeListener(this);
        itemEntities = new ArrayList<ItemEntity>();

        adapter = new ListItemAdapter(getActivity(), itemEntities);
        mListView.setAdapter(adapter);
        //在数据未从服务器返回前隐藏底层的上滑加载
        mListView.setLoadMoreEnable(false);
        //从服务器返回数据前先显示加载动画
        dialog = new CustomProgressDialog(getActivity(), "正在加载中", R.anim.frame);
        dialog.show();
        queryData(0, STATE_REFRESH);
        return home_layout;
    }

    @Override
    public void onLoad() {
        // 上拉加载更多(加载下一页数据)
        queryData(curPage, STATE_MORE);
    }

    @Override
    public void onRefresh() {
        // 下拉刷新(从第一页开始装载数据)
        queryData(0, STATE_REFRESH);
    }

    /**
     * 分页获取数据
     *
     * @param page       页码
     * @param actionType ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData(int page, final int actionType) {
        Log.i("bmob", "pageN:" + page + " limit:" + limit + " actionType:" + actionType);

        BmobQuery<MyGoods> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        query.include("author");//缺少该关联查询则无法获取到user
        // 如果是加载更多
        if (actionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            //query.setSkip(page * limit + 1);
        } else {
            page = 0;
            query.setSkip(page);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(getActivity(), new FindListener<MyGoods>() {
            @Override
            public void onSuccess(List<MyGoods> list) {
                if (list.size() > 0) {
                    if (actionType == STATE_REFRESH) {
                        // 当是下拉刷新操作时，将当前页的编号重置为0，并把itemEntities清空，重新添加
                        curPage = 0;
                        itemEntities.clear();
                        // 获取最后时间
                        lastTime = list.get(list.size() - 1).getCreatedAt();
                        //下拉刷新有数据，说明上拉加载更多可能有数据，因此激活上拉加载，之后要加判断，这里用户体验不好
                        mListView.setLoadMoreEnable(true);
                    }

                    // 将本次查询的数据添加到itemEntities中
                    for (MyGoods td : list) {
                        urls = (ArrayList<String>) td.getUrls();
                        //如果不重新new一个类的话，一直保留的都是对原有变量的引用，导致值重复
                        ItemEntity itemEntity = new ItemEntity();
                        itemEntity.setImageUrls(urls);
                        itemEntity.setTitle(td.getTitle());
                        itemEntity.setAvatar(td.getAuthor().getAvator().getFileUrl(getActivity()));
                        itemEntity.setContent(td.getDescription());
                        itemEntity.setName(td.getAuthor().getName());
                        itemEntity.setPrice(td.getPrice().toString() + "元");
                        itemEntity.setPlace(td.getPlace());
                        itemEntity.setObject_id(td.getObjectId());
                        adapter.add(itemEntity);
                    }

                    // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                    curPage++;
                    lastTime = list.get(list.size() - 1).getCreatedAt();
                    if (actionType == STATE_REFRESH) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListView.stopRefresh();
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListView.stopLoadMore();
                            }
                        });
                    }
                } else if (actionType == STATE_MORE) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListView.stopLoadMoreForNoDatas();
                        }
                    });
                    showToast("没有更多数据了");

                } else if (actionType == STATE_REFRESH) {
                    showToast("没有数据");
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void onError(int code, String msg) {
                showToast("查询失败:" + msg);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //待完善
        if (checkedId == R.id.btn1) {

        }
    }
}
