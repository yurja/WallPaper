package com.example.yurja.wallpaper.wallpaper;

import com.example.yurja.wallpaper.bmob_JavaBean.WallPaper;

import java.util.List;

/**
 * Created by MM on 2018/3/17.
 */

public interface WallPaperView {
    public void setWallPapaer(List<WallPaper> list);

    public void  setFail();
}
