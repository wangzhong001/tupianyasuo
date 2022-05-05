package com.qihe.imagecompression.ui.activity.image;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.adapter.MineWorkAdapter;
import com.qihe.imagecompression.util.ActivityUtil;


/**
 *
 * 更多精品Apps
 * Created by Administrator on 2020/11/14.
 */

public class MyWorksActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myapp);

        initView();
        ActivityUtil.fullScreen(getWindow());

    }

    private void initView(){
        findViewById(R.id.myapp_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.myapp_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MineWorkAdapter(this));

    }




}
