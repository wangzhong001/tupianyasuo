package com.qihe.imagecompression.util;

import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.provider.MediaStore;

import com.qihe.imagecompression.model.MusicInfo;
import com.qihe.imagecompression.model.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipei on 2020/5/22.
 */

public class MusicUtils {


    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static ObservableArrayList<Song> getMusicData(Context context) {
        ObservableArrayList<Song> list = new ObservableArrayList<>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if (song.size > 1000 * 800) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    if (song.song.contains("-")) {
                        String[] str = song.song.split("-");
                        song.singer = str[0];
                        song.song = str[1];
                    }
                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }

        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }


    private List<MusicInfo> musicInfoList;


    public void startScanLocalMusic(final Context context) {



        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    String[] muiscInfoArray = new String[]{
                            MediaStore.Audio.Media.TITLE,               //歌曲名称
                            MediaStore.Audio.Media.ARTIST,              //歌曲歌手
                            MediaStore.Audio.Media.ALBUM,               //歌曲的专辑名
                            MediaStore.Audio.Media.DURATION,            //歌曲时长
                            MediaStore.Audio.Media.DATA};               //歌曲文件的全路径
                    Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            muiscInfoArray, null, null, null);
                    if (cursor!= null && cursor.getCount() != 0){
                        musicInfoList = new ArrayList<MusicInfo>();
                        while (cursor.moveToNext()) {
//                            if (!scanning){
//                                return;
//                            }
                            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
                            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                            String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));

//                            if (filterCb.isChecked() && duration != null && Long.valueOf(duration) < 1000 * 60){
//                                Log.e(TAG, "run: name = "+name+" duration < 1000 * 60" );
//                                continue;
//                            }

                            File file = new File(path);
                            String parentPath = file.getParentFile().getPath();

                            name = replaseUnKnowe(name);
                            singer = replaseUnKnowe(singer);
                            album = replaseUnKnowe(album);
                            path = replaseUnKnowe(path);

                            MusicInfo musicInfo = new MusicInfo();

                            musicInfo.setName(name);
                            musicInfo.setSinger(singer);
                            musicInfo.setAlbum(album);
                            musicInfo.setPath(path);
                            musicInfo.setParentPath(parentPath);
//                            musicInfo.setFirstLetter(ChineseToEnglish.StringToPinyinSpecial(name).toUpperCase().charAt(0)+"");

                            musicInfoList.add(musicInfo);
//                            progress++;
//                            scanPath = path;
//                            musicCount = cursor.getCount();
//                            msg = new Message();    //每次都必须new，必须发送新对象，不然会报错
//                            msg.what = Constant.SCAN_UPDATE;
//                            msg.arg1 = musicCount;
////                                Bundle data = new Bundle();
////                                data.putInt("progress", progress);
////                                data.putString("scanPath", scanPath);
////                                msg.setData(data);
//                            handler.sendMessage(msg);  //更新UI界面
                            try {
                                sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }



                    }else {

                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        }.start();
    }


    public static String replaseUnKnowe(String oldStr){
        try {
            if (oldStr != null){
                if (oldStr.equals("<unknown>")){
                    oldStr = oldStr.replaceAll("<unknown>", "未知");
                }
            }
        }catch (Exception e){
        }
        return oldStr;
    }
}
