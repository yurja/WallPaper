package com.example.yurja.wallpaper.bmob_JavaBean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by yurja on 2018/4/9.
 */

public class PictureMsg extends BmobObject {

    private _User  writer;
    private String content;
    public List<BmobFile> PictureList;

    public _User getWriter() {
        return writer;
    }

    public void setWriter(_User writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<BmobFile> getPictureList() {
        return PictureList;
    }

    public void setPictureList(List<BmobFile> pictureList) {
        PictureList = pictureList;
    }
}
