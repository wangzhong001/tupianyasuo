package com.qihe.imagecompression.ui.activity.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.AdApplcation;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.view.LoadingDialog;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.JumpUtils;

import org.greenrobot.eventbus.EventBus;

import static com.qihe.imagecompression.app.AdApplcation.getContext;


public class LoginActivity1 extends AppCompatActivity {

    private boolean isShowPassword = false;

    private TextView login;

    private LoadingDialog loadingDialog;

    private JumpUtils jumpUtils;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        ActivityUtil.fullScreen(getWindow());
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();
    }



    private void initView() {
        loadingDialog = new LoadingDialog(this);
        final EditText loginName = findViewById(R.id.login_name);
        final EditText loginPassword = findViewById(R.id.login_password);
        final ImageView passwordIv = findViewById(R.id.password_iv);

        findViewById(R.id.wx_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  IWXAPI wxapi = WXAPIFactory.createWXAPI(LoginActivity1.this, "wxa481de9047d8bcaa", false);
                 wxapi.registerApp("wxa481de9047d8bcaa");    //应用ID*/
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                AdApplcation.getWXapi().sendReq(req);
                finish();

           // UMShareAPI mShareAPI = UMShareAPI.get(LoginActivity1.this);
            // mShareAPI.getPlatformInfo(LoginActivity1.this, SHARE_MEDIA.WEIXIN, umAuthListener);
            }
        });



        passwordIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowPassword) {
                    loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPassword = false;
                    passwordIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.buxianshi_icon));
                } else {
                    loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPassword = true;
                    passwordIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.xianshi_icon));
                }
                loginPassword.setSelection(loginPassword.getText().length());
            }

        });
        loginName.addTextChangedListener(watcher);
        //登录
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = loginName.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                //登录
                UserUtil.login1(name, password, new UserUtil.LoginCallBack() {
                    @Override
                    public void noRegist() {
                        Toast.makeText(LoginActivity1.this, "用户未注册，请先注册", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        loginSuccess();
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

        //去注册
        findViewById(R.id.to_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity1.this, RegisterActivity.class));
                finish();
            }
        });

        findViewById(R.id.mima).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(LoginActivity1.this,R.style.MyDialog);
                alterDiaglog.setView(R.layout.mima);//加载进去
                final AlertDialog dialog = alterDiaglog.create();
                //显示
                dialog.show();

                //dialog.setView(view);
               // dialog.show();
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
                WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
                p.height = (int) (d.getHeight() * 0.8);   //高度设置为屏幕的0.3
                p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
                dialog.getWindow().setAttributes(p);     //设置生效

                dialog.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (jumpUtils == null) {
                            jumpUtils = new JumpUtils(view.getContext());
                        }
                        jumpUtils.jumpQQ();

                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void loginSuccess() {
        EventBus.getDefault().post("登录成功");
        finish();
    }


    TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //只要编辑框内容有变化就会调用该方法，s为编辑框变化后的内容
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //编辑框内容变化之前会调用该方法，s为编辑框内容变化之前的内容
        }

        @Override
        public void afterTextChanged(Editable s) {
            //编辑框内容变化之后会调用该方法，s为编辑框内容变化后的内容
            if (s != null && s.length() > 0) {
                login.setBackground(getResources().getDrawable(R.drawable.login_confirm));
                login.setTextColor(getResources().getColor(R.color.color_292925));
            } else {
                login.setBackground(getResources().getDrawable(R.drawable.login_confirm_null));
                login.setTextColor(getResources().getColor(R.color.color_FFFFFF));
            }
        }
    };
}
