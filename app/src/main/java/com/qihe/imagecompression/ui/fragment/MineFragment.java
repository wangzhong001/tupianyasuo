package com.qihe.imagecompression.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.databinding.FragmentMineBinding;
import com.qihe.imagecompression.util.AndroidShareUtils;
import com.qihe.imagecompression.util.AppUpdateUtils;
import com.qihe.imagecompression.util.TimeUtil;
import com.qihe.imagecompression.viewmodel.FeaturesViewModel;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseFragment;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.login.UserModel;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.ui.activity.FeedBackActivity;
import com.xinqidian.adcommon.ui.activity.WebViewActivity;
import com.xinqidian.adcommon.util.AppUtils;
import com.xinqidian.adcommon.util.JumpUtils;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;

/**
 * Created by lipei on 2020/6/11.
 */

public class MineFragment extends BaseFragment<FragmentMineBinding,FeaturesViewModel> {

    private JumpUtils jumpUtils ;


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_mine;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
    }

    @Override
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }



    public static MineFragment newStance(){
        MineFragment homeFragment=new MineFragment();
        return homeFragment;
    }


    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if(isVisible){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewModel.isLogin.set(SharedPreferencesUtil.isLogin());
                    if(SharedPreferencesUtil.isLogin()){
                        UserUtil.getUserInfoCallBack(new UserUtil.UserinfoCallBack() {
                            @Override
                            public void getUserInfo(UserModel.DataBean dataBean) {
                                viewModel.userName.set(dataBean.getMobile());
                                viewModel.isVip.set(SharedPreferencesUtil.isVip());
                                if(viewModel.isVip.get()){
                                    binding.timeTv.setText("到期时间:"+ TimeUtil.timeStamp2Date(dataBean.getExpireDate(),TimeUtil.TIME));
                                }else {
                                    binding.timeTv.setText("");

                                }
                                if(dataBean.getUserLevel()==1){
                                    binding.vipIv.setImageResource(R.drawable.huangjin_icon);
                                    binding.vipTv.setText("黄金会员");
                                }else if(dataBean.getUserLevel()==2){
                                    binding.vipIv.setImageResource(R.drawable.baijin_icon);
                                    binding.vipTv.setText("白金会员");

                                }else if(dataBean.getUserLevel()==2){
                                    binding.vipIv.setImageResource(R.drawable.zuanshi_icon);
                                    binding.vipTv.setText("钻石会员");

                                }
                            }
                        });

                    }



                    isFirst=true;
                }
            },500);

        }
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
    public void setLoginState(boolean loginState) {
        super.setLoginState(loginState);
        if(loginState){

        }else {
            viewModel.isVip.set(false);
            viewModel.userName.set("点击登录");
        }

        viewModel.isLogin.set(loginState);
    }


    @Override
    public void setUserData(UserModel.DataBean data) {
        super.setUserData(data);
        viewModel.userName.set(data.getMobile());
        viewModel.isLogin.set(true);

        viewModel.isVip.set(SharedPreferencesUtil.isVip());
        if(viewModel.isVip.get()){
            binding.timeTv.setText("到期时间:"+ TimeUtil.timeStamp2Date(data.getExpireDate(),TimeUtil.TIME));
        }else {
            binding.timeTv.setText("");

        }
        if(data.getUserLevel()==1){
            binding.vipIv.setImageResource(R.drawable.huangjin_icon);
            binding.vipTv.setText("黄金会员");
        }else if(data.getUserLevel()==2){
            binding.vipIv.setImageResource(R.drawable.baijin_icon);
            binding.vipTv.setText("白金会员");

        }else if(data.getUserLevel()==2){
            binding.vipIv.setImageResource(R.drawable.zuanshi_icon);
            binding.vipTv.setText("钻石会员");

        }

    }


  

    @Override
    public void setAlipaySuccess(boolean alipaySuccessState) {
        super.setAlipaySuccess(alipaySuccessState);
        if(alipaySuccessState){
            UserUtil.getUserInfo();

        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.feedBackLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable FeaturesViewModel featuresViewModel) {
                startActivity(FeedBackActivity.class);
            }
        });


        binding.pingluRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在线联系
                AndroidShareUtils.goToMarket(getActivity());
            }
        });



        binding.yinsiRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐私政策
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", getResources().getString(R.string.privacy_policy));
                intent.putExtra("url", "http://www.shimukeji.cn/yinsimoban_shengyin.html");
                startActivity(intent);
            }
        });

        binding.yonghuXieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用户协议
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", getResources().getString(R.string.user_agreement));
                intent.putExtra("url", "http://www.shimukeji.cn/yonghu_xieyi.html");
                startActivity(intent);
            }
        });

        binding.ysquanxianRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //系统隐私权限
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", AppUtils.getPackageName(getContext()), null));
                startActivity(intent);
            }
        });


        binding.lianxiKefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jumpUtils==null){
                    jumpUtils=new JumpUtils(getActivity());
                }
                jumpUtils.jumpQQ();
            }
        });

        binding.versoinUpdateRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataBus.get().with(LiveBusConfig.updateApp,Boolean.class).postValue(true);

                AppUpdateUtils.update(getContext(),true);

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        binding.versoinTv.setText(AppUtils.getVersionName(getContext()));
    }
}
