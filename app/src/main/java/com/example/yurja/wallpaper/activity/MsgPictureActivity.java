package com.example.yurja.wallpaper.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yurja.wallpaper.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MsgPictureActivity extends AppCompatActivity {

    int index;
    List<String> pictureLists;
    Gallery gallery;
    galleryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_picture);
        initData();
        initView();

    }

    private void initData() {
        pictureLists = new ArrayList<>();
        Intent intent = getIntent();
        pictureLists = intent.getStringArrayListExtra("PictureUrlLists");
        Log.d("检查","size:"+pictureLists.size());
        index = intent.getIntExtra("index",0);
        Log.d("检查","index:"+index);
    }

    private void initView() {
        gallery = (Gallery) findViewById(R.id.msg_gallery);
        adapter = new galleryAdapter(MsgPictureActivity.this,pictureLists);
        gallery.setAdapter(adapter);
        gallery.setSelection(index);
    }

    class galleryAdapter extends BaseAdapter{

        Context context;
        List<String> urlLists;

        class ViewHolder{
            ImageView pict_image;
            TextView pict_order_text;
        }

        public galleryAdapter(Context context, List<String> urlLists) {
            this.context = context;
            this.urlLists = urlLists;
        }

        @Override
        public int getCount() {
            return urlLists.size();
        }

        @Override
        public Object getItem(int i) {
            return urlLists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View myView;
            ViewHolder holder;
            if( convertView == null ){
                myView = LayoutInflater.from(context).inflate(R.layout.msg_gallery_item,null);
                holder = new ViewHolder();
                holder.pict_image = (ImageView) myView.findViewById(R.id.pict_image);
                holder.pict_order_text = (TextView) myView.findViewById(R.id.pict_order_text);
                myView.setTag(holder);
            }else {
                myView = convertView;
                holder = (ViewHolder) myView.getTag();
            }


            Picasso.with(context).load(urlLists.get(i)).into(holder.pict_image);

            holder.pict_order_text.setText("（"+(i+1)+"/"+urlLists.size()+")");

            return myView;
        }
    }
}
