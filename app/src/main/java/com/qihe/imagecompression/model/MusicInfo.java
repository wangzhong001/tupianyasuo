package com.qihe.imagecompression.model;

import java.io.Serializable;

/**
 * Created by lijunyan on 2016/12/19.
 *
 *
 *
 MediaStore.Audio.Media._ID,                 //歌曲ID
 MediaStore.Audio.Media.TITLE,               //歌曲名称
 MediaStore.Audio.Media.ARTIST,              //歌曲歌手
 MediaStore.Audio.Media.ALBUM,               //歌曲的专辑名
 MediaStore.Audio.Media.DURATION,            //歌曲时长
 MediaStore.Audio.Media.DISPLAY_NAME,        //歌曲文件的名称
 MediaStore.Audio.Media.DATA};               //歌曲文件的全路径
 */

public class MusicInfo implements Serializable {

    private int id;
    private String name;
    private String singer;
    private String album;
    private String duration;
    private String path;
    private String parentPath; //父目录路径
    private int love; //1设置我喜欢 0未设置
    private String firstLetter;
    private String time;
    private String time1;
    private String videoPic;
    private boolean hasChose;
    private int postion;

    private boolean isRun;

    private boolean hasChange;


    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

//    @Override
//    public int compareTo(Object o) {
//        MusicInfo info = (MusicInfo)o;
//        if (info.getFirstLetter().equals("#"))
//            return -1;
//        if (firstLetter.equals("#"))
//            return 1;
//        return firstLetter.compareTo(info.getFirstLetter());
//    }
//
//    @Override
//    public String toString() {
//        return "MusicInfo{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", singer='" + singer + '\'' +
//                ", album='" + album + '\'' +
//                ", duration='" + duration + '\'' +
//                ", path='" + path + '\'' +
//                ", parentPath='" + parentPath + '\'' +
//                ", love=" + love +
//                ", firstLetter='" + firstLetter + '\'' +
//                '}';
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.id);
//        dest.writeString(this.name);
//        dest.writeString(this.singer);
//        dest.writeString(this.album);
//        dest.writeString(this.duration);
//        dest.writeString(this.path);
//        dest.writeString(this.parentPath);
//        dest.writeInt(this.love);
//        dest.writeString(this.firstLetter);
//    }

//    public MusicInfo() {
//    }
//
//    protected MusicInfo(Parcel in) {
//        this.id = in.readInt();
//        this.name = in.readString();
//        this.singer = in.readString();
//        this.album = in.readString();
//        this.duration = in.readString();
//        this.path = in.readString();
//        this.parentPath = in.readString();
//        this.love = in.readInt();
//        this.firstLetter = in.readString();
//    }
//
//    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
//        @Override
//        public MusicInfo createFromParcel(Parcel source) {
//            return new MusicInfo(source);
//        }
//
//        @Override
//        public MusicInfo[] newArray(int size) {
//            return new MusicInfo[size];
//        }
//    };

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(String videoPic) {
        this.videoPic = videoPic;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public boolean isHasChose() {
        return hasChose;
    }

    public void setHasChose(boolean hasChose) {
        this.hasChose = hasChose;
    }

    public boolean isHasChange() {
        return hasChange;
    }

    public void setHasChange(boolean hasChange) {
        this.hasChange = hasChange;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }
}

