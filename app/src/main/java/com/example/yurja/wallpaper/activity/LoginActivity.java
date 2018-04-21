package com.example.yurja.wallpaper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.bean._User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity  {

    EditText usernameEt,passwordEt;

    //UserPresenter presenter;

    TextView registerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //presenter = new UserPresenterImpl(this);
        Intent intent = getIntent();
        String uname = intent.getStringExtra("uname");
        String upwd = intent.getStringExtra("upwd");
        usernameEt.setText(uname);
        passwordEt.setText(upwd);
    }

    private void initViews() {
        usernameEt = (EditText) findViewById(R.id.username);
        passwordEt = (EditText) findViewById(R.id.password);
        registerTv = (TextView) findViewById(R.id.register_tv);
    }

    public void login(View view){
        final String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        Log.d("登录","LoginActivity");
        _User user = new _User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if ( e==null ){
                    Toast.makeText(LoginActivity.this,"欢迎你,"+username+"!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    @Override
//    public void setUsernameError() {
//        usernameEt.setError("username error");
//    }
//
//    @Override
//    public void setPasswordError() {
//        passwordEt.setError("password error");
//    }


//    //登录成功调用此方法
//    @Override
//    public void success(_User user) {
//        Toast.makeText(LoginActivity.this, "登录成功："+user.getUsername(), Toast.LENGTH_LONG).show();
//        //login_app(user);
//    }

    //传递数据到MainActvity
//    private void login_app(_User user) {
//        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//        intent.putExtra("username", user.getUsername());
//        if(user.getPicture().getFileUrl()!=null){
//            intent.putExtra("pic_url", user.getPicture().getFileUrl());
//            Log.d("url",user.getPicture().getFileUrl());
//        }
//        startActivity(intent);
//        finish();
//    }

//    @Override
//        public void fail() {
//            Toast.makeText(LoginActivity.this, "fail", Toast.LENGTH_SHORT).show();
//    }

}
