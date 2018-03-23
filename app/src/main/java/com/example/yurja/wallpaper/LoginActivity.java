package com.example.yurja.wallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurja.wallpaper.bmob_JavaBean._User;
import com.example.yurja.wallpaper.user.LoginView;
import com.example.yurja.wallpaper.user.UserPresenter;
import com.example.yurja.wallpaper.user.UserPresenterImpl;

public class LoginActivity extends AppCompatActivity implements LoginView {

    EditText usernameEt,passwordEt;

    UserPresenter presenter;

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
            }
        });
        presenter = new UserPresenterImpl(this);
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
        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        Log.d("登录","LoginActivity");
        presenter.login(username,password);
    }

    @Override
    public void setUsernameError() {
        usernameEt.setError("username error");
    }

    @Override
    public void setPasswordError() {
        passwordEt.setError("password error");
    }

    @Override
    public void success(_User user) {
       Toast.makeText(LoginActivity.this, "username:"+user.getUsername(), Toast.LENGTH_LONG).show();
        login_app(user);
    }

    private void login_app(_User user) {
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("username", user.getUsername());
        if(user.getPicture().getFileUrl()!=null){
            intent.putExtra("pic_url", user.getPicture().getFileUrl());
            Log.d("url",user.getPicture().getFileUrl());
        }
        // 1是返回的requestCode
        startActivity(intent);
        Log.d("Login","登录");
        //这里要直接干掉
        finish();
    }

    @Override
    public void fail() {
        Toast.makeText(LoginActivity.this, "fail", Toast.LENGTH_SHORT).show();
    }

}
