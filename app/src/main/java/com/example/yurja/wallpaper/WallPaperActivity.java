package com.example.yurja.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class WallPaperActivity extends AppCompatActivity {

    Gallery gallery;
    List<WallPaper> wallPaperList;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_paper);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        wallPaperList = new ArrayList<>();
        wallPaperList = (List<WallPaper>)intent.getSerializableExtra("wallPaperList");
        Log.d("WallPaperList size",""+wallPaperList.size());
        index = intent.getIntExtra("index",0);
        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new MyAdapter(this,wallPaperList));
        gallery.setSelection(index);
    }

    class MyAdapter extends BaseAdapter{

        Context context;
        List<WallPaper> list;

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
            ImageView collect_wp = (ImageView) myView.findViewById(R.id.collect_wp);


            collect_wp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _User user = BmobUser.getCurrentUser(_User.class);
                    WallPaper wp = list.get(position);
                    BmobRelation relation = new BmobRelation();
                    relation.add(wp);
                    user.setLikes(relation);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                                Log.i("bmob","多对多关联添加成功");
                            }else{
                                Log.i("bmob","失败："+e.getMessage());
                            }
                        }
                    });

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
