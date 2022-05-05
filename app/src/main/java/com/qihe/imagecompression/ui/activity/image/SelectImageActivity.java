package com.qihe.imagecompression.ui.activity.image;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.adapter.AddImageAdapter;
import com.qihe.imagecompression.adapter.SelectImageAdapter;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FileUtil;
import com.qihe.imagecompression.util.Utils;
import com.qihe.imagecompression.view.LoadingDialog;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择图片
 */
public class SelectImageActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private TextView open;

    private LinearLayout addImage;
    private RecyclerView selectRV;
    private RecyclerView addRV;
    private List<String> selectList = new ArrayList<>();
    private List<String> addList = new ArrayList<>();
    private SelectImageAdapter selectImageAdapter;
    private AddImageAdapter addImageAdapter;
    private String content;


    private Handler uiHandler = new Handler();

    private LoadingDialog loadingDialog;
    private PhotoLoader photoLoader;

    private String downType;
    private String savePath;
    private Bitmap bm;
    private String imagePath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        content = getIntent().getStringExtra("content");
        initView();
        ActivityUtil.fullScreen(getWindow());

    }

    private void initView() {
        findViewById(R.id.back).setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setOnClickListener(this);
        open = findViewById(R.id.open);
        open.setOnClickListener(this);
        addImage = findViewById(R.id.add_image);

        selectRV = findViewById(R.id.select_rv);
        selectRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addRV = findViewById(R.id.add_rv);
        addRV.setLayoutManager(new GridLayoutManager(this, 4));
        RelativeLayout selectRL = findViewById(R.id.select_rl);

        if (content.equals("尺寸") || content.equals("格式")) {
            selectRL.setVisibility(View.GONE);
            open.setVisibility(View.GONE);
        }
        initData();
    }

    private void initData() {

        loadingDialog = new LoadingDialog(this);

        selectImageAdapter = new SelectImageAdapter(this, selectList);
        selectRV.setAdapter(selectImageAdapter);
        selectImageAdapter.setOnItemClickListener(new AddImageAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //addImageAdapter.delect(selectList.get(position));
                selectList.remove(selectList.get(position));
                selectImageAdapter.notifyDataSetChanged();
                addImageAdapter.notifyDataSetChanged();
                open.setText("制作(" + selectList.size() + "" + "/9)");
                if (selectList.size() > 0) {
                    addImage.setVisibility(View.GONE);
                } else {
                    addImage.setVisibility(View.VISIBLE);
                }
            }
        });

        photoLoader = new PhotoLoader(this);

        downType = "加载";
        loadingDialog.show();
        DownThread downThread = new DownThread();
        downThread.start();


    }

    //加载文件
    class DownThread extends Thread {
        @Override
        public void run() {


            switch (downType) {
                case "加载":
                    getData();
                    break;
                case "保存":
                    savePath = FileUtil.saveBitmap(SelectImageActivity.this, bm);
                    break;
            }


            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    switch (downType) {
                        case "保存":
                            if (!savePath.equals("")) {
                                if (content.equals("格式")) {
                                    Intent intent = new Intent(SelectImageActivity.this, FormatConversionActivity.class);
                                    intent.putExtra("path", savePath);
                                    intent.putExtra("string", "格式");
                                    startActivity(intent);
                                } else if (content.equals("尺寸")) {
                                    Intent intent = new Intent(SelectImageActivity.this, FormatConversionActivity.class);
                                    intent.putExtra("path", savePath);
                                    intent.putExtra("string", "尺寸");
                                    startActivity(intent);
                                } else {
                                    selectList.add(savePath);
                                    open.setText("制作(" + selectList.size() + "" + "/9)");
                                    selectImageAdapter.notifyDataSetChanged();
                                }
                            }

                            break;
                    }
                    loadingDialog.dismiss();

                }
            };
            uiHandler.post(runnable);
        }

    }

    private void getData() {

        photoLoader.setLoadFinishCallback(new PhotoLoader.LoadFinishCallback() {
            @Override
            public void wholeImage(final List<String> imageWhole) {
                addList.clear();
                addList.add(0, "");
                for (int i = 0; i < imageWhole.size(); i++) {
                    if (new File(imageWhole.get(i)).length() != 0 && Utils.getFileType(imageWhole.get(i)) == 0) {
                        addList.add(imageWhole.get(i));
                    }

                }

                addImageAdapter = new AddImageAdapter(SelectImageActivity.this, addList, selectList);
                addRV.setAdapter(addImageAdapter);
                addImageAdapter.setOnItemClickListener(new AddImageAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {


                        if (position == 0) {//去拍照
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(SelectImageActivity.this, Manifest.permission.CAMERA)
                                        == PackageManager.PERMISSION_DENIED) {
                                    ActivityCompat.requestPermissions(SelectImageActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                                } else {
                                    openTakePhoto();

                                }

                            } else {
                                openTakePhoto();
                            }

                        } else {
                            Log.i("123456",addList.get(position));
                            if (content.equals("压缩")) {
                                if (selectList.contains(addList.get(position))) {
                                    selectList.remove(addList.get(position));
                                } else {
                                    if (selectList.size() < 9) {
                                        selectList.add(addList.get(position));
                                    } else {
                                        Toast.makeText(SelectImageActivity.this, "最多选取9张", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                if (selectList.size() > 0) {
                                    addImage.setVisibility(View.GONE);
                                } else {
                                    addImage.setVisibility(View.VISIBLE);
                                }
                                open.setText("制作(" + selectList.size() + "" + "/9)");
                                selectImageAdapter.notifyDataSetChanged();
                                addImageAdapter.notifyDataSetChanged();
                            } else if (content.equals("格式")) {
                                Intent intent = new Intent(SelectImageActivity.this, FormatConversionActivity.class);
                                intent.putExtra("path", addList.get(position));
                                intent.putExtra("string", "格式");
                                startActivity(intent);
                            } else if (content.equals("尺寸")) {
                                Intent intent = new Intent(SelectImageActivity.this, FormatConversionActivity.class);
                                intent.putExtra("path", addList.get(position));
                                intent.putExtra("string", "尺寸");
                                startActivity(intent);
                            }

                        }
                    }
                });
            }
        });

    }

    private void openTakePhoto() {
        /**
         * 在启动拍照之前最好先判断一下sdcard是否可用
         */
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            imagePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + System.currentTimeMillis() + ".jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri imageUri = Uri.fromFile(new File(imagePath));

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 2);
        } else {
            Toast.makeText(SelectImageActivity.this, "sdcard不可用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_DENIED) {
                openTakePhoto();

            } else {
                Toast.makeText(SelectImageActivity.this, "请先打开权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Log.i("bm", "返回了");
            bm = BitmapFactory.decodeFile(imagePath);
            if (bm != null) {
                downType = "保存";
                loadingDialog.show();
                DownThread downThread = new DownThread();
                downThread.start();
            }


           /* if (data.getData() != null || data.getExtras() != null) { //防止没有返回结果
                String path = data.getData().getPath();
                Log.i("bm", path);
                bm = (Bitmap) data.getExtras().get("data");
                int width = bm.getWidth();
                int height = bm.getHeight();
                Log.i("bm", width + "" + "px::::" + height + "" + "px");
                *//*if (bm != null ) {
                    int width = bm.getWidth();
                    int height = bm.getHeight();
                    Log.i("bm",width+""+"px::::"+height+""+"px");



                }*//*
            }*/
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title:
                //title.setText("我的分组");
                break;
            case R.id.open:

                if (selectList.size() > 0) {
                    Intent intent = new Intent(SelectImageActivity.this, CompressionActivity.class);
                    intent.putExtra("list", (Serializable) selectList);
                    startActivity(intent);
                } else {
                    Toast.makeText(SelectImageActivity.this, "请选择照片", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }
}
