package com.example.yurja.wallpaper.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yurja.wallpaper.R;
import com.example.yurja.wallpaper.Topbar;
import com.example.yurja.wallpaper.bean.PictureMsg;
import com.example.yurja.wallpaper.bean._User;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.squareup.picasso.Picasso;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class DeliverActivity extends AppCompatActivity {

    private Topbar topbar;
    Button button;
    ListView listView;
    EditText contentEt;
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    private List<LocalMedia> selectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);
        topbar = (Topbar) findViewById(R.id.topbar);
        button = (Button) findViewById(R.id.button);
        contentEt = (EditText) findViewById(R.id.contentEt);
        listView = (ListView) findViewById(R.id.listview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(3));
        setListener();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(DeliverActivity.this)
                        .openGallery(ofImage())
                        .maxSelectNum(9)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(3)// 每行显示个数 int
                        .compress(true)// 是否压缩 true or false
                        .selectionMedia(selectList)
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    Log.d("测试", "" + selectList.size());
                    adapter = new RecyclerAdapter(this, selectList);
                    recyclerView.setAdapter(adapter);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        Context context;
        List<LocalMedia> list;

        public RecyclerAdapter(Context context, List<LocalMedia> list) {
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.deliver_recycler_item,
                    parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (list.get(position).isCompressed()) {
                Uri uri = getImageContentUri(context, list.get(position).getCompressPath());
                holder.imageView.setImageURI(uri);
                Log.d("测试", list.get(position).getCompressPath());
                Log.d("测试", "into img");
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


    private void setListener() {
        topbar.setTopbarClickListener(new Topbar.TopbarClickListener() {
            @Override
            public void rightClick() {
                 if(selectList != null){
                     final String content = contentEt.getText().toString();
                     final String[] filePaths = new String[selectList.size()];
                     for (int i = 0; i <selectList.size() ; i++) {
                         filePaths[i] = selectList.get(i).getCompressPath();
                     }
                     Toast.makeText(DeliverActivity.this,"发送中...",Toast.LENGTH_LONG).show();
                     BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

                         @Override
                         public void onSuccess(List<BmobFile> list, List<String> list1) {
                             if(list1.size()==filePaths.length){//如果数量相等，则代表文件全部上传完成
                                 _User user = BmobUser.getCurrentUser(_User.class);
                                 PictureMsg pictureMsg = new PictureMsg();
                                 pictureMsg.setWritername(user.getUsername());
                                 pictureMsg.setWriterpicurl(user.getPicture().getFileUrl());
                                 pictureMsg.setContent(content);
                                 pictureMsg.setPictureList(list1);
                                 pictureMsg.save(new SaveListener<String>() {
                                     @Override
                                     public void done(String s, BmobException e) {
                                         if(e==null){
                                             Toast.makeText(DeliverActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                             PictureFileUtils.deleteCacheDirFile(DeliverActivity.this);//清除裁剪和压缩后的缓存
                                             finish();
                                         }else{
                                             Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                         }
                                     }
                                 });
                             }
                         }

                         @Override
                         public void onProgress(int i, int i1, int i2, int i3) {

                         }

                         @Override
                         public void onError(int i, String s) {

                         }
                     });
                 }else {
                     Toast.makeText(DeliverActivity.this,"不能发送空消息",Toast.LENGTH_SHORT).show();
                 }
            }
        });

    }

    //将文件路径转为Uri
    public Uri getImageContentUri(Context context, String filepath) {
        String filePath = filepath;
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (filePath != null) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

}
