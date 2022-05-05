package com.qihe.imagecompression.binding.viewadapter;

import android.databinding.BindingAdapter;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.qihe.imagecompression.util.ViewUtil;
import com.qihe.imagecompression.view.AudioEditView;
import com.xinqidian.adcommon.binding.command.BindingCommand;
import com.xinqidian.adcommon.util.Common;

/**
 * Created by lipei on 2020/6/3.
 */

public class AudioEditViewAdapter {

    @BindingAdapter(value = {"onScrollThumb","onScrollCursor"}, requireAll = false)
    public static void onAudioEditViewScrollListener(final AudioEditView audioEditView, final BindingCommand<AudioEditView.ScrollInfo> bindingCommand,final BindingCommand<AudioEditView.ScrollInfo> bindingCommandTwo) {
        audioEditView.setOnScrollListener(new AudioEditView.OnScrollListener() {
            @Override
            public void onScrollThumb(AudioEditView.ScrollInfo info) {
                bindingCommand.execute(info);

            }

            @Override
            public void onScrollCursor(AudioEditView.ScrollInfo info) {
                bindingCommandTwo.execute(info);

            }
        });
    }



    @BindingAdapter(value = {"setLeftMargins"}, requireAll = false)
    public static void onLeftSetMargins(final TextView audioEditView, AudioEditView.ScrollInfo scrollInfo) {
        if(scrollInfo!=null){

            int marginLeft = (int) scrollInfo.getStartPx() -audioEditView.getWidth() / 2;
            ViewUtil.setMargins(audioEditView, marginLeft, 0, 0, 0);
        }



    }


    @BindingAdapter(value = {"setRightMargins"}, requireAll = false)
    public static void onRightSetMargins(final TextView audioEditView, AudioEditView.ScrollInfo scrollInfo) {
        if(scrollInfo!=null){
            DisplayMetrics metrics = Common.getApplication().getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;

            int marginRight = (int) (screenWidth - scrollInfo.getEndPx() - audioEditView.getWidth() / 2);
            ViewUtil.setMargins(audioEditView, 0, 0, marginRight, 0);
        }



    }



    @BindingAdapter(value = {"setDuration"}, requireAll = false)
    public static void onSetDuration(final AudioEditView audioEditView,int voiceDuration) {
        audioEditView.setDuration(voiceDuration);



    }


    @BindingAdapter(value = {"setUpdateCursor"}, requireAll = false)
    public static void onUpdateCursor(final AudioEditView audioEditView,float voiceDuration) {
        audioEditView.updateCursor(voiceDuration);


    }




}
