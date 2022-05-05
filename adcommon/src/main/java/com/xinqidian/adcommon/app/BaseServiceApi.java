package com.xinqidian.adcommon.app;


import com.xinqidian.adcommon.http.BaseResponse;
import com.xinqidian.adcommon.login.AlipayModel;
import com.xinqidian.adcommon.login.AllipayRequestBody;
import com.xinqidian.adcommon.login.CanSeeAdModel;
import com.xinqidian.adcommon.login.KeysModel;
import com.xinqidian.adcommon.login.LoginRequestBody;
import com.xinqidian.adcommon.login.RegistRequestBody;
import com.xinqidian.adcommon.login.UserModel;
import com.xinqidian.adcommon.login.WxPayRequestBody;
import com.xinqidian.adcommon.login.WxUserModel;
import com.xinqidian.adcommon.login.WxpayModel;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lipei on 2019/1/9.
 */

public interface BaseServiceApi {

    /**
     * 登录
     * @param loginRequestBody
     * @return
     */
    @POST(BaseUrlApi.login)
    Observable<BaseResponse> login(@Body LoginRequestBody loginRequestBody);

    /**
     * 微信登录
     *
     * @return
     */
    @POST(BaseUrlApi.wxLogin)
    Observable<BaseResponse> wxLogin(@Query("appCode") String appCode,@Query("code") String code);



    /**
     * 退出登录
     * @return
     */
    @GET(BaseUrlApi.exitLogin)
    Observable<BaseResponse> exitLogin();

    /**
     * 用户注销
     * @return
     */
    @GET(BaseUrlApi.userClean)
    Observable<BaseResponse> userClean();



    /**
     * 注册
     * @param loginRequestBody
     * @return
     */
    @POST(BaseUrlApi.regist)
    Observable<BaseResponse> regist(@Body RegistRequestBody loginRequestBody);





    /**
     * 支付宝创建订单
     * @param allipayRequestBody
     * @return
     */
    @POST(BaseUrlApi.alipayCreateOrdr)
    Observable<AlipayModel> alipayCreateOrdr(@Body AllipayRequestBody allipayRequestBody);

    /**
     * 微信支付创建订单
     * @param allipayRequestBody
     * @return
     */
    @POST(BaseUrlApi.wxCreateOrdr)
    Observable<WxpayModel> wxCreateOrdr(@Body WxPayRequestBody allipayRequestBody);



    /**
     * 支付宝支付成功回调
     * @return
     */
    @POST(BaseUrlApi.alipaySuccessCall)
    Observable<BaseResponse> alipaySuceessCallBack();




    /**
     * 获取用户信息
     * @return
     */
    @GET(BaseUrlApi.getUserInfo)
    Observable<UserModel> getUserInfo();


    /**
     * 微信登录获取用户信息
     * @return
     */
    @GET(BaseUrlApi.getWxUserInfo)
    Observable<WxUserModel> getWxUserInfo();




    /**
     * 读key
     * @return
     */
    @GET(BaseUrlApi.getKey)
    Observable<KeysModel> getKey(@Query("keys") String keys);



    /**
     * 存key
     * @return
     */
    @POST(BaseUrlApi.setKey)
    Observable<BaseResponse> setKey(@Query("keys") String keys,@Query("vals") int vals);





    /**
     * 是否展示横幅广告
     * @return
     */
    @GET(BaseUrlApi.isShowBannerAd)
    Observable<CanSeeAdModel> isShowBannnerAd();


//    /**
//     * 获取最新版本
//     *
//     */
//
//    @GET(BaseUrlApi.new_version)
//    Observable<UpdateBean> getNewVersion(@Query("platform") String desc, @Header("Accept-Language") String lang);





}
