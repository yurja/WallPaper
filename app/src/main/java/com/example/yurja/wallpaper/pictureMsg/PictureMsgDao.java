package com.example.yurja.wallpaper.pictureMsg;

import com.example.yurja.wallpaper.bean.PictureMsg;

import java.util.List;

/**
 * Created by yurja on 2018/4/21.
 */

public interface PictureMsgDao {

    interface  PictureMsgListener{
        void onSuccess(List<PictureMsg> list);
        void  onFail();
    }

    public void queryPictureMsg(PictureMsgListener listener);
}
