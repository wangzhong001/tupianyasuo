package com.qihe.imagecompression.ui.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.databinding.FragmentHomeBinding;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.OpenFileLocalUtil;
import com.qihe.imagecompression.view.HomeMoreFetaureDialog;
import com.qihe.imagecompression.view.InputDialog;
import com.qihe.imagecompression.view.LoadingDialog;
import com.qihe.imagecompression.viewmodel.ResourceVideoViewModel;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.Contants;
import com.xinqidian.adcommon.base.BaseFragment;
import com.xinqidian.adcommon.util.AndroidShareUtils;
import com.xinqidian.adcommon.util.KLog;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;
import com.xinqidian.adcommon.view.SureDialog;

import java.io.File;

import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

/**
 * Created by lipei on 2020/6/11.
 */

public class HomeFragment extends BaseFragment<FragmentHomeBinding, ResourceVideoViewModel> {

    private LoadingDialog loadingDialog;

    private HomeMoreFetaureDialog moreFetaureDialog;

    private InputDialog inputDialog;

    private boolean isUpdate;

    private SureDialog sureDialog;


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
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
        return true;
    }

    @Override
    public void onStimulateSuccessCall() {
        ToastUtils.show("使用奖励领取成功");

    }

    @Override
    public void onStimulateSuccessDissmissCall() {
        int number = (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING, 0);
//        if(number<0){
//            number=0;
//        }
        number += 1;
        SharedPreferencesUtil.setParam(Contants.SHOW_STIMULATE_NUMBER_STRING, number);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.isCanSeeVideo.set((Boolean) SharedPreferencesUtil.getParam(AppConfig.musicEractHomeCanSeeVideo,true));

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            loadingDialog = new LoadingDialog(getContext());


            viewModel.getLocalVideo(getContext(), loadingDialog);

            isFirst = true;
        }
    }

    public static HomeFragment newStance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;

    }
    //视频请求码
    private static final int ALBUM_REQUEST_CODE = 1;

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("video/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);

//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    /**
     * 从相册获取图片，获取本地视频
     */
    @Override
    public void initViewObservable() {
        super.initViewObservable();

        binding.addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPicFromAlbm();


            }
        });

        /**
         * 跳转到商城评论
         */
        binding.commentIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidShareUtils.goToMarket(getContext());

            }
        });


        binding.commentTwoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidShareUtils.goToMarket(getContext());

            }
        });

        /**
         * 未显示奖励观看次数获得免费使用次数
         */
        binding.jiangliIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!shouldShowStimulateAdView()) {
                    ToastUtils.show("暂时不可领取奖励哦");
                    return;
                }
                if (sureDialog == null) {
                    sureDialog = new SureDialog(getContext(), "您可以观看一个视频来获取更多的使用次数")
                            .addItemListener(new SureDialog.ItemListener() {
                                @Override
                                public void onClickSure() {
                                    showStimulateAd();
                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                }


                sureDialog.show();

            }

        });


        viewModel.videoLiveData.observe(this, new Observer<ResourceVideoViewModel>() {
            @Override
            public void onChanged(@Nullable ResourceVideoViewModel resourceVideoViewModel) {
                resourceVideoViewModel.setVideoData();
            }
        });

        /**
         * 更多操作
         * 显示特征下拉框
         */
        viewModel.showFetureDialogLiveData.observe(this, new Observer<ResourceVideoViewModel>() {
            @Override
            public void onChanged(@Nullable ResourceVideoViewModel featuresViewModel) {
                moreFetaureDialog = new HomeMoreFetaureDialog(getContext(), featuresViewModel);
                moreFetaureDialog.show();
            }
        });


        /**
         * 重命名
         */
        viewModel.reNameLiveData.observe(this, new Observer<ResourceVideoViewModel>() {
            @Override
            public void onChanged(@Nullable final ResourceVideoViewModel featuresViewModel) {
                if (inputDialog == null) {
                    inputDialog = new InputDialog(getContext(), "")
                            .addItemListener(new InputDialog.ItemListener() {
                                @Override
                                public void onClickSure(String content) {//input输入框值
                                    isUpdate = true;
                                    FileUtils.rename(featuresViewModel.fileModelObservableFiel.get().song.getPath(), content + "." + getFileNameWithSuffix(featuresViewModel.fileModelObservableFiel.get().song.getPath()));
                                    // 父路径，文件名加后缀
                                    // 最后通知图库更新
//                                 getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                                            Uri.fromFile(new File(featuresViewModel.fileModelObservableFiel.get().song.getPath()))));
                                    featuresViewModel.fileModelObservableFiel.get().name.set(content + "." + getFileNameWithSuffix(featuresViewModel.fileModelObservableFiel.get().song.getPath()));
                                    // 设置content.后缀名
                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                }

                inputDialog.show();

            }
        });

        /**
         * 删除
         */
        viewModel.delectLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                isUpdate = aBoolean;
            }
        });
        /**
         * 本地视频播放器打开音频
         */
        viewModel.openLiveData.observe(this, new Observer<ResourceVideoViewModel>() {
            @Override
            public void onChanged(@Nullable ResourceVideoViewModel featuresViewModel) {
                OpenFileLocalUtil.openFile(getContext(), featuresViewModel.fileModelObservableFiel.get().song.getPath());
                //打开本地文件，传入信息路径

            }
        });
        /**
         * 文件路径
         */
        viewModel.filePathLiveData.observe(this, new Observer<ResourceVideoViewModel>() {
            @Override
            public void onChanged(@Nullable ResourceVideoViewModel featuresViewModel) {
                new AlertDialog.Builder(getContext())
                        .setTitle("文件路径")
                        .setMessage("手机存储: " + featuresViewModel.fileModelObservableFiel.get().song.getPath())
                        .show();
            }
        });

        /**
         * 调用分享按钮
         */
        viewModel.shareLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                new Share2.Builder(getActivity())
                        .setContentType(ShareContentType.FILE)
                        .setShareFileUri(FileUtil.getFileUri(getContext(), ShareContentType.FILE, new File( s)))
                        .build()
                        .shareBySystem();
            }
        });
    }

    @Override
    public void onStimulateFallCall() {
        super.onStimulateFallCall();
        ToastUtils.show("请稍后来领取奖励");

    }
