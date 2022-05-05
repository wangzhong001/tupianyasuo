package com.qihe.imagecompression.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.util.ActivityUtil;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.util.ToastUtils;

/**
 * Created by lipei on 2020/5/25.
 */

public class MianViewModel extends BaseViewModel {
    public MianViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * 音频裁剪
     */
    public BindingCommand audioCut=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ActivityUtil.start(ArouterPath.resource_video_activity);

        }
    });

    /**
     * 音频合并
     */
    public BindingCommand audioMerge=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ActivityUtil.start(ArouterPath.resource_video_activity, LiveDataBusData.isMerge,true);

        }
    });



    public MutableLiveData<MianViewModel> showAdLiveData=new MutableLiveData<>();

    public ObservableField<Integer> startType=new ObservableField<>(0);


    public void fromTypeToStart(){
        if(isLingqu.get()){
            ToastUtils.show("请不要频繁的领取奖励哦");
            return;
        }
        if(startType.get()==0){
            ActivityUtil.start(ArouterPath.video_list_activity);

        }else if(startType.get()==1){
            ActivityUtil.start(ArouterPath.video_list_activity,LiveDataBusData.isExtract,true);


        }else if(startType.get()==2){
            ActivityUtil.start(ArouterPath.video_list_activity,LiveDataBusData.isExtract,true);


        }else if(startType.get()==3){
            ActivityUtil.start(ArouterPath.video_list_activity,LiveDataBusData.isAddBackGroundMusic,true);

        }
    }

    /**
     * 视频格式化
     */
    public BindingCommand videoFormatClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startType.set(0);
            isLingqu.set(false);

//            int number = (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING, Contants.SHOW_STIMULATE_NUMBER);
//
//            if(number==0&&!SharedPreferencesUtil.isVip()&&Contants.IS_SHOW_STIMULATE_AD){
//                showAdLiveData.postValue(MianViewModel.this);
//                return;
//            }
            ActivityUtil.start(ArouterPath.video_list_activity);

        }
    });

    /**
     * 音频提取
     */
    public BindingCommand audioExtraction=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startType.set(1);
            isLingqu.set(false);

//            int number = (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING, Contants.SHOW_STIMULATE_NUMBER);
//
//            if(number==0&&!SharedPreferencesUtil.isVip()&&Contants.IS_SHOW_STIMULATE_AD){
//                showAdLiveData.postValue(MianViewModel.this);
//                return;
//            }
            ActivityUtil.start(ArouterPath.video_list_activity,LiveDataBusData.isExtract,true);

        }
    });


    /**
     * 视频压缩
     */
    public BindingCommand videoCompressionClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startType.set(2);
            isLingqu.set(false);

//            int number = (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING, Contants.SHOW_STIMULATE_NUMBER);
//
//            if(number==0&&!SharedPreferencesUtil.isVip()&&Contants.IS_SHOW_STIMULATE_AD){
//                showAdLiveData.postValue(MianViewModel.this);
//                return;
//            }
            ActivityUtil.start(ArouterPath.video_list_activity,LiveDataBusData.isCompression,true);

        }
    });


    /**
     * 视频添加背景音乐
     */
    public BindingCommand videoAddMusicClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLingqu.set(false);

            startType.set(3);
//            int number = (int) SharedPreferencesUtil.getParam(Contants.SHOW_STIMULATE_NUMBER_STRING, Contants.SHOW_STIMULATE_NUMBER);
//
//            if(number==0&&!SharedPreferencesUtil.isVip()&&Contants.IS_SHOW_STIMULATE_AD){
//                showAdLiveData.postValue(MianViewModel.this);
//                return;
//            }

            ActivityUtil.start(ArouterPath.video_list_activity,LiveDataBusData.isAddBackGroundMusic,true);

        }
    });




    /**
     * 领取奖励
     */
    public ObservableBoolean isLingqu=new ObservableBoolean();
    public BindingCommand linquClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLingqu.set(true);
            showAdLiveData.postValue(MianViewModel.this);
        }
    });



    public BindingCommand vipClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ActivityUtil.start(ArouterPath.vip_activity);
        }
    });

    public ObservableField<String> tvGreet=new ObservableField<>("");
}
