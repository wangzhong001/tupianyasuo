package com.qihe.imagecompression.ui.activity.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.view.LoadingDialog;

import com.xinqidian.adcommon.login.UserUtil;


import org.greenrobot.eventbus.EventBus;

import static com.qihe.imagecompression.app.AdApplcation.getContext;

public class RegisterActivity extends AppCompatActivity {

    private TextView register;

    private boolean isShowPassword = false;

    private LoadingDialog loadingDialog;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = loginName.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();
                UserUtil.regist1(name, password, new UserUtil.CallBack() {
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

        //去登录
        findViewById(R.id.to_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity1.class));
                finish();
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
                register.setBackground(getResources().getDrawable(R.drawable.login_confirm));
                register.setTextColor(getResources().getColor(R.color.color_292925));
            } else {
                register.setBackground(getResources().getDrawable(R.drawable.login_confirm_null));
                register.setTextColor(getResources().getColor(R.color.color_FFFFFF));
            }
        }
    };
}
