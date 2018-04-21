package com.example.yurja.wallpaper.wallpaper;

import com.example.yurja.wallpaper.bean.WallPaper;

import java.util.List;

/**
 * Created by yutja on 2018/3/17.
 */

public interface WallPaperDao {

    interface WallPaperListener{
        public void onSuccess(List<WallPaper> list);

        public void onFail();
    }

    public void queryWallPaper(String cidname,WallPaperListener listener);
}
