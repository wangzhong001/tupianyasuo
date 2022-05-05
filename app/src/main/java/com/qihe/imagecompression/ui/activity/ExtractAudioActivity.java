package com.qihe.imagecompression.ui.activity;

import android.arch.lifecycle.Observer;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
//import com.devlin_n.floatWindowPermission.FloatWindowManager;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.databinding.ActivityExtractAudioBinding;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FFmpegUtils;
import com.qihe.imagecompression.util.FileUtil;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.view.ChoseDataDialog;
import com.qihe.imagecompression.view.InputDialog;
import com.qihe.imagecompression.view.LoadingDialog;
import com.qihe.imagecompression.view.StandardVideoController;
import com.qihe.imagecompression.view.VideoControllerInterface;
import com.qihe.imagecompression.viewmodel.FeaturesViewModel;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.ACStatusBarUtil;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.SureDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.microshow.rxffmpeg.RxFFmpegCommandList;
import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;

/**
 * Created by lipei on 2020/6/14.
 */
@Route(path = ArouterPath.extract_audio_activity)
public class ExtractAudioActivity extends BaseActivity<ActivityExtractAudioBinding, FeaturesViewModel> {
    @Autowired
    String chosePath;

    private int fadeInTime;
    private int fadeOutTime;

    private LoadingDialog loadingDialog;

    private String resultPath = FileUtils.AUDIO_FILE_PATH;

    private String fileName = null;

    private String fianlyName;

    private String contentName;

    private InputDialog inputDialog;

    private String path;

    private String name;

    private int number;

    private SureDialog sureDialog;
//    private PIPManager mPIPManager;


    private MyRxFFmpegSubscriber myRxFFmpegSubscriber;

    private NewMyRxFFmpegSubscriber newMyRxFFmpegSubscriber;

