package com.example.yurja.wallpaper.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurja.wallpaper.CategActivity;
import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.bmob_JavaBean.Category;
import com.example.yurja.wallpaper.category.CategoryPresenter;
import com.example.yurja.wallpaper.category.CategoryPresenterImpl;
import com.example.yurja.wallpaper.category.CategoryView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yurja on 2018/3/17.
 */

public class CategFragment extends Fragment implements CategoryView{


    CategoryPresenter presenter;
    List<Category> list;
    GridView gridView;
    MyAdapter myAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CategoryPresenterImpl(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getCategory();
            }
        }).start();
        list = new ArrayList<>();
        myAdapter = new MyAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categ,container,false);
        gridView =(GridView)view;
        gridView.setAdapter(myAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category c = list.get(position);
                String cidname = c.getName();
                Intent intent =new Intent(getActivity(), CategActivity.class);
                intent.putExtra("cidname",cidname);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    class MyAdapter extends BaseAdapter{

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
        public View getView(int position, View convertView, ViewGroup parent) {
            View myView;
            if(convertView == null){
                myView = getActivity().getLayoutInflater().inflate(R.layout.categ_gridview_item,null);
            }else {
                myView = convertView;
            }
            ImageView imageView = (ImageView) myView.findViewById(R.id.image_view);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            TextView catg_name = (TextView) myView.findViewById(R.id.catg_name);
            Category category = list.get(position);
            String url = category.getCover().getFileUrl();
            Picasso.with(getActivity()).load(url).into(imageView);
            catg_name.setText(category.getName());
            return myView;
        }
    }

    @Override
    public void setCategory(List<Category> list) {
        this.list = list;
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFail() {
        Toast.makeText(getActivity(),"加载失败！",Toast.LENGTH_SHORT).show();
    }

}
