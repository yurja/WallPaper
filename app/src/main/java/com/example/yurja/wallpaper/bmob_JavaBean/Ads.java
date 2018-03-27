package com.example.yurja.wallpaper.bmob_JavaBean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by MM on 2018/3/26.
 */

public class Ads extends BmobObject {

    private BmobFile picture;

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }
}
