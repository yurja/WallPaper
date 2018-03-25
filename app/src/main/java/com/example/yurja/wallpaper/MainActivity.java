package com.example.yurja.wallpaper;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.fragments.CategFragment;
import com.example.yurja.wallpaper.fragments.HomeFragment;
import com.example.yurja.wallpaper.fragments.SettingsFragment;
import com.example.yurja.wallpaper.fragments.UserFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;


public class MainActivity extends AppCompatActivity {

    PageNavigationView pageNavigationView;
    ViewPager viewPager;
    List<Fragment> fragmentList;
    //private String uname;
    //private String upicurl;
    List<String> userinfolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragments();
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        initUser();
    }

    private void initUser() {

        BmobUser user = BmobUser.getCurrentUser();
        if(user!=null){user.getUsername();
            Log.d("获取到用户",user.getUsername());
            userinfolist.clear();
            userinfolist.add(user.getUsername());
        }

    }

//    //活动从可见变到不可见
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Intent intent = getIntent();
//        uname = intent.getStringExtra("username");
//        upicurl = intent.getStringExtra("pic_url");
//        userinfolist.clear();
//        userinfolist.add(uname);
//        if(upicurl!=null ){
//            userinfolist.add(upicurl);
//        }
//
//    }

    public List<String> getTitles(){
        return userinfolist;
    }

    private void initFragments() {
        HomeFragment homeFragment = new HomeFragment();
        CategFragment categFragment = new CategFragment();
        UserFragment userFragment = new UserFragment();
        SettingsFragment settingsFragment = new SettingsFragment();


        fragmentList = new ArrayList<>();
        fragmentList.add(homeFragment);
        fragmentList.add(categFragment);
        fragmentList.add(userFragment);
        fragmentList.add(settingsFragment);

    }

    class FragmentAdapter extends FragmentPagerAdapter{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private void initView() {
        userinfolist = new ArrayList<>();
        pageNavigationView = (PageNavigationView) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        NavigationController navigationController = pageNavigationView.custom()
                .addItem(newItem(R.drawable.homea, R.drawable.homeb, "首页"))
                .addItem(newItem(R.drawable.categorya, R.drawable.categoryb, "分类"))
                .addItem(newItem(R.drawable.usera, R.drawable.userb, "我的"))
                .addItem(newItem(R.drawable.settinga, R.drawable.settingb, "设置"))
                .build();

        navigationController.setupWithViewPager(viewPager);
    }

    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable, checkedDrawable, text);
        normalItemView.setTextCheckedColor(0xFFE51C32);
        return normalItemView;
    }

}
