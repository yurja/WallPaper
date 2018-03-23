package com.example.yurja.wallpaper.category;

import com.example.yurja.wallpaper.bmob_JavaBean.Category;

import java.util.List;

/**
 * Created by yurja on 2018/3/17.
 */

public class CategoryPresenterImpl  implements CategoryPresenter,CategoryDao.CategoryListener{

    CategoryView categoryView;
    CategoryDao dao;

    public CategoryPresenterImpl(CategoryView categoryView) {
        this.categoryView = categoryView;
        dao = new CategoryDaoImpl();
    }

    @Override
    public void getCategory() {
        dao.queryCategory(this);
    }

    @Override
    public void onSuccess(List<Category> list) {
        categoryView.setCategory(list);

    }

    @Override
    public void onFail() {
        categoryView.setFail();

    }
}
