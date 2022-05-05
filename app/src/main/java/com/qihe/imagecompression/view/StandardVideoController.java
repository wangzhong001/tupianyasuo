package com.qihe.imagecompression.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dueeeke.videoplayer.controller.GestureVideoController;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.util.L;
import com.dueeeke.videoplayer.util.WindowUtil;
import com.qihe.imagecompression.R;

/**
 * 直播/点播控制器
 * Created by Devlin_n on 2017/4/7.
 */

public class StandardVideoController extends GestureVideoController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    protected TextView totalTime, currTime, typeTv, playOrderTv,seekBarTv,typeTopTv,seekBarTopTv;
    protected ImageView fullScreenButton;
    protected LinearLayout bottomContainer, topContainer, nextPlayLl, lastPlayLl, floatingWindowLl, choseTypeLl, playOrderLl, playSuduLl,seekBarPlayLll,buttonLl,floatingWindowTopLl,choseTypeTopLl,playSuduTopLl,seekBarPlayTopLl;
    protected SeekBar videoProgress;
    protected ImageView backButton,typeTopIv;
    protected ImageView lock;
    protected MarqueeTextView title;
    private boolean isLive;
    private boolean isDragging;

    private ProgressBar bottomProgress;
    private ImageView playButton;
    private ImageView startPlayButton, typeIv, playOrderIv;
    private ProgressBar loadingProgress;
    private ImageView thumb;
    private LinearLayout completeContainer;
    private TextView sysTime;//系统当前时间
    private ImageView batteryLevel;//电量
    private Animation showAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dkplayer_anim_alpha_in);
    private Animation hideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dkplayer_anim_alpha_out);
    private BatteryReceiver mBatteryReceiver;
    protected ImageView refresh;
    private VideoControllerInterface videoControllerInterface;
    private int videoType = 0;
    private int playOrderType = 0;
    private SeekBar seekBar,seekTopBar;



    public StandardVideoController(@NonNull Context context) {
        this(context, null);
    }


    public StandardVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StandardVideoController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dkplayer_layout_standard_controller;
    }

    public void setVol(int vol){
        this.mAudioManager.setStreamVolume(3, vol, 0);
    }

    @Override
    protected void initView() {
        super.initView();
        fullScreenButton = controllerView.findViewById(R.id.fullscreen);
        fullScreenButton.setOnClickListener(this);
        bottomContainer = controllerView.findViewById(R.id.bottom_container);
        topContainer = controllerView.findViewById(R.id.top_container);
        videoProgress = controllerView.findViewById(R.id.seekBar);
        videoProgress.setOnSeekBarChangeListener(this);

//        nextPlayLl = controllerView.findViewById(R.id.next_play_ll);
//        nextPlayLl.setOnClickListener(this);

//        lastPlayLl = controllerView.findViewById(R.id.last_play_ll);
//        lastPlayLl.setOnClickListener(this);

//        floatingWindowLl = controllerView.findViewById(R.id.floating_window_ll);
//        floatingWindowLl.setOnClickListener(this);

//        choseTypeLl = controllerView.findViewById(R.id.chose_type_ll);
//        choseTypeLl.setOnClickListener(this);

//        playOrderTv = controllerView.findViewById(R.id.play_order_tv);
//        playOrderIv = controllerView.findViewById(R.id.play_order_iv);

//        playOrderLl = controllerView.findViewById(R.id.play_order_ll);
//        playOrderLl.setOnClickListener(this);

//        floatingWindowTopLl=controllerView.findViewById(R.id.floating_window_top_ll);
//        floatingWindowTopLl.setOnClickListener(this);

//        choseTypeTopLl=controllerView.findViewById(R.id.chose_type_top_ll);
//        choseTypeTopLl.setOnClickListener(this);

//        playSuduTopLl=controllerView.findViewById(R.id.play_sudu_top_ll);
//        playSuduTopLl.setOnClickListener(this);

//        typeTopTv=controllerView.findViewById(R.id.type_top_tv);
//        typeTopIv=controllerView.findViewById(R.id.type_top_iv);

        seekBarTopTv=controllerView.findViewById(R.id.seek_bar_top_tv);

        buttonLl=controllerView.findViewById(R.id.button_ll);
        seekBarPlayTopLl=controllerView.findViewById(R.id.seekBar_play_top_ll);

//        seekBarPlayLll=controllerView.findViewById(R.id.seekBar_play_ll);

//        seekBar=controllerView.findViewById(R.id.seek_bar);
//        seekBarTv=controllerView.findViewById(R.id.seek_bar_tv);

//        seekTopBar=controllerView.findViewById(R.id.seek_top_bar);

//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                seekBarTv.setText(convertProgress(progress)+"x");
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if(videoControllerInterface!=null){
//                    videoControllerInterface.playSudu(convertProgress(seekBar.getProgress()));
//                }
//
//            }
//        });

//        seekTopBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                seekBarTopTv.setText(convertProgress(progress)+"x");
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if(videoControllerInterface!=null){
//                    videoControllerInterface.playSudu(convertProgress(seekBar.getProgress()));
//                }
//            }
//        });




//        playSuduLl = controllerView.findViewById(R.id.play_sudu_ll);
//        playSuduLl.setOnClickListener(this);
//        typeIv = controllerView.findViewById(R.id.type_iv);
//        typeTv = controllerView.findViewById(R.id.type_tv);
        totalTime = controllerView.findViewById(R.id.total_time);
        currTime = controllerView.findViewById(R.id.curr_time);
        backButton = controllerView.findViewById(R.id.back);
        backButton.setOnClickListener(this);
        lock = controllerView.findViewById(R.id.lock);
        lock.setOnClickListener(this);
        thumb = controllerView.findViewById(R.id.thumb);
        thumb.setOnClickListener(this);
        playButton = controllerView.findViewById(R.id.iv_play);
        playButton.setOnClickListener(this);
        startPlayButton = controllerView.findViewById(R.id.start_play);
        loadingProgress = controllerView.findViewById(R.id.loading);
        bottomProgress = controllerView.findViewById(R.id.bottom_progress);
        ImageView rePlayButton = controllerView.findViewById(R.id.iv_replay);
        rePlayButton.setOnClickListener(this);
        completeContainer = controllerView.findViewById(R.id.complete_container);
        completeContainer.setOnClickListener(this);
        title = controllerView.findViewById(R.id.title);
        sysTime = controllerView.findViewById(R.id.sys_time);
        batteryLevel = controllerView.findViewById(R.id.iv_battery);
        mBatteryReceiver = new BatteryReceiver(batteryLevel);
//        refresh = controllerView.findViewById(R.id.iv_refresh);
//        refresh.setOnClickListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(mBatteryReceiver);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getContext().registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void reSetPlay(){
        if(mediaPlayer==null){
            return;
        }
        mediaPlayer.refresh();

    }

    public void doThing(){
        doPauseResume();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fullscreen || i == R.id.back) {
            doStartStopFullScreen();
        } else if (i == R.id.lock) {
            doLockUnlock();
        } else if (i == R.id.iv_play || i == R.id.thumb) {
            doPauseResume();
        } else if (i == R.id.iv_replay) {
            mediaPlayer.retry();
        }
//        else if (i == R.id.iv_refresh) {
//            mediaPlayer.refresh();
//
//        }
//        else if (i == R.id.play_sudu_ll) {
//            //播放速度
//            if(seekBarPlayLll.getVisibility()==VISIBLE){
//                seekBarPlayLll.setVisibility(GONE);
//            }else {
//                seekBarPlayLll.setVisibility(VISIBLE);
//            }
//
//        }
//        else if (i == R.id.next_play_ll) {
//            //下一首
//            if (videoControllerInterface != null) {
//                videoControllerInterface.playNext();
//            }
//
//        }
//        else if (i == R.id.last_play_ll) {
//            //上一首
//            if (videoControllerInterface != null) {
//                videoControllerInterface.playLast();
//            }
//
//        }
//        else if (i == R.id.floating_window_ll) {
//            //悬浮窗
//            if (videoControllerInterface != null) {
//                videoControllerInterface.floatingWindow();
//            }
//
//        }
//        else if(i==R.id.play_sudu_top_ll){
//            //播放速度
//            if(seekBarPlayTopLl.getVisibility()==VISIBLE){
//                seekBarPlayTopLl.setVisibility(GONE);
//            }else {
//                seekBarPlayTopLl.setVisibility(VISIBLE);
//            }
//
//        }
//        else if(i==R.id.chose_type_top_ll){
//
//        }
//        else if(i==R.id.floating_window_top_ll){
//            //悬浮窗
//            if (videoControllerInterface != null) {
//                videoControllerInterface.floatingWindow();
//            }
//        }
    }

    public void showTitle() {
        title.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPlayerState(int playerState) {
        switch (playerState) {
            case IjkVideoView.PLAYER_NORMAL:
                L.e("PLAYER_NORMAL");
                if (isLocked) return;
                setLayoutParams(new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                gestureEnabled = false;
                fullScreenButton.setSelected(false);
                backButton.setVisibility(View.GONE);
                lock.setVisibility(View.GONE);
                title.setVisibility(View.INVISIBLE);
                sysTime.setVisibility(View.GONE);
                batteryLevel.setVisibility(View.GONE);
                topContainer.setVisibility(View.GONE);
//                buttonLl.setVisibility(VISIBLE);
//                floatingWindowTopLl.setVisibility(GONE);
//                choseTypeTopLl.setVisibility(GONE);
//                playSuduTopLl.setVisibility(GONE);
//                seekBarPlayLll.setVisibility(GONE);
//                seekBarPlayTopLl.setVisibility(GONE);
                break;
            case IjkVideoView.PLAYER_FULL_SCREEN:
                L.e("PLAYER_FULL_SCREEN");
                if (isLocked) return;
                gestureEnabled = true;
                fullScreenButton.setSelected(true);
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                sysTime.setVisibility(View.VISIBLE);
                batteryLevel.setVisibility(View.VISIBLE);
                buttonLl.setVisibility(GONE);
//                floatingWindowTopLl.setVisibility(VISIBLE);
//                choseTypeTopLl.setVisibility(VISIBLE);
//                playSuduTopLl.setVisibility(VISIBLE);
//                seekBarPlayLll.setVisibility(GONE);
//                seekBarPlayTopLl.setVisibility(GONE);

                if (mShowing) {
                    lock.setVisibility(View.VISIBLE);
                    topContainer.setVisibility(View.VISIBLE);
                } else {
                    lock.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);
        switch (playState) {
            case IjkVideoView.STATE_IDLE:
                L.e("STATE_IDLE");
                hide();
                isLocked = false;
                lock.setSelected(false);
                mediaPlayer.setLock(false);
                bottomProgress.setProgress(0);
                bottomProgress.setSecondaryProgress(0);
                videoProgress.setProgress(0);
                videoProgress.setSecondaryProgress(0);
                completeContainer.setVisibility(View.GONE);
                bottomProgress.setVisibility(View.GONE);
                loadingProgress.setVisibility(View.GONE);
                startPlayButton.setVisibility(View.VISIBLE);
                thumb.setVisibility(View.VISIBLE);


                break;
            case IjkVideoView.STATE_PLAYING:
                L.e("STATE_PLAYING");
                post(mShowProgress);
                playButton.setSelected(true);
                loadingProgress.setVisibility(View.GONE);
                completeContainer.setVisibility(View.GONE);
                thumb.setVisibility(View.GONE);
                startPlayButton.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_PAUSED:
                L.e("STATE_PAUSED");
                playButton.setSelected(false);
                startPlayButton.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_PREPARING:
                L.e("STATE_PREPARING");
                completeContainer.setVisibility(View.GONE);
                startPlayButton.setVisibility(View.GONE);
                loadingProgress.setVisibility(View.VISIBLE);
//                thumb.setVisibility(View.VISIBLE);
                break;
            case IjkVideoView.STATE_PREPARED:
                L.e("STATE_PREPARED");
                if (!isLive) bottomProgress.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(GONE);
                startPlayButton.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_ERROR:
                L.e("STATE_ERROR");
                startPlayButton.setVisibility(View.GONE);
                loadingProgress.setVisibility(View.GONE);
                thumb.setVisibility(View.GONE);
                bottomProgress.setVisibility(View.GONE);
                topContainer.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_BUFFERING:
                L.e("STATE_BUFFERING");
                startPlayButton.setVisibility(View.GONE);
                loadingProgress.setVisibility(View.VISIBLE);
                thumb.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_BUFFERED:
                loadingProgress.setVisibility(View.GONE);
                startPlayButton.setVisibility(View.GONE);
                thumb.setVisibility(View.GONE);
                L.e("STATE_BUFFERED");
                break;
            case IjkVideoView.STATE_PLAYBACK_COMPLETED:
                L.e("STATE_PLAYBACK_COMPLETED");
                hide();
                removeCallbacks(mShowProgress);
                startPlayButton.setVisibility(View.GONE);
                thumb.setVisibility(View.VISIBLE);
                completeContainer.setVisibility(View.VISIBLE);
                bottomProgress.setProgress(0);
                bottomProgress.setSecondaryProgress(0);
                isLocked = false;
                mediaPlayer.setLock(false);
                break;
        }
    }

    protected void doLockUnlock() {
        if (isLocked) {
            isLocked = false;
            mShowing = false;
            gestureEnabled = true;
            show();
            lock.setSelected(false);
        } else {
            hide();
            isLocked = true;
            gestureEnabled = false;
            lock.setSelected(true);
        }
        mediaPlayer.setLock(isLocked);
    }

    /**
     * 设置是否为直播视频
     */
    public void setLive() {
        isLive = true;
        bottomProgress.setVisibility(View.GONE);
        videoProgress.setVisibility(View.INVISIBLE);
        totalTime.setVisibility(View.INVISIBLE);
        currTime.setVisibility(View.INVISIBLE);
        refresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDragging = true;
        removeCallbacks(mShowProgress);
        removeCallbacks(mFadeOut);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        long duration = mediaPlayer.getDuration();
        long newPosition = (duration * seekBar.getProgress()) / videoProgress.getMax();
        mediaPlayer.seekTo((int) newPosition);

        isDragging = false;
        post(mShowProgress);
        show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        long duration = mediaPlayer.getDuration();
        long newPosition = (duration * progress) / videoProgress.getMax();
        if (currTime != null)
            currTime.setText(stringForTime((int) newPosition));
    }

    @Override
    public void hide() {
        if (mShowing) {
            if (mediaPlayer.isFullScreen()) {
                lock.setVisibility(View.GONE);
                if (!isLocked) {
                    hideAllViews();
                }
            } else {
                bottomContainer.setVisibility(View.GONE);
                bottomContainer.startAnimation(hideAnim);
            }
            if (!isLive && !isLocked) {
                bottomProgress.setVisibility(View.VISIBLE);
                bottomProgress.startAnimation(showAnim);
            }
            mShowing = false;
        }
    }

    private void hideAllViews() {
        topContainer.setVisibility(View.GONE);
        topContainer.startAnimation(hideAnim);
        bottomContainer.setVisibility(View.GONE);
        bottomContainer.startAnimation(hideAnim);
    }

    private void show(int timeout) {
        if (sysTime != null)
            sysTime.setText(getCurrentSystemTime());
        if (!mShowing) {
            if (mediaPlayer.isFullScreen()) {
                lock.setVisibility(View.VISIBLE);
                if (!isLocked) {
                    showAllViews();
                }
            } else {
                bottomContainer.setVisibility(View.VISIBLE);
                bottomContainer.startAnimation(showAnim);
            }
            if (!isLocked && !isLive) {
                bottomProgress.setVisibility(View.GONE);
                bottomProgress.startAnimation(hideAnim);
            }
            mShowing = true;
        }
        removeCallbacks(mFadeOut);
        if (timeout != 0) {
            postDelayed(mFadeOut, timeout);
        }
    }

    private void showAllViews() {
        bottomContainer.setVisibility(View.VISIBLE);
        bottomContainer.startAnimation(showAnim);
        topContainer.setVisibility(View.VISIBLE);
        topContainer.startAnimation(showAnim);
    }

    @Override
    public void show() {
        show(sDefaultTimeout);
    }

    @Override
    protected int setProgress() {
        if (mediaPlayer == null || isDragging) {
            return 0;
        }

        if (title != null && TextUtils.isEmpty(title.getText())) {
            title.setText(mediaPlayer.getTitle());
        }

        if (isLive) return 0;

        int position = (int) mediaPlayer.getCurrentPosition();
        int duration = (int) mediaPlayer.getDuration();
        if (videoProgress != null) {
            if (duration > 0) {
                videoProgress.setEnabled(true);
                int pos = (int) (position * 1.0 / duration * videoProgress.getMax());
                videoProgress.setProgress(pos);
                bottomProgress.setProgress(pos);
            } else {
                videoProgress.setEnabled(false);
            }
            int percent = mediaPlayer.getBufferPercentage();
            if (percent >= 95) { //解决缓冲进度不能100%问题
                videoProgress.setSecondaryProgress(videoProgress.getMax());
                bottomProgress.setSecondaryProgress(bottomProgress.getMax());
            } else {
                videoProgress.setSecondaryProgress(percent * 10);
                bottomProgress.setSecondaryProgress(percent * 10);
            }
        }

        if (totalTime != null)
            totalTime.setText(stringForTime(duration));
        if (currTime != null)
            currTime.setText(stringForTime(position));

        return position;
    }


    @Override
    protected void slideToChangePosition(float deltaX) {
        if (isLive) {
            mNeedSeek = false;
        } else {
            super.slideToChangePosition(deltaX);
        }
    }

    public ImageView getThumb() {
        return thumb;
    }

    @Override
    public boolean onBackPressed() {
        if (isLocked) {
            show();
            return true;
        }
        if (mediaPlayer.isFullScreen()) {
            WindowUtil.scanForActivity(getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            mediaPlayer.stopFullScreen();
            return true;
        }
        return super.onBackPressed();
    }

    public void setVideoControllerInterface(VideoControllerInterface videoControllerInterface) {
        this.videoControllerInterface = videoControllerInterface;
    }


    private float convertProgress(int intVal) {
        float result;
        //这里/10 是因为前面每一个数都扩大10倍，因此这里需要/10还原
        result = intVal/10f;
        return result;
    }

}
