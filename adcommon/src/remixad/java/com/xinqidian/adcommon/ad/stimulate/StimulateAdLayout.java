package com.xinqidian.adcommon.ad.stimulate;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;
import com.xinqidian.adcommon.TTAdManagerHolder;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.KLog;

/**
 * Created by lipei on 2020/5/13.
 */

public class StimulateAdLayout implements RewardVideoADListener {
    private static final String TAG = "StimulateAdLayout";
    private Context context;
    private StimulateAdInterface stimulateAdInterface;

    private RewardVideoAD rewardVideoAD;
    private boolean adLoaded;//广告加载成功标志
    private boolean videoCached;//视频素材文件下载完成标志


    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mHasShowDownloadActive = false;
    private boolean isTencun;



    public StimulateAdLayout(Context context, StimulateAdInterface stimulateAdInterface) {
        this.context = context;
        this.stimulateAdInterface = stimulateAdInterface;
        mTTAdNative = TTAdManagerHolder.get().createAdNative(context);


    }


    public void loadAd() {

        UserUtil.getKey(Contants.videoparsemusicStimulateAd, new UserUtil.KeyInterFace() {
            @Override
            public void onSuccess(int number) {
                if (number == -1) {
                    isTencun=true;
                    rewardVideoAD = new RewardVideoAD(context, Contants.TENCENT_APPID, Contants.TENCENT_STIMULATE_ID, StimulateAdLayout.this, true);
                    adLoaded = false;
                    videoCached = false;
                    // 2. 加载激励视频广告
                    rewardVideoAD.loadAD();

                } else if (number == -2) {
                    isTencun=false;

                    loadChuanShanJiaAd();



                } else {
                    if (number < 3) {
                        isTencun=false;

                        loadChuanShanJiaAd();
                        number += 1;

                        UserUtil.setKey(Contants.videoparsemusicStimulateAd, number);


                    } else {
                        isTencun=true;

                        rewardVideoAD = new RewardVideoAD(context, Contants.TENCENT_APPID, Contants.TENCENT_STIMULATE_ID, StimulateAdLayout.this, true);
                        adLoaded = false;
                        videoCached = false;
                        // 2. 加载激励视频广告
                        rewardVideoAD.loadAD();


                        number += 1;
                        if (number == 5) {
                            UserUtil.setKey(Contants.videoparsemusicStimulateAd, 0);

                        } else {
                            UserUtil.setKey(Contants.videoparsemusicStimulateAd, number);

                        }

                    }
                }


            }

            @Override
            public void onFail() {
                isTencun=false;

                loadChuanShanJiaAd();

            }
        });


        rewardVideoAD = new RewardVideoAD(context, Contants.TENCENT_APPID, Contants.TENCENT_STIMULATE_ID, this, true);
        adLoaded = false;
        videoCached = false;
        // 2. 加载激励视频广告
        rewardVideoAD.loadAD();
//        loadChuanShanJiaAd();
    }


