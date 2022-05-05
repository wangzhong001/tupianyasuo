package com.qihe.imagecompression.viewmodel;

import android.app.Application;
import android.app.Dialog;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.app.Constant;
import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.model.MusicInfo;
import com.qihe.imagecompression.model.Song;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.ChineseToEnglish;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.viewmodel.itemviewmodel.VideoListItemViewModel;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.util.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by lipei on 2020/5/22.
 */

public class ResourceVideoViewModel extends BaseViewModel {

    public ObservableField<String> titleName = new ObservableField<>("");

    public ObservableArrayList<Song> musicList = new ObservableArrayList<>();


    public BindingCommand backClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


    public ResourceVideoViewModel(@NonNull Application application) {
        super(application);
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case Constant.SCAN_UPDATE:
                    setMusicData();

                    break;

                case Constant.SCAN_VIDEO_UPDATE:
                    setVideoData();
                    break;
            }
        }
    };

    private void setMusicData() {
        for (int i = 0; i < musicInfoList.size(); i++) {
//            resList.add(new ResourceVideoItemViewModel(this, musicInfoList.get(i)));
        }
    }


    public void getLocalMusic(Context context) {
        startScanLocalMusic(context);
//        musicList = MusicUtils.getMusicData(context);

    }

    @Override
    public void onDestory() {
        super.onDestory();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void getLocalPic(final Context context, final Dialog dialog) {
//        startScanLocalPic(context, dialog);
//        musicList = MusicUtils.getMusicData(context);
        dialog.show();

        disposable = Single
                .create(new SingleOnSubscribe<List<MusicInfo>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<MusicInfo>> e) {
                        e.onSuccess(getLocalPicFileSync(context));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                })
                .subscribe(new Consumer<List<MusicInfo>>() {
                    @Override
                    public void accept(List<MusicInfo> musicInfos) {
                        musicInfoList = musicInfos;
                        videoLiveData.postValue(ResourceVideoViewModel.this);


                        //是否显示没有数据视图
                        isEmpty.set(musicInfoList == null || musicInfoList.size() == 0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        dialog.dismiss();
                        isEmpty.set(true);
                        ToastUtils.show("未知错误，请稍后重试");
                    }
                });


    }

    private Disposable disposable;

    public MutableLiveData<ResourceVideoViewModel> videoLiveData = new MutableLiveData<>();

    public void getLocalVideo(final Context context, final Dialog dialog) {

        dialog.show();

        disposable = Single
                .create(new SingleOnSubscribe<List<MusicInfo>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<MusicInfo>> e) {
                        e.onSuccess(getLocalVideoFileSync(context));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
//                        dialog.dismiss();
                    }
                })
                .subscribe(new Consumer<List<MusicInfo>>() {
                    @Override
                    public void accept(List<MusicInfo> musicInfos) {
                        musicInfoList = musicInfos;

                        for (int i = 0; i < musicInfoList.size(); i++) {
                            videoList.add(new VideoListItemViewModel(ResourceVideoViewModel.this, musicInfoList.get(i)));
                        }
                        //是否显示没有数据视图
                        isEmpty.set(musicInfoList == null || musicInfoList.size() == 0);
                        dialog.dismiss();
//                        videoLiveData.postValue(ResourceVideoViewModel.this);



                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        dialog.dismiss();
                        isEmpty.set(true);
                        ToastUtils.show("未知错误，请稍后重试");
                    }
                });


//        startScanLocalVideo(context, dialog);
//        musicList = MusicUtils.getMusicData(context);

    }

    /**
     * 音频
     */