//获取文件名带后缀。

    public String getFileNameWithSuffix(String path) {

        if (TextUtils.isEmpty(path)) {
            return "";
        }
        int start = path.lastIndexOf(".");
        if (start != -1) {
            return path.substring(start + 1);
        }

        return "";


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.e("isupate--->", isUpdate);
        if (isUpdate) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);   //, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            String path = Environment.getExternalStorageDirectory() + "/Movies";
            Uri uri = Uri.fromFile(new File(path));
            intent.setData(uri);
            getActivity().sendBroadcast(intent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ALBUM_REQUEST_CODE){
            if (resultCode == getActivity().RESULT_OK) {
                Uri uri=data.getData();
                KLog.e("uri--->",uri);

                String path=getRealFilePath(getContext(),uri);
                ActivityUtil.start(ArouterPath.extract_audio_activity, LiveDataBusData.chosePath, path);
//
//                ContentResolver cr = getActivity().getContentResolver();
//                /** 数据库查询操作。
//                 * 第一个参数 uri：为要查询的数据库+表的名称。
//                 * 第二个参数 projection ： 要查询的列。
//                 * 第三个参数 selection ： 查询的条件，相当于SQL where。
//                 * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
//                 * 第四个参数 sortOrder ： 结果排序。
//                 */
//                Cursor cursor = cr.query(uri, null, null, null, null);
//                if (cursor != null) {
//                    if (cursor.moveToFirst()) {
//
//                        String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//                        // 视频时长：MediaStore.Audio.Media.DURATION
//
//                    }
//                    cursor.close();
//                }
            }

        }
    }




    /**
     *  根据Uri获取文件真实地址
     */
    public  String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String realPath = null;
        if (scheme == null)
            realPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {

            Cursor cursor =  getActivity().getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                String uriString = uri.toString();
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                File storageDir;

                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (file.exists()) {
                    realPath = file.getAbsolutePath();
                } else {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file1 = new File(storageDir, imageName);
                    realPath = file1.getAbsolutePath();
                }
            }
        }
        return realPath;
    }


}
