package com.qihe.imagecompression.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qihe.imagecompression.MainActivity;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.adapter.AddImageAdapter;
import com.qihe.imagecompression.adapter.RecordAdapter;
import com.qihe.imagecompression.app.ArouterPath;

import com.qihe.imagecompression.ui.activity.image.PlayActivity;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.Utils;
import com.qihe.imagecompression.view.InputDialog;
import com.qihe.imagecompression.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

public class RecordFragment extends Fragment implements View.OnClickListener {

    private View view;

    private List<String> list = new ArrayList<>();
    private List<String> sizelist = new ArrayList<>();
    private LinearLayout empty;
    private RecyclerView recordRV;
    private RecordAdapter recordAdapter;
    private String substring;

    private Handler uiHandler = new Handler();
    private LoadingDialog loadingDialog;

    private AlertDialog dialog;

    private boolean isShowDelete = false;

    private List<String> selectList = new ArrayList<>();
    private TextView deleteTV;
    private ImageView delete;
    private TextView textview;
    private TextView textview1;

    private String type;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);

        // 注册订阅者
        EventBus.getDefault().register(this);
        loadingDialog = new LoadingDialog(getActivity());
        initView();
        return view;
    }

    private void initView() {
        textview = view.findViewById(R.id.textview);
        textview1 = view.findViewById(R.id.textview1);
        deleteTV = view.findViewById(R.id.delete_tv);
        empty = view.findViewById(R.id.empty);
        view.findViewById(R.id.to_zhizuo).setOnClickListener(this);
        recordRV = view.findViewById(R.id.record_rv);
        recordRV.setLayoutManager(new LinearLayoutManager(view.getContext()));

        view.findViewById(R.id.to_vip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.start(ArouterPath.vip_activity);
            }
        });

        delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isShowDelete) {
                    long l = System.currentTimeMillis();
                    recordAdapter.showDelect();
                    isShowDelete = true;
                    textview.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                    deleteTV.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    long l1 = System.currentTimeMillis();
                    Log.i("+++++显示删除1",l1-l+"");
                }

            }
        });

        deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isShowDelete) {

                    if (selectList.size() > 0) {
                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(view.getContext());
                        normalDialog.setMessage("你确定要删除吗？");
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        type = "删除";
                                        loadingDialog.show();
                                        DownThread downThread = new DownThread();
                                        downThread.start();


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
                    } else {
                        Toast.makeText(view.getContext(), "请选择要删除的图片", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        textview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isShowDelete) {
                    long l = System.currentTimeMillis();
                    recordAdapter.showDelect();
                    recordAdapter.setClear();
                    selectList.clear();
                    isShowDelete = false;
                    textview.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    deleteTV.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    long l1 = System.currentTimeMillis();
                    Log.i("+++++显示删除1",l1-l+"");
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            if (
                    view.getContext().checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                            view.getContext().checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                // requestPermissions(perms, 200);
                empty.setVisibility(View.VISIBLE);
                delete.setVisibility(View.GONE);
            } else {
                type = "加载";
                loadingDialog.show();
                DownThread downThread = new DownThread();
                downThread.start();
            }
        } else {
            type = "加载";
            loadingDialog.show();
            DownThread downThread = new DownThread();
            downThread.start();
        }


    }

    //加载文件
    class DownThread extends Thread {
        @Override
        public void run() {
            switch (type) {
                case "加载":
                    getFilesAllName(FileUtils.COMPRESSION_FILE_PATH);
                    break;
                case "删除":
                    long l = System.currentTimeMillis();
                    for (int i = 0; i < selectList.size(); i++) {
                        list.remove(selectList.get(i));
                        FileUtils.deleteFile(selectList.get(i));
                    }
                    long l1 = System.currentTimeMillis();
                    Log.i("+++++删除",l1-l+"");
                    break;
            }


            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    switch (type) {
                        case "加载":
                            if (list != null && list.size() != 0) {
                                delete.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.GONE);
                                recordAdapter = new RecordAdapter(view.getContext(), list);
                                recordRV.setAdapter(recordAdapter);
                                recordAdapter.setOnItemClickListener(new AddImageAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        if (!isShowDelete) {
                                            int fileType = Utils.getFileType(list.get(position));
                                            if (fileType == 0) {//图片
                                                lookImage(list.get(position));
                                            } else if (fileType == 1) {
                                                Intent intent = new Intent(view.getContext(), PlayActivity.class);
                                                intent.putExtra("path", list.get(position));
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(view.getContext(), "文件类型未知", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            if (selectList.contains(list.get(position))) {
                                                selectList.remove(list.get(position));
                                            } else {
                                                selectList.add(list.get(position));
                                            }
                                            Log.i("selectList", selectList.size() + "");
                                        }

                                    }
                                });
                                recordAdapter.setOnItemMoreClickListener(new AddImageAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        // Log.i("filesAllName", list.get(position));
                                        shoeMoreDialog(list.get(position));
                                    }
                                });
                            } else {
                                empty.setVisibility(View.VISIBLE);
                                delete.setVisibility(View.GONE);
                            }
                            break;
                        case "删除":
                            recordAdapter.notifyDataSetChanged();

                            recordAdapter.showDelect();
                            recordAdapter.setClear();
                            selectList.clear();
                            isShowDelete = false;
                            textview.setVisibility(View.VISIBLE);
                            delete.setVisibility(View.VISIBLE);
                            deleteTV.setVisibility(View.GONE);
                            textview1.setVisibility(View.GONE);

                            if (list.size() == 0) {
                                empty.setVisibility(View.VISIBLE);
                                delete.setVisibility(View.GONE);
                            }

                            break;
                    }


                    loadingDialog.dismiss();
                }
            };
            uiHandler.post(runnable);
        }
    }


    private void lookImage(String path) {
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(view.getContext(), R.style.MyDialog);
        alterDiaglog.setView(R.layout.view_look_image_record);//加载进去
        dialog = alterDiaglog.create();
        //显示
        dialog.show();

        //dialog.setView(view);
        // dialog.show();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
        dialog.getWindow().setAttributes(p);     //设置生效


        ImageView image = dialog.findViewById(R.id.image);

        Glide.with(view.getContext())
                .load(path)
                .placeholder(R.drawable.loadpicture_icon)
                .error(R.drawable.loadpicture_icon)
                // .thumbnail(0.1f)
                .into(image);

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String string) {

        if (string.equals("数据")) {
            type = "加载";
            loadingDialog.show();
            DownThread downThread = new DownThread();
            downThread.start();
        }
    }

    private void shoeMoreDialog(final String path) {

        final Dialog dialog = new Dialog(getContext(), R.style.ActionChosePriceSheetDialogStyle);
        View dialogView = View.inflate(getContext(), R.layout.record_more_dialog, null);
        dialog.setContentView(dialogView);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView name = dialog.findViewById(R.id.name);
        substring = path.substring(0, path.lastIndexOf("/") + 1);
        name.setText(path.replace(substring, ""));
        dialog.findViewById(R.id.rename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                InputDialog inputDialog = new InputDialog(getContext(), "")
                        .addItemListener(new InputDialog.ItemListener() {
                            @Override
                            public void onClickSure(String content) {//input输入框值
                               /* String substring = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));

                                String replace = path.replace(substring, content);*/

                                String substring1 = path.substring(path.lastIndexOf("."), path.length());

                                // Log.i("substring1", substring1);

                                /*if(list.contains(path)){
                                    int i = list.indexOf(path);
                                    list.set(i,replace);
                                    recordAdapter.notifyDataSetChanged();
                                }*/

                                FileUtils.rename(path, content + substring1);
                                type = "加载";
                                loadingDialog.show();
                                DownThread downThread = new DownThread();
                                downThread.start();
                                //list.get(position).setName(content + "." + getFileNameWithSuffix(videolist.get(position).getPath()));

                                //recordAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onClickCanel() {

                            }
                        });
                String replace = path.replace(substring, "");
                inputDialog.setText(replace.replace(path.substring(path.lastIndexOf("."), path.length()), ""));
                inputDialog.show();

            }
        });
        dialog.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                int fileType = Utils.getFileType(path);
                if (fileType == 0) {//图片
                    new Share2.Builder(getActivity())
                            .setContentType(ShareContentType.IMAGE)
                            .setShareFileUri(FileUtil.getFileUri(getContext(), ShareContentType.IMAGE, new File(path)))
                            .build()
                            .shareBySystem();
                } else if (fileType == 1) {
                    new Share2.Builder(getActivity())
                            .setContentType(ShareContentType.VIDEO)
                            .setShareFileUri(FileUtil.getFileUri(getContext(), ShareContentType.VIDEO, new File(path)))
                            .build()
                            .shareBySystem();
                } else {
                    Toast.makeText(view.getContext(), "文件类型未知", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(view.getContext());
                normalDialog.setMessage("你确定要删除吗？");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                list.remove(path);
                                recordAdapter.notifyDataSetChanged();

                                if (list.size() == 0) {
                                    empty.setVisibility(View.VISIBLE);
                                    delete.setVisibility(View.GONE);
                                }
                                FileUtils.deleteFile(path);

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
        });

    }

    public void getFilesAllName(String path) {
        list.clear();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return;
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].exists() && files[i].length() != 0) {

                if (!files[i].getAbsolutePath().endsWith("hwbk")) {
                    list.add(files[i].getAbsolutePath());
                    /*if(Utils.getFileType(list.get(i)) == 0){

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        //只请求图片宽高，不解析图片像素(请求图片属性但不申请内存，解析bitmap对象，该对象不占内存)
                        opts.inJustDecodeBounds = true;
                        //String path = Environment.getExternalStorageDirectory() + "/dog.jpg";
                        BitmapFactory.decodeFile(files[i].getAbsolutePath(), opts);
                        int width = opts.outWidth;
                        int height = opts.outHeight;
                       *//* bitmap = BitmapFactory.decodeFile(files[i].getAbsolutePath());
                        int height = bitmap.getHeight();
                        int width = bitmap.getWidth();*//*
                        sizelist.add(width + "" + " x " + height + "" + "px");
                       Log.i("sizelist",width + "" + " x " + height + "" + "px");
                    }else{
                        sizelist.add("视频");
                    }*/


                }

            }
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_zhizuo:
                MainActivity activity = (MainActivity) getActivity();
                activity.setHomeFragment();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
