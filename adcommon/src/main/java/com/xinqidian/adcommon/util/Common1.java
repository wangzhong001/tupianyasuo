package com.xinqidian.adcommon.util;

import android.app.Application;


/**
 * Created by lipei on 2020/4/27.
 * 广告初始化,需在宿主的application中初始化
 */

public class Common1 {
    private static Application application;

    public static void init(Application application1){
        application=application1;

    }


    public static Application getApplication() {
        return application;
    }
}
