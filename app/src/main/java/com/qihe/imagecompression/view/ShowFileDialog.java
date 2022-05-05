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
import com.qihe.imagecompression.databinding.ShowFileDialogBinding;
import com.xinqidian.adcommon.util.DensityUtil;


/**
 * Created by lipei on 2018/5/14.
 */

public class ShowFileDialog {
    private CustomDialog customDialog;
    private ItemListener itemListener;//选择地址
    private Context mContext;
    private String content;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    //点击确认按钮回调接口
    public interface ItemListener {
        void onClickSure(String content);

        void onClickCanel();

    }


    public ShowFileDialog(Context mContext, String content) {
        this.mContext = mContext;
        this.content=content;
        customDialog = new CustomDialog(mContext);
    }

    public ShowFileDialog addItemListener(ItemListener itemListener) {
        setItemListener(itemListener);
        return this;
    }


    public ShowFileDialog show() {
        customDialog.show();
        return this;

    }


    public ShowFileDialog dismiss() {
        customDialog.dismiss();
        return this;

    }


    public final class CustomDialog extends Dialog {



        public CustomDialog(@NonNull Context context) {
            super(context, R.style.ActionSheetDialogStyle);
            init();
        }

        private void init() {

            final ShowFileDialogBinding payDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.show_file_dialog, null, false);
            setContentView(payDialogBinding.getRoot());
            payDialogBinding.inputEd.setText(content);



            payDialogBinding.sureTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemListener!=null){

                        dismiss();

//                        itemListener.onClickSure(payDialogBinding.inputEd.getText().toString());
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
