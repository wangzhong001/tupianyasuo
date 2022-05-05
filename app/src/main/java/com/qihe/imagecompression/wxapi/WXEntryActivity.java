package com.qihe.imagecompression.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qihe.imagecompression.view.LoadingDialog;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingDialog = new LoadingDialog(this);

        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, "wx7f3060cd7200e2e0", false);
        iwxapi.handleIntent(getIntent(), this);


    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    /*@Override
        public void onReq(com.tencent.mm.opensdk.modelbase.BaseReq req) {
            super.onReq(req);
        }
    */
    @Override
    public void onResp(BaseResp resp) {
       // super.onResp(resp);

        Log.i("resp.errCode", resp.errCode + "");

        //登录回调
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) resp).code;
                Log.i("resp.errCode", code);
                //获取accesstoken
                login(code);
                // Log.d("fantasychongwxlogin", code.toString()+ "");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                ToastUtils.show("授权失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                ToastUtils.show("登录取消");
                finish();
                break;
            default:
                finish();
                break;
        }


    }

    private void login(String code) {

        UserUtil.wxLogin(code, new UserUtil.LoginCallBack() {
            @Override
            public void noRegist() {
                Log.i("++++++", "用户未注册，请先注册");
                // Toast.makeText(view.getContext(), "用户未注册，请先注册", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                //loginSuccess();
                // Log.i("++++++","登录成功");

                EventBus.getDefault().post("登录成功");

                finish();

            }

            @Override
            public void onFail() {

            }

            @Override
            public void loginFial() {

            }
        }, loadingDialog);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
