package com.qihe.imagecompression.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qihe.imagecompression.app.AdApplcation;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity123";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.pay_result);
        Log.i(TAG,"onCreate");
        //api = AdApplcation.getWXapi();
        api = WXAPIFactory.createWXAPI(this, "wxa481de9047d8bcaa");
        api.registerApp("wxa481de9047d8bcaa");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }



    @Override
    public void onResp(BaseResp resp) {
        int code = resp.errCode;
        Log.i(TAG,"onResp");
        Log.i(TAG,code+"");

        switch (code) {
            case 0://支付成功后的界面
               // ToastUtils.show("支付成功");
                // Constants.WEI_XIN_PAY_RESULT = 0; //这个是调起微信支付后，zai
                //返回主页面 然后在跳转至订单页面
                SharedPreferencesUtil.alipaySuccess();
                this.finish();
                break;
            case -1:
                //ToastUtil.showToast(getApplicationContext(), "签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、您的微信账号异常等。");
                ToastUtils.show("支付失败");
                // Constants.WEI_XIN_PAY_RESULT = -1;

                this.finish();
                break;
            case -2://用户取消支付后的界面
                ToastUtils.show("您已取消支付");
                // Constants.WEI_XIN_PAY_RESULT = -2;
                this.finish();
                break;
        }
        //微信支付后续操作，失败，成功，取消
    }
}
