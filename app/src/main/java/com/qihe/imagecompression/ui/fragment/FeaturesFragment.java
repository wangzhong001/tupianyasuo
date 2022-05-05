package com.qihe.imagecompression.ui.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.databinding.FragmentFeaturesBinding;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.OpenFileLocalUtil;
import com.qihe.imagecompression.view.InputDialog;
import com.qihe.imagecompression.view.LoadingDialog;
import com.qihe.imagecompression.view.MoreFetaureDialog;
import com.qihe.imagecompression.view.RingDialog;
import com.qihe.imagecompression.viewmodel.FeaturesViewModel;
import com.xinqidian.adcommon.base.BaseFragment;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.ToastUtils;

import java.io.File;

import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

/**
 * Created by lipei on 2020/6/11.
 */

public class FeaturesFragment extends BaseFragment<FragmentFeaturesBinding,FeaturesViewModel> {

    private MoreFetaureDialog moreFetaureDialog;

    private RingDialog ringDialog;

    private InputDialog inputDialog;



    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_features;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
    }



    public static FeaturesFragment newStance(){
        FeaturesFragment homeFragment=new FeaturesFragment();
        return homeFragment;

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);

        if(isVisible){



            viewModel.getMusic(new LoadingDialog(getContext()));
            isFirst=true;
        }
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

    }

//    private void setViewPager() {
//        String titles[] = getResources().getStringArray(R.array.features_tablayout_title);
//        List<Fragment> mFragments = new ArrayList<>();
//
//        List<String> mTitles = new ArrayList<>();
//        for (int i = 0; i < titles.length; i++) {
//            mTitles.add(titles[i]);
//            mFragments.add(FeaturesTabFragment.newStance(i));
//
//        }
//        MainPageAdapter realEstatePageAdapter = new MainPageAdapter(getChildFragmentManager(), mFragments, mTitles);
//        binding.viewpage.setAdapter(realEstatePageAdapter);
//        binding.xtablayout.setupWithViewPager(binding.viewpage);

//    }


    @Override
    public void initViewObservable() {
        super.initViewObservable();
        LiveDataBus.get().with(LiveDataBusData.updateMineFile, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                viewModel.getFileList(1);
            }
        });

        /**
         * 显示下拉框
         */
        viewModel.showFetureDialogLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable FeaturesViewModel featuresViewModel) {
                moreFetaureDialog = new MoreFetaureDialog(getContext(), featuresViewModel);
                moreFetaureDialog.show();
            }
        });

        viewModel.reNameLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable final FeaturesViewModel featuresViewModel) {
                if (inputDialog == null) {
                    inputDialog = new InputDialog(getContext(), "")
                            .addItemListener(new InputDialog.ItemListener() {
                                @Override
                                public void onClickSure(String content) {
                                    FileUtils.rename(featuresViewModel.fileModelObservableFiel.get().fileModel.getPath(), content + "." + getFileNameWithSuffix(featuresViewModel.fileModelObservableFiel.get().fileModel.getPath()));
                                    featuresViewModel.fileModelObservableFiel.get().name.set(content + "." + getFileNameWithSuffix(featuresViewModel.fileModelObservableFiel.get().fileModel.getPath()));

                                }

                                @Override
                                public void onClickCanel() {

                                }
                            });
                }

                inputDialog.show();

            }
        });


        viewModel.openLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable FeaturesViewModel featuresViewModel) {
                OpenFileLocalUtil.openFile(getContext(), featuresViewModel.fileModelObservableFiel.get().fileModel.getPath());


            }
        });



        viewModel.filePathLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable FeaturesViewModel featuresViewModel) {
                new AlertDialog.Builder(getContext())
                        .setTitle("文件路径")
                        .setMessage(FileUtils.NEW_AUDIO_FILE_PATH+ featuresViewModel.fileModelObservableFiel.get().fileModel.getName())
                        .show();
            }
        });

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


        viewModel.setRingLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable FeaturesViewModel s) {
//                if(ringDialog==null){
                    ringDialog=new RingDialog(getActivity(),s);

//                }
                ringDialog.show();

            }
        });

        viewModel.jianJigLiveData.observe(this, new Observer<FeaturesViewModel>() {
            @Override
            public void onChanged(@Nullable FeaturesViewModel featuresViewModel) {
                goToMarket();
            }
        });
    }


    @Override
    public void onStimulateFallCall() {
        super.onStimulateFallCall();

    }

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
        Glide.get(getContext()).clearMemory();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    public  void goToMarket() {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id=" + "com.smkj.audioclip"));
            startActivity(i);
        } catch (Exception e) {
//            Toast.makeText(MoreActivity.this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
