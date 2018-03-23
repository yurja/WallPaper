package com.example.yurja.wallpaper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.bmob_JavaBean._User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    Button registerBt;
    EditText usernameEt,passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }

    private void initView() {
        registerBt = (Button) findViewById(R.id.register_button);
        usernameEt = (EditText) findViewById(R.id.username);
        passwordEt = (EditText) findViewById(R.id.password);
    }

    public void register(View view) {
        final String uname = usernameEt.getText().toString();
        final String upwd = passwordEt.getText().toString();
        BmobUser user = new BmobUser();
        user.setUsername(uname);
        user.setPassword(upwd);
        user.signUp(new SaveListener<_User>() {
            @Override
            public void done(_User o, BmobException e) {
                if (e == null){
                    Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    intent.putExtra("uname",uname);
                    intent.putExtra("upwd",upwd);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d("注册错误",e.getMessage());
                }
            }
        });
//        user.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null){
//                    Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
//                    intent.putExtra("uname",uname);
//                    intent.putExtra("upwd",upwd);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


    }
}
