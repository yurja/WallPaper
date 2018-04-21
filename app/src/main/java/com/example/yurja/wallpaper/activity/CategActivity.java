package com.example.yurja.wallpaper.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.bean.WallPaper;
import com.example.yurja.wallpaper.wallpaper.WallPaperPresenter;
import com.example.yurja.wallpaper.wallpaper.WallPaperPresenterImpl;
import com.example.yurja.wallpaper.wallpaper.WallPaperView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategActivity extends AppCompatActivity implements WallPaperView,Serializable {

    GridView gridView;
    List<WallPaper> list;
    MyAdapter myAdapter;
    WallPaperPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categ);
        initData();
        gridView = (GridView) findViewById(R.id.gridview);
        myAdapter = new MyAdapter();
        gridView.setAdapter(myAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategActivity.this, WallPaperActivity.class);

                intent.putExtra("index",position);

                intent.putExtra("wallPaperList",(Serializable) (list));

                startActivity(intent);
            }
        });

    }

    private void initData() {
        list = new ArrayList<>();
        presenter = new WallPaperPresenterImpl(this);
        Intent intent = getIntent();
        String cidname = intent.getStringExtra("cidname");
        presenter.queryWallPaper(cidname);
    }

    @Override
    public void setWallPapaer(List<WallPaper> list) {
        this.list = list;
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFail() {
        Toast.makeText(this,"查询失败",Toast.LENGTH_SHORT).show();
    }

    class  MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View myView;
            if(convertView == null){
                myView = getLayoutInflater().inflate(R.layout.home_gridview_item,null);
            }else {
                myView = convertView;
            }
            ImageView imageView = (ImageView) myView.findViewById(R.id.image_view);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //TextView wp_name = (TextView)myView.findViewById(R.id.wp_name);

            WallPaper wp = list.get(position);

            String url = wp.getWallpaper().getUrl();
            //String name = wp.getName();

            Picasso.with(getApplicationContext()).load(url).into(imageView);

            //wp_name.setText(name);

            return myView;
        }
    }

}
