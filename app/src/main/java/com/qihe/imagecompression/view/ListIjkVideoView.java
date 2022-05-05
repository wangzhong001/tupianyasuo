package com.qihe.imagecompression.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.dueeeke.videoplayer.player.IjkVideoView;
import com.qihe.imagecompression.model.FileModel;
import com.qihe.imagecompression.util.FileUtil;

import java.util.List;
import java.util.Random;

/**
 * 连续播放一个列表
 * Created by xinyu on 2017/12/25.
 */

public class ListIjkVideoView extends IjkVideoView implements ListMediaPlayerControl{

    protected List<FileModel> mVideoModels;//列表播放数据
    public int mCurrentVideoPosition = 0;//列表播放时当前播放视频的在List中的位置
    private int playOrderType=0;

    public ListIjkVideoView(@NonNull Context context) {
        super(context);
    }

    public ListIjkVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ListIjkVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
//        setPostion(true,false);
//        skipToNext();


    }

    /**
     * 播放下一条视频
     */
    private void playNext() {
        FileModel videoModel = mVideoModels.get(mCurrentVideoPosition);
        if (videoModel != null) {
            mCurrentUrl = FileUtil.StringToUriToString(videoModel.getPath());
            mCurrentTitle = videoModel.getName();
            mPlayerConfig.isCache = videoModel.isCache;
            mCurrentPosition = 0;
            setVideoController(null);
        }
    }

//    /**
//     * 设置一个列表的视频
//     */
//    public void setVideos(List<VideoModel> videoModels) {
//        this.mVideoModels = videoModels;
////        playNext();
//    }


    /**
     * 设置播放视频的位置
     * @param mCurrentVideoPosition
     */
    public void setMCurrentVideoPosition(int mCurrentVideoPosition){
        this.mCurrentVideoPosition=mCurrentVideoPosition;
    }

    /**
     * 播放下一条视频，可用于跳过广告
     */
    @Override
    public void skipToNext() {


//
//        if (mVideoModels != null && mVideoModels.size() > 1) {
//            if (mCurrentVideoPosition >= mVideoModels.size()) {
//                mCurrentVideoPosition=mVideoModels.size()-1;
//                return;
//
//            }
//
//
//
//            VideoModel videoModel = mVideoModels.get(mCurrentVideoPosition);
//
//            mCurrentUrl = FileUtil.StringToUriToString(videoModel.url);
//            mCurrentTitle = videoModel.title;
//            mPlayerConfig.isCache = videoModel.isCache;
//
//            addDisplay();
//            startPrepare(true);
//            start();
//
//
//        }
    }


    public void playToNext(){
        setPostion(false,false);
        skipToNext();
    }


    public void skipToLast(){

//        setPostion(false,true);
//
//
//        if (mVideoModels != null && mVideoModels.size() > 1) {
//            if (mCurrentVideoPosition <= -1) {
//                mCurrentVideoPosition=0;
//                return;
//            }
//
//
//            VideoModel videoModel = mVideoModels.get(mCurrentVideoPosition);
//            LogUtils.e("videoUrl---->",videoModel.url);
//
//            mCurrentUrl = FileUtil.StringToUriToString(videoModel.url);
//            mCurrentTitle = videoModel.title;
//            mPlayerConfig.isCache = videoModel.isCache;
//
//            addDisplay();
//            startPrepare(true);
//            start();
//
//        }
    }


    /**
     * 设置播放顺序
     * @param playOrderType
     */
    public void setPlayOrderType(int playOrderType) {
        this.playOrderType = playOrderType;
    }


    /**
     * 根据播放顺序来设置播放第几条视频
     */
    private void setPostion(boolean isAutomaticOrManual,boolean isLastOrNext){
        if(playOrderType==0){



            //顺序播放
            if(isLastOrNext){
                mCurrentVideoPosition-=1;

            }else {
                mCurrentVideoPosition+=1;
            }


        }else if(playOrderType==1){

            //随机播放
            int min=0;
            int max=mVideoModels.size()-1;
            Random random = new Random();
            mCurrentVideoPosition = random.nextInt(max)%(max-min+1) + min;



        }else if(playOrderType==2){

            //循环播放
            if(isAutomaticOrManual){


            }else {
                if(isLastOrNext){
                    mCurrentVideoPosition-=1;

                }else {
                    mCurrentVideoPosition+=1;
                }
            }

        }

    }


    /**
     * 设置播放速度
     * @param sudu
     */
    public void setPlaySudu(float sudu){
        setSpeed(sudu);
    }


}
