package com.example.yurja.wallpaper.user;

import com.example.yurja.wallpaper.bean._User;

/**
 * Created by yurja on 2018/3/17.
 */

public interface LoginView {
    public void setUsernameError();

    public void setPasswordError();

    public void fail();

    public void success(_User user);
}