    public void showAd() {
//        showChuannShanJiaAd();
//        if (adLoaded && rewardVideoAD != null) {//广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
//            if (!rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
//                long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
//                //广告展示检查3：展示广告前判断广告数据未过期
//                if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
//                    rewardVideoAD.showAD();
//                } else {
//                    Log.d(TAG,"激励视频广告已过期，请再次请求广告后进行广告展示！");
//                    if(stimulateAdInterface!=null){
//                        stimulateAdInterface.onFail();
//                    }
//                }
//            } else {
//                Log.d(TAG,"此条广告已经展示过，请再次请求广告后进行广告展示！");
//                if(stimulateAdInterface!=null){
//                    stimulateAdInterface.onFail();
//                }
//
//            }
//        } else {
//            if(stimulateAdInterface!=null){
//                stimulateAdInterface.onFail();
//            }
//            Log.d(TAG,"成功加载广告后再进行广告展示！");
//
//        }

        if(isTencun){
            if (adLoaded && rewardVideoAD != null) {//广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
                if (!rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
                    long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
                    //广告展示检查3：展示广告前判断广告数据未过期
                    if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
                        rewardVideoAD.showAD();
                    } else {
                        Log.d(TAG,"激励视频广告已过期，请再次请求广告后进行广告展示！");
                        if(stimulateAdInterface!=null){
                            stimulateAdInterface.onFail();
                        }
                    }
                } else {
                    Log.d(TAG,"此条广告已经展示过，请再次请求广告后进行广告展示！");
                    if(stimulateAdInterface!=null){
                        stimulateAdInterface.onFail();
                    }

                }
            } else {
                if(stimulateAdInterface!=null){
                    stimulateAdInterface.onFail();
                }
                Log.d(TAG,"成功加载广告后再进行广告展示！");

            }

        }else {
            showChuannShanJiaAd();

        }

    }

    public void destoryAdView() {

    }

    /**
     * 广告加载成功，可在此回调后进行广告展示
     **/
    @Override
    public void onADLoad() {
        adLoaded = true;
//        String msg = "load ad success ! expireTime = " + new Date(System.currentTimeMillis() +
//                rewardVideoAD.getExpireTimestamp() - SystemClock.elapsedRealtime());
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//        Log.d(TAG, "eCPM = " + rewardVideoAD.getECPM() + " , eCPMLevel = " + rewardVideoAD.getECPMLevel());
    }

    /**
     * 视频素材缓存成功，可在此回调后进行广告展示
     */
    @Override
    public void onVideoCached() {
        videoCached = true;
        Log.i(TAG, "onVideoCached");
    }

    /**
     * 激励视频广告页面展示
     */
    @Override
    public void onADShow() {
        Log.i(TAG, "onADShow");
    }

    /**
     * 激励视频广告曝光
     */
    @Override
    public void onADExpose() {
        Log.i(TAG, "onADExpose");
    }

    /**
     * 激励视频触发激励（观看视频大于一定时长或者视频播放完毕）
     */
    @Override
    public void onReward() {
        Log.i(TAG, "onReward");
    }

    /**
     * 激励视频广告被点击
     */
    @Override
    public void onADClick() {
//        Map<String, String> map = rewardVideoAD.getExts();
//        String clickUrl = map.get("clickUrl");
//        Log.i(TAG, "onADClick clickUrl: " + clickUrl);
    }

    /**
     * 激励视频播放完毕
     */
    @Override
    public void onVideoComplete() {
        Log.i(TAG, "onVideoComplete");
        stimulateAdInterface.onStimulateAdSuccess();
    }

    /**
     * 激励视频广告被关闭
     */
    @Override
    public void onADClose() {
        Log.i(TAG, "onADClose");
            stimulateAdInterface.onStimulateAdDismissed();


    }

    /**
     * 广告流程出错
     */
    @Override
    public void onError(AdError adError) {
        Log.e(TAG,adError.getErrorMsg());
//        String msg = String.format(Locale.getDefault(), "onError, error code: %d, error msg: %s",
//                adError.getErrorCode(), adError.getErrorMsg());
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }




    public void loadChuanShanJiaAd() {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档

        //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(Contants.CHUANSHANJIA_STIMULATE_ID)
                .setSupportDeepLink(true)
                .setRewardName("奖励次数领取成功") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(TTAdConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();


        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                KLog.e(TAG, "Callback --> onError: " + code + ", " + String.valueOf(message));
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                KLog.e(TAG, "Callback --> onRewardVideoCached");
            }

            @Override
            public void onRewardVideoCached(TTRewardVideoAd ttRewardVideoAd) {

            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                KLog.e(TAG, "Callback --> onRewardVideoAdLoad");

                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        KLog.d(TAG, "Callback --> rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        KLog.d(TAG, "Callback --> rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        KLog.d(TAG, "Callback --> rewardVideoAd close");
                        if(stimulateAdInterface!=null){
                            stimulateAdInterface.onStimulateAdDismissed();

                        }

                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        KLog.d(TAG, "Callback --> rewardVideoAd complete");
                        if(stimulateAdInterface!=null){
                            stimulateAdInterface.onStimulateAdSuccess();

                        }

                    }

                    @Override
                    public void onVideoError() {
                        KLog.e(TAG, "Callback --> rewardVideoAd error");
                        if(stimulateAdInterface!=null){
                            stimulateAdInterface.onFail();
                        }
                    }

                    @Override
                    public void onRewardVerify(boolean b, int i, String s, int i1, String s1) {

                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
//                    @Override
//                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
//                        String logString = "verify:" + rewardVerify + " amount:" + rewardAmount +
//                                " name:" + rewardName;
//                        KLog.e(TAG, "Callback --> " + logString);
//
//                    }

                    @Override
                    public void onSkippedVideo() {
                        KLog.e(TAG, "Callback --> rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        KLog.d("DML", "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);

                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        KLog.d("DML", "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        KLog.d("DML", "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        KLog.d("DML", "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        KLog.d("DML", "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
                    }
                });
            }
        });

        //step5:请求广告


    }





    public void showChuannShanJiaAd() {
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示,强烈建议在onRewardVideoCached回调后，展示广告，提升播放体验
            //该方法直接展示广告
            mttRewardVideoAd.showRewardVideoAd((Activity) context);

            //展示广告，并传入广告展示的场景
//            mttRewardVideoAd.showRewardVideoAd((Activity) context, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
//            mttRewardVideoAd = null;
        } else {
            stimulateAdInterface.onFail();

        }

    }


}
