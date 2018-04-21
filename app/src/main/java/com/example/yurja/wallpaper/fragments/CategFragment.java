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

import com.example.yurja.wallpaper.activity.CategActivity;
import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.bean.Category;
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

        class ViewHolder {
            ImageView imageView;
            TextView catg_name;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View myView;
            ViewHolder viewHolder;
            if(convertView == null){
                myView = getActivity().getLayoutInflater().inflate(R.layout.categ_gridview_item,null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) myView.findViewById(R.id.image_view);
                viewHolder.catg_name = (TextView) myView.findViewById(R.id.catg_name);
                myView.setTag(viewHolder);
            }else {
                myView = convertView;
                viewHolder = (ViewHolder) myView.getTag();
            }
            Category category = list.get(position);
            String url = category.getCover().getFileUrl();
            Picasso.with(getActivity()).load(url).placeholder(R.drawable.place_holder)
                    .into(viewHolder.imageView);
            viewHolder.catg_name.setText(category.getName());
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
