package com.qihe.imagecompression.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.qihe.imagecompression.app.LiveDataBusData;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.bus.LiveDataBus;


/**
 * Created by lipei on 2020/5/26.
 */

public class AudioUpdateViewModel extends BaseViewModel {
    public AudioUpdateViewModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand backClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });



    public ObservableField<String> fileName=new ObservableField<>("");


    public ObservableBoolean isPlay=new ObservableBoolean(true);

    public ObservableBoolean isPlayFinish=new ObservableBoolean();


    public MutableLiveData<AudioUpdateViewModel> startOrStopLiveData=new MutableLiveData<>();
    public BindingCommand startOrStopClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startOrStopLiveData.postValue(AudioUpdateViewModel.this);

        }
    });



    public ObservableField<String> filePath=new ObservableField<>("");

    /**
     * 分享
     */
    public MutableLiveData<String> shareLiveData=new MutableLiveData<>();
    public BindingCommand shareClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            AndroidShareUtils.shareAudio(getApplication(), filePath.get());
            shareLiveData.postValue(filePath.get());


        }
    });

    /**
     * 返回首页
     */
    public BindingCommand backHome=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            LiveDataBus.get().with(LiveDataBusData.backHome,String.class).postValue(LiveDataBusData.backHome);
            finish();


        }
    });

}
