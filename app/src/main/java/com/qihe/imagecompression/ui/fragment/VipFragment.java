package com.qihe.imagecompression.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.databinding.ActivityVipBinding;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.viewmodel.FeaturesViewModel;
import com.xinqidian.adcommon.base.BaseFragment;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lipei on 2020/6/11.
 */

public class VipFragment extends BaseFragment<ActivityVipBinding, FeaturesViewModel> {
    private String mercdName = "一年";
    private String mercdWorth = "40.99";
    private int orderNumber = 12;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.activity_vip;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
    }


    public static VipFragment newStance() {
        VipFragment homeFragment = new VipFragment();
        return homeFragment;

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);


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
        SpannableString spannableString01 = new SpannableString("￥239.99");
        SpannableString spannableString02 = new SpannableString("￥59.99");
        SpannableString spannableString03 = new SpannableString("￥19.99");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString01.setSpan(strikethroughSpan, 0, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString02.setSpan(strikethroughSpan, 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString03.setSpan(strikethroughSpan, 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.tv14.setText(spannableString01);
        binding.tv24.setText(spannableString02);
        binding.tv34.setText(spannableString03);

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.rll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mercdName = "一年";
                mercdWorth = "40.99";
                orderNumber = 12;
                setTextColorAndBg(false, binding.rll1, binding.tv11, binding.tv12, binding.tv13, binding.tv14);
                setTextColorAndBg(true, binding.rll2, binding.tv21, binding.tv22, binding.tv23, binding.tv24);
                setTextColorAndBg(true, binding.rll3, binding.tv31, binding.tv32, binding.tv33, binding.tv34);


            }
        });

        binding.rll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mercdName = "三个月";
                mercdWorth = "10.99";
                orderNumber = 3;
                setTextColorAndBg(true, binding.rll1, binding.tv11, binding.tv12, binding.tv13, binding.tv14);
                setTextColorAndBg(false, binding.rll2, binding.tv21, binding.tv22, binding.tv23, binding.tv24);
                setTextColorAndBg(true, binding.rll3, binding.tv31, binding.tv32, binding.tv33, binding.tv34);

            }
        });

        binding.rll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mercdName = "一个月";
                mercdWorth = "4.99";
                orderNumber = 1;
                setTextColorAndBg(true, binding.rll1, binding.tv11, binding.tv12, binding.tv13, binding.tv14);
                setTextColorAndBg(true, binding.rll2, binding.tv21, binding.tv22, binding.tv23, binding.tv24);
                setTextColorAndBg(false, binding.rll3, binding.tv31, binding.tv32, binding.tv33, binding.tv34);
            }
        });//选中后

        binding.tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtil.isLogin()) {

                    UserUtil.alipayOrder(mercdName, mercdWorth, orderNumber, getActivity(), new UserUtil.CallBack() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void loginFial() {

                            ActivityUtil.start(ArouterPath.login_activity);
                        }
                    });
                } else {
                    ActivityUtil.start(ArouterPath.login_activity);

                }

            }
        });
    }



    private void setTextColorAndBg(boolean isClick, RelativeLayout rll, TextView tv1, TextView tv2, TextView tv3, TextView tv4) {
        if (isClick) {
            rll.setBackground(getResources().getDrawable(R.drawable.black_bg));
            tv1.setTextColor(getResources().getColor(R.color.color_F9E7CB));
            tv2.setTextColor(getResources().getColor(R.color.color_F9E7CB));
            tv3.setTextColor(getResources().getColor(R.color.color_F9E7CB));
            tv4.setTextColor(getResources().getColor(R.color.color_F9E7CB));
        } else {
            rll.setBackground(getResources().getDrawable(R.drawable.xuanzhong_bg));
            tv1.setTextColor(getResources().getColor(R.color.color_47321C));
            tv2.setTextColor(getResources().getColor(R.color.color_47321C));
            tv3.setTextColor(getResources().getColor(R.color.color_47321C));
            tv4.setTextColor(getResources().getColor(R.color.color_47321C));
        }
    }


    @Override
    public void setAlipaySuccess(boolean alipaySuccessState) {
        super.setAlipaySuccess(alipaySuccessState);
        ToastUtils.show("支付成功,欢迎尊贵的会员");
        EventBus.getDefault().post("登录成功");
    }
}
