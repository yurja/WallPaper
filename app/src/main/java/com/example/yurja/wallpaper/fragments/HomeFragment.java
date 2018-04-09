package com.example.yurja.wallpaper.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.WallPaperActivity;
import com.example.yurja.wallpaper.bmob_JavaBean.Ads;
import com.example.yurja.wallpaper.bmob_JavaBean.WallPaper;
import com.example.yurja.wallpaper.wallpaper.WallPaperPresenter;
import com.example.yurja.wallpaper.wallpaper.WallPaperPresenterImpl;
import com.example.yurja.wallpaper.wallpaper.WallPaperView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;



/**
 * Created by yurja on 2018/3/17.
 */

public class HomeFragment extends Fragment implements WallPaperView,Serializable{

    static ViewPager viewPager;
    GridView gridView;
    WallPaperPresenter presenter;
    List<View> viewLists;
    List<WallPaper> list;
    ViewGroup DotViewGroup;
    List<ImageView> DotLists;
    MyAdapter myAdapter;
    ImgpagerAdapter imgpagerAdapter;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int index= viewPager.getCurrentItem();
            if(index == viewLists.size()-1){
                viewPager.setCurrentItem(0);
            }else {
                viewPager.setCurrentItem(index+1);
            }
            Message message = new Message();
            handler.sendMessageDelayed(message,4000);
        }
    };





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        viewLists = new ArrayList<>();
        DotLists = new ArrayList<>();
        presenter = new WallPaperPresenterImpl(this);
        presenter.queryWallPaper(null);//从后台，查询所有壁纸
        myAdapter = new MyAdapter();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void initViewPager() {
        BmobQuery<Ads> query = new BmobQuery<>("Ads");
        query.findObjects(new FindListener<Ads>() {
            @Override
            public void done(List<Ads> list, BmobException e) {
                if(e == null){
                    viewLists.clear();
                    DotLists.clear();
                    for(Ads ads : list){
                        ImageView imageView = new ImageView(getActivity());
                        Picasso.with(getActivity()).load(ads.getPicture().getFileUrl()).into(imageView);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        viewLists.add(imageView);
                        ImageView dot = new ImageView(getActivity());
                        dot.setImageResource(R.drawable.dota);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(25,25);
                        layoutParams.leftMargin = 20;
                        layoutParams.rightMargin = 20;
                        dot.setLayoutParams(layoutParams);
                        dot.setEnabled(false);
                        DotLists.add(dot);
                        DotViewGroup.addView(dot);
                    }
                    ChangDot(0);
                    imgpagerAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                Log.d("大小","view:"+viewLists.size());

            }
        });

}
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        gridView = (GridView) view.findViewById(R.id.gridview);
        DotViewGroup = (ViewGroup) view.findViewById(R.id.dot_layout);
        gridView.setAdapter(myAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WallPaperActivity.class);

                intent.putExtra("index",position);

                intent.putExtra("wallPaperList",(Serializable) (list));
                startActivity(intent);
            }
        });

        viewPager.setOffscreenPageLimit(viewLists.size());
        imgpagerAdapter = new ImgpagerAdapter();
        viewPager.setAdapter(imgpagerAdapter);
        initViewPager();
        Message message = new Message();
        handler.sendMessageDelayed(message,4000);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                ChangDot(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return view;
    }

    private void ChangDot(int position) {
        for (int i = 0; i <DotLists.size() ; i++) {
            DotLists.get(i).setImageResource(position == i ? R.drawable.dotb : R.drawable.dota);
        }
    }

    public class  ImgpagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(viewLists.get(position));;
            return viewLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewLists.get(position));
        }
    }

    @Override
    public void setWallPapaer(List<WallPaper> list) {
        for(int i = list.size()-10;i<list.size();i++){
            this.list.add(list.get(i));
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFail() {
        Toast.makeText(getActivity(),"查询失败",Toast.LENGTH_SHORT).show();
    }

    class  MyAdapter extends BaseAdapter{
        class  ViewHolder {
            ImageView imageView;
            TextView wp_name;
        }
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View myView;
            ViewHolder viewHolder;
            if(convertView == null){
                myView = getActivity().getLayoutInflater().inflate(R.layout.home_gridview_item,null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) myView.findViewById(R.id.image_view);
                viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                viewHolder.wp_name = (TextView) myView.findViewById(R.id.wp_name);
                myView.setTag(viewHolder);
            }else {
                myView = convertView;
                viewHolder = (ViewHolder) myView.getTag();
            }
                viewHolder.wp_name.setVisibility(View.GONE);
                WallPaper wp = list.get(position);
                String url = wp.getWallpaper().getUrl();
                //String name = wp.getName();
                Picasso.with(getActivity()).load(url).into(viewHolder.imageView);

            return myView;
        }
    }


}
