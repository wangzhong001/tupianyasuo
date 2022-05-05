package com.xinqidian.adcommon.ui.splash;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.TTAdManagerHolder;
import com.xinqidian.adcommon.adview.TwiceFragmentLayout;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.ui.activity.WebViewActivity;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.SecretDialogUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.StatusBarUtil;
import com.xinqidian.adcommon.util.UIUtils;
import com.xinqidian.adcommon.view.SecretInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 开屏广告
 *
 * @author lipei
 */

public class SplashActivity extends AppCompatActivity implements SplashADListener, SecretInterface {
    private static final String TAG = "SplashActivity";

    private SplashAD splashAD;
    private TwiceFragmentLayout container;
    private TextView skipView;
    private ImageView splashHolder;
    private static final String SKIP_TEXT = "点击跳过 %d";

    public boolean canJump = false;
    private int defaultImageID = R.drawable.default_splash;

    private boolean needStartDemoList = true;
    private Bitmap bitmap;
    private SecretDialogUtil secretDialogUtil;
    private TTAdNative mTTAdNative;
    private boolean isTecet;


    /**
     * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
     * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
     * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值（单位ms）
     **/
    private int minSplashTimeWhenNoAD = 2000;
    /**
     * 记录拉取广告的时间
     */
    private long fetchSplashADTime = 0;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        secretDialogUtil = new SecretDialogUtil(this);
        setContentView(R.layout.activity_splash);
        container = (TwiceFragmentLayout) findViewById(R.id.content_id);
        container.setComfirmed(!Contants.IS_NEED_COMFIRMED);
        skipView = (TextView) findViewById(R.id.skip_view);
        splashHolder = (ImageView) findViewById(R.id.splash_holder);
        bitmap = StatusBarUtil.scaleBitmap(StatusBarUtil.readBitMap(this, getDefaultImage()), 0.9F);
        splashHolder.setImageBitmap(bitmap);


/*
        UserUtil.isShowBanner(new UserUtil.SureInterface() {
            @Override
            public void sure(boolean sure) {

                if(Contants.PLATFORM.equals("huawei")){
                    container.setComfirmed(!sure);

                }else {
                    container.setComfirmed(true);

                }

            }
        });*/

       /* UserUtil.isShowBanner(new UserUtil.SureInterface() {
            @Override
            public void sure(boolean sure) {
                container.setComfirmed(!sure);

            }
        });*/

        secertData();


    }

    private void doMainThing() {
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
       /* if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
            Log.d(TAG, container + "--->" + skipView);
            showSplash();
        }*/
        showSplash();
    }


    private String getPosId() {
        return Contants.TENCENT_SPLASH_ID;
    }

    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            if (
                    checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 1024);
            } else {
                showSplash();

            }
        } else {
            showSplash();

        }

