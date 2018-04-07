package com.example.yurja.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.example.yurja.wallpaper.bmob_JavaBean.WallPaper;
import com.example.yurja.wallpaper.bmob_JavaBean._User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class WallPaperActivity extends AppCompatActivity {

    Gallery gallery;
    List<WallPaper> wallPaperList;//要显示在gallery中的壁纸
    List<WallPaper> collectWallPaperList; //用户收藏的壁纸
    int index;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_paper);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        wallPaperList = new ArrayList<>();
        collectWallPaperList = new ArrayList<>();//用户收藏的壁纸
        initCollectData();
        wallPaperList = (List<WallPaper>)intent.getSerializableExtra("wallPaperList");
        index = intent.getIntExtra("index",0);
        gallery = (Gallery) findViewById(R.id.gallery);
        myAdapter =new MyAdapter(this,wallPaperList);
        gallery.setAdapter(myAdapter);
        gallery.setSelection(index);
    }

    private void initCollectData() { //查询当当前用户收藏的壁纸
        BmobQuery<WallPaper> query = new BmobQuery<WallPaper>();
        _User user = BmobUser.getCurrentUser(_User.class);
        collectWallPaperList.clear();
        query.addWhereRelatedTo("likes",new BmobPointer(user));
        query.findObjects(new FindListener<WallPaper>() {
            @Override
            public void done(List<WallPaper> list, BmobException e) {
                    if(e==null){
                    collectWallPaperList.addAll(list);
                    Log.d("更新","收藏列表");
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }

            }
        });
    }

    class MyAdapter extends BaseAdapter{
        Context context;
        List<WallPaper> list;
        ImageView collect_wp;
        public MyAdapter(Context context, List<WallPaper> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View myView;

            if( convertView == null ){
                myView = LayoutInflater.from(context).inflate(R.layout.gallery_item,null);
            }else {
                myView = convertView;
            }
            final ImageView wallpaper = (ImageView) myView.findViewById(R.id.image_view);
            ImageView set_wp = (ImageView) myView.findViewById(R.id.set_wp);
            collect_wp = (ImageView) myView.findViewById(R.id.collect_wp);

            if(collectWallPaperList.contains(list.get(position)) == true){ //如何当前壁纸是用户收藏的壁纸
                collect_wp.setImageResource(R.drawable.collectb); //设置为收藏状态
            }

            collect_wp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _User user = BmobUser.getCurrentUser(_User.class);
                    WallPaper wp = list.get(position);
                    BmobRelation relation = new BmobRelation();
                    if(collectWallPaperList.contains(list.get(position)) == true){
                        relation.remove(wp);
                        user.setLikes(relation);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(context,"取消收藏",Toast.LENGTH_SHORT).show();
                                    collect_wp.setImageResource(R.drawable.collecta);
                                    initCollectData();
                                }else{
                                    Log.i("bmob","失败："+e.getMessage());
                                }
                            }
                        });
                    }else {
                        relation.add(wp);
                        user.setLikes(relation);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                                    collect_wp.setImageResource(R.drawable.collectb);
                                    initCollectData();
                                }else{
                                    Log.i("bmob","失败："+e.getMessage());
                                }
                            }
                        });

                    }




                }
            });
            set_wp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlstring = list.get(position).getWallpaper().getFileUrl();
                    InputStream in = null;
                    try {
                        URL url = new URL(urlstring);
                        in = url.openStream();
                        Bitmap bm = BitmapFactory.decodeStream(in);
                        WallpaperManager manager = WallpaperManager.getInstance(WallPaperActivity.this);
                        manager.setBitmap(bm);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        Toast.makeText(context,"设置失败",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context,"设置失败",Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(context,"设置成功",Toast.LENGTH_SHORT).show();
                }
            });
            WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);

            DisplayMetrics dm = new DisplayMetrics();

            wm.getDefaultDisplay().getMetrics(dm);

            ViewGroup.LayoutParams params = wallpaper.getLayoutParams();

            params.width = dm.widthPixels;
            params.height = dm.heightPixels;

            wallpaper.setScaleType(ImageView.ScaleType.FIT_XY);

            Picasso.with(context).load(list.get(position).getWallpaper().getFileUrl()).resize(params.width,params.height)
                    .centerCrop().into(wallpaper);

            return myView;
        }
    }

}
