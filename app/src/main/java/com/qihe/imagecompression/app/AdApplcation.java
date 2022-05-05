package com.qihe.imagecompression.app;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Process;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.qihe.imagecompression.BuildConfig;
import com.qihe.imagecompression.util.OKHttpUpdateHttpService;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.Common;
import com.xinqidian.adcommon.util.Common1;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.AutoSizeLog;

/**
 * Created by lipei on 2020/4/27.
 */

public class AdApplcation extends Application {
    public static Context mContext;
    private boolean isNeedShowToast;
    private static IWXAPI wxapi;
    public static IWXAPI getWXapi() {
        return wxapi;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        Common1.init(this);
        // 注册订阅者
        EventBus.getDefault().register(this);

        Contants.XIAOMI_APPID = "2882303761517750884";//小米appID
        Contants.XIAOMI_SPLASH_ID = "fb8c718e4fd4b233c83abeb18739d233";//小米开屏ID
        Contants.XIAOMI_BANNER_ID = "bccf5e15ccf805ec1590b14786ae8266";//小米横幅ID
        Contants.XIAOMI_NATIVE_ID = "c8989cfe2c767b4ceb214f12fdca8820";//小米原生ID
        Contants.XIAOMI_INTERSTITIAL_ID = "71bb43c37b528dff0f2d90bc114f7ee5";//小米插屏ID
        Contants.XIAOMI_STIMULATE_ID = "2dd546dccff5996704a15d32c0e9b5cf";//小米激励视频ID


        Contants.CHUANSHANJIA_APPID = "5146648";//穿山甲广告appID
        Contants.CHUANSHANJIA_SPLASH_ID = "887441445";//穿山甲开屏广告ID
        Contants.CHUANSHANJIA_BANNER_ID = "945868126";//穿山甲广告横幅ID
        Contants.CHUANSHANJIA_NATIVE_ID = "7091917232473114";//穿山甲广告原生ID
        Contants.CHUANSHANJIA_INTERSTITIAL_ID = "945286041";//穿山甲广告插屏ID
        Contants.CHUANSHANJIA_STIMULATE_ID = "945285874";//穿山甲激励视频ID

//        Contants.TENCENT_APPID="1110525007";//腾讯广告appID
//        Contants.TENCENT_SPLASH_ID="3081615634409508";//腾讯开屏广告ID
//        Contants.TENCENT_BANNER_ID="2001419664904620";//腾讯广告横幅ID
//        Contants.TENCENT_NATIVE_ID="4001212674548907";//腾讯广告原生ID
//        Contants.TENCENT_INTERSTITIAL_ID="8051612211396628";//腾讯广告插屏ID
//        Contants.TENCENT_STIMULATE_ID="6041819674840958";//腾讯激励视频ID


        // vivo分流
//        Contants.TENCENT_APPID = "1110622078";//腾讯广告appID
//        Contants.TENCENT_STIMULATE_ID = "4051717714477355";//腾讯激励视频ID
//        Contants.TENCENT_BANNER_ID = "4091318794877332";//腾讯广告横幅ID
//        Contants.TENCENT_NATIVE_ID = "9061511794379333";//腾讯广告原生ID
//        Contants.TENCENT_INTERSTITIAL_ID = "9041219784570349";//腾讯广告插屏ID
//        Contants.TENCENT_SPLASH_ID = "6051719754877258";//腾讯开屏广告ID


        //oppo分流
//        Contants.TENCENT_APPID = "1110622078";//腾讯广告appID
//        Contants.TENCENT_STIMULATE_ID = "3011519792844520";//腾讯激励视频ID
//        Contants.TENCENT_BANNER_ID = "1081919752742407";//腾讯广告横幅ID
//        Contants.TENCENT_NATIVE_ID = "6091016762845582";//腾讯广告原生ID
//        Contants.TENCENT_INTERSTITIAL_ID = "3071212782240429";//腾讯广告插屏ID
//        Contants.TENCENT_SPLASH_ID = "3021117772844486";//腾讯开屏广告ID


//        //华为分流
        Contants.TENCENT_APPID = "1110658492";//腾讯广告appID
        Contants.TENCENT_STIMULATE_ID = "3081518942302042";//腾讯激励视频ID
        Contants.TENCENT_BANNER_ID = "6071911942908166";//腾讯广告横幅ID
        Contants.TENCENT_NATIVE_ID = "9091015794874566";//腾讯广告原生ID
        Contants.TENCENT_INTERSTITIAL_ID = "2011311902508064";//腾讯广告插屏ID
        Contants.TENCENT_SPLASH_ID = "1051211972702001";//腾讯开屏广告ID


        //小米分流
//        Contants.TENCENT_APPID = "1110622078";//腾讯广告appID
//        Contants.TENCENT_STIMULATE_ID = "7091818702537160";//腾讯激励视频ID
//        Contants.TENCENT_BANNER_ID = "5061612752135029";//腾讯广告横幅ID
//        Contants.TENCENT_NATIVE_ID = "2061111762036162";//腾讯广告原生ID
//        Contants.TENCENT_INTERSTITIAL_ID = "6001913742233135";//腾讯广告插屏ID
//        Contants.TENCENT_SPLASH_ID = "6031815762132036";//腾讯开屏广告ID


        /** 是否开启开屏广告**/
        Contants.IS_SHOW_SPLASH_AD = BuildConfig.IS_SHOW_SPLASH_AD;

        /** 是否开启横幅广告**/
        Contants.IS_SHOW_BANNER_AD = BuildConfig.IS_SHOW_BANNER_AD;

        /** 是否开启原生广告**/
        Contants.IS_SHOW_NATIVE_AD = BuildConfig.IS_SHOW_NATIVE_AD;

        /** 是否开启插屏广告**/
        Contants.IS_SHOW_VERTICALINTERSTITIAL_AD = BuildConfig.IS_SHOW_VERTICALINTERSTITIAL_AD;

        /** 是否开启激励视频广告**/
        Contants.IS_SHOW_STIMULATE_AD = BuildConfig.IS_SHOW_STIMULATE_AD;

        /** 免费使用功能的次数**/
        Contants.SHOW_STIMULATE_NUMBER = 1;



        /** app启动是否需要显示隐私弹框*/
        Contants.IS_NEED_SERCERT = true;

        /** 多少次后显示好评弹框*/
        Contants.COMMENT_NUMBER = 5;

        /** 多少次后显示插屏*/
        Contants.VERTICALINTERSTITIAL_NUMBER = 5;

        /** 平台*/
        Contants.PLATFORM = "vivo";

        /**
         * vivo 用 相机图片编辑器
         * 其他用 图片编辑压缩
         */

        /** leanClound appId*/
        Contants.LEANCLOUND_APPID = "xlpQHV45cGOTUt0UtfAGfgf4-gzGzoHsz";

        /** leanClound appKey*/
        Contants.LEANCLOUND_APPKEY = "K4JQk83O2GHOh16VwCJrunUI";

//        Contants.BASE_URL="http://121.41.82.53:8085/";

        Contants.BASE_URL = "http://www.qihe.website:8090/";

        Contants.APP_CODE = "8088";

        Contants.QQ = "1156271983";




    }