//        List<String> lackedPermission = new ArrayList<String>();
//        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
//            KLog.e("size--->","READ_PHONE_STATE");
//
//        }
//
//        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            KLog.e("size--->","WRITE_EXTERNAL_STORAGE");
//
//        }
//
////        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
////            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
////        }
//
//        KLog.e("size--->",lackedPermission.size());
//        // 权限都已经有了，那么直接调用SDK
//        if (lackedPermission.size() == 0) {
//            showSplash();
//        } else {
//            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
//            String[] requestPermissions = new String[lackedPermission.size()];
//            lackedPermission.toArray(requestPermissions);
//            requestPermissions(requestPermissions, 1024);
//        }
    }

    private void showSplash() {
        EventBus.getDefault().post("初始化");
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        if (Contants.IS_SHOW_SPLASH_AD) {
//            if(SharedPreferencesUtil.isVip()){
//                toMainActivity();
//
//            }else {

            UserUtil.getKey(Contants.videoparsemusicSplashActivity, new UserUtil.KeyInterFace() {
                @Override
                public void onSuccess(int number) {
                    if (number == -1) {
                        fetchSplashAD(SplashActivity.this, container, skipView, Contants.TENCENT_SPLASH_ID, getPosId(), SplashActivity.this, 0);

                    } else if (number == -2) {
                        fetchSplashAD();
                        isTecet = false;

                    }else if (number == -3) {
                        if(Contants.PLATFORM.equals("huawei")){
                            toMainActivity();

                        }else {
                            fetchSplashAD();
                            isTecet = false;
                        }

                    } else {
                        if (number < 7) {

                            fetchSplashAD();


                            isTecet = false;

                            number += 1;
                            UserUtil.setKey(Contants.videoparsemusicSplashActivity, number);

                        } else {
                            isTecet = true;
                            fetchSplashAD(SplashActivity.this, container, skipView, Contants.TENCENT_SPLASH_ID, getPosId(), SplashActivity.this, 0);

                            number += 1;
                            if (number == 11) {
                                UserUtil.setKey(Contants.videoparsemusicSplashActivity, 0);

                            } else {
                                UserUtil.setKey(Contants.videoparsemusicSplashActivity, number);

                            }

                        }
                    }


                }

                @Override
                public void onFail() {
                    fetchSplashAD();

                }
            });


//            }

        } else {
            toMainActivity();

        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            showSplash();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            showSplash();
            /*Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();*/
        }
    }

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity      展示广告的activity
     * @param adContainer   展示广告的大容器
     * @param skipContainer 自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param appId         应用ID
     * @param posId         广告位ID
     * @param adListener    广告状态监听器
     * @param fetchDelay    拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        fetchSplashADTime = System.currentTimeMillis();
        splashAD = new SplashAD(activity, skipContainer, appId, posId, adListener, fetchDelay);
        splashAD.fetchAndShowIn(adContainer);
    }

    @Override
    public void onADPresent() {
        Log.i("AD_DEMO", "SplashADPresent");
    }

    @Override
    public void onADClicked() {
        Log.i("AD_DEMO", "SplashADClicked clickUrl: "
                + (splashAD.getExt() != null ? splashAD.getExt().get("clickUrl") : ""));
    }

    /**
     * 倒计时回调，返回广告还将被展示的剩余时间。
     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
     *
     * @param millisUntilFinished 剩余毫秒数
     */
    @Override
    public void onADTick(long millisUntilFinished) {
        Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");
        skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
    }

    @Override
    public void onADExposure() {
        Log.i("AD_DEMO", "SplashADExposure");
    }

    @Override
    public void onADLoaded(long l) {

    }

    @Override
    public void onADDismissed() {
        Log.i("AD_DEMO", "SplashADDismissed");
        next();
    }

    @Override
    public void onNoAD(AdError error) {
        Log.i(
                "AD_DEMO",
                String.format("LoadSplashADFail, eCode=%d, errorMsg=%s", error.getErrorCode(),
                        error.getErrorMsg()));
        /**
         * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
         * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
         * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值
         **/
        long alreadyDelayMills = System.currentTimeMillis() - fetchSplashADTime;//从拉广告开始到onNoAD已经消耗了多少时间
        long shouldDelayMills = alreadyDelayMills > minSplashTimeWhenNoAD ? 0 : minSplashTimeWhenNoAD
                - alreadyDelayMills;//为防止加载广告失败后立刻跳离开屏可能造成的视觉上类似于"闪退"的情况，根据设置的minSplashTimeWhenNoAD
        // 计算出还需要延时多久
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (needStartDemoList) {
                    toMainActivity();
                }
                SplashActivity.this.finish();
            }
        }, shouldDelayMills);
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private void next() {
        if (canJump) {
            if (needStartDemoList) {
                toMainActivity();
            }
            this.finish();
        } else {
            canJump = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    //是否强制跳转到主页面
    private boolean mForceGoMain;

    @Override
    protected void onResume() {
        super.onResume();

//        if(isTecet){
        if (canJump) {
            next();
        }
        canJump = true;
//        }else {
//            if (mForceGoMain) {
//                toMainActivity();
//            }
//        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mForceGoMain = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void toMainActivity() {

//        Log.e(TAG, "toMainActivity-->" + getTargetActivityClassNameFromMetaData());
//
//
//        try {
//            String targetActivityClassName = getTargetActivityClassNameFromMetaData();
//            Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
//            Intent intent = new Intent(this, targetActivityClass);
//            startActivity(intent);
//            finish();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }


        Boolean isNeedToOrtherActivity = (Boolean) SharedPreferencesUtil.getParam(Contants.IS_NEED_TO_ORTHER_ACTIVITY, false);

        if (isNeedToOrtherActivity) {
            //跳转到其他页面
            try {
                String targetActivityClassName = getOrtherTargetActivityClassNameFromMetaData();
                Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
                Intent intent = new Intent(this, targetActivityClass);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                String targetActivityClassName = getTargetActivityClassNameFromMetaData();
                Class<Activity> targetActivityClass = (Class<Activity>) Class.forName(targetActivityClassName);
                Intent intent = new Intent(this, targetActivityClass);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }


    private String getTargetActivityClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.TARGET_ACTIVITY);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.TARGET_ACTIVITY + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getOrtherTargetActivityClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.ORTHER_ACTIVITY);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.ORTHER_ACTIVITY + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private int getDefaultImage() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            int defaultImageID = ai.metaData.getInt(Contants.DEFAUT_IMAGE);
            if (defaultImageID <= 0) {
                throw new RuntimeException("meta-data named \"" + Contants.DEFAUT_IMAGE + "\" must be set!");
            } else {
                this.defaultImageID = defaultImageID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return defaultImageID;
    }


    private void secertData() {
        if (Contants.IS_NEED_SERCERT) {
            //需要弹出隐私弹框
            boolean isHasShowSercert = (boolean) SharedPreferencesUtil.getParam(Contants.IS_NEED_SERCERT_STRING, false);
            if (isHasShowSercert) {
                doMainThing();
            } else {
                secretDialogUtil.showPrivacy(this, getString(R.string.app_name));
            }

        } else {
            doMainThing();

        }
    }

    @Override
    public void canelClick() {
        SharedPreferencesUtil.setParam(Contants.IS_NEED_SERCERT_STRING, false);
        finish();

    }

    @Override
    public void sureClick() {
        SharedPreferencesUtil.setParam(Contants.IS_NEED_SERCERT_STRING, true);
        doMainThing();


    }

    @Override
    public void xieyiClick() {
        try {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", getXieyiClassNameFromMetaData());
            intent.putExtra("title", "用户协议");
            startActivity(intent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void yinsiClick() {
        try {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", getYinsiClassNameFromMetaData());
            intent.putExtra("title", "隐私政策");
            startActivity(intent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private String getWebViewActivityClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.WEBVIEW_ACTIVITY);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.WEBVIEW_ACTIVITY + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getXieyiClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.XIEYI);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.XIEYI + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getYinsiClassNameFromMetaData() {
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            String targetActivityClassName = ai.metaData.getString(Contants.YINSIZHENGCE);
            if (targetActivityClassName == null || targetActivityClassName.length() == 0) {
                throw new RuntimeException("meta-data named \"" + Contants.YINSIZHENGCE + "\" can not be empty!");
            }
            return targetActivityClassName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     */
    private void fetchSplashAD() {
        AdSlot adSlot = null;
        float expressViewWidth = UIUtils.getScreenWidthDp(this);
        float expressViewHeight = UIUtils.getHeight(this);
        adSlot = new AdSlot.Builder()
                .setCodeId(Contants.CHUANSHANJIA_SPLASH_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
//                .s(expressViewWidth, expressViewHeight)
                .build();


        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.d(TAG, String.valueOf(message));
                toMainActivity();
            }

            @Override
            @MainThread
            public void onTimeout() {
                toMainActivity();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                Log.d(TAG, "开屏广告请求成功");
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                if (view != null && container != null && !SplashActivity.this.isFinishing()) {
                    container.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    container.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                } else {
                    toMainActivity();
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClicked");

                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
                        toMainActivity();

                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
                        toMainActivity();
                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {
                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                KLog.e(TAG, "下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            KLog.e(TAG, "下载暂停...");


                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            KLog.e(TAG, "下载失败...");


                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                            KLog.e(TAG, "下载完成...");


                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
                            KLog.e(TAG, "安装完成...");


                        }
                    });
                }
            }
        }, Contants.SPLASH_TO_MAIN_TIME);
    }


}