    private ChoseDataDialog formatData, samplingRateData, bitRateData, fadeInData, fadeOutData;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_extract_audio;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
    }

    @Override
    public void onStimulateSuccessCall() {
        ToastUtils.show("奖励领取成功");

    }

    @Override
    public void onStimulateSuccessDissmissCall() {
        number=1;

        SharedPreferencesUtil.setParam(Contants.SHOW_STIMULATE_NUMBER_STRING,1);

    }

    @Override
    public void initData() {
        super.initData();
        number= (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING,1);
        loadingDialog = new LoadingDialog(this);
        viewModel.formatString.set("AAC");
        ARouter.getInstance().inject(this);
//        binding.videoView.setController(new RxFFmpegPlayerControllerImpl(this), MeasureHelper.FitModel.FM_DEFAULT);
//        binding.videoView.play(chosePath, false);

        path=chosePath;
        File file=new File(path);
        if(file!=null){
            name=file.getName();
        }


//        binding.videoView.setController(new RxFFmpegPlayerControllerImpl(this), MeasureHelper.FitModel.FM_DEFAULT);
//        binding.videoView.play(chosePath, false);

//        mPIPManager = PIPManager.getInstance();
//        final ListIjkVideoView ijkVideoView = mPIPManager.getIjkVideoView();

        StandardVideoController controller = new StandardVideoController(this);

        controller.setVideoControllerInterface(new VideoControllerInterface() {
            @Override
            public void playNext() {
//                ijkVideoView.playToNext();

            }

            @Override
            public void playLast() {
//                ijkVideoView.skipToLast();
            }

            @Override
            public void floatingWindow() {
                /*if (FloatWindowManager.getInstance().checkPermission(ExtractAudioActivity.this)) {
                    showStimulateNeedUserNumberAd(false);


                } else {
                    FloatWindowManager.getInstance().applyPermission(ExtractAudioActivity.this);

                }*/

            }

            @Override
            public void videoSize(int videoType) {
                if (videoType == 0) {
                    //全屏尺寸
//                    ijkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_ORIGINAL);



                } else if (videoType == 1) {
                    //16:9
//                    ijkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_16_9);


                } else if (videoType == 2) {
                    //4:3
//                    ijkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_4_3);


                } else if (videoType == 3) {
                    //原始尺寸
//                    ijkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_DEFAULT);


                }
            }

            @Override
            public void playOrder(int type) {
//                ijkVideoView.setPlayOrderType(type);

            }

            @Override
            public void playSudu(float view) {
//                ijkVideoView.setPlaySudu(view);
//                ijkVideoView.setSpeed(view);

            }
        });

        binding.player.setVideoController(controller);


//        StandardVideoController controller = new StandardVideoController(this);
////        controller.setLive();
////        ijkVideoView.setVideoController(controller);
//        if (mPIPManager.isStartFloatWindow()) {
//            mPIPManager.stopFloatWindow();
//            controller.setPlayerState(ijkVideoView.getCurrentPlayerState());
//            controller.setPlayState(ijkVideoView.getCurrentPlayState());
//        } else {
//            mPIPManager.setActClass(VideoPlayActivity.class);
//
//
//            ijkVideoView.setPlayerConfig(new PlayerConfig.Builder()
//                    .autoRotate()//自动旋转屏幕
////                    .enableCache()//启用边播边存
////                .enableMediaCodec()//启动硬解码
//                    .usingSurfaceView()//使用SurfaceView
//                    //ijkplayer不支持assets播放,需切换成MediaPlayer
////                .setCustomMediaPlayer(new AndroidMediaPlayer(VideoPlayActivity.this))
////                    .setCustomMediaPlayer(new AndroidMediaPlayer(this))
//                    .build());
            //播放assets文件
//        AssetManager am = getResources().getAssets();
//        AssetFileDescriptor afd = null;
//        try {
//            afd = am.openFd("test.mp4");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        binding.player.setAssetFileDescriptor(afd);

            //播放raw
//        String url = "android.resource://" + getPackageName() + "/" + R.raw.movie;

//
//            binding.player.setUrl(FileUtil.StringToUriToString(path));
//
//            binding.player.setTitle(name);
////            ijkVideoView.setPlaySudu(2.0f);
//
//            binding.player.start();

//        }


        binding.player.setPlayerConfig(new PlayerConfig.Builder()
                .autoRotate()//自动旋转屏幕
//                    .enableCache()//启用边播边存
//                .enableMediaCodec()//启动硬解码
                .usingSurfaceView()//使用SurfaceView
                //ijkplayer不支持assets播放,需切换成MediaPlayer
//                .setCustomMediaPlayer(new AndroidMediaPlayer(VideoPlayActivity.this))
//                    .setCustomMediaPlayer(new AndroidMediaPlayer(this))
                .build());
        //播放assets文件
//        AssetManager am = getResources().getAssets();
//        AssetFileDescriptor afd = null;
//        try {
//            afd = am.openFd("test.mp4");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        binding.player.setAssetFileDescriptor(afd);

        //播放raw
//        String url = "android.resource://" + getPackageName() + "/" + R.raw.movie;


        binding.player.setUrl(FileUtil.StringToUriToString(path));

        binding.player.setTitle(name);
//            ijkVideoView.setPlaySudu(2.0f);

        binding.player.start();

//        binding.player.addView(ijkVideoView);
    }


    @Override
    public void onResume() {
        super.onResume();
        //恢复播放
        binding.player.resume();
//        mPIPManager.resume();
    }


    @Override
    public void onPause() {
        super.onPause();
        //暂停视频
        binding.player.pause();

//        mPIPManager.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁播放器
//        mPIPManager.reset();

        binding.player.release();

        if (myRxFFmpegSubscriber != null) {
            myRxFFmpegSubscriber.dispose();
        }


    }

    @Override
    public boolean isShowBannerAd() {
        return (boolean) SharedPreferencesUtil.getParam(AppConfig.musicEractBannerAd,false);
    }

    @Override
    public boolean isShowNativeAd() {
        return false;
    }

    @Override
    public boolean isShowVerticllAndStimulateAd() {
        return true;
    }


    @Override
    public void initViewObservable() {
        super.initViewObservable();

        /**
         * 选取格式
         */
        viewModel.choseFormatLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable final FeaturesViewModel featuresViewModel) {
                if (formatData == null) {
                    formatData = new ChoseDataDialog(ExtractAudioActivity.this, getFormat()).addItemListener(new ChoseDataDialog.ItemListener() {
                        @Override
                        public void onClickChose(String coinName, int coinNamePostion) {

                            featuresViewModel.formatString.set(coinName);

                        }
                    });
                }
                formatData.show();
            }
        });


        /**
         * 采样率
         */
        viewModel.samplingRateLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable final FeaturesViewModel featuresViewModel) {
                if (samplingRateData == null) {
                    samplingRateData = new ChoseDataDialog(ExtractAudioActivity.this, getSamplingRate()).addItemListener(new ChoseDataDialog.ItemListener() {
                        @Override
                        public void onClickChose(String coinName, int coinNamePostion) {

                            featuresViewModel.samplingRateString.set(coinName );

                        }
                    });
                }
                samplingRateData.show();
            }
        });


        /**
         * 比特率
         */
        viewModel.bitRateLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable final FeaturesViewModel featuresViewModel) {
                if (bitRateData == null) {
                    bitRateData = new ChoseDataDialog(ExtractAudioActivity.this, getBitRate()).addItemListener(new ChoseDataDialog.ItemListener() {
                        @Override
                        public void onClickChose(String coinName, int coinNamePostion) {

                            featuresViewModel.bitRateString.set(coinName );

                        }
                    });
                }
                bitRateData.show();
            }
        });


        /**
         * 淡入
         */
        viewModel.fadeInLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable final FeaturesViewModel featuresViewModel) {
                if (fadeInData == null) {
                    fadeInData = new ChoseDataDialog(ExtractAudioActivity.this, getFadeIn()).addItemListener(new ChoseDataDialog.ItemListener() {
                        @Override
                        public void onClickChose(String coinName, int coinNamePostion) {

                            featuresViewModel.fadeInString.set(coinName);
                            fadeInTime = coinNamePostion;

                        }
                    });
                }
                fadeInData.show();
            }
        });


        /**
         * 淡出
         */
        viewModel.fadeOutLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable final FeaturesViewModel featuresViewModel) {
                if (fadeOutData == null) {
                    fadeOutData = new ChoseDataDialog(ExtractAudioActivity.this, getFadeIn()).addItemListener(new ChoseDataDialog.ItemListener() {
                        @Override
                        public void onClickChose(String coinName, int coinNamePostion) {

                            featuresViewModel.fadeOutString.set(coinName);
                            fadeOutTime = coinNamePostion;

                        }
                    });
                }
                fadeOutData.show();
            }
        });

        viewModel.startLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable FeaturesViewModel featuresViewModel) {

                int number = (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING, 1);

                KLog.e("number---->",number);

                if(!SharedPreferencesUtil.isVip()){
                    if(number<=0){
                        boolean isCanSeeAd= (boolean) SharedPreferencesUtil.getParam(AppConfig.musicEractCanSeeVideo,true);
                        if(sureDialog==null){

                            if(isCanSeeAd){
                                //可以观看视频
                                sureDialog=new SureDialog(ExtractAudioActivity.this,"您的免费使用次数已用完,您可以成为高级用户无限次使用或者观看视频领取奖励","观看视频","成为会员","会员订阅")
                                        .addItemListener(new SureDialog.ItemListener() {
                                            @Override
                                            public void onClickSure() {
                                                ActivityUtil.start(ArouterPath.vip_activity);

                                            }

                                            @Override
                                            public void onClickCanel() {
                                                showStimulateAd();


                                            }
                                        });
                            }else {
                                //无法观看视频
                                sureDialog=new SureDialog(ExtractAudioActivity.this,"您的免费使用次数已用完,您可以成为高级用户无限次使用","取消","成为会员","会员订阅")
                                        .addItemListener(new SureDialog.ItemListener() {
                                            @Override
                                            public void onClickSure() {
                                                ActivityUtil.start(ArouterPath.vip_activity);

                                            }

                                            @Override
                                            public void onClickCanel() {
//                                                showStimulateAd();


                                            }
                                        });
                            }

                        }

                        sureDialog.show();
                        return;
                    }
                }

                if (inputDialog == null) {
                    inputDialog = new InputDialog(ExtractAudioActivity.this, "")
                            .addItemListener(new InputDialog.ItemListener() {
                                @Override
                                public void onClickSure(String content) {
                                    contentName = content;

                                    runFFmpegRxJava(content);
                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                }

                inputDialog.show();
            }
        });


    }


    /**
     * 获取格式
     *
     * @return
     */
    public List<String> getFormat() {
        List<String> mList = new ArrayList<>();
        String titles[] = getResources().getStringArray(R.array.audio_format_title);
        for (int i = 0; i < titles.length; i++) {
            mList.add(titles[i]);
        }

        return mList;
    }


    /**
     * 采样率
     *
     * @return
     */
    public List<String> getSamplingRate() {
        List<String> mList = new ArrayList<>();
        String titles[] = getResources().getStringArray(R.array.sampling_rate_title);
        for (int i = 0; i < titles.length; i++) {
            mList.add(titles[i]);
        }

        return mList;
    }


    /**
     * 比特率
     *
     * @return
     */
    public List<String> getBitRate() {
        List<String> mList = new ArrayList<>();
        String titles[] = getResources().getStringArray(R.array.bit_ate_title);
        for (int i = 0; i < titles.length; i++) {
            mList.add(titles[i]);
        }

        return mList;
    }


    /**
     * 比特率
     *
     * @return
     */
    public List<String> getFadeIn() {
        List<String> mList = new ArrayList<>();
        String titles[] = getResources().getStringArray(R.array.fade_in_title);
        for (int i = 0; i < titles.length; i++) {
            mList.add(titles[i]);
        }

        return mList;
    }


    /**
     * rxjava方式调用
     */
    private void runFFmpegRxJava(String name) {
        loadingDialog.show();

        /** 提取视频里面的音频**/
        fileName = resultPath + name + ".AAC";
        String[] commands = getBoxblur(chosePath, fileName);


        myRxFFmpegSubscriber = new MyRxFFmpegSubscriber(this);

        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
                .runCommandRxJava(commands)
                .subscribe(myRxFFmpegSubscriber);

    }


    /**
     * rxjava方式调用
     */
    private void runNewFFmpegRxJava(String name) {
        loadingDialog.show();


        fianlyName = resultPath + name + "." + viewModel.formatString.get();

//        String[] commands = getNewBoxblur(fileName, fianlyName);
        KLog.e("ty-->",viewModel.bitRateString.get()+"--->"+viewModel.formatString.get()+"--->"+fileName);

        String ff="ffmpeg -y -i "+fileName+" -ar "+viewModel.samplingRateString.get()+" -ac 2 -ab "+viewModel.bitRateString.get()+"k"+" -vol 50 -f M4A /storage/emulated/0/AudioClip/Audio/m4a.M4A";

        String[] commands = FFmpegUtils.transformAudio(fileName,fianlyName) ;
//        ff.split(" ");

        newMyRxFFmpegSubscriber = new NewMyRxFFmpegSubscriber(this);

        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
                .runCommandRxJava(commands)
                .subscribe(newMyRxFFmpegSubscriber);

    }

    private boolean isToExtract;


    // 这里设为静态内部类，防止内存泄露
    public static class MyRxFFmpegSubscriber extends RxFFmpegSubscriber {

        private WeakReference<ExtractAudioActivity> mWeakReference;

        public MyRxFFmpegSubscriber(ExtractAudioActivity homeFragment) {
            mWeakReference = new WeakReference<>(homeFragment);
        }

        @Override
        public void onFinish() {
            final ExtractAudioActivity mHomeFragment = mWeakReference.get();
            mHomeFragment.isToExtract = true;

//            if(mHomeFragment.isToExtract){
//                mHomeFragment.runFFmpegRxJava(mHomeFragment.contentName);
//                mHomeFragment.isToExtract=false;
//            }else {
            if (mHomeFragment != null) {
                mHomeFragment.loadingDialog.dismiss();
            }

            if(mHomeFragment.viewModel.formatString.get().equalsIgnoreCase("AAC")){
                if(!SharedPreferencesUtil.isVip()){
                    mHomeFragment.number-=1;
                    SharedPreferencesUtil.setParam(Contants.SHOW_STIMULATE_NUMBER_STRING,mHomeFragment.number);

                }

                LiveDataBus.get().with(LiveDataBusData.updateMineFile,String.class).postValue(mHomeFragment.fileName);
                LiveDataBus.get().with(LiveDataBusData.backHome,String.class).postValue(LiveDataBusData.backHome);


                ActivityUtil.start(ArouterPath.audition_activity, LiveDataBusData.chosePath, mHomeFragment.fileName);
                mHomeFragment.finish();
            }else {
                mHomeFragment.runNewFFmpegRxJava(mHomeFragment.contentName);

            }




//            }


        }

        @Override
        public void onProgress(int progress, long progressTime) {
            final ExtractAudioActivity mHomeFragment = mWeakReference.get();
            if (mHomeFragment != null) {
                if (progress > 0) {
                    LiveDataBus.get().with(LiveDataBusData.time, String.class).postValue(progress + "%");

                }


                KLog.e("time--->", progress + "--->" + progressTime);

                //progressTime 可以在结合视频总时长去计算合适的进度值
//                mHomeFragment.setProgressDialog(progress, progressTime);
            }
        }

        @Override
        public void onCancel() {
            final ExtractAudioActivity mHomeFragment = mWeakReference.get();
            if (mHomeFragment != null) {
                mHomeFragment.loadingDialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            final ExtractAudioActivity mHomeFragment = mWeakReference.get();
            ToastUtils.show(message);
            if (mHomeFragment != null) {
                mHomeFragment.loadingDialog.dismiss();
            }
        }
    }


    // 这里设为静态内部类，防止内存泄露
    public static class NewMyRxFFmpegSubscriber extends RxFFmpegSubscriber {

        private WeakReference<ExtractAudioActivity> mWeakReference;

        public NewMyRxFFmpegSubscriber(ExtractAudioActivity homeFragment) {
            mWeakReference = new WeakReference<>(homeFragment);
        }

        @Override
        public void onFinish() {
            final ExtractAudioActivity mHomeFragment = mWeakReference.get();

//            if(mHomeFragment.isToExtract){
//                mHomeFragment.runFFmpegRxJava(mHomeFragment.contentName);
//                mHomeFragment.isToExtract=false;
//            }else {
            if (mHomeFragment != null) {
                mHomeFragment.loadingDialog.dismiss();
            }
            if(!mHomeFragment.fianlyName.contains("AAC")){
                FileUtils.deleteFile(mHomeFragment.fileName);

            }
            LiveDataBus.get().with(LiveDataBusData.updateMineFile,String.class).postValue(mHomeFragment.fianlyName);
            if(!SharedPreferencesUtil.isVip()){
                mHomeFragment.number-=1;
                SharedPreferencesUtil.setParam(Contants.SHOW_STIMULATE_NUMBER_STRING,mHomeFragment.number);

            }

            LiveDataBus.get().with(LiveDataBusData.backHome,String.class).postValue(LiveDataBusData.backHome);

            ActivityUtil.start(ArouterPath.audition_activity, LiveDataBusData.chosePath, mHomeFragment.fianlyName);
            mHomeFragment.finish();

//            }


        }

        @Override
        public void onProgress(int progress, long progressTime) {
            final ExtractAudioActivity mHomeFragment = mWeakReference.get();
            if (mHomeFragment != null) {
                if (progress > 0) {
                    LiveDataBus.get().with(LiveDataBusData.time, String.class).postValue(progress + "%");

                }


                KLog.e("time--->", progress + "--->" + progressTime);

                //progressTime 可以在结合视频总时长去计算合适的进度值
//                mHomeFragment.setProgressDialog(progress, progressTime);
            }
        }

        @Override
        public void onCancel() {
            final ExtractAudioActivity mHomeFragment = mWeakReference.get();
            if (mHomeFragment != null) {
                mHomeFragment.loadingDialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            final ExtractAudioActivity mHomeFragment = mWeakReference.get();
            ToastUtils.show(message);
            if (mHomeFragment != null) {
                mHomeFragment.loadingDialog.dismiss();
            }
        }
    }


    public String[] getBoxblur(String srcFile, String targetFile) {
        RxFFmpegCommandList cmdlist = new RxFFmpegCommandList();
        cmdlist.append("-i");
        cmdlist.append(srcFile);
        cmdlist.append("-acodec");
        cmdlist.append("copy");
        cmdlist.append("-vn");
        cmdlist.append(targetFile);

        return cmdlist.build();
    }


    public String[] getNewBoxblur(String srcFile, String targetFile) {
        RxFFmpegCommandList cmdlist = new RxFFmpegCommandList();
        cmdlist.append("-i");
        cmdlist.append(srcFile);
        cmdlist.append("-ar");
        cmdlist.append(viewModel.samplingRateString.get());
        cmdlist.append("-ac");
        cmdlist.append("2");
        cmdlist.append("-ab");
        cmdlist.append(viewModel.bitRateString.get() + "k");
        cmdlist.append("-vol");
        cmdlist.append("60");
        cmdlist.add("-f");
        cmdlist.add(viewModel.formatString.get());
        cmdlist.append(targetFile);

        return cmdlist.build();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        int orientation = newConfig.orientation;

        KLog.e("orientation--->",orientation);
        if (orientation == 2) {
            //横屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            ACStatusBarUtil.StatusBarLightMode(this); //设置白底黑字

        } else {
            //竖屏
            ACStatusBarUtil.StatusBarLightMode(this); //设置白底黑字

//            getWindow().clearFlags(
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

    }


    @Override
    public void onStimulateFallCall() {
        super.onStimulateFallCall();
        ToastUtils.show("请稍后再来领取奖励吧,成为高级用户可以无限次使用哦");
        ActivityUtil.start(ArouterPath.vip_activity);

    }
}