//    public ObservableArrayList<ResourceVideoItemViewModel> resList = new ObservableArrayList<>();
//
//    public BindingRecyclerViewAdapter<ResourceVideoItemViewModel> resAdapter = new BindingRecyclerViewAdapter<>();
//
//    public ItemBinding<ResourceVideoItemViewModel> orderListItemBinding = ItemBinding.of(BR.baseViewModel, R.layout.resource_video_item);


    //获取本地所有视频文件
    public static List<MusicInfo> getLocalVideoFileSync(Context context) {

        List<MusicInfo> listAudioModel = new ArrayList<>();
        //指定默认视频查询字段
        String[] PROJECTION_VIDEO_DEFAULT = new String[]{
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_MODIFIED,
        };

        int postionn = 0;
        //指定默认排序方式（视频文件)
        String SORTED_ORDER_DEFAULT_VIDEO = MediaStore.Video.Media.DATE_ADDED + " desc";
        try {
            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, PROJECTION_VIDEO_DEFAULT, null, null, SORTED_ORDER_DEFAULT_VIDEO);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setPostion(postionn);
                    postionn += 1;
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    final SimpleDateFormat sb = new SimpleDateFormat("mm:ss");

                    if (duration != null) {
                        Date date = new Date(Long.parseLong(duration));
                        String duTime = sb.format(date);
                        musicInfo.setDuration(duTime);
                    }

                    musicInfo.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));//文件名
                    musicInfo.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));//文件路径
                    musicInfo.setVideoPic(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));//视频缩略图路径
                    musicInfo.setTime(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED)) + "000");
                    listAudioModel.add(musicInfo);
                }
                cursor.close();
            }
        } catch(Exception ignored){
        }
        return listAudioModel;

//        List<MusicInfo> listAudioModel = new ArrayList<>();
//        //指定默认视频查询字段
//        String[] PROJECTION_VIDEO_DEFAULT = new String[]{
//                MediaStore.Video.Media.DISPLAY_NAME,
//                MediaStore.Video.Media.DATA,
//                MediaStore.Video.Media.DURATION,
//                MediaStore.Video.Media.DATE_MODIFIED,
//        };
//
//        int postionn = 0;
//        //指定默认排序方式（视频文件)
//        String SORTED_ORDER_DEFAULT_VIDEO = MediaStore.Video.Media.DATE_ADDED + " desc";
//        try {
//            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, PROJECTION_VIDEO_DEFAULT, null, null, SORTED_ORDER_DEFAULT_VIDEO);
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    MusicInfo musicInfo = new MusicInfo();
//                    musicInfo.setPostion(postionn);
//                    postionn += 1;
//                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
//                    final SimpleDateFormat sb = new SimpleDateFormat("mm:ss");
//
//                    if (duration != null) {
//                        Date date = new Date(Long.parseLong(duration));
//                        String duTime = sb.format(date);
//                        musicInfo.setDuration(duTime);
//                    }
//
//                    musicInfo.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));//文件名
//                    musicInfo.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));//文件路径
//                    musicInfo.setVideoPic(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));//视频缩略图路径
//                    musicInfo.setTime(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED)) + "000");
//                    listAudioModel.add(musicInfo);
//                }
//                cursor.close();
//            }
//        } catch (Exception ignored) {
//        }
//        return listAudioModel;
    }


    //获取本地所有视频文件
    public static List<MusicInfo> getLocalPicFileSync(Context context) {
        List<MusicInfo> listAudioModel = new ArrayList<>();
        //指定默认视频查询字段
        String[] PROJECTION_VIDEO_DEFAULT = new String[]{
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.DATA,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
        };

        int postionn = 0;
        //指定默认排序方式（视频文件)
        String SORTED_ORDER_DEFAULT_VIDEO = MediaStore.Images.Media.DATE_ADDED + " desc";
        try {
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION_VIDEO_DEFAULT, null, null, SORTED_ORDER_DEFAULT_VIDEO);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setPostion(postionn);
                    postionn += 1;
                    musicInfo.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));//文件名
                    musicInfo.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));//文件路径
                    musicInfo.setTime(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)) + "000");
                    listAudioModel.add(musicInfo);
                }
                cursor.close();
            }
        } catch (Exception ignored) {
        }
        return listAudioModel;
    }

    public List<MusicInfo> musicInfoList = new ArrayList<>();

    public void startScanLocalMusic(final Context context) {


        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    String[] muiscInfoArray = new String[]{
                            MediaStore.Audio.Media.DISPLAY_NAME,               //歌曲名称
                            MediaStore.Audio.Media.ARTIST,              //歌曲歌手
                            MediaStore.Audio.Media.ALBUM,               //歌曲的专辑名
                            MediaStore.Audio.Media.DURATION,            //歌曲时长
                            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.DATE_MODIFIED};               //歌曲文件的全路径
                    Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            muiscInfoArray, null, null, null);
                    if (cursor != null && cursor.getCount() != 0) {
                        while (cursor.moveToNext()) {
//                            if (!scanning){
//                                return;
//                            }
                            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
                            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                            String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
                            String time = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED));
                            String time1 = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED));


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
                            time = replaseUnKnowe(time + "000");
                            time1 = replaseUnKnowe(time1);
                            final SimpleDateFormat sb = new SimpleDateFormat("mm:ss");


                            MusicInfo musicInfo = new MusicInfo();
                            Date date = new Date(Long.parseLong(duration));
                            String duTime = sb.format(date);

                            musicInfo.setName(name);
                            musicInfo.setSinger(singer);
                            musicInfo.setAlbum(album);
                            musicInfo.setPath(path);
                            musicInfo.setParentPath(parentPath);
                            musicInfo.setTime(time);
                            musicInfo.setDuration(duTime);
                            musicInfo.setTime1(time1);
                            musicInfo.setFirstLetter(ChineseToEnglish.StringToPinyinSpecial(name).toUpperCase().charAt(0) + "");

                            musicInfoList.add(musicInfo);
