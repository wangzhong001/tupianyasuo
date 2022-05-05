package com.qihe.imagecompression.util;

import android.view.View;
import android.view.ViewGroup;


public class ViewUtil {


    /**
     * 动态设置控件的Margin
     *
     * @param view   控件
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


}


