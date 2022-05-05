package com.qihe.imagecompression.model;

import java.io.Serializable;

/**
 * Created by lipei on 2020/6/11.
 */

public class FeaturesModel implements Serializable{
    private String name;

    private String path;

    private int type;

    private int postion;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }
}
