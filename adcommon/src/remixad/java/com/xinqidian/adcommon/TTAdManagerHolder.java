package com.xinqidian.adcommon;

import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.util.AppUtils;

/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class TTAdManagerHolder {

    private static final String TAG = "TTAdManagerHolder";

    private static boolean sInit;


    public static TTAdManager get() {
        return TTAdSdk.getAdManager();
    }

    public static void init(final Context context) {
        doInit(context);
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context) {
        if (!sInit) {
            //TTAdSdk.init(context, buildConfig(context));
            TTAdSdk.init(context, buildConfig(context), new TTAdSdk.InitCallback() {
                @Override
                public void success() {

                    Log.i(TAG, "success: "+TTAdSdk.isInitSuccess());
                }

                @Override
                public void fail(int code, String msg) {
                    Log.i(TAG, "fail:  code = " + code + " msg = " + msg);
                }
            });
            sInit = true;
        }
    }

    private static TTAdConfig buildConfig(Context context) {
        return new TTAdConfig.Builder()
                .appId(Contants.CHUANSHANJIA_APPID)
                .useTextureView(false) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .debug(BuildConfig.DEBUG) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                .supportMultiProcess(false)//是否支持多进程
                .needClearTaskReset()
                .build();
    }

//    private static boolean sInit;
//
//
//    public static TTAdManager get() {
//        if (!sInit) {
//            throw new RuntimeException("TTAdSdk is not init, please check.");
//        }
//        return TTAdSdk.getAdManager();
//    }
//
//    public static void init(Context context) {
//        doInit(context);
//    }
//
//    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
//    private static void doInit(Context context) {
//        if (!sInit) {
//            TTAdSdk.init(context, buildConfig(context));
//            sInit = true;
//        }
//    }
//
//    private static TTAdConfig buildConfig(Context context) {
//        return new TTAdConfig.Builder()
//                .appId(Contants.CHUANSHANJIA_APPID)
//                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
//                .appName(AppUtils.getAppName(context))
//                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
//                .allowShowNotify(true) //是否允许sdk展示通知栏提示
//                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
//                .debug(BuildConfig.DEBUG) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
//                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
//                .supportMultiProcess(true)//是否支持多进程
//                .needClearTaskReset()
//                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
//                .build();
//    }
}
