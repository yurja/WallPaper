package com.example.yurja.wallpaper.category;

import com.example.yurja.wallpaper.bean.Category;

import java.util.List;

/**
 * Created by yurja on 2018/3/17.
 */

public interface CategoryDao {

    interface CategoryListener{
        void onSuccess(List<Category> list);
        void onFail();
    }

    public void queryCategory(CategoryListener listener);
}
