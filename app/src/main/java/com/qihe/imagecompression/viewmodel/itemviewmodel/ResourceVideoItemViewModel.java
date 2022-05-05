package com.qihe.imagecompression.viewmodel.itemviewmodel;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.model.MusicInfo;
import com.qihe.imagecompression.viewmodel.ResourceVideoViewModel;
import com.xinqidian.adcommon.base.ItemViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.bus.LiveDataBus;
import com.xinqidian.adcommon.util.KLog;


/**
 * Created by lipei on 2020/5/22.
 */

public class ResourceVideoItemViewModel extends ItemViewModel<ResourceVideoViewModel> {
    private static final String TAG = "ResourceVideoItemViewMo";

    public ObservableBoolean isHasChose = new ObservableBoolean();
    public MusicInfo song;

    public ResourceVideoItemViewModel(@NonNull ResourceVideoViewModel viewModel, MusicInfo song) {
        super(viewModel);
        this.song = song;
    }


    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            KLog.e(TAG, song.getParentPath() + "--->" + song.getPath());
//            if(!isHasChose.get()){
            isHasChose.set(!isHasChose.get());
            song.setHasChose(isHasChose.get());
            LiveDataBus.get().with(LiveDataBusData.choseState, Boolean.class).postValue(true);
            LiveDataBus.get().with(LiveDataBusData.chosePath, MusicInfo.class).postValue(song);

//            }


        }
    });

}
