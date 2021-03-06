package com.example.yurja.wallpaper.fragments;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurja.wallpaper.activity.LoginActivity;
import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.activity.UserCollectActivity;
import com.example.yurja.wallpaper.bean._User;
import com.squareup.picasso.Picasso;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by yurja on 2018/3/17.
 */

public class UserFragment extends Fragment {

    ImageView quit;
    ImageView user_pic;
    TextView username;
    TextView quitTV;
    private String uname;
    private String upicurl;
    List<String> userinfolist;
    private _User user;
    List<Item> ITEMS;
    ListView listView;
    MyAdapter myAdapter;
    static MyHandler myHandler;

    private class Item {
        int rid;
        String name;

        public Item(int rid,String name){
            this.rid= rid ;
            this.name =name;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        ITEMS = new ArrayList<>();
        myHandler = new MyHandler(this);
        username = (TextView) view.findViewById(R.id.user_name);
        user_pic = (ImageView) view.findViewById(R.id.user_pic);
        quit =(ImageView) view.findViewById(R.id.quit);
        quitTV = (TextView) view.findViewById(R.id.quitTV);
        userinfolist = new ArrayList<>();
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
        initUser();
        initData();
        listView = (ListView) view.findViewById(R.id.listview);
        myAdapter = new UserFragment.MyAdapter();
        listView.setAdapter(myAdapter);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                quit.setVisibility(View.INVISIBLE);
                quitTV.setVisibility(View.INVISIBLE);
                username.setText("点击登录");
                user_pic.setImageDrawable(getResources().getDrawable(R.drawable.null_user));
                Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
                username.setClickable(true);
            }
        });

        return view;
    }

    private void initData() {

        UserFragment.Item item1 = new UserFragment.Item(R.drawable.likes,"我的收藏");
        UserFragment.Item item2 = new UserFragment.Item(R.drawable.setwp,"自动设置壁纸");
        UserFragment.Item item3 = new UserFragment.Item(R.drawable.lock,"锁屏设置");
        UserFragment.Item item4 = new UserFragment.Item(R.drawable.trash,"清楚缓存");
        UserFragment.Item item5 = new UserFragment.Item(R.drawable.comments,"用户反馈");
        UserFragment.Item item6 = new UserFragment.Item(R.drawable.star,"五星支持");
        UserFragment.Item item7 = new UserFragment.Item(R.drawable.help,"帮助");
        UserFragment.Item item8 = new UserFragment.Item(R.drawable.setting,"设置");



        ITEMS.add(item1);
        ITEMS.add(item2);
        ITEMS.add(item3);
        ITEMS.add(item4);
        ITEMS.add(item5);
        ITEMS.add(item6);
        ITEMS.add(item7);
        ITEMS.add(item8);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUser();

    }

    public void initUser() { //获取到当前用户
        new Thread(new Runnable() {
            @Override
            public void run() {
                user = BmobUser.getCurrentUser(_User.class);
                if(user !=null){
                    userinfolist.clear();
                    userinfolist.add(user.getUsername());
                    if(user.getPicture().getFileUrl()!=null){
                        userinfolist.add(user.getPicture().getFileUrl());
                    }else {
                        Log.d("照片","用户没有设置头像");
                    }
                    Message message = new Message();
                    myHandler.sendMessage(message);
                }
            }
        }).start();

    }


    static class MyHandler extends Handler {
        WeakReference<UserFragment>  mFragmentReference;
        MyHandler(UserFragment fragment) {
            mFragmentReference= new WeakReference<UserFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final UserFragment fragment =  mFragmentReference.get();
            dispUser(fragment);
        }
    }

    private static void dispUser(UserFragment fragment) {
        if (fragment!= null) {
            fragment.quit.setVisibility(View.VISIBLE);
            fragment.quitTV.setVisibility(View.VISIBLE);
            fragment.username.setClickable(false);
            if(fragment.userinfolist.size() == 2){
                fragment.uname = fragment.userinfolist.get(0);
                fragment.upicurl = fragment.userinfolist.get(1);
            }else if(fragment.userinfolist.size() == 1){
                fragment.uname = fragment.userinfolist.get(0);
            }
            if( fragment.uname != null ){
                fragment.username.setText(fragment.uname);
            }
            if (fragment.upicurl!=null){
                Picasso.with(fragment.getActivity()).load(fragment.upicurl).into(fragment.user_pic);
            }
        }
    }


    class MyAdapter extends BaseAdapter {

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

            UserFragment.Item item = ITEMS.get(position);

            item_picture.setImageResource(item.rid);
            item_name.setText(item.name);

            if(item.name.equals("我的收藏")){
                myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), UserCollectActivity.class);
                        startActivity(intent);
                    }
                });
            }


            return myView;
        }
    }

}
