package com.xinqidian.adcommon.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import com.xinqidian.adcommon.login.WxpayModel;

import java.util.Map;

/**
 * Created by lipei on 2018/12/17.
 */

public class PayUtils {
    /**
     * 支付类型
     */
    public static final int PAY_TYPE_WX = 1;
    public static final int PAY_TYPE_ALI = 2;
    /**
     * 支付宝返回参数
     */
    final static int SDK_PAY_FLAG = 1001;

    private static PayUtils instance;


    private PayUtils() {

    }


    public static PayUtils getInstance() {
        if (instance == null) {
            instance = new PayUtils();
        }
        return instance;
    }


    public static void ailPay(String result, Activity activity) {


        toAliPay(result, activity);

    }

    public static void wxPay(WxpayModel.DataBean data, Context activity) {

        toWXPay(data, activity);

    }


    /**
     * 微信支付
     *
     * @param bean
     */
    private static void toWXPay(WxpayModel.DataBean bean, Context activity) {
//        //result中是重服务器请求到的签名后各个字符串，可自定义
//        //result是服务器返回结果
//        PayReq request = new PayReq();
//        request.appId = "wxfe2fa2f264353d7d3";
//        request.partnerId = "1494934532";
//        request.prepayId = "wx201802011023453926020588351720";
//        request.packageValue = "Sign=WXPay";
//        request.nonceStr = "4cdCo3o4453xhGPpv";
//        request.timeStamp = System.currentTimeMillis() / 1000 + "";
//        request.sign = "F8A42CF827345377A34646ADD9E321FAB4";
//        boolean send = getWxapi().sendReq(request);

        /**
         *
         * "appid": "wx7f3060cd7200e2e0",
         *          *      "noncestr": "wODZdttB7KND88sOYk0z1t6xznufC4r0",
         *          *      "partnerid": "1606984314",
         *          *      "prepayid": "wx17192129192803ae64c181d21c90610000",
         *          *      "sign": "E48C515203E2734F7C0DE251714DFB12",
         *          *      "timestamp": "1615980089"
         */

        Log.i("WxpayModel.DataBean", bean.getAppid());


        IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, bean.getAppid());  //应用ID 即微信开放平台审核通过的应用APPID
        wxapi.registerApp(bean.getAppid());    //应用ID
        PayReq payReq = new PayReq();
        payReq.appId = bean.getAppid();        //应用ID
        payReq.partnerId = bean.getPartnerid();      //商户号 即微信支付分配的商户号
        payReq.prepayId = bean.getPrepayid();        //预支付交易会话ID
        payReq.packageValue = "Sign=WXPay";    //扩展字段
        payReq.nonceStr = bean.getNoncestr();        //随机字符串不长于32位。
        payReq.timeStamp = bean.getTimestamp(); //时间戳
        payReq.sign = "MD5";             //签名
        wxapi.sendReq(payReq);

    }


    /**
     * 支付宝
     */
    private static void toAliPay(String result, final Activity activity) {
        //result中是重服务器请求到的签名后字符串,赋值，此处随便写的
        final String orderInfo = result;

//        final String orderInfo = "alipay_sdk=alipay-sdk-java-3.4.49.ALL&app_id=2018120662450585&biz_content=%7B%22extend_params%22%3A%7B%22sys_service_provider_id%22%3A%222088331738103728%22%7D%2C%22out_trade_no%22%3A%22198000006%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%9C%AC%E8%81%AA%E5%9B%BD%E6%97%85-%E6%97%85%E6%B8%B8%E5%AE%9A%E5%88%B6%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpansy.wicp.io%2Fapi%2Falipay%2FpayMessCallBack.json&sign=faNsFkxzep%2FIGTwU6lGg5ZbyWaUOuWDOENQNnn6ZqOa%2FXavucR1vXKkajdtbZpG6NUKOT1YlBeCnCx00yTj76xJ8lEx95VPkMmPFOblfBl%2BJjul%2FrzMQ%2B0pV4MjHLTaD4EOkhPOxugVMfFQthoJuC7ao0VoOFinNxhLdMgbc01g5M%2F6q683CvQKPKe%2FU4AUJL21e3gPJX9zqyaAIlWu6BbiiLvEkNKVksYCwIQXrmuJ7hOR%2BolwHlMeRHc%2FUndodcsXoqrb7jcFvu6A8tv%2FkENX51a0kkZWvl%2B4VQZ7tIcOKMWuhuzUIWMXIv35KGT%2FsoNuVuW8fWZkW0NOC800CfA%3D%3D&sign_type=RSA2&timestamp=2018-12-18+09%3A30%3A58&version=1.0";   // 订单信息
//        AlipayRunable alipayRunable=new AlipayRunable(activity,orderInfo);
//        // 必须异步调用
//        Thread payThread = new Thread(alipayRunable);
//        payThread.start();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();


    }


    /**
     * 支付宝状态
     * 9000 订单支付成功
     * 8000 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     * 4000 订单支付失败
     * 5000 重复请求
     * 6001 用户中途取消
     * 6002 网络连接出错
     * 6004 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     * 其它   其它支付错误
     */
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    Map<String, String> data = (Map<String, String>) msg.obj;
                    String resultStatus = data.get("resultStatus");
                    String resultInfo = data.get("memo");
//                    PayResultMap<String, String> payResult = new PayResult(() msg.obj);
//                    /**
//                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
//                     */
//                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//                    String resultStatus = payResult.getResultStatus();
//                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        SharedPreferencesUtil.setVip(true);
                        SharedPreferencesUtil.alipaySuccess();

                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.show("取消支付");//                        PayListenerUtils.getInstance(mContext).addCancel();
                    } else {
                        ToastUtils.show(resultInfo);
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        showMessage("支付失败");
//                        PayListenerUtils.getInstance(mContext).addError();
                    }
                    break;
                }
            }
        }


    };

//    private static void showMessage(String str) {
//        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
//    }


}
