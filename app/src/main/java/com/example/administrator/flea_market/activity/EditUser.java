package com.example.administrator.flea_market.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.administrator.flea_market.config.MyConfig;

import java.io.File;

import circleimageview.CircleImageView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/*
此为用户登录后立刻完善资料的ui
 */
public class EditUser extends Activity {
    public final int REQUESTCODE_TAKE_ALBUM = 2;
    private Button mBackwardbButton;
    private Button mForwardButton;
    private LinearLayout edit_pic;
    private CircleImageView person_pic;
    private EditText edit_name;
    private RadioGroup radio_sex;
    private RadioGroup radio_school;
    private String object_id;
    private String pathImage;
    private String url;
    private String name;//用户昵称
    private BmobFile avator;//用于存放头像文件
    private Boolean sex;//性别，true为男，false为女
    private Integer school_place;//所在校区，1代表南校、2代表东校、3代表珠海、4代表北校
    ProgressDialog progressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        object_id = intent.getStringExtra("object_id");
        setContentView(R.layout.activity_edit_user);
        Toast.makeText(EditUser.this, "请进一步完善个人资料", Toast.LENGTH_LONG).show();
        mBackwardbButton = (Button) findViewById(R.id.button_backward);
        mForwardButton = (Button) findViewById(R.id.button_forward);
        edit_pic = (LinearLayout) findViewById(R.id.edit_pic);
        person_pic = (CircleImageView) findViewById(R.id.person_pic);
        edit_name = (EditText) findViewById(R.id.person_name);
        radio_sex = (RadioGroup) findViewById(R.id.sex);
        radio_school = (RadioGroup) findViewById(R.id.school_type);
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
                new AlertDialog.Builder(EditUser.this)
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
                startActivity(new Intent(EditUser.this, LoginActivity.class));
            }
        });
        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edit_name.getText().toString();
                if (name == null || sex == null || school_place == null) {
                    Toast.makeText(EditUser.this, "请填写完整", Toast.LENGTH_SHORT).show();
                } else {
                    //创建ProgressDialog对象
                    progressDialog = new ProgressDialog(EditUser.this);
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

                    final MyUser newUser = new MyUser();
                    newUser.setName(name);
                    newUser.setSex(sex);
                    newUser.setSchool_place(school_place);
                    //avator = new BmobFile(filename, null, new File(pathImage).toString());
                    //bmobfile需要上传之后成功了再保存进user中，否则是一个空文件
                    avator = new BmobFile(new File(pathImage));
                    avator.uploadblock(EditUser.this, new UploadFileListener() {
                        @Override
                        public void onSuccess() {
                            //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                            url = avator.getFileUrl(EditUser.this);
                            newUser.setAvator(avator);
                            newUser.setAvatorUrl(url);
                            newUser.update(EditUser.this, object_id, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditUser.this, "完善资料成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("object_id", object_id);
                                    intent.setClass(EditUser.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(int code, String msg) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditUser.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onProgress(Integer value) {
                            // 返回的上传进度（百分比）
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            progressDialog.dismiss();
                            Toast.makeText(EditUser.this, msg, Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_edit_user, menu);
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
