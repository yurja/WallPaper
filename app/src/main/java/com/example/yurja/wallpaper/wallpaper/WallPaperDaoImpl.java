package com.example.yurja.wallpaper.wallpaper;

import android.util.Log;

import com.example.yurja.wallpaper.bmob_JavaBean.WallPaper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by MM on 2018/3/17.
 */

public class WallPaperDaoImpl implements WallPaperDao {
    @Override
    public void queryWallPaper(String cidname, final WallPaperListener listener) {
        BmobQuery<WallPaper> query = new BmobQuery<>("WallPaper");
         if(cidname == null){
            Log.d("22","cid为空查询全部");
        }else {
            query.addWhereEqualTo("cidname",cidname);
            Log.d("22222222",cidname);
        }
        query.findObjects(new FindListener<WallPaper>() {
            @Override
            public void done(List<WallPaper> list, BmobException e) {
              if(e == null){
                  listener.onSuccess(list);
                  Log.d("222 list size",list.size()+"");
              }else {
                  listener.onFail();
              }
            }
        });
    }
}
