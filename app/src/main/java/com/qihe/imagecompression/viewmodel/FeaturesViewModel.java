package com.qihe.imagecompression.viewmodel;

import android.app.Application;
import android.app.Dialog;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.qihe.imagecompression.BR;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.ArouterPath;
import com.qihe.imagecompression.model.FileModel;
import com.qihe.imagecompression.util.ActivityUtil;
import com.qihe.imagecompression.util.FileUtils;
import com.qihe.imagecompression.util.Rxutil;
import com.qihe.imagecompression.viewmodel.itemviewmodel.MineTabItemViewModel;
import com.xinqidian.adcommon.base.BaseViewModel;
import com.xinqidian.adcommon.binding.command.BindingAction;
import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.login.UserUtil;
import com.xinqidian.adcommon.util.SharedPreferencesUtil;
import com.xinqidian.adcommon.util.ToastUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by lipei on 2020/6/12.
 */

public class FeaturesViewModel extends BaseViewModel {
    public FeaturesViewModel(@NonNull Application application) {
        super(application);
    }



    public ObservableField<String> titleString = new ObservableField<>("");

    public BindingCommand backClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


    /**
     * 开始
     */
    public MutableLiveData<FeaturesViewModel> startLiveData = new MutableLiveData<>();
    public BindingCommand startClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startLiveData.postValue(FeaturesViewModel.this);
        }
    });


    /**
     * 选择文件
     */
    public MutableLiveData<FeaturesViewModel> chosePathLiveData = new MutableLiveData<>();
    public BindingCommand chosePathClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            chosePathLiveData.postValue(FeaturesViewModel.this);
        }
    });


    public ObservableField<String> chosePath = new ObservableField<>("请选择源文件");

    public ObservableField<String> formatString = new ObservableField<>("MP4");


    /**
     * 选择格式
     */
    public MutableLiveData<FeaturesViewModel> choseFormatLiveData = new MutableLiveData<>();
    public BindingCommand choseFormatClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            choseFormatLiveData.postValue(FeaturesViewModel.this);
        }
    });


    /**
     * 输入名称
     */
    public MutableLiveData<FeaturesViewModel> inputLiveData = new MutableLiveData<>();
    public ObservableField<String> fileName = new ObservableField<>("请输入文件名");
    public BindingCommand inputClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            inputLiveData.postValue(FeaturesViewModel.this);
        }
    });

    /**
     * 开始时间
     */
    public MutableLiveData<FeaturesViewModel> startTimeLiveData=new MutableLiveData<>();
    public ObservableField<String> startTimeString=new ObservableField<>("0");
    public BindingCommand startTimeClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startTimeLiveData.postValue(FeaturesViewModel.this);
        }
    });

    /**
     * 总时长时间
     */
    public MutableLiveData<FeaturesViewModel> allTimeLiveData=new MutableLiveData<>();//可变的观察类
    public ObservableField<String> allTimeString=new ObservableField<>("3");

    public BindingCommand allTimeClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            allTimeLiveData.postValue(FeaturesViewModel.this);
        }
    });




    public ObservableField<Integer> postion = new ObservableField<>();

    public ObservableBoolean isVideo=new ObservableBoolean();

    public ObservableBoolean isGif=new ObservableBoolean();

    public void setFormat(int postion) {
        if (postion == 1 || postion == 3) {
            formatString.set("jpg");
            isVideo.set(false);
            isGif.set(false);

        } else if (postion == 4) {
            formatString.set("gif");
            isVideo.set(false);
            isGif.set(true);

        } else {
            formatString.set("MP4");
            isVideo.set(true);
            isGif.set(false);

        }
    }


    /**
     * 登录
     */
    public BindingCommand toLoginClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (!SharedPreferencesUtil.isLogin()) {
                ActivityUtil.start(ArouterPath.login_activity);
            }
        }
    });


    /**
     * 设置
     */
    public BindingCommand settingClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ActivityUtil.start(ArouterPath.setting_activity);
        }
    });


    /**
     * 是否登录
     */

    public ObservableBoolean isLogin = new ObservableBoolean();


    /**
     * 意见反馈
     */
    public MutableLiveData<FeaturesViewModel> feedBackLiveData = new MutableLiveData<>();
    public BindingCommand feedBackClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            feedBackLiveData.postValue(FeaturesViewModel.this);
        }
    });


    public ObservableField<String> userName = new ObservableField<>("点击登录");


    public ObservableField<String> phoneUmber = new ObservableField<>("");

    public ObservableField<String> passWord = new ObservableField<>("");


    public MutableLiveData<FeaturesViewModel> loginLiveData=new MutableLiveData<>();

    public BindingCommand loginClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loginLiveData.postValue(FeaturesViewModel.this);


        }
    });


    /**
     * 退出登录
     */
    public BindingCommand exitClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            UserUtil.exitLogin();

        }
    });


    /**
     * 是否在录音中
     */
    public ObservableBoolean isRecording = new ObservableBoolean();

    public ObservableBoolean isPause = new ObservableBoolean();


    public ObservableBoolean isShowButton = new ObservableBoolean();


    /**
     * 开始和停止录音
     */
    public MutableLiveData<Boolean> startOrStopRecordLiveData=new MutableLiveData<>();
    public BindingCommand startOrStopRecordClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startOrStopRecordLiveData.postValue(isRecording.get());


        }
    });


    /**
     * 开始和暂停录音
     */
    public MutableLiveData<Boolean> isPauseLiveData=new MutableLiveData<>();
    public BindingCommand startOrPause=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(isRecording.get()){
                //开始或者暂停
                isPauseLiveData.postValue(isPause.get());

            }else {
                isPause.set(false);
                startOrStopRecordLiveData.postValue(isRecording.get());

            }
        }
    });


    /**
     * 是否消除原音
     */
    public ObservableBoolean isDestoryVolue=new ObservableBoolean(true);

    public BindingCommand isDestoryVolueClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isDestoryVolue.set(!isDestoryVolue.get());
        }
    });


    /**
     * 试听
     */

    public MutableLiveData<FeaturesViewModel> tryListenLiveData=new MutableLiveData<>();
    public BindingCommand tryListenClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(isRecording.get()){
                ToastUtils.show("请先停止录音");
                return;
            }
            tryListenLiveData.postValue(FeaturesViewModel.this);
        }
    });

    public ObservableField<String> timeString=new ObservableField<>("00:00:00");

    /**
     * 采样率选取
     */
    public MutableLiveData<FeaturesViewModel> samplingRateLiveData=new MutableLiveData<>();
    public BindingCommand samplingRateClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            samplingRateLiveData.postValue(FeaturesViewModel.this);
        }
    });

    /**
     * 比特率选取
     */
    public MutableLiveData<FeaturesViewModel> bitRateLiveData=new MutableLiveData<>();
    public BindingCommand bitRateClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            bitRateLiveData.postValue(FeaturesViewModel.this);
        }
    });


    /**
     * 淡入选取
     */
    public MutableLiveData<FeaturesViewModel> fadeInLiveData=new MutableLiveData<>();
    public BindingCommand fadeInClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            fadeInLiveData.postValue(FeaturesViewModel.this);
        }
    });


    /**
     * 淡出选取
     */
    public MutableLiveData<FeaturesViewModel> fadeOutLiveData=new MutableLiveData<>();
    public BindingCommand fadeOutClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            fadeOutLiveData.postValue(FeaturesViewModel.this);
        }
    });


    public ObservableField<String> samplingRateString=new ObservableField<>("32000");

    public ObservableField<String> bitRateString=new ObservableField<>("32");

    public ObservableField<String> fadeInString=new ObservableField<>("关闭");

    public ObservableField<String> fadeOutString=new ObservableField<>("关闭");


    /**
     * 获取编辑的文件
     */


    public BindingRecyclerViewAdapter<MineTabItemViewModel> mineFileAdapter=new BindingRecyclerViewAdapter<>();

    public ItemBinding<MineTabItemViewModel> mineFileItemBinding=ItemBinding.of(BR.baseViewModel, R.layout.mine_tab_item);

    public ObservableArrayList<MineTabItemViewModel> mineFileList=new ObservableArrayList<>();


    public ObservableBoolean isEmpty=new ObservableBoolean(true);

    /**
     * 获取文件名及后缀
     */

    private Disposable disposable;


    public void getMusic(final Dialog dialog){
        dialog.show();
        disposable = Single
                .create(new SingleOnSubscribe<List<File>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<File>> e) {
                        e.onSuccess(FileUtils.listFilesInDir(FileUtils.AUDIO_FILE_PATH,false));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                })
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> musicInfos) {
//                        musicInfoList = musicInfos;
//                        videoLiveData.postValue(ResourceVideoViewModel.this);

                        if(mineFileList.size()>0){
                            mineFileList.clear();
                        }

                        int size=musicInfos.size();
                        for (int i=0;i<size;i++){
                            if(!FileUtils.isDir(musicInfos.get(i))){
                                FileModel fileModel=new FileModel();
                                fileModel.setName(musicInfos.get(i).getName());
                                fileModel.setPath(musicInfos.get(i).getAbsolutePath());
                                fileModel.setVideo(true);
                                fileModel.setSize(Rxutil.showFileSize(musicInfos.get(i).length()));
                                mineFileList.add(new MineTabItemViewModel(FeaturesViewModel.this,fileModel));
                            }

                        }
                        //是否显示没有数据视图
                        isEmpty.set(mineFileList == null || mineFileList.size() == 0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        dialog.dismiss();
                        isEmpty.set(true);
//                        ToastUtils.show("未知错误，请稍后重试");
                    }
                });


    }


    @Override
    public void onDestory() {
        super.onDestory();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    public void getFileList(final int type){





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mineFileList!=null){
                    if(mineFileList.size()>0){
                        mineFileList.clear();
                    }
                }

                if(type==0){
                    //视频
                    List<File> mList= FileUtils.listFilesInDir(FileUtils.FILE_PATH,false);
                    if(mList!=null){
                        if(mList.size()>0){
                            isEmpty.set(false);

                            int size=mList.size();
                            for (int i=0;i<size;i++){
                                if(!FileUtils.isDir(mList.get(i))){
                                    FileModel fileModel=new FileModel();
                                    fileModel.setName(mList.get(i).getName());
                                    fileModel.setPath(mList.get(i).getAbsolutePath());
                                    fileModel.setVideo(true);
                                    fileModel.setSize(Rxutil.showFileSize(mList.get(i).length()));
                                    mineFileList.add(new MineTabItemViewModel(FeaturesViewModel.this,fileModel));
                                }

                            }
                        }else {
                            isEmpty.set(true);
                        }
                    }else {
                        isEmpty.set(true);
                    }
                }else if(type==1) {
                    List<File> mList= FileUtils.listFilesInDir(FileUtils.AUDIO_FILE_PATH,false);
                    if(mList!=null){
                        if(mList.size()>0){
                            isEmpty.set(false);

                            int size=mList.size();
                            for (int i=0;i<size;i++){
                                FileModel fileModel=new FileModel();
                                fileModel.setName(mList.get(i).getName());
                                fileModel.setPath(mList.get(i).getAbsolutePath());
                                fileModel.setVideo(false);
                                fileModel.setSize(Rxutil.showFileSize(mList.get(i).length()));
                                mineFileList.add(new MineTabItemViewModel(FeaturesViewModel.this,fileModel));
                            }

                        }else {
                            isEmpty.set(true);

                        }
                    }else {
                        isEmpty.set(true);

                    }
                }else {
                    List<File> mList= FileUtils.listFilesInDir(FileUtils.PIC_FILE_PATH,false);
                    if(mList!=null){
                        if(mList.size()>0){
                            isEmpty.set(false);

                            int size=mList.size();
                            for (int i=0;i<size;i++){
                                FileModel fileModel=new FileModel();
                                fileModel.setName(mList.get(i).getName());
                                fileModel.setPath(mList.get(i).getAbsolutePath());
                                fileModel.setPic(true);
                                fileModel.setVideo(true);
                                fileModel.setSize(Rxutil.showFileSize(mList.get(i).length()));
                                mineFileList.add(new MineTabItemViewModel(FeaturesViewModel.this,fileModel));
                            }

                        }else {
                            isEmpty.set(true);

                        }
                    }else {
                        isEmpty.set(true);

                    }
                }
            }
        },500);



    }


    /**
     * 修改
     */
    public MutableLiveData<FeaturesViewModel> showFetureDialogLiveData=new MutableLiveData<>();

    public ObservableField<MineTabItemViewModel> fileModelObservableFiel=new ObservableField<>();
    public void showFetureDialog(MineTabItemViewModel mineTabItemViewModel){
        fileModelObservableFiel.set(mineTabItemViewModel);
        showFetureDialogLiveData.postValue(FeaturesViewModel.this);
    }


    /**
     * 重命名
     */
    public MutableLiveData<FeaturesViewModel> reNameLiveData=new MutableLiveData<>();
    public BindingCommand renameClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            reNameLiveData.postValue(FeaturesViewModel.this);
            dissmissLiveData.postValue(FeaturesViewModel.this);

        }
    });


    /**
     * 打开方式
     */
    public MutableLiveData<FeaturesViewModel> openLiveData=new MutableLiveData<>();
    public BindingCommand openClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            openLiveData.postValue(FeaturesViewModel.this);
            dissmissLiveData.postValue(FeaturesViewModel.this);


        }
    });

    public void openPic(){
        openLiveData.postValue(FeaturesViewModel.this);

    }


    /**
     * 分享
     */
    public MutableLiveData<String> shareLiveData=new MutableLiveData<>();
    public BindingCommand shareClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            AndroidShareUtils.shareAudio(getApplication(), fileModelObservableFiel.get().fileModel.getPath());

            shareLiveData.postValue(fileModelObservableFiel.get().fileModel.getPath());
            dissmissLiveData.postValue(FeaturesViewModel.this);

        }
    });



    /**
     * 设置铃声
     */
    public MutableLiveData<FeaturesViewModel> setRingLiveData=new MutableLiveData<>();
    public BindingCommand setRingClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            AndroidShareUtils.shareAudio(getApplication(), fileModelObservableFiel.get().fileModel.getPath());

            setRingLiveData.postValue(FeaturesViewModel.this);
            dissmissLiveData.postValue(FeaturesViewModel.this);

        }
    });






    /**
     * 剪辑
     */
    public MutableLiveData<FeaturesViewModel> jianJigLiveData=new MutableLiveData<>();
    public BindingCommand jianjilick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            AndroidShareUtils.shareAudio(getApplication(), fileModelObservableFiel.get().fileModel.getPath());

            jianJigLiveData.postValue(FeaturesViewModel.this);
            dissmissLiveData.postValue(FeaturesViewModel.this);

        }
    });


    /**
     * 删除
     */
    public BindingCommand deleteClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            FileUtils.deleteFile(fileModelObservableFiel.get().fileModel.getPath());
            mineFileList.remove(fileModelObservableFiel.get());
            if(mineFileList.size()==0){
                isEmpty.set(true);
            }
            dissmissLiveData.postValue(FeaturesViewModel.this);

        }
    });


    public MutableLiveData<FeaturesViewModel> dissmissLiveData=new MutableLiveData<>();


    /**
     * 展示名称
     */

    public MutableLiveData<FeaturesViewModel> showFileNameLiveData=new MutableLiveData<>();
    public BindingCommand showFileNameClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            showFileNameLiveData.setValue(FeaturesViewModel.this);
        }
    });

    public ObservableField<String> featureFileName=new ObservableField("");


    public ObservableBoolean isShowTab=new ObservableBoolean(true);

    public ObservableBoolean isVip=new ObservableBoolean();



    /**
     * 文件路径
     */
    public MutableLiveData<FeaturesViewModel> filePathLiveData=new MutableLiveData<>();
    public BindingCommand filePathClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

//            AndroidShareUtils.shareAudio(getApplication(), fileModelObservableFiel.get().fileModel.getPath(),type.get());

            filePathLiveData.postValue(FeaturesViewModel.this);
            dissmissLiveData.postValue(FeaturesViewModel.this);



        }
    });
}

