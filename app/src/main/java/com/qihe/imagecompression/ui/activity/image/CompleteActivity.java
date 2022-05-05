package com.qihe.imagecompression.ui.activity.image;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qihe.imagecompression.MainActivity;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.adapter.MyViewPagerAdapter;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.Utils;
import com.qihe.imagecompression.view.LoadingDialog;
import com.xinqidian.adcommon.util.JumpUtils;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

/***
 * 转换、压缩完成页面
 */
public class CompleteActivity extends AppCompatActivity implements View.OnClickListener {
    private List<String> list;
    private AlertDialog dialog;

    private List<View> viewList = new ArrayList<>();
    private TextView imageCount;
    private TextView name;
    private TextView size;
    private TextView size1;

    private int count = 0;

    private boolean isDownComplete = false;//是否加载完成

    private Handler uiHandler = new Handler();
    private LoadingDialog loadingDialog;

    private List<Integer> widthList;
    private List<Integer> heightList;
    private List<String> sizeList;
    private MyViewPagerAdapter adapter;

    private ArrayList<File> fileList = new ArrayList<>();
    private String type = "";
    private BitmapFactory.Options opts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        list = (List<String>) getIntent().getSerializableExtra("list");

        type = getIntent().getStringExtra("type");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        ActivityUtil.fullScreen(getWindow());

    }

    private void initView() {

        if (!SharedPreferencesUtil.isVip()) {
            SharedPreferencesUtil.setCount(SharedPreferencesUtil.getCount() - 1);
        }

        EventBus.getDefault().post("数据");
        EventBus.getDefault().post("次数");
        loadingDialog = new LoadingDialog(this);
        findViewById(R.id.to_home).setOnClickListener(this);
        findViewById(R.id.look).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        TextView preserve = findViewById(R.id.preserve);
        TextView preservePath = findViewById(R.id.preserve_path);
        if (type.equals("视频")) {
            preserve.setText("保存成功");
            preservePath.setText(FileUtils.NEW_COMPRESSION_FILE_PATH + new File(list.get(0)).getName());
        } else {
            preservePath.setVisibility(View.INVISIBLE);
            for (int i = 0; i < list.size(); i++) {
                fileList.add(new File(list.get(i)));
            }
            loadingDialog.show();
            DownThread downThread = new DownThread();
            downThread.start();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_home:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.look:
                if (type.equals("视频")) {
                    Intent intent = new Intent(CompleteActivity.this, PlayActivity.class);
                    intent.putExtra("path", list.get(0));
                    startActivity(intent);
                } else {
                    lookImage();
                }

                break;
            case R.id.share:
                if (type.equals("视频")) {
                    new Share2.Builder(CompleteActivity.this)
                            .setContentType(ShareContentType.VIDEO)
                            .setShareFileUri(FileUtil.getFileUri(CompleteActivity.this, ShareContentType.VIDEO, new File(list.get(0))))
                            .build()
                            .shareBySystem();
                } else {
                    originalShareImage(CompleteActivity.this, fileList);
                }

                break;
        }

    }

    /**
     * 分享
     *
     * @param context
     * @param files
     */
    public void originalShareImage(Context context, ArrayList<File> files) {
        Intent share_intent = new Intent();
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (File f : files) {
                Uri imageContentUri = getImageContentUri(context, f);
                imageUris.add(imageContentUri);
            }
        } else {
            for (File f : files) {
                imageUris.add(Uri.fromFile(f));
            }
        }
        share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);//设置分享行为
        share_intent.setType("image/*");//设置分享内容的类型
        share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        context.startActivity(Intent.createChooser(share_intent, "Share"));
    }


    /**
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    //加载文件
    class DownThread extends Thread {
        @Override
        public void run() {
            getData();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    isDownComplete = true;
                    loadingDialog.dismiss();
                }
            };
            uiHandler.post(runnable);
        }
    }

    private void getData() {

        if (widthList == null) {
            widthList = new ArrayList<>();
        }
        if (heightList == null) {
            heightList = new ArrayList<>();
        }
        if (sizeList == null) {
            sizeList = new ArrayList<>();
        }

        for (int i = 0; i < list.size(); i++) {

            opts = new BitmapFactory.Options();
            //只请求图片宽高，不解析图片像素(请求图片属性但不申请内存，解析bitmap对象，该对象不占内存)
            opts.inJustDecodeBounds = true;
            //String path = Environment.getExternalStorageDirectory() + "/dog.jpg";
            BitmapFactory.decodeFile(list.get(i), opts);

            widthList.add(opts.outWidth);
            heightList.add(opts.outHeight);

            try {
                String fileSize = Utils.getFileSize(new File(list.get(i)));
                sizeList.add(fileSize);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    private void lookImage() {
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(this, R.style.MyDialog);
        alterDiaglog.setView(R.layout.view_look_image);//加载进去
        dialog = alterDiaglog.create();
        //显示
        dialog.show();

        //dialog.setView(view);
        // dialog.show();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
        dialog.getWindow().setAttributes(p);     //设置生效

        initDialog();

    }

    private void initDialog() {
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ViewPager viewPager = dialog.findViewById(R.id.view_pager);

        name = dialog.findViewById(R.id.image_name);
        size = dialog.findViewById(R.id.image_size);
        size1 = dialog.findViewById(R.id.image_size1);
        imageCount = dialog.findViewById(R.id.image_count);
        TextView text = dialog.findViewById(R.id.image_text);

        String substring = list.get(0).substring(0, list.get(0).lastIndexOf("/") + 1);
        name.setText(list.get(0).replace(substring, ""));
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //只请求图片宽高，不解析图片像素(请求图片属性但不申请内存，解析bitmap对象，该对象不占内存)
        opts.inJustDecodeBounds = true;
        //String path = Environment.getExternalStorageDirectory() + "/dog.jpg";
        BitmapFactory.decodeFile(list.get(0), opts);

        /*widthList.add(opts.outWidth);
        heightList.add(opts.outHeight);
        Bitmap bitmap = BitmapFactory.decodeFile(list.get(0));*/
        int height = opts.outHeight;
        int width =opts.outWidth;

        size.setText(width + "" + " x " + height + "" + "" + "px");


        try {
            String fileSize = Utils.getFileSize(new File(list.get(0)));
            size1.setText(fileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageCount.setText("(" + 1 + "" + "/" + list.size() + "" + ")");
        if (list.size() == 1) {
            text.setVisibility(View.INVISIBLE);
            imageCount.setVisibility(View.INVISIBLE);
        }
        viewList.clear();
        LayoutInflater lf = getLayoutInflater().from(this);
        for (int i = 0; i < list.size(); i++) {
            viewList.add(lf.inflate(R.layout.view_compression, null, false));
        }


        for (int i = 0; i < list.size(); i++) {
            ImageView image = viewList.get(i).findViewById(R.id.image);
            Glide.with(this)
                    .load(list.get(i))
                    .placeholder(R.drawable.loadpicture_icon)
                    .error(R.drawable.loadpicture_icon)
                    .into(image);
        }

        setData();


        adapter = new MyViewPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                count = i;

                imageCount.setText("(" + (count + 1) + "" + "/" + list.size() + "" + ")");
                String substring = list.get(i).substring(0, list.get(i).lastIndexOf("/") + 1);
                name.setText(list.get(i).replace(substring, ""));


                if (isDownComplete) {
                    setData();
                }


            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setData() {

        size.setText(widthList.get(count) + "" + " x " + heightList.get(count) + "" + "" + "px");
        size1.setText(sizeList.get(count));
    }
}
