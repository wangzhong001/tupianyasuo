package com.qihe.imagecompression.viewmodel.itemviewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.model.MusicInfo;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.viewmodel.ResourceVideoViewModel;
import com.xinqidian.adcommon.base.ItemViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;


/**
 * Created by lipei on 2020/5/22.
 */

public class VideoListItemViewModel extends ItemViewModel<ResourceVideoViewModel> {
    public MusicInfo song;
    public ObservableField<String> name=new ObservableField<>("");

    public VideoListItemViewModel(@NonNull ResourceVideoViewModel viewModel, MusicInfo song) {
        super(viewModel);
        this.song=song;
        name.set(song.getName());
    }


    public BindingCommand itemClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ActivityUtil.start(ArouterPath.extract_audio_activity, LiveDataBusData.chosePath, song.getPath());


        }
    });


    public BindingCommand featrueClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            viewModel.showFetureDialog(VideoListItemViewModel.this);
        }
    });

}
