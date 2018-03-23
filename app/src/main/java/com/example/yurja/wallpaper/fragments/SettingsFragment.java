package com.example.yurja.wallpaper.fragments;

import android.content.ClipData;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yurja.wallpaper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yurja on 2018/3/17.
 */

public class SettingsFragment extends Fragment {

    List<Item> ITEMS;
    ListView listView;
    MyAdapter myAdapter;

    private class Item{
        int rid;
        String name;

        public Item(int rid,String name){
            this.rid= rid ;
            this.name =name;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ITEMS = new ArrayList<>();
        initData();
    }

    private void initData() {
        Item item1 = new Item(R.drawable.photo,"自动设置壁纸");
        Item item2 = new Item(R.drawable.lock,"锁屏设置");
        Item item3 = new Item(R.drawable.trash,"清楚缓存");
        Item item4 = new Item(R.drawable.up_load,"我要上传");
        Item item5 = new Item(R.drawable.like,"五星支持");
        Item item6 = new Item(R.drawable.comments,"用户反馈");
        Item item7 = new Item(R.drawable.help,"帮助");

        ITEMS.add(item1);
        ITEMS.add(item2);
        ITEMS.add(item3);
        ITEMS.add(item4);
        ITEMS.add(item5);
        ITEMS.add(item6);
        ITEMS.add(item7);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        listView = (ListView) view;
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        return view;
    }



    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return ITEMS.size();
        }

        @Override
        public Object getItem(int position) {
            return ITEMS.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View myView;
            if(convertView == null){
                myView = getActivity().getLayoutInflater().inflate(R.layout.listview_item,null);
            }else {
                myView = convertView;
            }
            ImageView item_picture = (ImageView) myView.findViewById(R.id.item_picture);
            TextView item_name = (TextView) myView.findViewById(R.id.item_name);

            Item item = ITEMS.get(position);

            item_picture.setImageResource(item.rid);
            item_name.setText(item.name);

            return myView;
        }
    }

}
