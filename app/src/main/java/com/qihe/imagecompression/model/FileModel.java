package com.qihe.imagecompression.model;

/**
 * Created by lipei on 2020/6/15.
 */

public class FileModel {
    private String name;

    private String path;

    private String size;

    private boolean video;

    public boolean isCache;

    private boolean pic;



    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isPic() {
        return pic;
    }

    public void setPic(boolean pic) {
        this.pic = pic;
    }
}
