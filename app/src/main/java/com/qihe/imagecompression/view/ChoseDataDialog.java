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
import com.qihe.imagecompression.databinding.ChoseDataDialogBinding;

import java.util.List;

/**
 * Created by lipei on 2019/1/7.
 */

public class ChoseDataDialog {
    private CustomDialog customDialog;
    private ItemListener itemListener;//选择地址
    private Context mContext;
    private List mData;
    private String coinName;
    private int coinNamepostion;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    //点击确认按钮回调接口
    public interface ItemListener {
        void onClickChose(String coinName, int coinNamePostion);



    }


    public ChoseDataDialog(Context mContext) {
        this.mContext = mContext;
        customDialog = new CustomDialog(mContext);
    }


    public ChoseDataDialog(Context mContext,List mData) {
        this.mContext = mContext;
        this.mData=mData;
        customDialog = new CustomDialog(mContext);
    }

    public ChoseDataDialog addItemListener(ItemListener itemListener) {
        setItemListener(itemListener);
        return this;
    }


    public ChoseDataDialog show() {
        customDialog.show();
        return this;

    }


    public ChoseDataDialog dismiss() {
        customDialog.dismiss();
        return this;

    }


    public final class CustomDialog extends Dialog implements View.OnClickListener{



        public CustomDialog(@NonNull Context context) {
            super(context, R.style.ActionChosePriceSheetDialogStyle);
            init();
        }

        private void init() {

            ChoseDataDialogBinding chosePriceDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.chose_data_dialog, null, false);
            setContentView(chosePriceDialogBinding.getRoot());
            chosePriceDialogBinding.canelTv.setOnClickListener(this);
            chosePriceDialogBinding.sureTv.setOnClickListener(this);
            if(mData!=null&& mData.size()>0){
                chosePriceDialogBinding.wheelPicker.setData(mData);
            }

            chosePriceDialogBinding.wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    if(data instanceof String){
                        coinName= (String) data;

                    }else if(data instanceof Integer){
                        coinName=data+"";
                    }
                    coinNamepostion=position;


                }
            });





            setCancelable(true);
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        }


        @Override
        public void onClick(View v) {
            dismiss();
            if(v.getId()==R.id.sure_tv){
                if(coinName!=null && !coinName.isEmpty()){
                    if(itemListener!=null){
                        itemListener.onClickChose(coinName,coinNamepostion);
                    }
                }

            }

        }
    }
}
