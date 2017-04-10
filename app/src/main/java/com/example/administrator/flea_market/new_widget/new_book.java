package com.example.administrator.flea_market.new_widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.activity.MainActivity;
import com.example.administrator.flea_market.activity.detial_info;
import com.example.administrator.flea_market.bean.MyGoods;
import com.example.administrator.flea_market.bean.MyUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class new_book extends Activity {
    private GridView gridView1;    //网格显示缩略图
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器

    private EditText edit_description;
    private EditText edit_title;
    private EditText edit_number;
    private EditText edit_price;
    private EditText edit_old_price;
    private EditText edit_phone;
    private EditText edit_place;
    private EditText edit_school;
    private EditText edit_rank;
    PopupMenu popup = null;
    PopupMenu popup2 = null;
    ProgressDialog progressDialog = null;
    private Button forward_btn;

    private MyUser myUser;
    private String object_id;
    private String type;
    private String title;
    private String description;
    private Integer number;
    private String book_school;
    private String rank;
    private Integer price;
    private Integer old_price;
    private String phone;
    private String place;
    private MyGoods post;
    private ArrayList<String> pic_urls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        Intent i = getIntent();
        object_id = i.getStringExtra("object_id");
        type = i.getStringExtra("type");
        myUser = BmobUser.getCurrentUser(this, MyUser.class);
        edit_school = (EditText) findViewById(R.id.editschool);
        edit_rank = (EditText) findViewById(R.id.editrank);
        edit_title = (EditText) findViewById(R.id.post_title);
        edit_description = (EditText) findViewById(R.id.post_description);
        edit_number = (EditText) findViewById(R.id.post_number);
        edit_price = (EditText) findViewById(R.id.post_price);
        edit_old_price = (EditText) findViewById(R.id.post_old_price);
        edit_phone = (EditText) findViewById(R.id.post_phone);
        edit_place = (EditText) findViewById(R.id.post_place);
        forward_btn = (Button) findViewById(R.id.forward_btn);
        //院系选项，用了popupMenu弹出菜单
        edit_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(new_book.this, v);
                popup.getMenuInflater().inflate(R.menu.menu_new_book, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        edit_school.setText(item.getTitle());
                        return true;
                    }
                });
                popup.show();
            }
        });
        edit_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup2 = new PopupMenu(new_book.this, v);
                popup2.getMenuInflater().inflate(R.menu.menu_rank, popup2.getMenu());
                popup2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        edit_rank.setText(item.getTitle());
                        return true;
                    }
                });
                popup2.show();
            }
        });
        //获取控件对象
        gridView1 = (GridView) findViewById(R.id.gridView1);
        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic); //加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        pic_urls = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[]{"itemImage"}, new int[]{R.id.imageView1});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 4 && position == 0) { //第一张为默认图片
                    Toast.makeText(new_book.this, "图片数3张已满", Toast.LENGTH_SHORT).show();
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    //选择图片
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                    //通过onResume()刷新数据
                } else {
                    dialog(position);
                    //Toast.makeText(MainActivity.this, "点击第" + (position + 1) + " 号图片",
                    //		Toast.LENGTH_SHORT).show();
                }

            }
        });

        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            //之后添加等待进度圆圈
            public void onClick(View view) {
                title = edit_title.getText().toString();
                number = Integer.valueOf(edit_number.getText().toString());
                book_school = edit_school.getText().toString();
                rank = edit_rank.getText().toString();
                description = edit_description.getText().toString();
                price = Integer.valueOf(edit_price.getText().toString());
                old_price = Integer.valueOf(edit_old_price.getText().toString());
                phone = edit_phone.getText().toString();
                place = edit_place.getText().toString();
                if (title == null || number == null || book_school == null || rank == null || description == null || price == null || old_price == null || phone == null || place == null) {
                    Toast.makeText(new_book.this, "请填写完整", Toast.LENGTH_SHORT).show();
                } else {
                    //创建ProgressDialog对象
                    progressDialog = new ProgressDialog(new_book.this);
                    // 设置进度条风格，风格为圆形，旋转的
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // 设置ProgressDialog 标题
                    progressDialog.setTitle("提示");
                    // 设置ProgressDialog 提示信息
                    progressDialog.setMessage("正在提交，请等候");
                    // 设置ProgressDialog 的进度条是否不明确
                    progressDialog.setIndeterminate(false);
                    // 设置ProgressDialog 是否可以按退回按键取消
                    progressDialog.setCancelable(false);
                    // 让ProgressDialog显示
                    progressDialog.show();
                    String[] urls = pic_urls.toArray(new String[pic_urls.size()]);
                    post = new MyGoods();
                    BmobFile.uploadBatch(new_book.this, urls, new UploadBatchListener() {
                        @Override
                        public void onSuccess(List<BmobFile> list, List<String> list1) {
                            //如果数量相等，则代表文件全部上传完成,此时再进行操作
                            if (list1.size() == pic_urls.size()) {
                                post.setUrls(list1);
                                post.setAuthor(myUser);
                                post.setType(1);
                                post.setGood_type("教学书籍");
                                post.setNumber(number);
                                post.setBook_for_school(book_school);
                                post.setRank(rank);
                                post.setDescription(description);
                                post.setPrice(price);
                                post.setOld_price(old_price);
                                post.setPhone(phone);
                                post.setPlace(place);
                                post.setTitle(title);
                                post.save(new_book.this, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.dismiss();
                                        Toast.makeText(new_book.this, "发表成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(new_book.this, detial_info.class);
                                        intent.putExtra("object_id", post.getObjectId());
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int code, String arg0) {
                                        progressDialog.dismiss();
                                        Toast.makeText(new_book.this, arg0, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onProgress(int i, int i1, int i2, int i3) {

                        }

                        @Override
                        public void onError(int i, String s) {
                            progressDialog.dismiss();
                            Toast.makeText(new_book.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[]{MediaStore.Images.Media.DATA},
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                pic_urls.add(pathImage);
            }
        }  //end if 打开图片
    }

    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[]{"itemImage"}, new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView1.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new_book.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                //删除图片时也应该更新保存图片路径的数组,这里注意减1，因为它一开始啥都没保存
                pic_urls.remove(position-1);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_book, menu);
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
