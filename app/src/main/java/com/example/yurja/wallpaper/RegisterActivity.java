package com.example.yurja.wallpaper;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.yurja.wallpaper.bmob_JavaBean._User;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegisterActivity extends AppCompatActivity {

    ImageView user_pic;
    Button registerBt;
    EditText usernameEt, passwordEt;

    public static final int CAMERA_CODE = 1;//拍照
    public static final int ALBUM_CODE = 2; //相册
    private BmobFile bmobFile; //相册照片文件
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopueWindow();
            }
        });
    }


    private void initView() {
        registerBt = (Button) findViewById(R.id.register_button);
        usernameEt = (EditText) findViewById(R.id.username);
        passwordEt = (EditText) findViewById(R.id.password);
        user_pic = (ImageView) findViewById(R.id.image_view);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ALBUM_CODE:
                Uri album_imageUri = data.getData();
                user_pic.setImageURI(album_imageUri);
                        Cursor cursor = getContentResolver().query(album_imageUri,null, null, null, null);
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                                upload(path);
                            }
                            cursor.close();
                        }


                break;
            case CAMERA_CODE:
                user_pic.setImageURI(imageUri);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        upload(imageUri.getPath());
                    }
                }).start();
                Toast.makeText(RegisterActivity.this,"头像上传成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void upload(String path) {
        bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    Toast.makeText(RegisterActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                    Log.d("头像",e.getMessage());
                }
            }
        });
    }


    private void initPopueWindow() {
        View popView = View.inflate(this, R.layout.popupwindow_upload, null);
        Button cameraBt = (Button) popView.findViewById(R.id.camera);
        Button albumBt = (Button) popView.findViewById(R.id.album);
//        Button cancelBt = (Button) popView.findViewById(R.id.cancel);

        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels * 1 / 3;

        final PopupWindow popupWindow = new PopupWindow(popView, weight, height);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);

        albumBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, ALBUM_CODE);
            }
        });

        cameraBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(), "out_image.jpg");
                if (outputImage.exists()) {
                    outputImage.delete();
                        }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(RegisterActivity.this,
                            "com.example.yurja.wallpaper.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAMERA_CODE);
            }
        });
    }


    public void register(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String uname = usernameEt.getText().toString();
                final String upwd = passwordEt.getText().toString();
                final _User user = new _User();
                if (bmobFile != null) {
                    user.setPicture(bmobFile);
                }
                user.setUsername(uname);
                user.setPassword(upwd);
                user.signUp(new SaveListener<_User>() {
                    @Override
                    public void done(_User o, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("uname", uname);
                            intent.putExtra("upwd", upwd);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d("注册错误", e.getMessage());
                        }
                    }
                });
            }
        }).start();
    }
}