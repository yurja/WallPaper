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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WallPaperActivity extends AppCompatActivity {

    Gallery gallery;
    List<String> urllist;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_paper);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        urllist = new ArrayList<>();
        urllist = intent.getStringArrayListExtra("urllist");
        Log.d("urllist size",""+urllist.size());
        index = intent.getIntExtra("index",0);
        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new MyAdapter(this,urllist));
        Log.d("适配器","设置适配器");
        gallery.setSelection(index);
    }

    class MyAdapter extends BaseAdapter{

        Context context;
        List<String> list;

        public MyAdapter(Context context, List<String> list) {
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
            ImageView wallpaper = (ImageView) myView.findViewById(R.id.image_view);
            ImageView set_wp = (ImageView) myView.findViewById(R.id.set_wp);

            set_wp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlstring = list.get(position);
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

            Picasso.with(context).load(list.get(position)).resize(params.width,params.height)
                    .centerCrop().into(wallpaper);

            return myView;
        }
    }

}
