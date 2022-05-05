package com.qihe.imagecompression.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.microshow.rxffmpeg.RxFFmpegInvoke;

/**
 * Created by Super on 2018/12/8.
 */
public class Utils {

    //bmp,jpg,png,tif,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,WMF,webp,avif

    public static int getFileType(String path) {
        int i = -1;

        if (path.endsWith("jpg")
                || path.endsWith("png")
                || path.endsWith("webp")
                || path.endsWith("jpeg")
                || path.endsWith("gif")) {
            i = 0;

        } else if (path.endsWith("mp4")//AVI、mov、rmvb、rm、FLV、mp4、3GP
                || path.endsWith("rmvb")
                || path.endsWith("AVI")
                || path.endsWith("3GP")
                || path.endsWith("FLV")) {
            i = 1;
        }
        return i;
    }


    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String getFileSize(File file)  {
        long size = 0;
       /* if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }*/
        size = file.length();
        return FormetFileSize(size);
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName=null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    //时间戳转字符串
    public static String getStrTime(String timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        long l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));
        return timeString;
    }

    public static void jumpToReview(Context context, String packageName) {
        try {
            String mAddress = "market://details?id=" + packageName;
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
        } catch (Exception e) {
            Toast.makeText(context, "暂无发现应用市场", Toast.LENGTH_SHORT).show();
        }

    }

    //优化内存使用
    static StringBuilder mUsDurationText = new StringBuilder();

    /**
     * 微秒转换为 时分秒毫秒,如 00:00:00.000
     *
     * @param us           微秒
     * @param autoEllipsis true:如果小时为0，则这样显示00:00.000;  false:全部显示 00:00:00.000
     * @return
     */
    public static String convertUsToTime(long us, boolean autoEllipsis) {

        mUsDurationText.delete(0, mUsDurationText.length());

        long ms = us / 1000;
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        //天
        long day = ms / dd;
        //小时
        long hour = (ms - day * dd) / hh;
        //分
        long minute = (ms - day * dd - hour * hh) / mi;
        //秒
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        //毫秒
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        if (autoEllipsis) {
            if (hour > 0) {
                mUsDurationText.append(strHour).append(":");
            }
        } else {
            mUsDurationText.append(strHour).append(":");
        }
        mUsDurationText.append(strMinute).append(":")
                .append(strSecond).append(".").append(strMilliSecond);
        return mUsDurationText.toString();
    }

    public static void showDialog(Context context, String message, String runTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(message + "\n\n耗时时间：" + runTime);

        builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
//        builder.setCancelable(false);
        builder.create().show();
    }

    public static ProgressDialog openProgressDialog(Context context) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        final int totalProgressTime = 100;
        mProgressDialog.setMessage("正在转换视频，请稍后...");
        mProgressDialog.setButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //中断 ffmpeg
                RxFFmpegInvoke.getInstance().exit();
            }
        });
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgressNumberFormat("");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(totalProgressTime);
        mProgressDialog.show();
        return mProgressDialog;
    }

    /**
     * 用于解决输入法内存泄露
     * @param context context
     */
    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj = f.get(imm);
                if (obj instanceof View) {
                    View vGet = (View) obj;
                    if (vGet.getContext() == context) {
                        f.set(imm, null);
                    } else {
                        break;
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
