package com.qihe.imagecompression.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by lipei on 2018/12/4.
 */

public class ActivityUtil {
    public final static String OREDR = "orderCode";

    public static void fullScreen(Window window) {
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                //View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                WindowManager.LayoutParams attributes                = window.getAttributes();
                int flagTranslucentStatus     = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                window.setAttributes(attributes);
            }
        }

    }


    public static void start(String path) {
        ARouter.getInstance().build(path).navigation();
    }


    public static void startForResult(String path, Activity activity, int requestCode, String key, boolean value) {
        ARouter.getInstance().build(path)
                .withBoolean(key, value)
                .navigation(activity, requestCode);
    }


    public static void start(String path, String key, String value) {
        ARouter.getInstance().build(path)
                .withString(key, value)
                .navigation();
    }


    public static void start(String path, String key, boolean value) {
        ARouter.getInstance().build(path)
                .withBoolean(key, value)
                .navigation();
    }


    public static void start(String path, String key, boolean value,String key2,boolean value2) {
        ARouter.getInstance().build(path)
                .withBoolean(key, value)
                .withBoolean(key2, value2)

                .navigation();
    }


    public static void start(String path, String key, boolean value,String key1,String value1) {
        ARouter.getInstance().build(path)
                .withBoolean(key, value)
                .withString(key1,value1)
                .navigation();
    }


    public static void start(String path, String key, boolean value,String key1,String value1,String key2,boolean value2) {
        ARouter.getInstance().build(path)
                .withBoolean(key, value)
                .withString(key1,value1)
                .withBoolean(key2,value2)
                .navigation();
    }


    public static void start(String path, String key, boolean value,String key1,Object value1) {
        ARouter.getInstance().build(path)
                .withBoolean(key, value)
                .withObject(key1,value1)
                .navigation();
    }


    public static void start(String path, String key, boolean value,String key1,Object value1,String key2,boolean value2) {
        ARouter.getInstance().build(path)
                .withBoolean(key, value)
                .withObject(key1,value1)
                .withBoolean(key2,value2)
                .navigation();
    }

    public static void start(String path, String key, boolean value,String key1,Object value1,String key2,String value2,String key3,boolean value3) {
        ARouter.getInstance().build(path)
                .withBoolean(key, value)
                .withObject(key1,value1)
                .withString(key2,value2)
                .withBoolean(key3,value3)
                .navigation();
    }

    public static void start(String path, String key, Object value) {
        ARouter.getInstance().build(path)
                .withObject(key, value)
                .navigation();
    }


    public static void start(String path, String key, int value) {
        ARouter.getInstance().build(path)
                .withInt(key, value)
                .navigation();
    }


    public static void start(String path, String key, int value,String key1,String value1) {
        ARouter.getInstance().build(path)
                .withInt(key, value)
                .withString(key1,value1)
                .navigation();
    }


    public static void startNewThread(String path, String key, Object value,String key1,Object value1) {
        ARouter.getInstance().build(path)
                .withObject(key, value)
                .withObject(key1,value1)
                .navigation();
    }

    public static void start(String path, String key, Object value,String key1,Object value1) {
        ARouter.getInstance().build(path)
                .withObject(key, value)
                .withObject(key1,value1)
                .navigation();
    }


    public static void start(String path, String key, Object value,String key1,int value1,String key2,boolean value2) {
        ARouter.getInstance().build(path)
                .withObject(key, value)
                .withInt(key1,value1)
                .withBoolean(key2,value2)
                .navigation();
    }


//    public static void start(String path, String key, boolean value,String key1,Object value1) {
//        ARouter.getInstance().build(path)
//                .withBoolean(key, value)
//                .withObject(key1,value1)
//                .navigation();
//    }


    public static void start(String path, String key, String value,String key1,boolean value1) {
        ARouter.getInstance().build(path)
                .withString(key, value)
                .withBoolean(key1,value1)
                .navigation();
    }


    public static void start(String path, String key, String value,String key1,String value1) {
        ARouter.getInstance().build(path)
                .withString(key, value)
                .withString(key1,value1)
                .navigation();
    }

    public static void start(String path, String key, int value,String key1,Object value1,String key2,String value2) {
        ARouter.getInstance().build(path)
                .withInt(key, value)
                .withObject(key1,value1)
                .withString(key2,value2)
                .navigation();
    }


    public static void start(String path, String key, String value,String key1,String value1,String key2,String value2) {
        ARouter.getInstance().build(path)
                .withString(key, value)
                .withString(key1,value1)
                .withString(key2,value2)
                .navigation();
    }


    public static void start(String path, String key, String value,String key1,String value1,String key2,String value2,String key3,String value3) {
        ARouter.getInstance().build(path)
                .withString(key, value)
                .withString(key1,value1)
                .withString(key2,value2)
                .withString(key3,value3)

                .navigation();
    }



    public static void start(String path, String key, String value,String key1,String value1,String key2,long value2) {
        ARouter.getInstance().build(path)
                .withString(key, value)
                .withString(key1,value1)
                .withLong(key2,value2)
                .navigation();
    }


    public static void start(String path, String key, Object value,String key1,Object value1,String key2,boolean value2,String key3,boolean value3) {
        ARouter.getInstance().build(path)
                .withObject(key, value)
                .withObject(key1,value1)
                .withBoolean(key2,value2)
                .withBoolean(key3,value3)
                .navigation();
    }
}
