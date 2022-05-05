package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2020/6/9.
 */

public class WxPayRequestBody {
    private String appCode;
    private String mercdName;
    private String mercdWorth;
    private int orderNumber;
    private String remark;
    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getMercdName() {
        return mercdName;
    }

    public void setMercdName(String mercdName) {
        this.mercdName = mercdName;
    }

    public String getMercdWorth() {
        return mercdWorth;
    }

    public void setMercdWorth(String mercdWorth) {
        this.mercdWorth = mercdWorth;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
 /* "appCode": "string",
          "mercdName": "string",
          "mercdWorth": 0,
          "orderNumber": 0,
          "remark": "string"*/



}
