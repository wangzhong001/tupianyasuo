package com.qihe.imagecompression.ui.activity.image;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FileUtil;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.Utils;
import com.qihe.imagecompression.view.InputDialog;
import com.qihe.imagecompression.view.LoadingDialog;
import com.qihe.imagecompression.view.StandardVideoController;
import com.qihe.imagecompression.view.VideoControllerInterface;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.SureDialog;


import java.io.File;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.microshow.rxffmpeg.AudioVideoUtils;


public class VideoPlayActivity extends AppCompatActivity {

    private static final String TAG = "VideoPlayActivityTAG";
    private String path;
    private IjkVideoView player;

    private int mOriginalWidth;
    private int mOriginalHeight;

    private int with;
    private int height;


    private long bit;
    private long bitFinal;
    private TextView videoSize1;
    private SeekBar seekbar;
    private File file;

    private Handler uiHandler = new Handler() {
        public void handleMessage(Message msg) {
            int arg1 = msg.arg1;
            //Log.i("arg1",arg1+"");

            LiveDataBus.get().with(LiveDataBusData.time, String.class).postValue(arg1 + "%");
        }

        ;
    };

    private LoadingDialog loadingDialog;
    private String input;
    private List<String> pathList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
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
        loadingDialog = new LoadingDialog(this);
        player = findViewById(R.id.player);
        TextView videoSize = findViewById(R.id.video_size);
        videoSize1 = findViewById(R.id.video_size1);
        seekbar = findViewById(R.id.seekbar);
        file = new File(path);
        String fileSize = Utils.getFileSize(file);
        videoSize.setText("原始大小: " + fileSize);
        videoSize1.setText("压缩后: " + Utils.FormetFileSize((long) (file.length() * 0.8)) + "(80%)");

        try {
            mOriginalWidth = AudioVideoUtils.getVideoWidth(path);
            mOriginalHeight = AudioVideoUtils.getVideoHeight(path);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show("文件获取失败");
            finish();
        }

        with = mOriginalWidth;
        height = mOriginalHeight;
        mOriginalWidth *= 0.8;
        mOriginalHeight *= 0.8;

        bit = with * height;
        bitFinal = (long) (bit * 0.8);
        Log.i("bitFinal", bitFinal + "");


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                i = i + 20;
                videoSize1.setText("压缩后: " + Utils.FormetFileSize((long) (file.length() * i / 100)) + "(" + i + "" + "%)");
                mOriginalWidth = with * i / 100;
                mOriginalHeight = height * i / 100;
                bitFinal = bit * i / 100;


                Log.i("bitFinal", bitFinal + "");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


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
               /* if (FloatWindowManager.getInstance().checkPermission(VideoPlayActivity.this)) {
                    // showStimulateNeedUserNumberAd(false);

                } else {
                    FloatWindowManager.getInstance().applyPermission(VideoPlayActivity.this);

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


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreferencesUtil.isVip() || SharedPreferencesUtil.getCount() > 0 ) {
                    InputDialog inputDialog = new InputDialog(VideoPlayActivity.this, "")
                            .addItemListener(new InputDialog.ItemListener() {
                                @Override
                                public void onClickSure(String content) {//input输入框值
                                    String replace = path.replace(path.substring(0, path.lastIndexOf(".")), "");


                                    Log.i("replace123",replace);

                                    input = FileUtils.COMPRESSION_FILE_PATH + content + replace;

                                    loadingDialog.show();
                                    DownThread downThread = new DownThread();
                                    downThread.start();
                                    //list.get(position).setName(content + "." + getFileNameWithSuffix(videolist.get(position).getPath()));

                                    //recordAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                    String replace = path.replace(path.substring(0, path.lastIndexOf("/") + 1), "");
                    inputDialog.setText(replace.replace(path.substring(path.lastIndexOf("."), path.length()), ""));
                    inputDialog.show();
                } else {

                    SureDialog sureDialog = new SureDialog(VideoPlayActivity.this, "您的免费使用次数已用完,您可以成为高级用户无限次使用", "取消", "成为会员", "会员订阅")
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {

                                    ActivityUtil.start(ArouterPath.vip_activity);
                                    // ActivityUtil.start(ArouterPath.vip_activity);

                                }

                                @Override
                                public void onClickCanel() {
//                                                showStimulateAd();


                                }
                            });

                    sureDialog.show();
                }


            }
        });

    }

    class DownThread extends Thread {
        @Override
        public void run() {


            pathList = new ArrayList<>();

            try {
                String compressedFilePath = SiliCompressor.with(VideoPlayActivity.this)

                        //                            .compressVideo(srcPath,input);

                        .compressVideo(path, input, mOriginalWidth, mOriginalHeight, (int) bitFinal, new CompressInterface() {
                            @Override
                            public void onProgress(float percent) {
                                Message msg = new Message();
                                // msg.what = MSG_DOWN_SUCCESS;
                                msg.arg1 = (int) percent;
                                //msg.arg2 = 222; msg.obj = obj;
                                uiHandler.sendMessage(msg);


                            }
                        });
                Log.i("compressedFilePath", compressedFilePath);
                pathList.add(compressedFilePath);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + compressedFilePath)));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(VideoPlayActivity.this, CompleteActivity.class);
                    intent.putExtra("type", "视频");
                    intent.putExtra("list", (Serializable) pathList);
                    startActivity(intent);
                    loadingDialog.dismiss();

                }
            };
            uiHandler.post(runnable);
        }
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
