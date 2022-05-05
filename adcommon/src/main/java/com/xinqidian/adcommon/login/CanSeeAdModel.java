package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2020/8/7.
 */

public class CanSeeAdModel {


    /**
     * isCanSeeAd : true
     * isShowBannerAd : true
     */

    private boolean isCanSeeAd;
    private boolean isShowBannerAd;
    private boolean isNeedTwoice;
    private boolean isHomeCanSeeAd;

    public boolean isIsCanSeeAd() {
        return isCanSeeAd;
    }

    public void setIsCanSeeAd(boolean isCanSeeAd) {
        this.isCanSeeAd = isCanSeeAd;
    }

    public boolean isIsShowBannerAd() {
        return isShowBannerAd;
    }

    public void setIsShowBannerAd(boolean isShowBannerAd) {
        this.isShowBannerAd = isShowBannerAd;
    }

    public boolean isNeedTwoice() {
        return isNeedTwoice;
    }

    public void setNeedTwoice(boolean needTwoice) {
        isNeedTwoice = needTwoice;
    }

    public boolean isHomeCanSeeAd() {
        return isHomeCanSeeAd;
    }

    public void setHomeCanSeeAd(boolean homeCanSeeAd) {
        isHomeCanSeeAd = homeCanSeeAd;
    }
}
