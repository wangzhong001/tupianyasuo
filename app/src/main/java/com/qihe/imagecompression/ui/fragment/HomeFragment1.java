package com.qihe.imagecompression.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.ui.activity.image.SelectImageActivity;
import com.qihe.imagecompression.ui.activity.image.SelectVideoActivity;
import com.qihe.imagecompression.util.ActivityUtil;

public class HomeFragment1 extends Fragment implements View.OnClickListener {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home1, container, false);
        initView();
        return view;
    }

    private void initView() {
        view.findViewById(R.id.to_vip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.start(ArouterPath.vip_activity);
            }
        });

        view.findViewById(R.id.compression).setOnClickListener(this);
        view.findViewById(R.id.size).setOnClickListener(this);
        view.findViewById(R.id.format).setOnClickListener(this);
        view.findViewById(R.id.video).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.compression:
                //startActivity(new Intent(view.getContext(), SelectImageActivity.class));
                toSelectImageActivity("压缩");
                break;
            case R.id.size:
                toSelectImageActivity("尺寸");
                break;
            case R.id.format:
                toSelectImageActivity("格式");
                break;
            case R.id.video:
                // toSelectImageActivity("格式");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
                    if (
                            view.getContext().checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                                    view.getContext().checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(perms, 200);
                        //Toast.makeText(view.getContext(), "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(view.getContext(), SelectVideoActivity.class));
                    }
                }else{
                    startActivity(new Intent(view.getContext(), SelectVideoActivity.class));
                }

                break;
        }
    }


    private void toSelectImageActivity(String s) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            if (
                    view.getContext().checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                            view.getContext().checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
                // Toast.makeText(view.getContext(), "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(view.getContext(), SelectImageActivity.class);
                intent.putExtra("content", s);
                startActivity(intent);
            }
        }else{
            Intent intent = new Intent(view.getContext(), SelectImageActivity.class);
            intent.putExtra("content", s);
            startActivity(intent);
        }


    }
}
