package com.example.yurja.wallpaper.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yurja.wallpaper.R;
import com.luck.picture.lib.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends AppCompatActivity {

    int index;
    List<String> pictureLists;
    ViewPager viewPager;
    TextView photoPosition;
    private MyImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        initView();
        initData();
    }

    private void initData() {
        pictureLists = new ArrayList<>();
        Intent intent = getIntent();
        pictureLists = intent.getStringArrayListExtra("PictureUrlLists");
        Log.d("检查","size:"+pictureLists.size());
        index = intent.getIntExtra("index",0);
        Log.d("检查","index:"+index);

        adapter = new MyImageAdapter(pictureLists, this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index,false);
        photoPosition.setText(index+1+"/"+pictureLists.size());
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                index = position;
                photoPosition.setText(index+1+"/"+pictureLists.size());
            }
        });
    }

    private void initView() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        photoPosition = (TextView) findViewById(R.id.photoPosition);

    }


    public class MyImageAdapter extends PagerAdapter {
        private List<String> imageUrls;
        private AppCompatActivity activity;

        public MyImageAdapter(List<String> imageUrls, AppCompatActivity activity) {
            this.imageUrls = imageUrls;
            this.activity = activity;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = imageUrls.get(position);
            PhotoView photoView = new PhotoView(activity);
            Picasso.with(activity)
                    .load(url)
                    .into(photoView);
            container.addView(photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("111", "onClick: ");
                    activity.finish();
                }
            });
            return photoView;
        }

        @Override
        public int getCount() {
            return imageUrls != null ? imageUrls.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    public class PhotoViewPager extends ViewPager {

        public PhotoViewPager(Context context) {
            super(context);
        }

        public PhotoViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
    }



}
