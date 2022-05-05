package com.qihe.imagecompression.util;

import io.microshow.rxffmpeg.RxFFmpegCommandList;

/**
 * Created by lipei on 2020/6/13.
 */

public class FFUtil {

    /**
     * @param srcPath   视频源路径
     * @param outPath   视频输出路径
     */


    public static String[] videoTransUtil(String srcPath, String outPath,String startTime,String imageFormat,int postion,String allTime,String name) {

        if(postion==0){
            //视频转码
            return FFmpegUtils.transformVideo(srcPath,outPath);

        }else if(postion==1){
            //获取第一帧
            return FFmpegUtils.frame2Image(srcPath,outPath,"00:00:00");

        }else if(postion==2){
            //抽取视频
            return FFmpegUtils.extractVideo(srcPath,outPath);

        }else if(postion==3){
            //视频转图片
            return FFmpegUtils.video2Image(srcPath,outPath,"jpg");
        }else if(postion==4){
            //视频转gif
            return FFmpegUtils.video2Gif(srcPath,Integer.parseInt(startTime),Integer.parseInt(allTime),outPath);
        }else if(postion==5){
            //图片转视频
            return picToVideo(srcPath,outPath);
        }else if(postion==6){
            //倒叙播放
            return FFmpegUtils.reverseVideo(srcPath,outPath);
        }else if(postion==7){
            //视频降噪
            return FFmpegUtils.denoiseVideo(srcPath,outPath);
        }else if(postion==8){
            //视频缩小一倍
            return FFmpegUtils.videoDoubleDown(srcPath,outPath);
        }else if(postion==7){
            //倍速播放
            return FFmpegUtils.videoSpeed2(srcPath,outPath);
        }
        return null;

    }

    public static boolean isShowFormat(int postion){
        if(postion==1 || postion==3||postion==4){
            return false;
        }

        return true;
    }



    /**
     * @param srcPath   视频源路径
     * @param outPath   视频输出路径
     * "ffmpeg -y -loop 1 -r 25 -i /storage/emulated/0/FFmpegCmd/Output/video/第一帧.jpg -vf zoompan=z=1.1:x='if(eq(x,0),100,x-1)':s='960*540' -t 10 -pix_fmt yuv420p /storage/emulated/0/FFmpegCmd/Output/video/图片合成视频.mp4";

     */


    public static String[] picToVideo(String srcPath, String outPath) {
        RxFFmpegCommandList cmd = new RxFFmpegCommandList();
        cmd.add("-loop");
        cmd.add("1");
        cmd.add("-r");
        cmd.add("25");
        cmd.add("-i");
        cmd.add(srcPath);
        cmd.add("-vf");
        cmd.add("zoompan=z=1.1:x='if(eq(x,0),100,x-1)':s='960*540'");
        cmd.add("-t");
        cmd.add("10");
        cmd.add("-pix_fmt");
        cmd.add("yuv420p");
        cmd.add(outPath);
        return cmd.build();
    }
}
