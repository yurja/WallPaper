package com.example.yurja.wallpaper.wallpaper;

import com.example.yurja.wallpaper.bmob_JavaBean.WallPaper;

import java.util.List;

/**
 * Created by MM on 2018/3/17.
 */

public class WallPaperPresenterImpl implements  WallPaperPresenter,WallPaperDao.WallPaperListener {

    WallPaperView wallPaperView;
    WallPaperDao dao;

    public WallPaperPresenterImpl(WallPaperView wallPaperView) {
        this.wallPaperView = wallPaperView;
        dao = new WallPaperDaoImpl();
    }

    @Override
    public void queryWallPaper(String cidname) {
        dao.queryWallPaper(cidname,this);
    }

    @Override
    public void onSuccess(List<WallPaper> list) {
        wallPaperView.setWallPapaer(list);
    }

    @Override
    public void onFail() {
        wallPaperView.setFail();
    }
}
