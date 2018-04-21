package com.example.yurja.wallpaper.user;

import com.example.yurja.wallpaper.bean._User;

/**
 * Created by yurja on 2018/3/17.
 */

public interface UserDao {

    interface LoginListener{
        void onUsernameError();
        void onPasswordError();
        void onSuccess(_User user);
        void onFail();

    }

    public void login(String username,String password,LoginListener loginListener);
}
