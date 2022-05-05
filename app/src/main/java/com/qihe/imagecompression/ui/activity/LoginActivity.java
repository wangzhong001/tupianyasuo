package com.qihe.imagecompression.ui.activity;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.databinding.ActivityLoginBinding;
import com.qihe.imagecompression.view.LoadingDialog;
import com.qihe.imagecompression.viewmodel.FeaturesViewModel;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.login.UserUtil;

/**
 * Created by lipei on 2020/6/13.
 */
@Route(path = ArouterPath.login_activity)
public class LoginActivity extends BaseActivity<ActivityLoginBinding,FeaturesViewModel> {

    private LoadingDialog loadingDialog;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
    }

    @Override
    public boolean isShowBannerAd() {
        return false;
    }

    @Override
    public boolean isShowNativeAd() {
        return false;
    }

    @Override
    public boolean isShowVerticllAndStimulateAd() {
        return false;
    }

    @Override
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }


    @Override
    public void initData() {
        super.initData();
        loadingDialog=new LoadingDialog(this);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.loginLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable FeaturesViewModel featuresViewModel) {
                UserUtil.login(featuresViewModel.phoneUmber.get(),featuresViewModel.passWord.get(), new UserUtil.CallBack() {
                    @Override
                    public void onSuccess() {
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
        });
    }
}
