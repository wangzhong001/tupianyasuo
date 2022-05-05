package com.qihe.imagecompression.view;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.qihe.imagecompression.R;
import com.qihe.imagecompression.databinding.InputDialogBinding;
import com.qihe.imagecompression.util.FileUtils;
import com.xinqidian.adcommon.util.DensityUtil;
import com.xinqidian.adcommon.util.ToastUtils;


/**
 * Created by lipei on 2018/5/14.
 */

public class InputDialog {
    private CustomDialog customDialog;
    private ItemListener itemListener;//选择地址
    private Context mContext;
    private String content;
    private InputDialogBinding payDialogBinding;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    //点击确认按钮回调接口
    public interface ItemListener {
        void onClickSure(String content);

        void onClickCanel();

    }


    public InputDialog(Context mContext, String content) {
        this.mContext = mContext;
        this.content=content;
        customDialog = new CustomDialog(mContext);
    }

    public InputDialog addItemListener(ItemListener itemListener) {
        setItemListener(itemListener);
        return this;
    }


    public InputDialog show() {
        customDialog.show();
        return this;

    }


    public InputDialog dismiss() {
        customDialog.dismiss();
        return this;

    }

    public void setText(String s){
        payDialogBinding.inputEd.setText(s);
    }


    public final class CustomDialog extends Dialog {



        public CustomDialog(@NonNull Context context) {
            super(context, R.style.ActionSheetDialogStyle);
            init();
        }

        private void init() {

            payDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.input_dialog, null, false);
            setContentView(payDialogBinding.getRoot());

            payDialogBinding.canelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (itemListener != null){
                        itemListener.onClickCanel();
                    }
                }
            });


            payDialogBinding.typeTv.setText(getContext().getString(R.string.wenjian_path)+ FileUtils.NEW_COMPRESSION_FILE_PATH
                    );


            payDialogBinding.sureTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemListener!=null){
                        if(payDialogBinding.inputEd.getText().toString().isEmpty()){
                            ToastUtils.show("请输入名称");
                            return;
                        }
                        dismiss();

                        itemListener.onClickSure(payDialogBinding.inputEd.getText().toString());
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
