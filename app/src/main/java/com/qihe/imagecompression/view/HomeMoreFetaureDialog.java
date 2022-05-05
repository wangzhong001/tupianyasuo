package com.qihe.imagecompression.view;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.databinding.HomeMoreFetrueDialogBinding;
import com.qihe.imagecompression.viewmodel.ResourceVideoViewModel;

/**
 * Created by lipei on 2019/1/7.
 */

public class HomeMoreFetaureDialog {
    private CustomDialog customDialog;
    private ItemListener itemListener;//选择地址
    private Context mContext;
    private ResourceVideoViewModel featuresViewModel;


    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    //点击确认按钮回调接口
    public interface ItemListener {
        void onClickChose(String coinName, int coinNamePostion);



    }


    public HomeMoreFetaureDialog(Context mContext, ResourceVideoViewModel featuresViewModel) {
        this.mContext = mContext;
        this.featuresViewModel=featuresViewModel;
        customDialog = new CustomDialog(mContext);
    }




    public HomeMoreFetaureDialog addItemListener(ItemListener itemListener) {
        setItemListener(itemListener);
        return this;
    }


    public HomeMoreFetaureDialog show() {
        customDialog.show();
        return this;

    }


    public HomeMoreFetaureDialog dismiss() {
        customDialog.dismiss();
        return this;

    }


    public final class CustomDialog extends Dialog {



        public CustomDialog(@NonNull Context context) {
            super(context, R.style.ActionChosePriceSheetDialogStyle);
            init();
        }

        private void init() {

            HomeMoreFetrueDialogBinding chosePriceDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.home_more_fetrue_dialog, null, false);
            setContentView(chosePriceDialogBinding.getRoot());
            chosePriceDialogBinding.setBaseViewModel(featuresViewModel);

            featuresViewModel.dissmissLiveData.observeForever(new Observer<ResourceVideoViewModel>() {
                @Override
                public void onChanged(@Nullable ResourceVideoViewModel featuresViewModel) {
                    dismiss();
                }
            });

            chosePriceDialogBinding.dissView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            setCancelable(true);
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        }



    }
}