    private void init(){
        wxapi = WXAPIFactory.createWXAPI(this, "wxa481de9047d8bcaa", false);
        wxapi.registerApp("wxa481de9047d8bcaa");    //应用ID
        StatService.trackCustomEvent(this, "onCreate", "");

        ARouter.init(this);
        KLog.init(BuildConfig.DEBUG);

        initSize();

        Common.init(this);

        initOkGo();

    }


    private void initSize() {


        if(Contants.PLATFORM.equals("huawei")){
            /** 下载广告的时候是否需要已经提示 true点击下载先提示 false无需用提示,暂时只有华为平台需要*/
            Contants.IS_NEED_COMFIRMED = true;
        }else{
            /** 下载广告的时候是否需要已经提示 true点击下载先提示 false无需用提示,暂时只有华为平台需要*/
            Contants.IS_NEED_COMFIRMED = false;
        }

        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        //在 Demo 中跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this);

        //如果在某些特殊情况下出现 InitProvider 未能正常实例化, 导致 AndroidAutoSize 未能完成初始化
        //可以主动调用 AutoSize.checkAndInit(this) 方法, 完成 AndroidAutoSize 的初始化后即可正常使用
//        AutoSize.checkAndInit(this);

//        如何控制 AndroidAutoSize 的初始化，让 AndroidAutoSize 在某些设备上不自动启动？https://github.com/JessYanCoding/AndroidAutoSize/issues/249

        /**
         * 以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
         * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
         */
        AutoSizeConfig.getInstance()

                //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                //如果没有这个需求建议不开启
                .setCustomFragment(true)

                //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
                //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
//                .setExcludeFontScale(true)

                //区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
                //当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效, 不设置或将此值设为 0 则取消此功能
//                .setPrivateFontScale(0.8f)

                //屏幕适配监听器
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
                        //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                        //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
//                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
//                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName()));
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName()));
                    }
                })

        //是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
//                .setLog(false)

        //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
        //AutoSize 会将屏幕总高度减去状态栏高度来做适配
        //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
        //在全面屏或刘海屏幕设备中, 获取到的屏幕高度可能不包含状态栏高度, 所以在全面屏设备中不需要减去状态栏高度，所以可以 setUseDeviceSize(true)
//                .setUseDeviceSize(true)

        //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
//                .setBaseOnWidth(false)

        //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
//                .setAutoAdaptStrategy(new AutoAdaptStrategy())
        ;
        //友盟
        UMConfigure.init(this, "6013ad156a2a470e8f96daf4", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        //*
        //* 设置组件化的Log开关
        //* 参数: boolean 默认为false，如需查看LOG设置为true

        // UMConfigure.setLogEnabled(true);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);


        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(Process.myPid());
        // 设置是否为上报进程
        UserStrategy strategy = new UserStrategy(context);
        strategy.setBuglyLogUpload(processName == null || processName.equals(packageName));
        //  strategy.setUploadProcess();
        // 初始化Bugly
        CrashReport.initCrashReport(context, "9dc502637c", false, strategy);
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        // CrashReport.initCrashReport(context, strategy);
        //     CrashReport.testJavaCrash();

      //  PlatformConfig.setWeixin("wxa481de9047d8bcaa", "35355fbeb52815136ab5145897c5b16d");
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


    private void initOkGo() {
        LiveDataBus.get().with(LiveBusConfig.updateApp, Boolean.class).observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                isNeedShowToast = aBoolean;
            }
        });


        XUpdate.get()
                .debug(BuildConfig.DEBUG)
                .isWifiOnly(false)                                               //默认设置只在wifi下检查版本更新
                .isGet(true)                                                    //默认设置使用get请求检查版本
                .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
                .setOnUpdateFailureListener(new OnUpdateFailureListener() {     //设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        if (isNeedShowToast) {
                            ToastUtils.show("已是最新版本了");
                        }

//                        KLog.e("error---->",new Gson().toJson(error));
//
                    }
                })
                .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
                .init(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String s) {
        if (s.equals("初始化")) {
            Log.i("123456","初始化");
            init();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }

}
