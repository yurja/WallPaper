package com.example.yurja.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by yurja on 2018/4/8.
 */

public class Topbar extends RelativeLayout {

    private ImageView leftImg,rightImg;
    private TextView titleTv;

    private Drawable leftBackground;
    private LayoutParams leftParams;

    private Drawable rightBackground;
    private LayoutParams rightParams;

    private String title;
    private float titleTextSize;
    private int titleTextColor;
    private LayoutParams titleParams;

    public Topbar(Context context) {
        super(context);
    }

    public Topbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.Topbar);

        leftBackground = ta.getDrawable(R.styleable.Topbar_LeftBackground);
        rightBackground = ta.getDrawable(R.styleable.Topbar_RightBackground);
        title = ta.getString(R.styleable.Topbar_Title);
        titleTextSize = ta.getDimension(R.styleable.Topbar_TitleTextSize,22);
        titleTextColor = ta.getColor(R.styleable.Topbar_TitleTextColor,0);

        ta.recycle();

       // final int TAG_RIGHT = 0x002;

        leftImg = new ImageView(context);
        rightImg = new ImageView(context);
        titleTv = new TextView(context);

        leftImg.setBackground(leftBackground);
        rightImg.setBackground(rightBackground);
       // rightImg.setTag(TAG_RIGHT);

        titleTv.setText(title);
        titleTv.setTextColor(titleTextColor);
        titleTv.setTextSize(titleTextSize);
        titleTv.setGravity(Gravity.CENTER);//设置居中

        setBackgroundColor(0xFFCCCCCC);

        leftParams = new LayoutParams(125,125);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftImg,leftParams);

        rightParams = new LayoutParams(150,150);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightImg,rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(titleTv,titleParams);

        leftImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish(); //销毁活动
            }
        });

        OnClickListener sysListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.rightClick();
            }
        };

        rightImg.setOnClickListener(sysListener);

    }

    public interface TopbarClickListener{
        void rightClick();
    }

    private TopbarClickListener listener;

    public void setTopbarClickListener(TopbarClickListener listener){
        this.listener = listener;
    }

    public void setRightVisibility(boolean flag) {
        if (flag) rightImg.setVisibility(VISIBLE);
        else rightImg.setVisibility(GONE);
    }

    public void setLeftVisibility(boolean flag) {
        if (flag) leftImg.setVisibility(VISIBLE);
        else leftImg.setVisibility(GONE);
    }

}
