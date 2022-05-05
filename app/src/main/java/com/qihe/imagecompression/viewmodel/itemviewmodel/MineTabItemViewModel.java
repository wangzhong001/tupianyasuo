package com.qihe.imagecompression.viewmodel.itemviewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.app.LiveDataBusData;
import com.qihe.imagecompression.model.FileModel;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.viewmodel.FeaturesViewModel;
import com.xinqidian.adcommon.base.ItemViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;

/**
 * Created by lipei on 2020/6/15.
 */

public class MineTabItemViewModel extends ItemViewModel<FeaturesViewModel> {
    public FileModel fileModel;
    public ObservableField<String> name=new ObservableField<>("");
    public MineTabItemViewModel(@NonNull FeaturesViewModel viewModel,FileModel fileModel) {
        super(viewModel);
        this.fileModel=fileModel;
        name.set(fileModel.getName());
    }

    public BindingCommand featrueClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            viewModel.showFetureDialog(MineTabItemViewModel.this);
        }
    });

    public BindingCommand itemClickCommand=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            if(fileModel.isPic()){
////                viewModel.fileModelObservableFiel.set(MineTabItemViewModel.this);
////                viewModel.openPic();
//                ActivityUtil.start(ArouterPath.pic_activity, LiveDataBusData.chosePath,fileModel.getPath());
//
//            }else {
//                if(fileModel.isVideo()){
//                    ActivityUtil.start(ArouterPath.new_video_play_activity, LiveDataBusData.chosePath,fileModel.getPath());
//
//                }else {
                    ActivityUtil.start(ArouterPath.audition_activity, LiveDataBusData.chosePath,fileModel.getPath());
//
//                }
//            }

        }
    });
}
