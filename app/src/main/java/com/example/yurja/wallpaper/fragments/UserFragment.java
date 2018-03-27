package com.example.yurja.wallpaper.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurja.wallpaper.LoginActivity;
import com.example.yurja.wallpaper.MainActivity;
import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.bmob_JavaBean._User;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


/**
 * Created by yurja on 2018/3/17.
 */

public class UserFragment extends Fragment {

    Button quit;
    ImageView user_pic;
    TextView username;
    private String uname;
    private String upicurl;
    List<String> userinfolist;
    private _User user;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        username = (TextView) view.findViewById(R.id.user_name);
        user_pic = (ImageView) view.findViewById(R.id.user_pic);
        quit = view.findViewById(R.id.quit);
        userinfolist = new ArrayList<>();
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
        initUser();
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                quit.setVisibility(View.INVISIBLE);
                username.setText("点击登录");
                user_pic.setImageDrawable(getResources().getDrawable(R.drawable.null_user));
                Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
                username.setClickable(true);
            }
        });
        return view;
    }

    private void initUser() { //获取到当前用户
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
                    handler.sendMessage(message);
                }
            }
        }).start();

    }

    private void dispUser(){ //显示用户
        quit.setVisibility(View.VISIBLE);
        username.setClickable(false);
        if(userinfolist.size() == 2){
            uname = userinfolist.get(0);
            upicurl = userinfolist.get(1);
        }else if(userinfolist.size() == 1){
            uname = userinfolist.get(0);
        }
        if( uname != null ){
            username.setText(uname);
        }
        if (upicurl!=null){
            Picasso.with(getContext()).load(upicurl).into(user_pic);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dispUser();
        }
    };

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        userinfolist = ((MainActivity) activity).getTitles();
//        new Thread(new Runnable() { //跳转到主线程更新UI
//            @Override
//            public void run() {
//                Message message = new Message();
//                handler.sendMessage(message);
//            }
//        }).start();
//    }

}
