package com.qihe.imagecompression.ui.activity.image;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.Utils;
import com.qihe.imagecompression.view.LoadingDialog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.view.SureDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 格式转换、尺寸调整页面
 */
public class FormatConversionActivity extends AppCompatActivity implements View.OnClickListener {

    private String path;
    private TextView size;
    private TextView size1;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;

    private String type = "png";

    private List<String> pathList = new ArrayList<>();
    private String string;
    private Handler uiHandler = new Handler();
    private LoadingDialog loadingDialog;
    private int height;
    private int width;

    private int height1 = 0;
    private int width1 = 0;
    private String fileSize;
    private TextView format;


    private String downType;
    private ImageView image;
   // private Bitmap bitmap1;
    private EditText editText1;
    private EditText editText2;

    private Boolean flag = false;//标记edittext不会死循环

    private Double proportion;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format_conversion);
        path = getIntent().getStringExtra("path");
        string = getIntent().getStringExtra("string");

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
        String substring = path.substring(0, path.lastIndexOf(".") + 1);

        loadingDialog = new LoadingDialog(this);
        TextView title = findViewById(R.id.title);
        LinearLayout sizeLL = findViewById(R.id.size_ll);

        size = findViewById(R.id.size);
        size1 = findViewById(R.id.size1);
        format = findViewById(R.id.format);
        format.setText(path.replace(substring, ""));
        text1 = findViewById(R.id.text1);
        text1.setOnClickListener(this);
        text2 = findViewById(R.id.text2);
        text2.setOnClickListener(this);
        text3 = findViewById(R.id.text3);
        text3.setOnClickListener(this);
        text4 = findViewById(R.id.text4);
        text4.setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(this);

        editText1 = findViewById(R.id.edit_text1);
        editText2 = findViewById(R.id.edit_text2);


        if (string.equals("尺寸")) {
            title.setText("尺寸调整");
        } else if (string.equals("格式")) {
            title.setText("格式转换");
            sizeLL.setVisibility(View.GONE);
        }

        initData();
    }

    private void initData() {
        Log.i("path1", path);
        image = findViewById(R.id.image);
        Glide.with(this)
                .load(path)
                .placeholder(R.drawable.loadpicture_icon)
                .error(R.drawable.loadpicture_icon)
                .into(image);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        //只请求图片宽高，不解析图片像素(请求图片属性但不申请内存，解析bitmap对象，该对象不占内存)
        opts.inJustDecodeBounds = true;
        //String path = Environment.getExternalStorageDirectory() + "/dog.jpg";
        BitmapFactory.decodeFile(path, opts);

        width = opts.outWidth;
        height = opts.outHeight;

        /*bitmap1 = BitmapFactory.decodeFile(path);
        //image.setImageBitmap(bitmap1);
        height = bitmap1.getHeight();
        width = bitmap1.getWidth();*/
        //proportion =  width * 100 / height;
        proportion = new BigDecimal((float) width / height).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        //int bitmapSize = getBitmapSize(bitmap1);
        //Log.i("bitmapSize", proportion + "");
        //Log.i("bitmapSize", bitmapSize + "");
        //Log.i("bitmapSize", bitmap1.getWidth() + "" + ":::" + bitmap1.getHeight() + "");
        downType = "加载";
        loadingDialog.show();
        DownThread downThread = new DownThread();
        downThread.start();

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // String s = charSequence.toString();
                //editText2.setText(s);

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (flag) {
                    return;
                }
                flag = true;

                String s = editable.toString();

                if (!s.equals("")) {
                    double v = Double.parseDouble(s);
                    int v1 = (int) (v / proportion);
                    editText2.setText(v1 + "");
                    width1 = (int) v;
                    height1 = v1;
                } else {
                    editText2.setText("");
                }


                flag = false;
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (flag) {
                    return;
                }
                flag = true;

                String s = editable.toString();
                if (!s.equals("")) {
                    double v = Double.parseDouble(s);
                    int v1 = (int) (v * proportion);
                    editText1.setText(v1 + "");
                    height1 = (int) v;
                    width1 = v1;
                } else {
                    editText1.setText("");
                }

                flag = false;

            }
        });

    }

    //加载文件
    class DownThread extends Thread {
        @SuppressLint("NewApi")
        @Override
        public void run() {
            switch (downType) {
                case "加载":
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    height = bitmap.getHeight();
                    width = bitmap.getWidth();
                    try {
                        fileSize = Utils.getFileSize(new File(path));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "转换":
                    pathList.clear();
                    String s = convertToPng();
                    pathList.add(s);


                    //bitmap1 = bitmapFactory(path);
                   /* bitmap1 = Bitmap.createScaledBitmap(bitmap1, width1, height1, true);
                    height = bitmap1.getHeight();
                    width = bitmap1.getWidth();
                    int bitmapSize = getBitmapSize(bitmap1);
                    Log.i("bitmapSize", bitmapSize + "");
                    Log.i("bitmapSize", bitmap1.getWidth() + "" + ":::" + bitmap1.getHeight() + "");*/
                    break;
            }


            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    switch (downType) {
                        case "加载":
                            size.setText("原图：" + width + "" + " x " + height + "" + "px");
                            size1.setText(fileSize);
                            break;
                        case "转换":
                            Intent intent = new Intent(FormatConversionActivity.this, CompleteActivity.class);
                            intent.putExtra("list", (Serializable) pathList);
                            intent.putExtra("type", "");
                            startActivity(intent);
                            /*image.setImageBitmap(bitmap1);
                            size.setText("原图：" + width + "" + " x " + height + "" + "pxpx");*/
                            break;
                    }
                    loadingDialog.dismiss();

                }
            };
            uiHandler.post(runnable);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text1:
                if (!type.equals("原始")) {
                    setText();
                    text1.setTextColor((Color.parseColor("#ffffffff")));
                    text1.setBackgroundResource(R.drawable.select_btn_bg);
                    type = "原始";
                }
                break;
            case R.id.text2:
                if (!type.equals("PNG")) {
                    setText();
                    text2.setTextColor((Color.parseColor("#ffffffff")));
                    text2.setBackgroundResource(R.drawable.select_btn_bg);
                    type = "png";
                }
                break;
            case R.id.text3:
                if (!type.equals("JPG")) {
                    setText();
                    text3.setTextColor((Color.parseColor("#ffffffff")));
                    text3.setBackgroundResource(R.drawable.select_btn_bg);
                    type = "jpg";
                }
                break;
            case R.id.text4:
                if (!type.equals("Webp")) {
                    setText();
                    text4.setTextColor((Color.parseColor("#ffffffff")));
                    text4.setBackgroundResource(R.drawable.select_btn_bg);
                    type = "webp";
                }
                break;
            case R.id.btn:

                if (SharedPreferencesUtil.isVip() || SharedPreferencesUtil.getCount() > 0) {
                    downType = "转换";
                    loadingDialog.show();
                    DownThread downThread = new DownThread();
                    downThread.start();
                } else {

                    SureDialog sureDialog = new SureDialog(FormatConversionActivity.this, "您的免费使用次数已用完,您可以成为高级用户无限次使用", "取消", "成为会员", "会员订阅")
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


    private void setText() {
        text1.setTextColor((Color.parseColor("#ff2f3238")));
        text2.setTextColor((Color.parseColor("#ff2f3238")));
        text3.setTextColor((Color.parseColor("#ff2f3238")));
        text4.setTextColor((Color.parseColor("#ff2f3238")));
        text1.setBackgroundResource(R.drawable.select_btn_bg1);
        text2.setBackgroundResource(R.drawable.select_btn_bg1);
        text3.setBackgroundResource(R.drawable.select_btn_bg1);
        text4.setBackgroundResource(R.drawable.select_btn_bg1);
    }


    public Bitmap bitmapFactory(String filePath) {

        // 配置压缩的参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //获取当前图片的边界大小，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        ////inSampleSize的作用就是可以把图片的长短缩小inSampleSize倍，所占内存缩小inSampleSize的平方
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options); // 解码文件


        return bm;
    }

    /**
     * 得到bitmap的大小
     */
    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * 计算出所需要压缩的大小
     *
     * @param options
     * @param reqWidth  我们期望的图片的宽，单位px
     * @param reqHeight 我们期望的图片的高，单位px
     * @return
     */
    private int caculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        if (picWidth > reqWidth || picHeight > reqHeight) {
            int halfPicWidth = picWidth / 2;
            int halfPicHeight = picHeight / 2;
            while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
        }
        Log.i("sampleSize", sampleSize + "");
        return sampleSize;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String convertToPng() {
        Bitmap.CompressFormat format;
        switch (type) {
            case "png":
                format = Bitmap.CompressFormat.PNG;
                break;
            case "jpg":
                format = Bitmap.CompressFormat.JPEG;
                break;
            case "webp":
                format = Bitmap.CompressFormat.WEBP;
                break;
            case "原始":
                return "";
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        String filePath1 = "";

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (string.equals("尺寸")) {
            if (width1 == 0 || height1 == 0) {
                filePath1 = FileUtils.COMPRESSION_FILE_PATH + "尺寸调整_" + System.currentTimeMillis() + "." + type;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            } else {
                filePath1 = FileUtils.COMPRESSION_FILE_PATH + "尺寸调整_" + System.currentTimeMillis() + "." + type;
                bitmap = Bitmap.createScaledBitmap(bitmap, width1, height1, true);
            }

            Log.i("filePath1", filePath1);
        } else if (string.equals("格式")) {
            filePath1 = FileUtils.COMPRESSION_FILE_PATH + "格式转换_" + System.currentTimeMillis() + "." + type;
            Log.i("filePath1", filePath1);
        }


        // File file1 = new File(filePath1);
        /*try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath1))) {
            if (bitmap.compress(format, 100, bos)) {
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        saveBitmapFile(format, filePath1, bitmap);

        //MediaStore.Images.Media.insertImage(FormatConversionActivity.this.getContentResolver(), BitmapFactory.decodeFile(filePath1), "", "");
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath1)));
        return filePath1;
    }


    public void saveBitmapFile(Bitmap.CompressFormat format, String path, Bitmap bitmap) {
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(format, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
