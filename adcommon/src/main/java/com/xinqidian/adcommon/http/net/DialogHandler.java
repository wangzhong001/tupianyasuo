package com.xinqidian.adcommon.http.net;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.xinqidian.adcommon.http.DialogCancelListener;


/**
 * Created by 李佩
 * @date 2018/01/28
 * @desrcption 创建一个dialoghandler类来操作dialog加载进度框  以便于 请求取消的处理
 */

public class DialogHandler extends Handler {
    /**
     * 显示加载框
     */
    public static final int SHOW_PROGRESS_DIALOG=1;

    /**
     * 隐藏加载框
     */
    public static final int DISMISS_PROGRESS_DIALOG=2;

    private Dialog loadingDialog;

    private DialogCancelListener dialogCancelListener;

    /**
     * 构造方法接收一个加载框对象,由对应的view层创建之后传递过来
     * @param dialog
     * @param dialogCancelListener
     */
    public DialogHandler(Dialog dialog, DialogCancelListener dialogCancelListener){
        this.loadingDialog=dialog;
        this.dialogCancelListener=dialogCancelListener;
        initDialogDismissListenner();

    }

    private void initDialogDismissListenner() {
        if(loadingDialog!=null){
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if(dialogCancelListener!=null){
                        dialogCancelListener.onCancel();
                    }
                }
            });
        }
    }

    /**
     * 显示加载框
     */
    private void showLoadingDialog(){
        if(loadingDialog!=null){
            loadingDialog.show();
        }
    }

    private void dismissLoadingDialog(){
        if(loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }

    @Override
    public void handleMessage(Message msg) {
       switch (msg.what){
           case SHOW_PROGRESS_DIALOG:
               showLoadingDialog();
               break;
           case DISMISS_PROGRESS_DIALOG:
               dismissLoadingDialog();
               break;
       }
    }
}
