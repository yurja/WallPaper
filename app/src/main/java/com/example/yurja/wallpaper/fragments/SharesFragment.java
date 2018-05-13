package com.example.yurja.wallpaper.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.view.Topbar;
import com.example.yurja.wallpaper.activity.DeliverActivity;
import com.example.yurja.wallpaper.activity.PhotoViewActivity;
import com.example.yurja.wallpaper.bean.PictureMsg;
import com.example.yurja.wallpaper.pictureMsg.PictureMsgPresenter;
import com.example.yurja.wallpaper.pictureMsg.PictureMsgPresenterImpl;
import com.example.yurja.wallpaper.pictureMsg.PictureMsgView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yurja on 2018/3/17.
 */

public class SharesFragment extends Fragment implements PictureMsgView {

    Topbar topbar;
    View view;
    ListView PictureMsgLV;
    List<PictureMsg> pictureMsgList;
    PicMsgAdapter picMsgAdapter;
    PictureMsgPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PictureMsgPresenterImpl(this);

    }

    private void setListener() {
        topbar.setTopbarClickListener(new Topbar.TopbarClickListener() {
            @Override
            public void rightClick() {
                Intent intent = new Intent(getActivity(), DeliverActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("shareF","onStart");
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getPictureMsg();
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share, container, false);
        topbar = view.findViewById(R.id.topbar);
        topbar.setLeftVisibility(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getPictureMsg();
            }
        }).start();
        pictureMsgList = new ArrayList<>();
        picMsgAdapter = new PicMsgAdapter(pictureMsgList, getActivity());
        PictureMsgLV = view.findViewById(R.id.PictureMsgLV);
        PictureMsgLV.setAdapter(picMsgAdapter);
        setListener();
        return view;
    }


    @Override
    public void setPictureMsg(List<PictureMsg> list) {
        pictureMsgList.clear();
        pictureMsgList.addAll(list);
        picMsgAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFail() {
        Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_SHORT).show();
    }

    public class PicMsgAdapter extends BaseAdapter {

        ViewHolder viewHolder;
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(3);

        class ViewHolder {
            ImageView wPicture;
            TextView wName;
            TextView wContent;
            RecyclerView rcView;
        }

        List<PictureMsg> List;
        Context context;

        public PicMsgAdapter(java.util.List<PictureMsg> list, Context context) {
            List = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return pictureMsgList==null? 0:pictureMsgList.size();
        }

        @Override
        public Object getItem(int i) {
            return pictureMsgList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            RecyclerAdapter recyclerAdapter;
            View myView;
            List<String> urlists;
            urlists = new ArrayList<>();
            urlists.addAll(pictureMsgList.get(pictureMsgList.size()-1-i).getPictureList());
            recyclerAdapter = new RecyclerAdapter(context, urlists);

            if (view == null) {
                myView = getActivity().getLayoutInflater().inflate(R.layout.picture_msg_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.wPicture = myView.findViewById(R.id.writer_pic);
                viewHolder.wName = myView.findViewById(R.id.writer_name);
                viewHolder.wContent = myView.findViewById(R.id.contentTV);
                viewHolder.rcView = myView.findViewById(R.id.picture_recycler);
                myView.setTag(viewHolder);
            } else {
                myView = view;
                viewHolder = (ViewHolder) myView.getTag();
            }
            if (urlists.size() == 4) { //图片数量为4，设置recyclerview为2列
                GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
                viewHolder.rcView.setLayoutManager(manager);
            } else {//其他情况均为3列
                GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
                viewHolder.rcView.setLayoutManager(manager);
            }
            viewHolder.rcView.removeItemDecoration(spaceItemDecoration);
            viewHolder.rcView.addItemDecoration(spaceItemDecoration);
            viewHolder.rcView.setAdapter(recyclerAdapter);
            //pictureMsgList.size()-1-i  新动态 显示在前面
            viewHolder.wName.setText(List.get(pictureMsgList.size()-1-i).getWritername());
            viewHolder.wContent.setText(List.get(pictureMsgList.size()-1-i).getContent());
            Picasso.with(context).load(List.get(pictureMsgList.size()-1-i).getWriterpicurl())
                    .placeholder(R.drawable.place_holder).into(viewHolder.wPicture);
            recyclerAdapter.notifyDataSetChanged();
            return myView;
        }


    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        Context context;
        List<String> list;

        public RecyclerAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            View myView;
            ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                myView = view;
                imageView = (ImageView) view.findViewById(R.id.deliver_img_view);
            }
        }

        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.deliver_recycler_item,
                    parent, false);
            final RecyclerAdapter.ViewHolder holder = new RecyclerAdapter.ViewHolder(view);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                    int position = holder.getAdapterPosition();
                    intent.putExtra("index",position);
                    intent.putExtra("PictureUrlLists",(ArrayList)list);
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
            Picasso.with(context).load(list.get(position))
                    .placeholder(R.drawable.place_holder).into(holder.imageView);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom =  mSpace;
            outRect.top = mSpace;
        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }


}
