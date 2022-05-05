package com.xinqidian.adcommon.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.BaseServiceApi;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.http.BaseResponse;
import com.xinqidian.adcommon.http.ResponseThrowable;
import com.xinqidian.adcommon.http.net.NetWorkSubscriber;
import com.xinqidian.adcommon.http.util.RetrofitClient;
import com.xinqidian.adcommon.http.util.RetrofitNewClient;
import com.xinqidian.adcommon.http.util.RxUtils;
import com.xinqidian.adcommon.util.AesUtil;
import com.xinqidian.adcommon.util.AppUtils;
import com.xinqidian.adcommon.util.PayUtils;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

/**
 * Created by lipei on 2020/6/6.
 */

public class UserUtil {

    /**
     * 登录
     *
     * @param account
     * @param password
     */
    public static void login(final String account, final String password, final CallBack callBack, final Dialog dialog) {

        LoginRequestBody loginRequestBody = new LoginRequestBody();
        loginRequestBody.setLoginName(account);
        loginRequestBody.setAppCode(Contants.APP_CODE);

        loginRequestBody.setPassword(AesUtil.aesEncrypt(password, AesUtil.KEY));
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .login(loginRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return dialog;
                    }

                    @Override
                    protected void onSuccess(BaseResponse o) {
                        if (o.getCode() == AppConfig.user_not_regist) {
                            //用户未注册
                            regist(account, password, new NetWorkSubscriber<BaseResponse>() {
                                @Override
                                protected Dialog showDialog() {
                                    return dialog;
                                }

                                @Override
                                protected void onSuccess(BaseResponse o) {
                                    if (o.getCode() == AppConfig.success_code) {
                                        SharedPreferencesUtil.toLogin();
                                        getUserInfo();
                                        if (callBack != null) {
                                            callBack.onSuccess();
                                        }

                                    } else {
                                        ToastUtils.show(o.getMsg());
                                        if (callBack != null) {
                                            callBack.onFail();
                                        }

                                    }

                                }
                            });

                        } else if (o.getCode() == AppConfig.success_code) {
                            SharedPreferencesUtil.toLogin();
                            getUserInfo();
                            if (callBack != null) {
                                callBack.onSuccess();
                            }

                        } else {
                            ToastUtils.show(o.getMsg());
                            if (callBack != null) {
                                callBack.onFail();
                            }

                        }

                    }
                });


    }


    /**
     * 只登录
     *
     * @param account
     * @param password
     */
    public static void login1(final String account, final String password, final LoginCallBack callBack, final Dialog dialog) {

        LoginRequestBody loginRequestBody = new LoginRequestBody();
        loginRequestBody.setLoginName(account);
        loginRequestBody.setAppCode(Contants.APP_CODE);

        loginRequestBody.setPassword(AesUtil.aesEncrypt(password, AesUtil.KEY));
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .login(loginRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return dialog;
                    }

                    @Override
                    protected void onSuccess(BaseResponse o) {
                        if (o.getCode() == AppConfig.user_not_regist) {

                            if (callBack != null) {
                                callBack.noRegist();
                            }

                        } else if (o.getCode() == AppConfig.success_code) {
                            SharedPreferencesUtil.toLogin();
                            SharedPreferencesUtil.setLoginType(false);
                            getUserInfo();
                            if (callBack != null) {
                                callBack.onSuccess();
                            }

                        } else {
                            ToastUtils.show(o.getMsg());
                            if (callBack != null) {
                                callBack.onFail();
                            }

                        }

                    }
                });
    }


    /**
     * 微信登录
     *
     * @param account
     *
     */
    public static void wxLogin(final String account, final LoginCallBack callBack, final Dialog dialog) {

     /*   WxLoginRequestBody wxLoginRequestBody = new WxLoginRequestBody();
        wxLoginRequestBody.setAppCode(Contants.APP_CODE);
        wxLoginRequestBody.setCode(account);*/
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .wxLogin(Contants.APP_CODE,account)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return dialog;
                    }

                    @Override
                    protected void onSuccess(BaseResponse o) {
                        if (o.getCode() == AppConfig.user_not_regist) {

                            if (callBack != null) {
                                callBack.noRegist();
                            }


                        } else if (o.getCode() == AppConfig.success_code) {
                            SharedPreferencesUtil.toLogin();
                            SharedPreferencesUtil.setLoginType(true);
                            getWxUserInfo(callBack);


                        } else {
                            ToastUtils.show(o.getMsg());
                            if (callBack != null) {
                                callBack.onFail();
                            }

                        }

                    }
                });


    }

    /**
     * 只注册
     */

    public static void regist1(final String account, final String password, final CallBack callBack, final Dialog dialog) {
        regist(account, password, new NetWorkSubscriber<BaseResponse>() {
            @Override
            protected Dialog showDialog() {
                return dialog;
            }

            @Override
            protected void onSuccess(BaseResponse o) {
                if (o.getCode() == AppConfig.success_code) {
                    SharedPreferencesUtil.toLogin();
                    SharedPreferencesUtil.setLoginType(false);
                    getUserInfo();
                    if (callBack != null) {
                        callBack.onSuccess();
                    }

                } else {
                    ToastUtils.show(o.getMsg());
                    if (callBack != null) {
                        callBack.onFail();
                    }

                }

            }
        });
    }


    /**
     * 退出登录
     */
    public static void exitLogin(){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .exitLogin()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        if(baseResponse.getCode()==AppConfig.success_code){
                            SharedPreferencesUtil.exitLogin();
                        }else {
                            ToastUtils.show(baseResponse.getMsg());
                        }

                    }
                });
    }

    /***
     *
     *账号注销
     * @param callBack
     */
    public static void UserClean(final CallBack callBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .userClean()
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        if(baseResponse.getCode()==AppConfig.success_code){
                            ToastUtils.show("注销成功");
                            SharedPreferencesUtil.exitLogin();
                            if(callBack!=null){
                                callBack.onSuccess();
                            }
                        }else if(baseResponse.getCode()==AppConfig.login_fail){
                            SharedPreferencesUtil.exitLogin();

                        }else {
                            ToastUtils.show("注销失败");

                        }
                    }
                });
    }


    /**
     * 微信登录获取用户信息
     */
    public static void getWxUserInfo(final LoginCallBack callBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getWxUserInfo()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<WxUserModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        SharedPreferencesUtil.exitLogin();

                    }

                    @Override
                    protected void onSuccess(WxUserModel userModel) {
                        if(userModel.getCode()==AppConfig.success_code){
                            if(userModel.getData()!=null){
                                int userLevel = userModel.getData().getUserLevel();
                                if (userLevel != 0) {
                                    //是会员
                                    SharedPreferencesUtil.setVip(true);
                                } else {
                                    //不是会员
                                    SharedPreferencesUtil.setVip(false);
                                }
                                if (callBack != null) {
                                    callBack.onSuccess();
                                }
                                // LiveDataBus.get().with(LiveBusConfig.userData,WxUserModel.DataBean.class).postValue(userModel.getData());

                            }

                        }else {
                            SharedPreferencesUtil.exitLogin();
                        }

                    }
                });
    }


    /**
     * 获取用户信息
     */
    public static void getUserInfo(){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getUserInfo()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<UserModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        SharedPreferencesUtil.exitLogin();

                    }

                    @Override
                    protected void onSuccess(UserModel userModel) {
                        if(userModel.getCode()==AppConfig.success_code){
                            if(userModel.getData()!=null){
                                int userLevel = userModel.getData().getUserLevel();
                                if (userLevel != 0) {
                                    //是会员
                                    SharedPreferencesUtil.setVip(true);
                                } else {
                                    //不是会员
                                    SharedPreferencesUtil.setVip(false);
                                }

                                LiveDataBus.get().with(LiveBusConfig.userData,UserModel.DataBean.class).postValue(userModel.getData());

                            }

                        }else {
                            SharedPreferencesUtil.exitLogin();
                        }

                    }
                });
    }



    public interface UserinfoCallBack{
        void getUserInfo(UserModel.DataBean dataBean);
    }
    public interface WxUserinfoCallBack{
        void getWxUserInfo(WxUserModel.DataBean dataBean);
    }

    /**
     * 获取用户信息
     */
    public static void getUserInfoCallBack(final UserinfoCallBack userinfoCallBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getUserInfo()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<UserModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        SharedPreferencesUtil.exitLogin();

                    }

                    @Override
                    protected void onSuccess(UserModel userModel) {
                        if(userModel.getCode()==AppConfig.success_code){
                            if(userModel.getData()!=null){
                                int userLevel = userModel.getData().getUserLevel();
                                if (userLevel != 0) {
                                    //是会员
                                    SharedPreferencesUtil.setVip(true);
                                } else {
                                    //不是会员
                                    SharedPreferencesUtil.setVip(false);
                                }

                                if(userinfoCallBack!=null){
                                    userinfoCallBack.getUserInfo(userModel.getData());
                                }
                            }

                        }else {
                            SharedPreferencesUtil.exitLogin();
                        }

                    }
                });
    }



    /**
     * 微信登录获取用户信息
     */
    public static void getWxUserInfoCallBack(final WxUserinfoCallBack userinfoCallBack){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getWxUserInfo()
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<WxUserModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        SharedPreferencesUtil.exitLogin();

                    }

                    @Override
                    protected void onSuccess(WxUserModel userModel) {
                        if(userModel.getCode()==AppConfig.success_code){
                            if(userModel.getData()!=null){
                                int userLevel = userModel.getData().getUserLevel();
                                if (userLevel != 0) {
                                    //是会员
                                    SharedPreferencesUtil.setVip(true);
                                } else {
                                    //不是会员
                                    SharedPreferencesUtil.setVip(false);
                                }

                                if(userinfoCallBack!=null){
                                    userinfoCallBack.getWxUserInfo(userModel.getData());
                                }
                            }

                        }else {
                            SharedPreferencesUtil.exitLogin();
                        }

                    }
                });
    }



    /**
     *
     * @param mercdName 会员订阅名称 一个月会员、三个月会员、一年会员
     * @param mercdWorth 订阅金额
     * @param orderNumber 订阅类型  1:一个月  3:3个月   12 一年
     * @param activity
     * @param callBack
     */

    public static void alipayOrder(String mercdName, String mercdWorth, int orderNumber, final Activity activity, final CallBack callBack) {
        AllipayRequestBody allipayRequestBody = new AllipayRequestBody();
        allipayRequestBody.setMercdName(mercdName);
        allipayRequestBody.setMercdWorth(mercdWorth);
        allipayRequestBody.setOrderNumber(orderNumber);
        allipayRequestBody.setRemark(Contants.PLATFORM+"--版本:"+ AppUtils.getVersionName(activity));

        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .alipayCreateOrdr(allipayRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<AlipayModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(AlipayModel alipayModel) {
                        if (alipayModel.getCode() == AppConfig.success_code) {
                            if (alipayModel.getData() != null) {
                                String orderString = alipayModel.getData().getOrderStr();
                                PayUtils.ailPay( orderString, activity);
                            } else {
                                ToastUtils.show("支付失败");

                            }

                        } else if (alipayModel.getCode()==AppConfig.login_fail) {

                            if(callBack!=null){
                                callBack.loginFial();
                            }
                            SharedPreferencesUtil.exitLoginNotSend();

                            //登录失败

                        } else {
                            ToastUtils.show(alipayModel.getMsg());
                        }

                    }
                });
    }
    /**
     * wxCreateOrdr
     */

    public static void wxOrder(String mercdName, String mercdWorth, int orderNumber, final Context activity, final CallBack callBack) {
        WxPayRequestBody allipayRequestBody = new WxPayRequestBody();
        allipayRequestBody.setAppCode(Contants.APP_CODE);
        allipayRequestBody.setMercdName(mercdName);
        allipayRequestBody.setMercdWorth(mercdWorth);
        allipayRequestBody.setOrderNumber(orderNumber);
        allipayRequestBody.setRemark(Contants.PLATFORM+"--版本:"+ AppUtils.getVersionName(activity));

        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .wxCreateOrdr(allipayRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(new NetWorkSubscriber<WxpayModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(WxpayModel alipayModel) {
                        if (alipayModel.getCode() == AppConfig.success_code) {
                            if (alipayModel.getData() != null) {
                                WxpayModel.DataBean data = alipayModel.getData();
                                PayUtils.wxPay(data, activity);
                            } else {
                                ToastUtils.show("支付失败");

                            }

                        } else if (alipayModel.getCode()==AppConfig.login_fail) {

                            if(callBack!=null){
                                callBack.loginFial();
                            }
                            SharedPreferencesUtil.exitLoginNotSend();

                            //登录失败

                        } else {
                            ToastUtils.show(alipayModel.getMsg());
                        }

                    }
                });
    }

    /**
     * 注册
     *
     * @param account
     * @param password
     * @param observable
     */
    public static void regist(String account, String password, NetWorkSubscriber observable) {

        RegistRequestBody registRequestBody = new RegistRequestBody();
        registRequestBody.setAccount(account);
        registRequestBody.setPassword(AesUtil.aesEncrypt(password, AesUtil.KEY));
        registRequestBody.setRePassword(AesUtil.aesEncrypt(password, AesUtil.KEY));
        registRequestBody.setAppCode(Contants.APP_CODE);


        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .regist(registRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(observable);


    }





    /**
     * 支付宝成功回调
     * @param observable
     */
    public static void alipaySuccess(NetWorkSubscriber observable) {




        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .alipaySuceessCallBack()
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(observable);


    }
    public interface LoginCallBack {


        void noRegist();

        void onSuccess();

        void onFail();

        void loginFial();
    }


    public interface CallBack {
        void onSuccess();

        void onFail();

        void loginFial();
    }




    /**
     *
     * @param keys
     * 埋点
     *
     */
    public static void getKey(String keys, final KeyInterFace keyInterFace){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .getKey(keys)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<KeysModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onFail(ResponseThrowable responseThrowable) {
                        super.onFail(responseThrowable);
                        if(keyInterFace!=null){
                            keyInterFace.onFail();
                        }
                    }

                    @Override
                    protected void onSuccess(KeysModel keysModel) {
                        if(keysModel.getCode()==AppConfig.success_code){
                            if(keyInterFace!=null){
                                keyInterFace.onSuccess(keysModel.getData());
                            }
                        }else {
                            if(keyInterFace!=null){
                                keyInterFace.onFail();
                            }
                        }

                    }
                });
    }



    /**
     *
     * @param keys
     * @param number
     * 埋点
     *
     */
    public static void setKey(String keys,int number){
        RetrofitClient.getInstance().create(BaseServiceApi.class)
                .setKey(keys,number)
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<BaseResponse>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {

                    }
                });
    }




    /**
     * 获取是否展示横幅广告
     */
    public static void isShowBanner(final SureInterface sureInterface){
        RetrofitNewClient.getInstance().create(BaseServiceApi.class)
                .isShowBannnerAd()
                .compose(RxUtils.schedulersTransformer())
                .subscribeWith(new NetWorkSubscriber<CanSeeAdModel>() {
                    @Override
                    protected Dialog showDialog() {
                        return null;
                    }

                    @Override
                    protected void onSuccess(CanSeeAdModel canSeeAdModel) {
                        if(sureInterface!=null){
                            sureInterface.sure(canSeeAdModel.isNeedTwoice());
                        }
                        SharedPreferencesUtil.setParam(AppConfig.musicEractBannerAd,canSeeAdModel.isIsShowBannerAd());
                        SharedPreferencesUtil.setParam(AppConfig.musicEractCanSeeVideo,canSeeAdModel.isIsCanSeeAd());
                        SharedPreferencesUtil.setParam(AppConfig.musicEractHomeCanSeeVideo,canSeeAdModel.isHomeCanSeeAd());



                    }
                });
    }


    public interface SureInterface{
        void sure(boolean sure);
    }



    public interface KeyInterFace{
        void onSuccess(int number);

        void onFail();
    }

}
