package com.example.yurja.wallpaper.pictureMsg;

import com.example.yurja.wallpaper.bean.PictureMsg;

import java.util.List;

/**
 * Created by yurja on 2018/4/21.
 */

public class PictureMsgPresenterImpl implements PictureMsgPresenter,PictureMsgDao.PictureMsgListener {

    PictureMsgView pictureMsgView;
    PictureMsgDao pictureMsgDao;

    public PictureMsgPresenterImpl(PictureMsgView pictureMsgView) {
        this.pictureMsgView = pictureMsgView;
        pictureMsgDao = new PictureMsgDaoImpl();

    }

    @Override
    public void getPictureMsg() {
        pictureMsgDao.queryPictureMsg(this);
    }

    @Override
    public void onSuccess(List<PictureMsg> list) {
        pictureMsgView.setPictureMsg(list);

    }

    @Override
    public void onFail() {
        pictureMsgView.setFail();
    }
}
