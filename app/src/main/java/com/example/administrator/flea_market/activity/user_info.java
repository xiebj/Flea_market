package com.example.administrator.flea_market.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.flea_market.R;
import com.example.administrator.flea_market.bean.MyUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import circleimageview.CircleImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

//用户在个人中心编辑个人资料
public class user_info extends Activity {
    public final int REQUESTCODE_TAKE_ALBUM = 2;
    private Button mBackwardbButton;
    private Button mForwardButton;
    private LinearLayout edit_pic;
    private CircleImageView person_pic;
    private EditText edit_name;
    private RadioGroup radio_sex;
    private RadioGroup radio_school;
    private RadioButton male;
    private RadioButton female;
    private RadioButton school1;
    private RadioButton school2;
    private RadioButton school3;
    private RadioButton school4;
    private String object_id;
    private String pathImage;
    private String name;//用户昵称
    private BmobFile avator;//用于存放头像文件
    private Boolean sex;//性别，true为男，false为女
    private Integer school_place;//所在校区，1代表南校、2代表东校、3代表珠海、4代表北校

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        object_id = intent.getStringExtra("object_id");
        setContentView(R.layout.activity_user_info);
        mBackwardbButton = (Button) findViewById(R.id.button_backward);
        mForwardButton = (Button) findViewById(R.id.button_forward);
        edit_pic = (LinearLayout) findViewById(R.id.edit_pic);
        person_pic = (CircleImageView) findViewById(R.id.person_pic);
        edit_name = (EditText) findViewById(R.id.person_name);
        radio_sex = (RadioGroup) findViewById(R.id.sex);
        radio_school = (RadioGroup) findViewById(R.id.school_type);
        male = (RadioButton) findViewById(R.id.male_btn);
        female = (RadioButton) findViewById(R.id.female_btn);
        school1 = (RadioButton) findViewById(R.id.south);
        school2 = (RadioButton) findViewById(R.id.east);
        school3 = (RadioButton) findViewById(R.id.zhuhai);
        school4 = (RadioButton) findViewById(R.id.north);
        //先获取原始资料。这里需要调用bmob的查询
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.getObject(object_id, new QueryListener<MyUser>() {

            @Override
            public void done(MyUser object, BmobException e) {
                if (e == null) {
                    name = object.getName();
                    avator = object.getAvator();
                    sex = object.getSex();
                    school_place = object.getSchool_place();
                    edit_name.setText(name);
                    if (sex) {
                        male.setChecked(true);
                    } else {
                        female.setChecked(true);
                    }
                    if (school_place == 1) {
                        school1.setChecked(true);
                    } else if (school_place == 2) {
                        school2.setChecked(true);
                    } else if (school_place == 3) {
                        school3.setChecked(true);
                    } else if (school_place == 4) {
                        school4.setChecked(true);
                    }
                    // 使用ImageLoader加载网络图片
                    DisplayImageOptions options = new DisplayImageOptions.Builder()//
                            .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
                            .showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图�?
                            .cacheInMemory(true) // 内存缓存
                            .cacheOnDisk(true) // sdcard缓存
                            .bitmapConfig(Bitmap.Config.RGB_565)// 设置�?低配�?
                            .build();//
                    ImageLoader.getInstance().displayImage(avator.getFileUrl(), person_pic, options);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });
        radio_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //获取变更后的选中项的ID
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                //获取选择内容，rb.getText()
                if (rb.getText().equals("男")) {
                    sex = true;
                } else if (rb.getText().equals("女")) {
                    sex = false;
                } else {
                    sex = null;
                }
            }
        });
        radio_school.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //获取变更后的选中项的ID
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                //获取选择内容，rb.getText()
                if (rb.getText().equals("南校")) {
                    school_place = new Integer(1);
                } else if (rb.getText().equals("东校")) {
                    school_place = new Integer(2);
                } else if (rb.getText().equals("珠海")) {
                    school_place = new Integer(3);
                } else if (rb.getText().equals("北校")) {
                    school_place = new Integer(4);
                } else {
                    school_place = null;
                }
            }
        });
        //点击设置头像
        edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示dialog，分拍照和相册两种选择
                new AlertDialog.Builder(user_info.this)
                        .setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    //跳转到相机界面
                                    go_CameraActivity();
                                } else {
                                    //跳转到相册界面
                                    go_changeFromAlbum();
                                }
                            }
                        }).show();
            }
        });

        mBackwardbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edit_name.getText().toString();
                if (name == null || sex == null || school_place == null) {
                    Toast.makeText(user_info.this, "请填写完整", Toast.LENGTH_SHORT).show();
                } else {
                    String filename = name + "_avator";
                    //avator = new BmobFile(filename, null, new File(pathImage).toString());
                    //bmobfile需要上传之后成功了再保存进user中，否则是一个空文件
                    if (pathImage != null) {
                        avator = new BmobFile(new File(pathImage));
                    }
                    avator.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                MyUser newUser = new MyUser();
                                newUser.setName(name);
                                newUser.setSex(sex);
                                newUser.setSchool_place(school_place);
                                newUser.setAvator(avator);
                                newUser.update(object_id, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(user_info.this, "修改资料成功", Toast.LENGTH_SHORT).show();
                                            Intent intent1 = new Intent();
                                            intent1.setAction("renew_user_info");
                                            sendBroadcast(intent1);
                                            finish();
                                        } else {
                                            Toast.makeText(user_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(user_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //跳转到系统相册界面
    public void go_changeFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUESTCODE_TAKE_ALBUM);
    }

    /**
     * 跳转到相机界面
     */
    public void go_CameraActivity() {

    }

    /**
     * 回调
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_TAKE_ALBUM:
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
                        Bitmap bmp = BitmapFactory.decodeFile(pathImage);
                        person_pic.setImageBitmap(bmp);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
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
