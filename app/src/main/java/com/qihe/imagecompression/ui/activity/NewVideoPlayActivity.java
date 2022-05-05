package com.qihe.imagecompression.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
//import com.devlin_n.floatWindowPermission.FloatWindowManager;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.databinding.ActivityNewVideoPlayBinding;
import com.qihe.imagecompression.util.FileUtil;
import com.qihe.imagecompression.util.PIPManager;
import com.qihe.imagecompression.view.ListIjkVideoView;
import com.qihe.imagecompression.view.StandardVideoController;
import com.qihe.imagecompression.view.VideoControllerInterface;
import com.qihe.imagecompression.viewmodel.FeaturesViewModel;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.util.ACStatusBarUtil;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import java.io.File;


/**
 * 视频播放界面
 * Created by lipei on 2018/10/16.
 */
@Route(path = ArouterPath.new_video_play_activity)
public class NewVideoPlayActivity extends BaseActivity<ActivityNewVideoPlayBinding, FeaturesViewModel> {
    @Autowired
    String chosePath;

    private Handler handler=new Handler();


    private String path;

    private String name;

    private PIPManager mPIPManager;

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public void initData() {
        ARouter.getInstance().inject(this);
        path=chosePath;
        File file=new File(path);
        if(file!=null){
            name=file.getName();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showCommentFromFeatureDialog("觉得功能不错的话给个好评鼓励一下吧!");

            }
        },500);




        viewModel.titleString.set(name);
        mPIPManager = PIPManager.getInstance();
        final ListIjkVideoView ijkVideoView = mPIPManager.getIjkVideoView();

        StandardVideoController controller = new StandardVideoController(this);

        controller.setVideoControllerInterface(new VideoControllerInterface() {
            @Override
            public void playNext() {
                ijkVideoView.playToNext();

            }

            @Override
            public void playLast() {
                ijkVideoView.skipToLast();
            }

            @Override
            public void floatingWindow() {
               /* if (FloatWindowManager.getInstance().checkPermission(NewVideoPlayActivity.this)) {
                    showStimulateNeedUserNumberAd(false);


                } else {
                    FloatWindowManager.getInstance().applyPermission(NewVideoPlayActivity.this);

                }*/

            }

            @Override
            public void videoSize(int videoType) {
                if (videoType == 0) {
                    //全屏尺寸
                    ijkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_ORIGINAL);



                } else if (videoType == 1) {
                    //16:9
                    ijkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_16_9);


                } else if (videoType == 2) {
                    //4:3
                    ijkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_4_3);


                } else if (videoType == 3) {
                    //原始尺寸
                    ijkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_DEFAULT);


                }
            }

            @Override
            public void playOrder(int type) {
                ijkVideoView.setPlayOrderType(type);

            }

            @Override
            public void playSudu(float view) {
//                ijkVideoView.setPlaySudu(view);
                ijkVideoView.setSpeed(view);

            }
        });

        ijkVideoView.setVideoController(controller);


//        StandardVideoController controller = new StandardVideoController(this);
//        controller.setLive();
//        ijkVideoView.setVideoController(controller);
        if (mPIPManager.isStartFloatWindow()) {
            mPIPManager.stopFloatWindow();
            controller.setPlayerState(ijkVideoView.getCurrentPlayerState());
            controller.setPlayState(ijkVideoView.getCurrentPlayState());
        } else {
            mPIPManager.setActClass(NewVideoPlayActivity.class);


            ijkVideoView.setPlayerConfig(new PlayerConfig.Builder()
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


            ijkVideoView.setUrl(FileUtil.StringToUriToString(path));

            ijkVideoView.setTitle(name);
//            ijkVideoView.setPlaySudu(2.0f);

            ijkVideoView.start();

        }


        binding.player.addView(ijkVideoView);


    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_new_video_play;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
    }



    @Override
    protected void onPause() {
        super.onPause();
        mPIPManager.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPIPManager.resume();
    }

    @Override
    public void onStimulateSuccessCall() {
        ToastUtils.show("使用次数领取成功");


    }

    @Override
    public void onStimulateSuccessDissmissCall() {

        mPIPManager.startFloatWindow();
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPIPManager.reset();
        handler.removeCallbacksAndMessages(null);
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
        return false;
    }


    @Override
    public void onBackPressed() {
        if (mPIPManager.onBackPress()) return;
        super.onBackPressed();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == FloatWindowManager.PERMISSION_REQUEST_CODE) {
            if (FloatWindowManager.getInstance().checkPermission(this)) {
                mPIPManager.startFloatWindow();
                finish();
            } else {
                ToastUtils.show("权限授予失败，无法开启悬浮窗");
            }
        }*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        int orientation = newConfig.orientation;

        KLog.e("orientation--->",orientation);
        if (orientation == 2) {
            //横屏
            viewModel.isShowTab.set(false);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            ACStatusBarUtil.StatusBarLightMode(this); //设置白底黑字

        } else {
            //竖屏
            viewModel.isShowTab.set(true);
            ACStatusBarUtil.StatusBarLightMode(this); //设置白底黑字

//            getWindow().clearFlags(
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

    }


}
