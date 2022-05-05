package com.qihe.imagecompression.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.databinding.ActivityVipBinding;
import com.qihe.imagecompression.ui.activity.image.LoginActivity1;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.viewmodel.FeaturesViewModel;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lipei on 2020/7/6.
 */
@Route(path = ArouterPath.vip_activity)
public class VipActivity extends BaseActivity<ActivityVipBinding,FeaturesViewModel> {

    private String mercdName = "永久";
    private String mercdWorth = "88.99";
   // private String mercdWorth = "0.03";
    private int orderNumber = 100;

    private boolean isWX = true;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_vip;
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
        SpannableString spannableString01 = new SpannableString("￥399.99");
        SpannableString spannableString02 = new SpannableString("￥199.99");
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

        binding.backIv.setVisibility(View.VISIBLE);

        binding.backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.rll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mercdName = "永久";
                mercdWorth = "88.99";
                //mercdWorth = "0.03";
                orderNumber = 100;
                setTextColorAndBg(false, binding.rll1, binding.tv11, binding.tv12, binding.tv13, binding.tv14);
                setTextColorAndBg(true, binding.rll2, binding.tv21, binding.tv22, binding.tv23, binding.tv24);
                setTextColorAndBg(true, binding.rll3, binding.tv31, binding.tv32, binding.tv33, binding.tv34);


            }
        });

        binding.rll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mercdName = "一年";
                mercdWorth = "40.99";
               // mercdWorth = "0.02";
                orderNumber = 12;
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
                //mercdWorth = "0.01";
                orderNumber = 1;
                setTextColorAndBg(true, binding.rll1, binding.tv11, binding.tv12, binding.tv13, binding.tv14);
                setTextColorAndBg(true, binding.rll2, binding.tv21, binding.tv22, binding.tv23, binding.tv24);
                setTextColorAndBg(false, binding.rll3, binding.tv31, binding.tv32, binding.tv33, binding.tv34);
            }
        });

        binding.tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtil.isLogin()) {
                    showPlay();
                   /* UserUtil.alipayOrder(mercdName, mercdWorth, orderNumber, VipActivity.this, new UserUtil.CallBack() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void loginFial() {

                           // ActivityUtil.start(ArouterPath.login_activity);
                            startActivity(new Intent(VipActivity.this, LoginActivity1.class));
                        }
                    });*/
                } else {
                   // ActivityUtil.start(ArouterPath.login_activity);
                    startActivity(new Intent(VipActivity.this, LoginActivity1.class));

                }

            }
        });
    }


    private void showPlay(){
        isWX = true;
        final Dialog dialog = new Dialog(this, R.style.ActionChosePriceSheetDialogStyle);
        View view = View.inflate(this, R.layout.select_pay_dialog, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView payTV = view.findViewById(R.id.pay_tv);
        payTV.setText("支付金额：￥"+mercdWorth);
        final ImageView wxIV = view.findViewById(R.id.wx_iv);
        final ImageView aliIV = view.findViewById(R.id.ali_iv);
        view.findViewById(R.id.wx_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isWX){
                    wxIV.setImageResource(R.drawable.selected_icon);
                    aliIV.setImageResource(R.drawable.unselected_icon);
                    isWX = true;
                }

            }
        });
        view.findViewById(R.id.ali_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWX){
                    wxIV.setImageResource(R.drawable.unselected_icon);
                    aliIV.setImageResource(R.drawable.selected_icon);
                    isWX = false;

                }
            }
        });
        view.findViewById(R.id.to_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(isWX){
                    UserUtil.wxOrder(mercdName, mercdWorth, orderNumber, VipActivity.this, new UserUtil.CallBack() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void loginFial() {


                        }
                    });

                }else{
                    UserUtil.alipayOrder(mercdName, mercdWorth, orderNumber, VipActivity.this, new UserUtil.CallBack() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void loginFial() {


                        }
                    });
                }

            }
        });


    }



    private void setTextColorAndBg(boolean isClick, RelativeLayout rll, TextView tv1, TextView tv2, TextView tv3, TextView tv4) {
        if (isClick) {
            rll.setBackground(getResources().getDrawable(R.drawable.black_bg1));
            tv1.setTextColor(getResources().getColor(R.color.color_090F0F));
            tv2.setTextColor(getResources().getColor(R.color.color_090F0F));
            tv3.setTextColor(getResources().getColor(R.color.color_090F0F));
            tv4.setTextColor(getResources().getColor(R.color.color_090F0F));
        } else {
            rll.setBackground(getResources().getDrawable(R.drawable.xuanzhong_bg1));
            tv1.setTextColor(getResources().getColor(R.color.color_090F0F));
            tv2.setTextColor(getResources().getColor(R.color.color_090F0F));
            tv3.setTextColor(getResources().getColor(R.color.color_090F0F));
            tv4.setTextColor(getResources().getColor(R.color.color_090F0F));
        }
    }


    @Override
    public void setAlipaySuccess(boolean alipaySuccessState) {
        super.setAlipaySuccess(alipaySuccessState);
        ToastUtils.show("支付成功,欢迎尊贵的会员");
        EventBus.getDefault().post("登录成功");
        finish();
    }
}