//                            progress++;
//                            scanPath = path;
//                            musicCount = cursor.getCount();

//                            msg.arg1 = musicCount;
////                                Bundle data = new Bundle();
////                                data.putInt("progress", progress);
////                                data.putString("scanPath", scanPath);
////                                msg.setData(data);
                            try {
                                sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // 根据a-z进行排序源数据
//                        Collections.sort(musicInfoList);
                        Message msg = new Message();    //每次都必须new，必须发送新对象，不然会报错
                        msg.what = Constant.SCAN_UPDATE;
                        handler.sendMessage(msg);  //更新UI界面

                    } else {

                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }.start();
    }


    public String replaseUnKnowe(String oldStr) {
        try {
            if (oldStr != null) {
                if (oldStr.equals("<unknown>")) {
                    oldStr = oldStr.replaceAll("<unknown>", "未知");
                }
            }
        } catch (Exception e) {
        }
        return oldStr;
    }


    /**
     * 视频
     */
    public ObservableArrayList<VideoListItemViewModel> videoList = new ObservableArrayList<>();

    public BindingRecyclerViewAdapter<VideoListItemViewModel> videoAdapter = new BindingRecyclerViewAdapter<>();

    public ItemBinding<VideoListItemViewModel> videoListItemBinding = ItemBinding.of(BR.baseViewModel, R.layout.video_list_item);


    public void startScanLocalVideo(final Context context, final Dialog dialog) {
        if (dialog != null) {
            dialog.show();
        }


//        List<File> mList = FileUtils.listFilesInDir(FileUtils.VIDEO_FILE_PATH, false);
//
//        if (mList != null) {
//            KLog.e("mList-->", mList.size());
//
//            if (mList.size() > 0) {
//
//                int size = mList.size();
//                for (int i = 0; i < size; i++) {
//                    File file = mList.get(i);
//                    MusicInfo musicInfo = new MusicInfo();
//                    musicInfo.setName(file.getName());
//                    musicInfo.setPath(file.getAbsolutePath());
//                    musicInfo.setTime("13313131");
//                    musicInfo.setPostion(i);
//                    videoList.add(new VideoListItemViewModel(this, musicInfo));
//
//                }
//            }
//        }


//
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    String[] muiscInfoArray = new String[]{
                            MediaStore.Video.Media._ID, // 视频id
                            MediaStore.Video.Media.DATA, // 视频路径
                            MediaStore.Video.Media.SIZE, // 视频字节大小
                            MediaStore.Video.Media.DISPLAY_NAME, // 视频名称 xxx.mp4
                            MediaStore.Video.Media.TITLE, // 视频标题
                            MediaStore.Video.Media.DATE_ADDED, // 视频添加到MediaProvider的时间
                            MediaStore.Video.Media.DATE_MODIFIED, // 上次修改时间，该列用于内部MediaScanner扫描，外部不要修改
                            MediaStore.Video.Media.MIME_TYPE, // 视频类型 video/mp4
                            MediaStore.Video.Media.DURATION, // 视频时长
                            MediaStore.Video.Media.ARTIST, // 艺人名称
                            MediaStore.Video.Media.ALBUM, // 艺人专辑名称
                            MediaStore.Video.Media.RESOLUTION, // 视频分辨率 X x Y格式
                            MediaStore.Video.Media.DESCRIPTION, // 视频描述
                            MediaStore.Video.Media.IS_PRIVATE,
                            MediaStore.Video.Media.TAGS,
                            MediaStore.Video.Media.CATEGORY, // YouTube类别
                            MediaStore.Video.Media.LANGUAGE, // 视频使用语言
                            MediaStore.Video.Media.LATITUDE, // 拍下该视频时的纬度
                            MediaStore.Video.Media.LONGITUDE, // 拍下该视频时的经度
                            MediaStore.Video.Media.DATE_TAKEN,
                            MediaStore.Video.Media.MINI_THUMB_MAGIC,
                            MediaStore.Video.Media.BUCKET_ID,
                            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                            MediaStore.Video.Media.BOOKMARK,// 上次视频播放的位置
                            MediaStore.Video.Thumbnails.DATA, // 视频缩略图路径

                    };               //歌曲文件的全路径


                    int postion = 0;

                    //指定默认视频查询字段
                    String[] PROJECTION_VIDEO_DEFAULT = new String[]{
                            MediaStore.Video.Media.DISPLAY_NAME,
                            MediaStore.Video.Media.TITLE,
                            MediaStore.Video.Media.DATA,
                            MediaStore.Video.Thumbnails.DATA,
                            MediaStore.Video.Media.SIZE,
                            MediaStore.Video.Media.DATE_ADDED,
                            MediaStore.Video.Media.DATE_MODIFIED,
                    };

                    //指定默认排序方式（视频文件)
                    String SORTED_ORDER_DEFAULT_VIDEO = MediaStore.Video.Media.DATE_ADDED + " desc";

//                    ContentResolver mContentResolver = context.getContentResolver();
//
//
//                    //只查询jpeg和png的图片
//                    Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
//                            MediaStore.Images.Media.MIME_TYPE + "=? or "
//                                    + MediaStore.Images.Media.MIME_TYPE + "=?",
//                            new String[] { "image/jpeg", "image/png", "image/jpg", "image/gif" }, MediaStore.Images.Media.DATE_MODIFIED);


                    Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, PROJECTION_VIDEO_DEFAULT, null, null, null);


