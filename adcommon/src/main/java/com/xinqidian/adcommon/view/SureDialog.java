package com.xinqidian.adcommon.view;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.xinqidian.adcommon.R;
import com.xinqidian.adcommon.databinding.SureDialogBinding;
import com.xinqidian.adcommon.util.DensityUtil;


/**
 * Created by lipei on 2018/5/14.
 */

public class SureDialog {
    private CustomDialog customDialog;
    private ItemListener itemListener;//选择地址
    private Context mContext;
    private String content;
    private boolean isAd;
    private String canelString="取消";
    private String sureString="确定";
    private String titleString="解锁";

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    //点击确认按钮回调接口
    public interface ItemListener {
        void onClickSure();

        void onClickCanel();

    }

    public SureDialog(Context mContext, String content, boolean isAd) {
        this.mContext = mContext;
        this.content=content;
        this.isAd=isAd;
        customDialog = new CustomDialog(mContext);
    }


    public SureDialog(Context mContext, String content) {
        this.mContext = mContext;
        this.content=content;
        customDialog = new CustomDialog(mContext);
    }



    public SureDialog(Context mContext, String content,String canelString,String sureString,String titleString) {
        this.mContext = mContext;
        this.content=content;
        this.canelString=canelString;
        this.sureString=sureString;
        this.titleString=titleString;
        customDialog = new CustomDialog(mContext);
    }

    public SureDialog addItemListener(ItemListener itemListener) {
        setItemListener(itemListener);
        return this;
    }


    public SureDialog show() {
        customDialog.show();
        return this;

    }


    public SureDialog dismiss() {
        customDialog.dismiss();
        return this;

    }


    public final class CustomDialog extends Dialog {



        public CustomDialog(@NonNull Context context) {
            super(context, R.style.PrivacyThemeDialog);
            init();
        }

        private void init() {

            SureDialogBinding payDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.sure_dialog, null, false);
            setContentView(payDialogBinding.getRoot());
            payDialogBinding.setContent(content);
            payDialogBinding.setIsAd(isAd);
            payDialogBinding.titleTv.setText(titleString);
            payDialogBinding.canelTv.setText(canelString);
            payDialogBinding.sureTv.setText(sureString);
            if(isAd){
                payDialogBinding.titleTv.setText("请确认");
            }
            payDialogBinding.canelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (itemListener != null){
                        itemListener.onClickCanel();
                    }
                }
            });

            payDialogBinding.sureTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(itemListener!=null){
                        itemListener.onClickSure();
                    }
                }
            });

            setCancelable(true);
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setLayout(DensityUtil.getScreenWidth(mContext) / 6 * 5, WindowManager.LayoutParams.WRAP_CONTENT);

        }


    }


}
