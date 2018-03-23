package com.example.yurja.wallpaper.bmob_JavaBean;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by yurja on 2018/3/16.
 */

public class _User extends BmobUser {


    private BmobFile picture;


    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }


}
