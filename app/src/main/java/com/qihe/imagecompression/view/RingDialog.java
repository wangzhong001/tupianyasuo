package com.qihe.imagecompression.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.databinding.LayoutAudioRingBottomSheetDialogBinding;
import com.qihe.imagecompression.util.SetRingUtil;
import com.qihe.imagecompression.viewmodel.FeaturesViewModel;

/**
 * Created by lipei on 2019/1/7.
 */

public class RingDialog {
    private CustomDialog customDialog;
    private ItemListener itemListener;//选择地址
    private Context mContext;
    private FeaturesViewModel featuresViewModel;
    //默认选中手机铃声
    private int typeSelect = 0;
    private LayoutAudioRingBottomSheetDialogBinding chosePriceDialogBinding;


    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    //点击确认按钮回调接口
    public interface ItemListener {
        void onClickChose(String coinName, int coinNamePostion);



    }


    public RingDialog(Context mContext, FeaturesViewModel featuresViewModel) {
        this.mContext = mContext;
        this.featuresViewModel=featuresViewModel;
        customDialog = new CustomDialog(mContext);
    }




    public RingDialog addItemListener(ItemListener itemListener) {
        setItemListener(itemListener);
        return this;
    }


    public RingDialog show() {
        customDialog.show();
        return this;

    }


    public RingDialog dismiss() {
        customDialog.dismiss();
        return this;

    }


    public final class CustomDialog extends Dialog {



        public CustomDialog(@NonNull Context context) {
            super(context, R.style.ActionChosePriceSheetDialogStyle);
            init();
        }

        private void init() {

            chosePriceDialogBinding= DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_audio_ring_bottom_sheet_dialog, null, false);
            setContentView(chosePriceDialogBinding.getRoot());
            chosePriceDialogBinding.setBaseViewModel(featuresViewModel);

//            featuresViewModel.dissmissLiveData.observeForever(new Observer<FeaturesViewModel>() {
//                @Override
//                public void onChanged(@Nullable FeaturesViewModel featuresViewModel) {
//                    dismiss();
//                }
//            });

            chosePriceDialogBinding.dissView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            chosePriceDialogBinding.layoutPhoneRing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSelectStatus(0);
                }
            });
            chosePriceDialogBinding.layoutNotificationRing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSelectStatus(1);
                }
            });
            chosePriceDialogBinding.layoutAlarmRing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSelectStatus(2);
                }
            });

            chosePriceDialogBinding.tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetRingUtil.setRing((Activity) mContext,typeSelect,featuresViewModel.fileModelObservableFiel.get().fileModel.getPath(),featuresViewModel.fileModelObservableFiel.get().fileModel.getName());

                    dismiss();
                }
            });


            setCancelable(true);
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        }


        private void updateSelectStatus(int type) {
            chosePriceDialogBinding.ivPhoneRing.setImageResource(R.drawable.select_icon);
            chosePriceDialogBinding.ivNotificationRing.setImageResource(R.drawable.select_icon);
            chosePriceDialogBinding.ivAlarmRing.setImageResource(R.drawable.select_icon);

            switch (type) {
                case 0:
                    chosePriceDialogBinding.ivPhoneRing.setImageResource(R.drawable.select_icon_cover);
                    break;
                case 1:
                    chosePriceDialogBinding.ivNotificationRing.setImageResource(R.drawable.select_icon_cover);
                    break;
                case 2:
                    chosePriceDialogBinding.ivAlarmRing.setImageResource(R.drawable.select_icon_cover);
                    break;
            }

            typeSelect = type;
        }


    }
}
