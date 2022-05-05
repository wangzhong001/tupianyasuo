package com.qihe.imagecompression.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.ui.activity.image.AboutusActivity;
import com.qihe.imagecompression.ui.activity.image.LoginActivity1;
import com.qihe.imagecompression.ui.activity.image.MyWorksActivity;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.Utils;
import com.xinqidian.adcommon.login.UserModel;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.login.WxUserModel;
import com.xinqidian.adcommon.ui.activity.FeedBackActivity;
import com.xinqidian.adcommon.util.JumpUtils;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MyFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private ImageView userIcon;
    private TextView userName;
    private LinearLayout zhuxiao;
    private TextView logOut;
    private JumpUtils jumpUtils;
    private LinearLayout vipLL;
    private TextView userVIP;
    private TextView countTv;
    private TextView vipTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        context = view.getContext();
        initView();
        return view;
    }

    private void initView() {
        // 注册订阅者
        EventBus.getDefault().register(this);
        userIcon = view.findViewById(R.id.user_icon);
        userName = view.findViewById(R.id.user_name);
        vipLL = view.findViewById(R.id.vip_LL);
        userVIP = view.findViewById(R.id.user_vip);
        countTv = view.findViewById(R.id.count_tv);
        vipTV = view.findViewById(R.id.vip_tv);

        countTv.setText(SharedPreferencesUtil.getCount() + "" + "次");

        view.findViewById(R.id.to_vip).setOnClickListener(this);
        TextView version = view.findViewById(R.id.version);
        version.setText("版本v" + Utils.getAppVersionName(context));
        view.findViewById(R.id.login).setOnClickListener(this);
        view.findViewById(R.id.my_app).setOnClickListener(this);
        view.findViewById(R.id.free).setOnClickListener(this);
        view.findViewById(R.id.like).setOnClickListener(this);
        view.findViewById(R.id.opinion).setOnClickListener(this);
        view.findViewById(R.id.service).setOnClickListener(this);
        view.findViewById(R.id.aboutus).setOnClickListener(this);
        zhuxiao = view.findViewById(R.id.zhuxiao);
        zhuxiao.setOnClickListener(this);
        logOut = view.findViewById(R.id.log_out);
        logOut.setOnClickListener(this);


        loginSuccess();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (!SharedPreferencesUtil.isLogin()) {
                    startActivity(new Intent(context, LoginActivity1.class));
                }
                break;
            case R.id.to_vip:
                ActivityUtil.start(ArouterPath.vip_activity);
                break;
            case R.id.my_app:
                startActivity(new Intent(view.getContext(), MyWorksActivity.class));
                break;
            case R.id.free:

                break;
            case R.id.like:
                Utils.jumpToReview(view.getContext(), view.getContext().getPackageName());
                break;
            case R.id.opinion:
                startActivity(new Intent(getContext(), FeedBackActivity.class));
                break;
            case R.id.service:
                if (jumpUtils == null) {
                    jumpUtils = new JumpUtils(view.getContext());
                }
                jumpUtils.jumpQQ();
                break;
            case R.id.aboutus:
                startActivity(new Intent(view.getContext(), AboutusActivity.class));
                break;
            case R.id.zhuxiao://注销
                zhuxiao();
                break;
            case R.id.log_out://退出登录
                logOut();
                break;
        }
    }

    private void zhuxiao() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(view.getContext());
        normalDialog.setMessage("你确定要注销吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(SharedPreferencesUtil.getLoginType()){
                            UserUtil.exitLogin();
                            SharedPreferencesUtil.exitLogin();
                            logOut.setVisibility(View.GONE);
                            zhuxiao.setVisibility(View.GONE);
                            vipLL.setVisibility(View.GONE);
                            userName.setText("点击登录");
                            vipTV.setText("开通会员");
                            userIcon.setImageDrawable(getResources().getDrawable((R.drawable.user_icon1)));
                            ToastUtils.show("注销成功");
                        }else{
                            UserUtil.UserClean(new UserUtil.CallBack() {
                                @Override
                                public void onSuccess() {
                                    SharedPreferencesUtil.exitLogin();
                                    logOut.setVisibility(View.GONE);
                                    zhuxiao.setVisibility(View.GONE);
                                    vipLL.setVisibility(View.GONE);
                                    userName.setText("点击登录");
                                    vipTV.setText("开通会员");
                                    userIcon.setImageDrawable(getResources().getDrawable((R.drawable.user_icon1)));

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
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }

    private void logOut() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(view.getContext());
        normalDialog.setMessage("你确定要退出吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserUtil.exitLogin();
                        SharedPreferencesUtil.exitLogin();
                        logOut.setVisibility(View.GONE);
                        zhuxiao.setVisibility(View.GONE);
                        vipLL.setVisibility(View.GONE);
                        userName.setText("点击登录");
                        vipTV.setText("开通会员");
                        userIcon.setImageDrawable(getResources().getDrawable((R.drawable.user_icon1)));

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String string) {

        if (string.equals("登录成功")) {
            loginSuccess();
        } else if (string.equals("次数")) {
            countTv.setText(SharedPreferencesUtil.getCount() + "" + "次");
        }
    }


    private void loginSuccess() {
        if (SharedPreferencesUtil.isLogin()) {
            if (SharedPreferencesUtil.getLoginType()) {
                UserUtil.getWxUserInfoCallBack(new UserUtil.WxUserinfoCallBack() {
                    @Override
                    public void getWxUserInfo(WxUserModel.DataBean dataBean) {
                        userName.setText(dataBean.getNickname());
                        if (SharedPreferencesUtil.isVip()) {
                            if (dataBean.getUserLevel() == 4) {
                                userVIP.setText("永久会员");
                            } else {
                                String expireDate = dataBean.getExpireDate();
                                if(expireDate != null &&  !expireDate.equals("")){
                                    String s = Utils.getStrTime(expireDate);
                                    userVIP.setText(s + "到期");
                                }

                            }
                            vipLL.setVisibility(View.VISIBLE);
                            vipTV.setText("已开通");
                        } else {
                            vipLL.setVisibility(View.GONE);
                            vipTV.setText("开通会员");
                        }
                    }
                });
            } else {
                UserUtil.getUserInfoCallBack(new UserUtil.UserinfoCallBack() {
                    @Override
                    public void getUserInfo(UserModel.DataBean dataBean) {
                        userName.setText(dataBean.getMobile());
                        if (SharedPreferencesUtil.isVip()) {
                            if (dataBean.getUserLevel() == 4) {
                                userVIP.setText("永久会员");
                            } else {
                                String expireDate = dataBean.getExpireDate();
                                if(expireDate != null &&  !expireDate.equals("")){
                                    String s = Utils.getStrTime(expireDate);
                                    userVIP.setText(s + "到期");
                                }

                            }
                            vipLL.setVisibility(View.VISIBLE);
                            vipTV.setText("已开通");
                        } else {
                            vipLL.setVisibility(View.GONE);
                            vipTV.setText("开通会员");
                        }
                    }
                });
            }
            userIcon.setImageDrawable(getResources().getDrawable((R.drawable.user_icon2)));
            logOut.setVisibility(View.VISIBLE);
            zhuxiao.setVisibility(View.VISIBLE);
        } else {
            logOut.setVisibility(View.GONE);
            zhuxiao.setVisibility(View.GONE);
            vipLL.setVisibility(View.GONE);
            vipTV.setText("开通会员");
            userIcon.setImageResource(R.drawable.user_icon1);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
