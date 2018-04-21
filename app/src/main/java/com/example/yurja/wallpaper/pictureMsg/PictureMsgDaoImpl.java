package com.example.yurja.wallpaper.pictureMsg;

import android.util.Log;
import android.widget.Toast;

import com.example.yurja.wallpaper.bean.PictureMsg;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yurja on 2018/4/21.
 */

public class PictureMsgDaoImpl implements PictureMsgDao {
    @Override
    public void queryPictureMsg(final PictureMsgListener listener) {
        BmobQuery<PictureMsg> query = new BmobQuery<>("PictureMsg");
        query.findObjects(new FindListener<PictureMsg>() {
            @Override
            public void done(List<PictureMsg> list, BmobException e) {
                if (e == null) {
                    listener.onSuccess(list);
                } else {
                    listener.onFail();
                }
            }
        });
    }
}
