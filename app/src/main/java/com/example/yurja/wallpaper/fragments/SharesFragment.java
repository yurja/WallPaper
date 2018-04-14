package com.example.yurja.wallpaper.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.Topbar;

/**
 * Created by yurja on 2018/3/17.
 */

public class SharesFragment extends Fragment {

    private Topbar topbar;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    private void setListener() {
        topbar.setTopbarClickListener(new Topbar.TopbarClickListener() {
            @Override
            public void rightClick() {
                Toast.makeText(getActivity(), "发表动态", Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share,container,false);
        topbar = view.findViewById(R.id.topbar);
        topbar.setLeftVisibility(false);
        setListener();
        return view;
    }




}
