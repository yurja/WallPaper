package com.example.yurja.wallpaper.user;

import android.text.TextUtils;
import android.util.Log;

import com.example.yurja.wallpaper.bean._User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yurja on 2018/3/17.
 */

public class UserDaoImpl implements UserDao {

    @Override
    public void login(String username, String password, final LoginListener loginListener) {
        Log.d("登录2","UserDaoImpl");
        if(TextUtils.isEmpty(username)){
            loginListener.onUsernameError();
        }
        if(TextUtils.isEmpty(password)){
            loginListener.onPasswordError();
        }

        BmobQuery<_User> query = new BmobQuery<>("_User");
        query.addWhereEqualTo("username",username);
        query.addWhereEqualTo("password",password);
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if(e != null){
                    loginListener.onFail();
                    Log.d("登录失败",e.getMessage());
                }else{
                    if(list != null && list.size() > 0)
                        loginListener.onSuccess(list.get(0));
                }
            }
        });
    }
}
