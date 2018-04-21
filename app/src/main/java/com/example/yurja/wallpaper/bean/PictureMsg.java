package com.example.yurja.wallpaper.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by yurja on 2018/4/9.
 */

public class PictureMsg extends BmobObject {

    private String writername;
    private String writerpicurl;
    private String content;
    public List<String> PictureList;


    public String getWritername() {
        return writername;
    }

    public void setWritername(String writername) {
        this.writername = writername;
    }

    public String getWriterpicurl() {
        return writerpicurl;
    }

    public void setWriterpicurl(String writerpicurl) {
        this.writerpicurl = writerpicurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPictureList() {
        return PictureList;
    }

    public void setPictureList(List<String> pictureList) {
        PictureList = pictureList;
    }
}
