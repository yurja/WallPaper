package com.example.yurja.wallpaper.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.WallPaperActivity;
import com.example.yurja.wallpaper.bmob_JavaBean.WallPaper;
import com.example.yurja.wallpaper.wallpaper.WallPaperPresenter;
import com.example.yurja.wallpaper.wallpaper.WallPaperPresenterImpl;
import com.example.yurja.wallpaper.wallpaper.WallPaperView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yurja on 2018/3/17.
 */

public class HomeFragment extends Fragment implements WallPaperView{

    GridView gridView;
    WallPaperPresenter presenter;

    List<WallPaper> list;

    ArrayList <String> urllist;

    MyAdapter myAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        urllist = new ArrayList<>();
        presenter = new WallPaperPresenterImpl(this);
        presenter.queryWallPaper(null);//查询所有
        myAdapter = new MyAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(myAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WallPaperActivity.class);

                intent.putExtra("index",position);

                intent.putStringArrayListExtra("urllist",urllist);

                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void setWallPapaer(List<WallPaper> list) {
        this.list = list;
        this.urllist.clear();
        for(WallPaper wp : list){
            urllist.add(wp.getWallpaper().getFileUrl());
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFail() {
        Toast.makeText(getActivity(),"查询失败",Toast.LENGTH_SHORT).show();
    }

    class  MyAdapter extends BaseAdapter{
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
                myView = getActivity().getLayoutInflater().inflate(R.layout.home_gridview_item,null);
            }else {
                myView = convertView;
            }
            ImageView imageView = (ImageView) myView.findViewById(R.id.image_view);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            TextView wp_name = (TextView)myView.findViewById(R.id.wp_name);
            //TextView download_num = (TextView) convertView.findViewById(R.id.download_num);

            WallPaper wp = list.get(position);

            String url = wp.getWallpaper().getUrl();
            String name = wp.getName();

            Picasso.with(getActivity()).load(url).into(imageView);

            wp_name.setText(name);

            return myView;
        }
    }


}
