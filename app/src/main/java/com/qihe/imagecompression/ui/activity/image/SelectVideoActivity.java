package com.qihe.imagecompression.ui.activity.image;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.adapter.AddVideoAdapter;

import com.qihe.imagecompression.util.ActivityUtil;

import com.qihe.imagecompression.view.LoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择视频
 */
public class SelectVideoActivity extends AppCompatActivity {

    private RecyclerView videoRV;
    private List<String> pathList = new ArrayList<>();
    private List<String> durationList = new ArrayList<>();

    private Handler uiHandler = new Handler();
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ActivityUtil.fullScreen(getWindow());
        initView();
    }

    private void initView() {

        loadingDialog = new LoadingDialog(this);
        videoRV = findViewById(R.id.video_rv);
        videoRV.setLayoutManager(new GridLayoutManager(this, 4));
        loadingDialog.show();
        DownThread downThread = new DownThread();
        downThread.start();


    }

    //加载文件
    class DownThread extends Thread {
        @Override
        public void run() {

           /* try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            getVideo(SelectVideoActivity.this);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    AddVideoAdapter adapter = new AddVideoAdapter(SelectVideoActivity.this, pathList, durationList);
                    videoRV.setAdapter(adapter);
                    adapter.setOnItemClickListener(new AddVideoAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            if (position == 0) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (ContextCompat.checkSelfPermission(SelectVideoActivity.this, Manifest.permission.CAMERA)
                                            == PackageManager.PERMISSION_DENIED) {
                                        ActivityCompat.requestPermissions(SelectVideoActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                                    } else {
                                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                        // 录制视频最大时长15s
                                        //intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                                        startActivityForResult(intent, 2);

                                    }
                                } else {
                                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                    // 录制视频最大时长15s
                                    //intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                                    startActivityForResult(intent, 2);
                                }

                            } else {
                                Intent intent = new Intent(SelectVideoActivity.this, VideoPlayActivity.class);
                                intent.putExtra("path", pathList.get(position));
                                startActivity(intent);
                            }

                        }
                    });

                    loadingDialog.dismiss();

                }
            };
            uiHandler.post(runnable);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_DENIED) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                // 录制视频最大时长15s
                //intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                startActivityForResult(intent, 2);

            } else {
                Toast.makeText(SelectVideoActivity.this, "请先打开权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getVideo(Context context) {
        pathList.clear();
        pathList.add(0, "");
        durationList.clear();
        durationList.add(0, "");

        // MediaMetadataRetriever media = new MediaMetadataRetriever();
        try {
            // String[] mediaColumns = { "_id", "_data", "_display_name",
            // "_size", "date_modified", "duration", "resolution" };
            Cursor c = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            while (c.moveToNext()) {

                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));// 路径
                String duration = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));// 时长
                // Log.i("new File(path).length()",new File(path).length()+"");
                if (new File(path) != null && new File(path).length() > 1024) {
                    pathList.add(path);
                    durationList.add(duration);
                }

              /*  try {
                    media.setDataSource(path);
                    if(media.getFrameAtTime() != null){

                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }*/


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Uri uri = data.getData();
                Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                    // 视频路径
                    String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                    Intent intent = new Intent(SelectVideoActivity.this, VideoPlayActivity.class);
                    intent.putExtra("path", filePath);
                    startActivity(intent);
                    //  Log.i("filePath",filePath);
                    // ThumbnailUtils类2.2以上可用  Todo 获取视频缩略图
                    // Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
                    // 图片Bitmap转file
                    // File file = CommonUtils.compressImage(bitmap);
                    // 保存成功后插入到图库，其中的file是保存成功后的图片path。这里只是插入单张图片
                    // 通过发送广播将视频和图片插入相册
                    // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

                    cursor.close();
                }
            }
        }
    }
}
