package com.example.administrator.flea_market.home_widget;

import java.util.ArrayList;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.activity.user_info;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import circleimageview.CircleImageView;

/**
 * ListView的数据适配器
 *
 * @author Administrator
 */
public class ListItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ItemEntity> items;

    public ListItemAdapter(Context ctx, ArrayList<ItemEntity> items) {
        this.mContext = ctx;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.listview_item, null);
            holder.iv_avatar = (CircleImageView) convertView.findViewById(R.id.circleImageView);
            holder.tv_title = (TextView) convertView.findViewById(R.id.title);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.place = (TextView) convertView.findViewById(R.id.place);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.tv_content = (TextView) convertView.findViewById(R.id.description);
            holder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gridview);

            holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, user_info.class);
                    mContext.startActivity(intent);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemEntity itemEntity = items.get(position);
        holder.tv_title.setText(itemEntity.getTitle());
        holder.place.setText(itemEntity.getPlace());
        holder.name.setText(itemEntity.getName());
        holder.price.setText(itemEntity.getPrice());
        holder.tv_content.setText(itemEntity.getContent());
        // 使用ImageLoader加载网络图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图�?
                .cacheInMemory(true) // 内存缓存
                .cacheOnDisk(true) // sdcard缓存
                .bitmapConfig(Config.RGB_565)// 设置�?低配�?
                .build();//
        ImageLoader.getInstance().displayImage(itemEntity.getAvatar(), holder.iv_avatar, options);
        final ArrayList<String> imageUrls = itemEntity.getImageUrls();
        if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
            holder.gridview.setVisibility(View.GONE);
        } else {
            holder.gridview.setAdapter(new NoScrollGridAdapter(mContext, imageUrls));
            holder.gridview.setClickable(false);
            holder.gridview.setPressed(false);
            holder.gridview.setEnabled(false);
        }
        /* 点击回帖九宫格，查看大图
        holder.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				imageBrower(position, imageUrls);
			}
		});
		*/
        return convertView;
    }

    /**
     * 打开图片查看�?
     *
     * @param position
     * @param urls2
     */
    /*
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}
	*/

    /**
     * listview组件复用，防止卡顿
     *
     * @author Administrator
     */
    class ViewHolder {
        private CircleImageView iv_avatar;
        private TextView tv_title;
        private TextView price;
        private TextView name;
        private TextView place;
        private TextView tv_content;
        private NoScrollGridView gridview;
    }
}
