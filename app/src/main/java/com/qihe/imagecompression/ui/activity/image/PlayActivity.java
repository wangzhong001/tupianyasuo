package com.qihe.imagecompression.ui.activity.image;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

//import com.devlin_n.floatWindowPermission.FloatWindowManager;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.iceteck.silicompressorr.SiliCompressor;
import com.iceteck.silicompressorr.videocompression.CompressInterface;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FileUtil;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.Utils;
import com.qihe.imagecompression.view.InputDialog;
import com.qihe.imagecompression.view.LoadingDialog;
import com.qihe.imagecompression.view.StandardVideoController;
import com.qihe.imagecompression.view.VideoControllerInterface;

import java.io.File;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.microshow.rxffmpeg.AudioVideoUtils;


public class PlayActivity extends AppCompatActivity {

    private String path;
    private IjkVideoView player;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        path = getIntent().getStringExtra("path");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        ActivityUtil.fullScreen(getWindow());

    }

    private void initView() {

        player = findViewById(R.id.player);

        TextView title = findViewById(R.id.title);

        title.setText(new File(path).getName());
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
               /* if (FloatWindowManager.getInstance().checkPermission(PlayActivity.this)) {
                    // showStimulateNeedUserNumberAd(false);

                } else {
                    FloatWindowManager.getInstance().applyPermission(PlayActivity.this);

                }*/

            }

            @Override
            public void videoSize(int videoType) {

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
        player.setVideoController(controller);
        player.setPlayerConfig(new PlayerConfig.Builder()
                .autoRotate()//自动旋转屏幕
//                    .enableCache()//启用边播边存
//                .enableMediaCodec()//启动硬解码
                .usingSurfaceView()//使用SurfaceView
                //ijkplayer不支持assets播放,需切换成MediaPlayer
//                .setCustomMediaPlayer(new AndroidMediaPlayer(VideoPlayActivity.this))
//                    .setCustomMediaPlayer(new AndroidMediaPlayer(this))
                .build());

        player.setUrl(FileUtil.StringToUriToString(path));
        // player.setTitle(name);
        player.start();



    }



    @Override
    public void onResume() {
        super.onResume();
        //恢复播放
        player.resume();
//        mPIPManager.resume();
    }


    @Override
    public void onPause() {
        super.onPause();
        //暂停视频
        player.pause();

//        mPIPManager.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁播放器
//        mPIPManager.reset();

        player.release();

    }
}
