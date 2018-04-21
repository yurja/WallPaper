package com.example.yurja.wallpaper.category;

import com.example.yurja.wallpaper.bean.Category;

import java.util.List;

/**
 * Created by yurja on 2018/3/17.
 */

public interface CategoryView {
    public void setCategory(List<Category> list);
    public  void setFail();
}