//                    Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                            muiscInfoArray, null, null, null);
                    if (cursor != null && cursor.getCount() != 0) {
                        while (cursor.moveToNext()) {
//                            if (!scanning){
//                                return;
//                            }
                            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                            String time = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                            String videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
//                            String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));


//                            if (filterCb.isChecked() && duration != null && Long.valueOf(duration) < 1000 * 60){
//                                Log.e(TAG, "run: name = "+name+" duration < 1000 * 60" );
//                                continue;
//                            }

                            File file = new File(path);
                            String parentPath = file.getParentFile().getPath();

                            name = replaseUnKnowe(name);
                            album = replaseUnKnowe(album);
                            path = replaseUnKnowe(path);
                            time = replaseUnKnowe(time + "000");
                            videoPath = replaseUnKnowe(videoPath);

                            final SimpleDateFormat sb = new SimpleDateFormat("mm:ss");


                            MusicInfo musicInfo = new MusicInfo();
//                            if(duration!=null){
//                                Date date = new Date(Long.parseLong(duration));
//                                String duTime = sb.format(date);
//                                musicInfo.setDuration(duTime);
//
//
//
//                            }

                            musicInfo.setName(name);
                            musicInfo.setAlbum(album);
                            musicInfo.setPath(path);
                            musicInfo.setParentPath(parentPath);
                            musicInfo.setTime(time);
                            musicInfo.setVideoPic(videoPath);
                            musicInfo.setPostion(postion);
                            postion += 1;
                            musicInfo.setFirstLetter(ChineseToEnglish.StringToPinyinSpecial(name).toUpperCase().charAt(0) + "");

                            musicInfoList.add(musicInfo);
//                            progress++;
//                            scanPath = path;
//                            musicCount = cursor.getCount();

//                            msg.arg1 = musicCount;
////                                Bundle data = new Bundle();
////                                data.putInt("progress", progress);
////                                data.putString("scanPath", scanPath);
////                                msg.setData(data);
                            try {
                                sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // 根据a-z进行排序源数据
                        if (dialog != null) {
                            dialog.dismiss();

                        }
                        Message msg = new Message();    //每次都必须new，必须发送新对象，不然会报错
                        msg.what = Constant.SCAN_VIDEO_UPDATE;
                        handler.sendMessage(msg);  //更新UI界面

                    } else {
                        if (dialog != null) {
                            dialog.dismiss();

                        }
                        isEmpty.set(true);

                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Exception e) {
                    if (dialog != null) {
                        dialog.dismiss();

                    }
                    isEmpty.set(true);

                    e.printStackTrace();

                }
            }
        }.start();
    }


    public void startScanLocalPic(final Context context, final Dialog dialog) {
        if (dialog != null) {
            dialog.show();
        }


//        List<File> mList = FileUtils.listFilesInDir(FileUtils.VIDEO_FILE_PATH, false);
//
//        if (mList != null) {
//            KLog.e("mList-->", mList.size());
//
//            if (mList.size() > 0) {
//
//                int size = mList.size();
//                for (int i = 0; i < size; i++) {
//                    File file = mList.get(i);
//                    MusicInfo musicInfo = new MusicInfo();
//                    musicInfo.setName(file.getName());
//                    musicInfo.setPath(file.getAbsolutePath());
//                    musicInfo.setTime("13313131");
//                    musicInfo.setPostion(i);
//                    videoList.add(new VideoListItemViewModel(this, musicInfo));
//
//                }
//            }
//        }


//
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    String[] muiscInfoArray = new String[]{
                            MediaStore.Images.Media.DATA, // 图片路径
                            MediaStore.Images.Media.DISPLAY_NAME,
                            MediaStore.Images.Media.LATITUDE,
                            MediaStore.Images.Media.LONGITUDE,
                            MediaStore.Images.Media._ID,// 图片id
                            MediaStore.Images.Media.SIZE, // 视频字节大小
                            MediaStore.Images.Media.DATE_ADDED, // 视频添加到MediaProvider的时间


//                            MediaStore.Video.Media._ID, // 视频id
//                            MediaStore.Video.Media.DATA, // 视频路径
//                            MediaStore.Video.Media.SIZE, // 视频字节大小
//                            MediaStore.Video.Media.DISPLAY_NAME, // 视频名称 xxx.mp4
//                            MediaStore.Video.Media.TITLE, // 视频标题
//                            MediaStore.Video.Media.DATE_ADDED, // 视频添加到MediaProvider的时间
//                            MediaStore.Video.Media.DATE_MODIFIED, // 上次修改时间，该列用于内部MediaScanner扫描，外部不要修改
//                            MediaStore.Video.Media.MIME_TYPE, // 视频类型 video/mp4
//                            MediaStore.Video.Media.DURATION, // 视频时长
//                            MediaStore.Video.Media.ARTIST, // 艺人名称
//                            MediaStore.Video.Media.ALBUM, // 艺人专辑名称
//                            MediaStore.Video.Media.RESOLUTION, // 视频分辨率 X x Y格式
//                            MediaStore.Video.Media.DESCRIPTION, // 视频描述
//                            MediaStore.Video.Media.IS_PRIVATE,
//                            MediaStore.Video.Media.TAGS,
//                            MediaStore.Video.Media.CATEGORY, // YouTube类别
//                            MediaStore.Video.Media.LANGUAGE, // 视频使用语言
//                            MediaStore.Video.Media.LATITUDE, // 拍下该视频时的纬度
//                            MediaStore.Video.Media.LONGITUDE, // 拍下该视频时的经度
//                            MediaStore.Video.Media.DATE_TAKEN,
//                            MediaStore.Video.Media.MINI_THUMB_MAGIC,
//                            MediaStore.Video.Media.BUCKET_ID,
//                            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
//                            MediaStore.Video.Media.BOOKMARK,// 上次视频播放的位置
//                            MediaStore.Video.Thumbnails.DATA, // 视频缩略图路径

                    };               //歌曲文件的全路径


                    int postion = 0;
                    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            muiscInfoArray, null, null, null);
                    if (cursor != null && cursor.getCount() != 0) {
                        while (cursor.moveToNext()) {
//                            if (!scanning){
//                                return;
//                            }
                            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            String time = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                            String videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
//                            String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));


//                            if (filterCb.isChecked() && duration != null && Long.valueOf(duration) < 1000 * 60){
//                                Log.e(TAG, "run: name = "+name+" duration < 1000 * 60" );
//                                continue;
//                            }

                            File file = new File(path);
                            String parentPath = file.getParentFile().getPath();

                            name = replaseUnKnowe(name);
                            album = replaseUnKnowe(album);
                            path = replaseUnKnowe(path);
                            time = replaseUnKnowe(time + "000");
                            videoPath = replaseUnKnowe(videoPath);


                            MusicInfo musicInfo = new MusicInfo();

                            musicInfo.setName(name);
                            musicInfo.setAlbum(album);
                            musicInfo.setPath(path);
                            musicInfo.setParentPath(parentPath);
                            musicInfo.setTime(time);
                            musicInfo.setVideoPic(videoPath);
                            musicInfo.setPostion(postion);
                            postion += 1;
                            musicInfo.setFirstLetter(ChineseToEnglish.StringToPinyinSpecial(name).toUpperCase().charAt(0) + "");

                            musicInfoList.add(musicInfo);
//                            progress++;
//                            scanPath = path;
//                            musicCount = cursor.getCount();

//                            msg.arg1 = musicCount;
////                                Bundle data = new Bundle();
////                                data.putInt("progress", progress);
////                                data.putString("scanPath", scanPath);
////                                msg.setData(data);
                            try {
                                sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // 根据a-z进行排序源数据
                        if (dialog != null) {
                            dialog.dismiss();

                        }
                        Message msg = new Message();    //每次都必须new，必须发送新对象，不然会报错
                        msg.what = Constant.SCAN_VIDEO_UPDATE;
                        handler.sendMessage(msg);  //更新UI界面

                    } else {
                        isEmpty.set(true);

                        if (dialog != null) {
                            dialog.dismiss();

                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Exception e) {
                    if (dialog != null) {
                        dialog.dismiss();

                    }
                    isEmpty.set(true);
                    e.printStackTrace();

                }
            }
        }.start();
    }


    public void setVideoData() {

    }

    private static final String TAG = "ResourceVideoViewModel";

    /**
     * 资源是否选中
     */
    public ObservableBoolean isHasChose = new ObservableBoolean();

    /**
     * 资源路经
     */
    public ObservableField<MusicInfo> chosePath = new ObservableField<>();


    /**
     * 跳转到裁剪
     */

    public BindingCommand toCutAudio = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (isMerge.get()) {
                //合并

                if (isRemax.get()) {
                    //混音
                    ActivityUtil.start(ArouterPath.merge_activity, LiveDataBusData.chosePath, new Gson().toJson(choseMusicList), LiveDataBusData.isRemax, isRemax.get());

                } else {
                    //合并
                    ActivityUtil.start(ArouterPath.merge_activity, LiveDataBusData.chosePath, new Gson().toJson(choseMusicList));

                }

            } else {
                //剪辑
                ActivityUtil.start(ArouterPath.cut_audio_activity, LiveDataBusData.chosePath, chosePath.get());

            }
        }
    });


    public ObservableField<String> choseVideoPath = new ObservableField<>("");


    public void setType(int postion) {
//        videoAdapter.setType(postion);
    }

    /**
     * 从视频中提取音频
     */

    public MutableLiveData<ResourceVideoViewModel> getMusicFromViedeoLiveData = new MutableLiveData<>();

    public BindingCommand getMusicFromViedeoClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            getMusicFromViedeoLiveData.postValue(ResourceVideoViewModel.this);
        }
    });


    public Map<String, MusicInfo> choseMusicList = new HashMap<>();//需要合并的音乐

    public ObservableBoolean isMerge = new ObservableBoolean();//是否是合并

    public ObservableBoolean isRemax = new ObservableBoolean();//是否是混音


    public ObservableBoolean isEmpty = new ObservableBoolean(true);


    /**
     * 修改
     */
    public MutableLiveData<ResourceVideoViewModel> showFetureDialogLiveData = new MutableLiveData<>();

    public ObservableField<VideoListItemViewModel> fileModelObservableFiel = new ObservableField<>();

    public void showFetureDialog(VideoListItemViewModel mineTabItemViewModel) {
        fileModelObservableFiel.set(mineTabItemViewModel);//音乐属性
        showFetureDialogLiveData.postValue(ResourceVideoViewModel.this);
    }


    /**
     * 重命名
     * 点击linearlayout触发observe的ONCHANGE方法,以及dismiss方法。
     */
    public MutableLiveData<ResourceVideoViewModel> reNameLiveData = new MutableLiveData<>();
    public BindingCommand renameClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            reNameLiveData.postValue(ResourceVideoViewModel.this);
            dissmissLiveData.postValue(ResourceVideoViewModel.this);

        }
    });


    /**
     * 提取音频
     */
    public BindingCommand exMusicClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            ActivityUtil.start(ArouterPath.extract_audio_activity, LiveDataBusData.chosePath, fileModelObservableFiel.get().song.getPath());

