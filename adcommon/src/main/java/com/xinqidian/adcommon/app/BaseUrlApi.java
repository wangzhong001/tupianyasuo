package com.xinqidian.adcommon.app;

/**
 * Created by lipei on 2019/1/9.
 */

public interface BaseUrlApi {
    /**
     * 登录
     */
    String login="/api/user/login/login.json";

    /**
     * 微信登录
     */
    String wxLogin="/api/user/login/wxlogin.json ";

    /**
     * 退出登录
     */
    String exitLogin="/api/user/login/loginOut.json";

    /**
     *用户注销
     */
    String userClean="/api/user/info/userClear.json";


    /**
     * 注册
     */
    String regist="/api/user/register/account.json";



    /**
     *支付宝创建订单
     */
    String alipayCreateOrdr="/api/payOrder/createAliOrder.json";


    /**
     *微信支付创建订单
     */
    String wxCreateOrdr="/api/payOrder/createWxOrder.json";

    /**
     *支付宝支付成功回调
     */
    String alipaySuccessCall="/api/alipay/payMessCallBack.json";



    /**
     *获取用户信息
     */
    String getUserInfo="/api/user/info/home.json";

    /**
     *微信登录获取用户信息
     */
    String getWxUserInfo="/api/user/info/wxhome.json";

    /**
     *读key埋点
     */
    String getKey="/api/user/login/readParameter.json";




    /**
     *存key埋点
     */
    String setKey="/api/user/login/writeParameter.json";




    /**
     *是否展示横幅广告
     */
    String isShowBannerAd="/mock/3d91b66011024e11f4bd5da14787bcb5/voiceErctBannerAd";

//    /**
//     * 获取最新版本
//     */
//    String new_version="/underwriter/getVersion.json";




}
