package com.example.yurja.wallpaper.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.yurja.wallpaper.LoginActivity;
import com.example.yurja.wallpaper.MainActivity;
import com.example.yurja.wallpaper.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by yurja on 2018/3/17.
 */

public class UserFragment extends Fragment {

    Button enter_login;
    ImageView user_pic;
    TextView username;
    private String uname;
    private String upicurl;
    List<String> userinfolist;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        username = (TextView) view.findViewById(R.id.user_name);
        user_pic = (ImageView) view.findViewById(R.id.user_pic);
        enter_login = view.findViewById(R.id.enter_login);
        userinfolist = new ArrayList<>();
        enter_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
        initUser();
        user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopueWindow();
            }
        });
        return view;
    }

    private void initPopueWindow() {
        View popView  = View.inflate(getActivity(),R.layout.popupwindow_upload,null);
        //Button cameraBt = (Button) popView.findViewById(R.id.camera);
        //Button albumBt = (Button) popView.findViewById(R.id.album);
       // Button cancelBt = (Button) popView.findViewById(R.id.cancel);

        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });


        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);


    }

    private void initUser() { //获取到当前用户
        BmobUser user = BmobUser.getCurrentUser();
        if(user!=null){
            userinfolist.clear();
            userinfolist.add(user.getUsername());
            dispUser();
        }

    }

    private void dispUser(){ //显示用户
        if(userinfolist.size() == 2){
            uname = userinfolist.get(0);
            upicurl = userinfolist.get(1);
        }else{
            uname = userinfolist.get(0);
        }
        if( uname != null ){
            username.setText(uname);
            enter_login.setText("退出");
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        userinfolist = ((MainActivity) activity).getTitles();
        new Thread(new Runnable() { //跳转到主线程更新UI
            @Override
            public void run() {
                Message message = new Message();
                handler.sendMessage(message);
            }
        });
    }

}