//            reNameLiveData.postValue(ResourceVideoViewModel.this);
            dissmissLiveData.postValue(ResourceVideoViewModel.this);

        }
    });



    /**
     * 打开方式
     */
    public MutableLiveData<ResourceVideoViewModel> openLiveData = new MutableLiveData<>();
    public BindingCommand openClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            openLiveData.postValue(ResourceVideoViewModel.this);
            dissmissLiveData.postValue(ResourceVideoViewModel.this);


        }
    });


    /**
     * 分享
     */
    public MutableLiveData<String> shareLiveData=new MutableLiveData<>();
    public BindingCommand shareClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            AndroidShareUtils.shareAudio(getApplication(), fileModelObservableFiel.get().song.getPath());

            shareLiveData.postValue(fileModelObservableFiel.get().song.getPath());
            dissmissLiveData.postValue(ResourceVideoViewModel.this);

        }
    });


    /**
     * 删除
     */
    public MutableLiveData<Boolean> delectLiveData = new MutableLiveData<>();
    public BindingCommand deleteClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            FileUtils.deleteFile(fileModelObservableFiel.get().song.getPath());
            videoList.remove(fileModelObservableFiel.get());
            delectLiveData.postValue(true);
            if (videoList.size() == 0) {
                isEmpty.set(true);
            }

            dissmissLiveData.postValue(ResourceVideoViewModel.this);

        }
    });


    public MutableLiveData<ResourceVideoViewModel> dissmissLiveData = new MutableLiveData<>();


    public ObservableBoolean isUpdate = new ObservableBoolean();

    /**
     * 文件路径
     */
    public MutableLiveData<ResourceVideoViewModel> filePathLiveData=new MutableLiveData<>();
    public BindingCommand filePathClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

//            AndroidShareUtils.shareAudio(getApplication(), fileModelObservableFiel.get().fileModel.getPath(),type.get());

            filePathLiveData.postValue(ResourceVideoViewModel.this);
            dissmissLiveData.postValue(ResourceVideoViewModel.this);



        }
    });


    public ObservableBoolean isCanSeeVideo=new ObservableBoolean(true);
}
