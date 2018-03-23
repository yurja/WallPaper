package com.example.yurja.wallpaper.wallpaper;

import android.util.Log;

import com.example.yurja.wallpaper.bmob_JavaBean.WallPaper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 李颖佳 on 2018/3/17.
 */

public class WallPaperDaoImpl implements WallPaperDao{

    @Override
    public void queryWallPaper(String cidname, final WallPaperListener listener) {
        BmobQuery<WallPaper> query = new BmobQuery<>("WallPaper");
         if(cidname == null){
            Log.d("111","cid为空查询全部");
        }else {
            query.addWhereEqualTo("cidname",cidname);
        }
        query.findObjects(new FindListener<WallPaper>() {
            @Override
            public void done(List<WallPaper> list, BmobException e) {
              if(e == null){
                  listener.onSuccess(list);
              }else {
                  listener.onFail();
              }
            }
        });
    }

}
