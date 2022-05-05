package com.qihe.imagecompression.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.arch.lifecycle.Observer;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.databinding.AcctivityAuditionBinding;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.viewmodel.AudioUpdateViewModel;
import com.xinqidian.adcommon.BR;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

/**
 * Created by lipei on 2020/6/1.
 * 试听
 */
@Route(path = ArouterPath.audition_activity)
public class AuditionActivity extends BaseActivity<AcctivityAuditionBinding, AudioUpdateViewModel> implements MediaPlayer.OnCompletionListener, Runnable, SeekBar.OnSeekBarChangeListener {

    private ObjectAnimator discAnimation;//自定义指针和唱盘

    private boolean running;

    @Autowired
    String chosePath;

    private Thread thread;

    private MediaPlayer mMediaPlayer;

    private Handler handler=new Handler();


    private boolean isPlaying = false;//0,1 判断是否处于播放状态


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.acctivity_audition;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
    }

    @Override
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }


    @Override
    public void initData() {
        super.initData();
        ARouter.getInstance().inject(this);
        showCommentFromFeatureDialog("觉得功能不错的话给个好评鼓励一下吧!");


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showVerticalCommentInterstitialAd();

            }
        },1000);
        if (chosePath != null) {
            File file = new File(chosePath);
            if (file != null) {
                viewModel.fileName.set(file.getName());
                viewModel.filePath.set(FileUtils.NEW_AUDIO_FILE_PATH+file.getName());

            }
        }
        initPlay(chosePath);
        thread = new Thread(this);
        thread.start();

        setAnimations();
        binding.seekBar.setOnSeekBarChangeListener(this);
    }


    //动画设置
    private void setAnimations() {
        discAnimation = ObjectAnimator.ofFloat(binding.iv, "rotation", 0, 360);
        discAnimation.setDuration(2000);
        discAnimation.setInterpolator(new LinearInterpolator());
        discAnimation.setRepeatCount(ValueAnimator.INFINITE);
        discAnimation.start();


    }

//    private void playing() {
//        needleAnimation.start();
//        discAnimation.start();
//        playingPlay.setImageResource(R.drawable.music_pause);
//        mMusicController.play();//播放
//        isPlaying = false;
//    }


    private void initPlay(String path) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            // 绑定播放完毕监听器
            mMediaPlayer.setOnCompletionListener(this);
        }
        try {
            // 切歌之前先重置，释放掉之前的资源
            mMediaPlayer.reset();
            // 设置播放源
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("播放错误");
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        KLog.e("end--->", "end");
        viewModel.isPlayFinish.set(true);
//        running = false;
        viewModel.isPlay.set(false);
        stopAnimation();
    }

    private void stopAnimation() {
        if (discAnimation != null) {
            discAnimation.reverse();
            discAnimation.end();

        }
    }

    private void startAnimation() {
        if (discAnimation != null) {
            discAnimation.start();


        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.startOrStopLiveData.observe(this, new Observer<AudioUpdateViewModel>() {
            @Override
            public void onChanged(@Nullable AudioUpdateViewModel audioUpdateViewModel) {
                if (audioUpdateViewModel.isPlay.get()) {
                    //暂停
//                    running = false;


                    mMediaPlayer.pause();
                    stopAnimation();

                }else {
////                    if(viewModel.isPlayFinish.get()){
//                        mMediaPlayer.start();
////                    }else {
////                        viewModel.isPlayFinish.set(false);
////                        mMediaPlayer.reset();
//                    running = true;

                    startAnimation();
                    mMediaPlayer.start();
////                    }
                }
//
                audioUpdateViewModel.isPlay.set(!audioUpdateViewModel.isPlay.get());
            }
        });

        viewModel.shareLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                new Share2.Builder(AuditionActivity.this)
                        .setContentType(ShareContentType.FILE)
                        .setShareFileUri(FileUtil.getFileUri(AuditionActivity.this, ShareContentType.FILE, new File( chosePath)))
                        .build()
                        .shareBySystem();
            }
        });
    }

    @Override
    public void run() {
        running = true;
        try {
            while (running) {
                if (mMediaPlayer != null) {
                    long musicDuration = mMediaPlayer.getDuration();
                    final long position = mMediaPlayer.getCurrentPosition();
                    final Date dateTotal = new Date(musicDuration);
                    final SimpleDateFormat sb = new SimpleDateFormat("mm:ss");
                    binding.seekBar.setMax((int) musicDuration);
                    binding.seekBar.setProgress((int) position);

                    binding.textSwitcher.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Date date = new Date(position);
                                    String time = sb.format(date) + "/" + sb.format(dateTotal);
                                    binding.textSwitcher.setCurrentText(time);
                                }
                            }
                    );
                }

                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(seekBar.getProgress());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        running=false;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        mMediaPlayer = null;

        stopAnimation();
        discAnimation.cancel();
        discAnimation = null;
        handler.removeCallbacksAndMessages(null);
//        if (thread != null) {
//            running = false;
//            thread.stop();
//        }
    }

    @Override
    public boolean isShowBannerAd() {
        return false;
    }

    @Override
    public boolean isShowNativeAd() {
        return false;
    }

    @Override
    public boolean isShowVerticllAndStimulateAd() {
        return false;
    }


}
