package io.microshow.rxffmpeg.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import io.microshow.rxffmpeg.R;

/**
 * 控制层 实现类
 * Created by Super on 2020/5/4.
 */
public class RxFFmpegPlayerControllerImpl extends RxFFmpegPlayerController {

    private TextView mTimeView;
    private SeekBar mProgressView;
    private ProgressBar mProgressBar;
    private View mBottomPanel;
    private ImageView playBtn;
    private View repeatPlay;
    private ImageView muteImage;//静音图标
    private boolean isHasPlayFinish=false;

    private boolean isSeeking = false;
    public int mPosition;

    public RxFFmpegPlayerControllerImpl(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.rxffmpeg_player_controller;
    }

    @Override
    public void initView() {
        mBottomPanel = findViewById(R.id.bottomPanel);
        mProgressView = findViewById(R.id.progress_view);
        mTimeView = findViewById(R.id.time_view);
        mProgressBar = findViewById(R.id.progressBar);
        playBtn = findViewById(R.id.iv_play);
        repeatPlay = findViewById(R.id.repeatPlay);
        repeatPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏重播按钮
                if (mPlayerView != null) {
                    if (mPlayerView.getVolume() == 0) {
                        //当前是静音，设置为非静音
                        mPlayerView.setVolume(100);
                        muteImage.setImageResource(R.mipmap.rxffmpeg_player_unmute);
                    }
                }
                mPlayerView.repeatPlay();
                repeatPlay.setVisibility(View.GONE);
            }
        });
        View mFullScreenIv = findViewById(R.id.iv_fullscreen);
        mFullScreenIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayerView != null) {
                    //屏幕旋转 全屏
                    mPlayerView.switchScreen();
                }
            }
        });
        muteImage = findViewById(R.id.iv_mute);
        muteImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //静音
                switchMute();
            }
        });

        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayerView != null) {
                    if (mPlayerView.isPlaying()) {//暂停播放
                        mPlayerView.pause();
                    } else {//恢复播放
//                        if(isHasPlayFinish){
//                            playBtn.setImageResource(R.drawable.timeout_icon);
//                            playBtn.animate().alpha(1f).start();//隐藏 播放按钮
//                            mPlayerView.repeatPlay();
//                            isHasPlayFinish=false;
//                        }else {
                            mPlayerView.resume();

//                        }
                    }
                }
            }
        });
    }

    public void switchMute() {
        if (mPlayerView != null) {
            if (mPlayerView.getVolume() == 0) {
                //当前是静音，设置为非静音
                mPlayerView.setVolume(100);
                muteImage.setImageResource(R.mipmap.rxffmpeg_player_unmute);
            } else {
                //当前不是静音，设置为静音
                mPlayerView.setVolume(0);
                muteImage.setImageResource(R.mipmap.rxffmpeg_player_mute);
            }
        }
    }

    @Override
    public void initListener() {
        PlayerListener mPlayerListener = new PlayerListener(this);
        mPlayer.setOnLoadingListener(mPlayerListener);
        mPlayer.setOnTimeUpdateListener(mPlayerListener);
        mPlayer.setOnErrorListener(mPlayerListener);
        mPlayer.setOnCompleteListener(mPlayerListener);

        mProgressView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPosition = progress * mPlayer.getDuration() / 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayer.seekTo(mPosition);
                isSeeking = false;
            }
        });
    }

    /**
     * 播放完成
     */
    public void onCompletion(final IMediaPlayer mediaPlayer) {
        post(new Runnable() {
            @Override
            public void run() {
//                isHasPlayFinish=true;
//                playBtn.setImageResource(R.drawable.bofang_icon);
//                playBtn.animate().alpha(1f).start();//显示 播放按钮
//                        Toast.makeText(getContext(), "播放完成了", Toast.LENGTH_SHORT).show();
                if (mPlayerView != null && !mPlayerView.isLooping()) {
                    //不是循环模式 显示重新播放页面
                    repeatPlay.setVisibility(View.VISIBLE);
                } else {
                    repeatPlay.setVisibility(View.GONE);
                }
            }
        });
    }


    public void setRepeatPlayGone(){
        if(repeatPlay.getVisibility()==VISIBLE){
            repeatPlay.setVisibility(View.GONE);

        }


    }
    /**
     * 播放出错
     */
    public void onError(final IMediaPlayer mediaPlayer, final int code, final String msg) {
        post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "出错了：code=" + code + ", msg=" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 时间更新
     */
    public void onTimeUpdate(final IMediaPlayer mediaPlayer, final int currentTime, final int totalTime) {
        post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                if (totalTime <= 0) {
                    //总时长为0 (直播视频)，则隐藏时间进度条
                    mBottomPanel.setVisibility(View.GONE);
                    return;
                } else {
                    mBottomPanel.setVisibility(View.VISIBLE);
                }

                mTimeView.setText(Helper.secdsToDateFormat(currentTime, totalTime)
                        + " / " + Helper.secdsToDateFormat(totalTime, totalTime));

                if (!isSeeking && totalTime > 0) {
                    mProgressView.setProgress(currentTime * 100 / totalTime);
                }
            }
        });
    }

    /**
     * 加载状态
     */
    public void onLoading(final IMediaPlayer mediaPlayer, final boolean isLoading) {
        post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * 播放器监听
     */
    public static class PlayerListener implements RxFFmpegPlayer.OnCompletionListener,
            RxFFmpegPlayer.OnErrorListener, RxFFmpegPlayer.OnLoadingListener,
            RxFFmpegPlayer.OnTimeUpdateListener {

        private WeakReference<RxFFmpegPlayerControllerImpl> mWeakReference;

        PlayerListener(RxFFmpegPlayerControllerImpl playerControllerImpl) {
            mWeakReference = new WeakReference<>(playerControllerImpl);
        }

        @Override
        public void onCompletion(IMediaPlayer mediaPlayer) {
            final RxFFmpegPlayerControllerImpl playerControllerImpl = mWeakReference.get();
            if (playerControllerImpl != null) {
                playerControllerImpl.onCompletion(mediaPlayer);
            }
        }

        @Override
        public void onError(IMediaPlayer mediaPlayer, int err, String msg) {
            final RxFFmpegPlayerControllerImpl playerControllerImpl = mWeakReference.get();
            if (playerControllerImpl != null) {
                playerControllerImpl.onError(mediaPlayer, err, msg);
            }
        }

        @Override
        public void onLoading(IMediaPlayer mediaPlayer, boolean isLoading) {
            final RxFFmpegPlayerControllerImpl playerControllerImpl = mWeakReference.get();
            if (playerControllerImpl != null) {
                playerControllerImpl.onLoading(mediaPlayer, isLoading);
            }
        }

        @Override
        public void onTimeUpdate(IMediaPlayer mediaPlayer, int currentTime, int totalTime) {
            final RxFFmpegPlayerControllerImpl playerControllerImpl = mWeakReference.get();
            if (playerControllerImpl != null) {
                playerControllerImpl.onTimeUpdate(mediaPlayer, currentTime, totalTime);
            }
        }
    }

    @Override
    public void onPause() {
        playBtn.setImageResource(R.drawable.bofang_icon);
        playBtn.animate().alpha(1f).start();//显示 播放按钮
    }

    @Override
    public void onResume() {
        playBtn.setImageResource(R.drawable.timeout_icon);
        playBtn.animate().alpha(1f).start();//隐藏 播放按钮
        //设置静音图标
        if (mPlayerView != null) {
            muteImage.setImageResource(mPlayerView.getVolume() == 0 ? R.mipmap.rxffmpeg_player_mute : R.mipmap.rxffmpeg_player_unmute);
        }
    }

}
