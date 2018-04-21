package com.example.yurja.wallpaper.user;

import android.util.Log;

import com.example.yurja.wallpaper.bean._User;

/**
 * Created by yurja on 2018/3/17.
 */

public class UserPresenterImpl implements UserPresenter,UserDao.LoginListener { //纽带

    LoginView loginView; //前端关联view
    UserDao dao; //后端关联dao


    public UserPresenterImpl (LoginView loginView){
        this.loginView = loginView;
        dao = new UserDaoImpl();
    }


    @Override
    public void login(String username, String password) {

        Log.d("登录1","UserPresent");
        dao.login(username,password,this);
    }

    @Override
    public void onUsernameError() {
        loginView.setUsernameError();
    }

    @Override
    public void onPasswordError() {
        loginView.setPasswordError();
    }

    @Override
    public void onSuccess(_User user) {
        loginView.success(user);
    }
    @Override
    public void onFail() {
        Log.i("LoginView","fail");
        loginView.fail();
    }


}
