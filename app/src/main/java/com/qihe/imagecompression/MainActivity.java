package com.qihe.imagecompression;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qihe.imagecompression.adapter.HomePageAdapter;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.databinding.ActivityMainBinding;
import com.qihe.imagecompression.ui.fragment.FeaturesFragment;
import com.qihe.imagecompression.ui.fragment.HomeFragment;
import com.qihe.imagecompression.ui.fragment.HomeFragment1;
import com.qihe.imagecompression.ui.fragment.MineFragment;
import com.qihe.imagecompression.ui.fragment.MyFragment;
import com.qihe.imagecompression.ui.fragment.RecordFragment;
import com.qihe.imagecompression.ui.fragment.VipFragment;
import com.qihe.imagecompression.util.AppUpdateUtils;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.SetRingUtil;
import com.qihe.imagecompression.viewmodel.MianViewModel;
import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.base.BaseActivity;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

@Route(path = ArouterPath.main_activity)
public class MainActivity extends BaseActivity<ActivityMainBinding,MianViewModel> {

    private List<Fragment> mFragment = new ArrayList<>();
    private ViewPager viewPager;
    private RadioGroup mainRadio;

    private long mExitTime;       //实现“再按一次退出”的记录时间变量

    private HomePageAdapter mainPageAdapter;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.baseViewModel;
    }

    @Override
    public boolean isShowBannerAd() {
       // return false;
        return (boolean) SharedPreferencesUtil.getParam(AppConfig.musicEractBannerAd,true);
    }

    @Override
    public boolean isShowNativeAd() {
        return true;
    }

    @Override
    public boolean isShowVerticllAndStimulateAd() {
        return true;
    }

    @Override
    public void onStimulateSuccessCall() {

    }

    @Override
    public void onStimulateSuccessDissmissCall() {

    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showVerticalInterstitialAd();
//            }
//        },5000);
//    }


    private Handler handler=new Handler();

    @Override
    public void initData() {
        super.initData();
        LiveDataBus.get().with(LiveBusConfig.updateApp,Boolean.class).postValue(false);

        AppUpdateUtils.update(MainActivity.this,true);

        showCommentFromFeatureDialog("觉得功能不错的话给个好评鼓励一下吧!");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showVerticalCommentInterstitialAd();

            }
        },1000);


//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET}, 0);
//            }
//        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = { "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            if (
                    checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            } else {
//                FileUtils.createOrExistsDir(FileUtils.FILE_PATH);
                FileUtils.createOrExistsDir(FileUtils.COMPRESSION_FILE_PATH);
//                FileUtils.createOrExistsDir(FileUtils.PIC_FILE_PATH);
            }
        }






        viewPager = binding.mainViewpager;
        mainRadio = binding.buttonLl.mainRg;

        initFragment();
        initRadioGroup();



    }

    public void setHomeFragment(){
        mainRadio.check(R.id.mall_rb);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                FileUtils.createOrExistsDir(FileUtils.FILE_PATH);
                FileUtils.createOrExistsDir(FileUtils.COMPRESSION_FILE_PATH);
//                FileUtils.createOrExistsDir(FileUtils.PIC_FILE_PATH);
            }else{
                Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void initFragment() {
        mFragment.add(new HomeFragment1());
        mFragment.add(new RecordFragment());
        //mFragment.add(VipFragment.newStance());
        mFragment.add(new MyFragment());


        mainPageAdapter = new HomePageAdapter(getSupportFragmentManager(), mFragment);
        viewPager.setAdapter(mainPageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
    }



    private void initRadioGroup() {

        mainRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.mall_rb:
                        viewPager.setCurrentItem(0);
                        hideGannerAd();
                        break;
                    case R.id.shopping_cart_rb:
                        viewPager.setCurrentItem(1);
                        showGannerAd();
                        break;
                   /* case R.id.vip_rb:
                        viewPager.setCurrentItem(2);

                        break;*/
                    case R.id.wallet_rb:
                        viewPager.setCurrentItem(2);
                        hideGannerAd();
                        break;

                }
            }
        });

    }//选择viewpager跳转对应的fragment,


    @Override //再按一次退出程序
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime < 2000) {//与上次点击返回键时差小于2s退出程序
            super.onBackPressed();
        } else {
            mExitTime = System.currentTimeMillis();
            ToastUtils.show("再按一次返回键退出应用");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SetRingUtil.REQUEST_CODE_SETTING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(this)) {
                    Log.d("Tag call", "授权成功");
                    SetRingUtil.setRing(this);
                } else {
                    ToastUtils.show("授权失败");
                }
            }
        }
    }


    @Override
    public void initViewObservable() {
        super.initViewObservable();
        LiveDataBus.get().with(LiveDataBusData.backHome,String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(binding.mainViewpager.getCurrentItem()!=1){
                    binding.mainViewpager.setCurrentItem(1);
                    binding.buttonLl.shoppingCartRb.setChecked(true);

                }
            }
        });//注册订阅
    }
}
