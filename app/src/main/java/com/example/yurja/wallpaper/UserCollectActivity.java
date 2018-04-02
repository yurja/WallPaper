package com.example.yurja.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yurja.wallpaper.bmob_JavaBean.WallPaper;
import com.example.yurja.wallpaper.bmob_JavaBean._User;
import com.example.yurja.wallpaper.fragments.HomeFragment;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserCollectActivity extends AppCompatActivity {

    List<WallPaper> wallPaperList;
    CollectAdapter collectAdapter;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_collect);
        wallPaperList = new ArrayList<WallPaper>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initData();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager manager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(manager);
        collectAdapter = new CollectAdapter(this,wallPaperList);
        recyclerView.setAdapter(collectAdapter);
        Log.d("1111",""+wallPaperList.size());
    }

    private void initData() {
        BmobQuery<WallPaper> query = new BmobQuery<WallPaper>();
        _User user = BmobUser.getCurrentUser(_User.class);
        query.addWhereRelatedTo("likes",new BmobPointer(user));
        query.findObjects(new FindListener<WallPaper>() {
            @Override
            public void done(List<WallPaper> list, BmobException e) {
                if(e==null){
                    wallPaperList.addAll(list);
                    collectAdapter.notifyDataSetChanged();
                    Log.i("bmob","查询个数："+wallPaperList.size());
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }

            }
        });
    }



    public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder>
    {
        Context context;
        private List<WallPaper> wallPaperList;

        public  class ViewHolder extends RecyclerView.ViewHolder{

            ImageView imageView;

            public ViewHolder(View view){
                super(view);
                imageView = (ImageView) view.findViewById(R.id.collect_img_view);
            }
        }

        public CollectAdapter(Context context, List<WallPaper> wallPaperList) {
            this.context = context;
            this.wallPaperList = wallPaperList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,
                    parent,false);
            ViewHolder holder = new ViewHolder(view);
            Log.d("111","返回holder");
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            WallPaper wallPaper = wallPaperList.get(position);
            Picasso.with(getApplicationContext()).load(wallPaper.getWallpaper().getFileUrl()).
                    into(holder.imageView);
            Log.d("111","Bind");
        }

        @Override
        public int getItemCount() {
            return wallPaperList.size();
        }
    }



}
