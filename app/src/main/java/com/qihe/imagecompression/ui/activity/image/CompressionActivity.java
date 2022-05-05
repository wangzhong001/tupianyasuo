package com.qihe.imagecompression.ui.activity.image;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dueeeke.videoplayer.util.L;
import com.nanchen.compresshelper.CompressHelper;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.adapter.ImageContentAdapter;
import com.qihe.imagecompression.adapter.MyViewPagerAdapter;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.Utils;
import com.qihe.imagecompression.view.LoadingDialog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.view.SureDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 压缩页面
 */
public class CompressionActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView singleTV;
    private TextView manyTV;
    private View singleV;
    private View manyV;

    private int type = 0;//0.单张  1.批量

    private List<String> list;
    private TextView countTV;
    private TextView seekbarTV1;
    private TextView seekbarTV2;

    private List<View> viewList = new ArrayList<>();
    private ViewPager viewPager;
    private TextView size;
    private TextView size1;

    private Handler uiHandler = new Handler();
    private LoadingDialog loadingDialog;

    private List<Integer> widthList;
    private List<Integer> heightList;
    private List<String> sizeList;
    private List<String> formatList;

    private List<String> contextList = new ArrayList<>();


    private boolean isDownComplete = false;//是否加载完成

    private int count = 0;

    private String downType;

    private int compressionCount = 50;
    private int sizeCount = 50;


    private List<String> pathList = new ArrayList<>();
    private TextView size0;
    private TextView size01;
    private RecyclerView contentRV;
    private BitmapFactory.Options opts;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compression);
        list = (List<String>) getIntent().getSerializableExtra("list");

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
        loadingDialog = new LoadingDialog(this);
        findViewById(R.id.single_ll).setOnClickListener(this);
        findViewById(R.id.many_ll).setOnClickListener(this);

        viewPager = findViewById(R.id.view_pager);
        singleTV = findViewById(R.id.single_tv);
        manyTV = findViewById(R.id.many_tv);
        singleV = findViewById(R.id.single_v);
        manyV = findViewById(R.id.many_v);

        countTV = findViewById(R.id.count_tv);
        countTV.setText("(1/" + list.size() + "" + ")");

        contentRV = findViewById(R.id.content_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        contentRV.setLayoutManager(linearLayoutManager);

        size = findViewById(R.id.size);
        size1 = findViewById(R.id.size1);
        size0 = findViewById(R.id.size0);
        size01 = findViewById(R.id.size01);

        SeekBar seekbar1 = findViewById(R.id.seekbar1);
        SeekBar seekbar2 = findViewById(R.id.seekbar2);
        seekbarTV1 = findViewById(R.id.seekbar_tv1);
        seekbarTV2 = findViewById(R.id.seekbar_tv2);
        findViewById(R.id.btn).setOnClickListener(this);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekbarTV1.setText(i + "" + "%");
                sizeCount = i;
                setData();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekbarTV2.setText(i + "" + "%");
                compressionCount = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        initData();
    }

    private void initData() {

        downType = "加载";
        loadingDialog.show();
        DownThread downThread = new DownThread();
        downThread.start();
        LayoutInflater lf = getLayoutInflater().from(this);
        for (int i = 0; i < list.size(); i++) {
            viewList.add(lf.inflate(R.layout.view_compression, null));

        }
        for (int i = 0; i < list.size(); i++) {
            ImageView image = viewList.get(i).findViewById(R.id.image);
            Glide.with(this)
                    .load(list.get(i))
                    .placeholder(R.drawable.loadpicture_icon)
                    .error(R.drawable.loadpicture_icon)
                    .into(image);
        }

        viewPager.setAdapter(new MyViewPagerAdapter(viewList));
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                count = i;
                if (type == 0) {
                    countTV.setText("(" + (count + 1) + "" + "/" + list.size() + "" + ")");
                } else {
                    countTV.setText("(共" + list.size() + "" + "张)");
                }

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
        if (formatList == null) {
            formatList = new ArrayList<>();
        }

        for (int i = 0; i < list.size(); i++) {
            opts = new BitmapFactory.Options();
            //只请求图片宽高，不解析图片像素(请求图片属性但不申请内存，解析bitmap对象，该对象不占内存)
            opts.inJustDecodeBounds = true;
            //String path = Environment.getExternalStorageDirectory() + "/dog.jpg";
            BitmapFactory.decodeFile(list.get(i), opts);

          /*  width = opts.outWidth;
            height = opts.outHeight;
            Bitmap bitmap = BitmapFactory.decodeFile(list.get(i));*/
            int height = opts.outHeight;
            int width = opts.outWidth;
            widthList.add(width);
            heightList.add(height);

            try {
                String fileSize = Utils.getFileSize(new File(list.get(i)));
                sizeList.add(fileSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String substring = list.get(i).substring(0, list.get(i).lastIndexOf(".") + 1);
            Log.i("formatList", list.get(i).replace(substring, ""));
            formatList.add(list.get(i).replace(substring, ""));


        }

    }


    /**
     * 质量压缩
     *
     * @param path    图片格式 jpeg,png,webp
     * @param quality 图片的质量,0-100,数值越小质量越差
     */
    public void compress(String path, int quality, int w, int h, String format) {

        Bitmap image = getBitmap(CompressionActivity.this, path);
        int size = (int)(500*1.32);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, out);
        float zoom = (float)Math.sqrt(size * 1024 / (float)out.toByteArray().length);

        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

        out.reset();
        result.compress(Bitmap.CompressFormat.JPEG, 90, out);
        Log.i("123456",out.toByteArray().length+"");
        while(out.toByteArray().length > size * 1024){
            Log.i("123456",out.toByteArray().length+"");
            System.out.println(out.toByteArray().length);
            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            out.reset();
            result.compress(Bitmap.CompressFormat.JPEG, 90, out);
        }

        Log.i("quality",quality+"");
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        switch (format) {
            case "jpg":
                compressFormat = Bitmap.CompressFormat.JPEG;
                break;
            case "jpeg":
                compressFormat = Bitmap.CompressFormat.JPEG;
                break;
            case "png":
                compressFormat = Bitmap.CompressFormat.PNG;
                break;
            case "webp":
                compressFormat = Bitmap.CompressFormat.WEBP;
                break;

        }
        String filePath1 = FileUtils.COMPRESSION_FILE_PATH + "图片压缩_" + System.currentTimeMillis() + "." + format;

     /*   Bitmap newBitmap = new CompressHelper.Builder(this)
                .setMaxWidth(w)  // 默认最大宽度为720
                .setMaxHeight(h) // 默认最大高度为960
                //.setQuality(100)    // 默认压缩质量为80
                // .setFileName("图片压缩_" + System.currentTimeMillis()+"") // 设置你需要修改的文件名
                .setCompressFormat(compressFormat) // 设置默认压缩为jpg格式
                // .setDestinationDirectoryPath(FileUtils.COMPRESSION_FILE_PATH)
                .build()
                .compressToBitmap(new File(path));*/

     //   Bitmap scaledBitmap = Bitmap.createScaledBitmap(newBitmap, (int) (w * sizeCount / 100), (int) (h * sizeCount / 100), true);
        saveBitmapFile(filePath1, result,90);


       /* try {
            String path1 = newFile.getPath();
            String fileSize = Utils.getFileSize(newFile);

            Log.i("fileSize", fileSize);
            Log.i("fileSize", path1);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath1)));
        pathList.add(filePath1);
        /*String filePath1 = FileUtils.COMPRESSION_FILE_PATH + "图片压缩_" + System.currentTimeMillis()  + "." + imageType;
        Log.i("filePath1", filePath1);
        //File sdFile = Environment.getExternalStorageDirectory();
        File originFile = new File(filePath1);
        Bitmap originBitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        originBitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        try {
            FileOutputStream fos = new FileOutputStream(originFile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(filePath1);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (w * sizeCount / 100), (int) (h * sizeCount / 100), true);
        saveBitmapFile(filePath1, scaledBitmap);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath1)));
        pathList.add(filePath1);
        try {
            String fileSize = Utils.getFileSize(new File(filePath1));

            Log.i("fileSize", fileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void saveBitmapFile(String path, Bitmap bitmap, int quality) {
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //加载文件
    class DownThread extends Thread {
        @Override
        public void run() {
            switch (downType) {
                case "加载":
                    getData();
                    break;
                case "转换":
                    pathList.clear();
                    if (type == 0) {
                        compress(list.get(count), compressionCount, widthList.get(count), heightList.get(count), formatList.get(count));
                    } else if (type == 1) {
                        for (int i = 0; i < list.size(); i++) {
                            compress(list.get(i), compressionCount, widthList.get(i), heightList.get(i), formatList.get(i));
                        }
                    }

                    break;
            }
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    switch (downType) {
                        case "加载":
                            isDownComplete = true;
                            setData();
                            break;
                        case "转换":
                            Intent intent = new Intent(CompressionActivity.this, CompleteActivity.class);
                            intent.putExtra("list", (Serializable) pathList);
                            intent.putExtra("type", "");
                            startActivity(intent);
                            break;
                    }
                    loadingDialog.dismiss();
                }
            };
            uiHandler.post(runnable);
        }
    }


    private void setData() {
        contextList.clear();
        contextList.add("原图：" + widthList.get(count) + "" + " x " + heightList.get(count) + "" + "" + "px");
        contextList.add(sizeList.get(count));
        contextList.add("压缩后：" + (int) (widthList.get(count) * sizeCount / 100) + "" + " x " + (int) (heightList.get(count) * sizeCount / 100) + "" + "" + "px");
        ImageContentAdapter adapter = new ImageContentAdapter(CompressionActivity.this, contextList);
        contentRV.setAdapter(adapter);
        //size.setText("原图：" + widthList.get(count) + "" + " x " + heightList.get(count) + "" + "" + "px");
        //size1.setText(sizeList.get(count));
        //size0.setText("压缩后：" + (int) (widthList.get(count) * sizeCount / 100) + "" + " x " + (int) (heightList.get(count) * sizeCount / 100) + "" + "" + "px");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.single_ll:
                if (type != 0) {
                    type = 0;
                    singleTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                    manyTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//取消加粗
                    singleTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp_16));
                    manyTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp_14));
                    manyV.setVisibility(View.GONE);
                    singleV.setVisibility(View.VISIBLE);
                    countTV.setText("(" + (count + 1) + "" + "/" + list.size() + "" + ")");
                }
                break;
            case R.id.many_ll:
                if (type != 1) {
                    type = 1;
                    manyTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                    singleTV.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//取消加粗
                    manyTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp_16));
                    singleTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp_14));
                    singleV.setVisibility(View.GONE);
                    manyV.setVisibility(View.VISIBLE);
                    countTV.setText("(共" + list.size() + "" + "张)");
                }
                break;
            case R.id.btn:
                if (SharedPreferencesUtil.isVip()
                        || (SharedPreferencesUtil.getCount() > 0 && type == 0)) {
                    downType = "转换";
                    loadingDialog.show();
                    DownThread downThread = new DownThread();
                    downThread.start();
                } else {

                    SureDialog sureDialog = new SureDialog(CompressionActivity.this, "您的免费使用次数已用完,您可以成为高级用户无限次使用", "取消", "成为会员", "会员订阅")
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {

                                    ActivityUtil.start(ArouterPath.vip_activity);
                                    // ActivityUtil.start(ArouterPath.vip_activity);

                                }

                                @Override
                                public void onClickCanel() {
//                                                showStimulateAd();


                                }
                            });

                    sureDialog.show();
                }

                break;
        }

    }


    public Bitmap getBitmap(Context context,String path) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.e("aaa", "图片路径...28" + path);
            bitmap = BitmapFactory.decodeFile(path);
            Log.e("aaa", "bitmap...28..." + bitmap);
         //   saveBitmapPhoto(bitmap,fileName);
            return bitmap;
        } else {
            Log.e("aaa", "图片路径...28以上版本" + path);
            Uri imageContentUri = getImageContentUri(context, path);
            bitmap = getBitmapFromUri(context, imageContentUri);
            Log.e("aaa", "bitmap...29..." + bitmap);
           // saveBitmapPhoto(bitmap,fileName);
            return bitmap;
        }
    }

    public Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    // 通过uri加载图片
    public Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
