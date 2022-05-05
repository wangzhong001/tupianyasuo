package com.qihe.imagecompression.ui.activity.image;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.util.ActivityUtil;
import com.xinqidian.adcommon.ui.activity.WebViewActivity;


/**
 * 关于我们
 * Created by Administrator on 2020/11/14.
 */

public class AboutusActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
        ActivityUtil.fullScreen(getWindow());

    }

    private void initView() {
        findViewById(R.id.about_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        TextView version = findViewById(R.id.version);
        version.setText("当前版本v" + getVerName(this));

        findViewById(R.id.title_tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户协议
                Intent intent = new Intent(AboutusActivity.this, WebViewActivity.class);
                intent.putExtra("title", getResources().getString(R.string.user_agreement));//http://www.qihe.website/yonghu_xieyi_qihe.html
                intent.putExtra("url", "http://www.qihe.website/yonghu_xieyi_qihe.html");
                startActivity(intent);

            }
        });
        findViewById(R.id.title_tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐私政策
                Intent intent = new Intent(AboutusActivity.this, WebViewActivity.class);
                intent.putExtra("title", getResources().getString(R.string.privacy_policy));// http://www.qihe.website/yinsimoban_shengyin_qihe_vivo.html
                intent.putExtra("url", "http://www.qihe.website/yinsimoban_tupian_vivo.html");
                startActivity(intent);

            }
        });

    }

    /**
     * 获取版本号名称
     *
     * @param context
     * @return
     */
    public String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

}
