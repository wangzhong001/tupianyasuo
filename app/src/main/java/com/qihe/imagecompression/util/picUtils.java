package com.qihe.imagecompression.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.app.AdApplcation;
import com.qihe.imagecompression.view.GlideRoundTransform;

import java.io.File;

/**
 * Created by lipei on 2018/1/29.
 */

public class picUtils {


    // 下载一个图片 设置到ImageView
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        if (url == null || view.getContext() == null) {
            view.setImageResource(R.drawable.pic_error);
        } else {

            Glide.with(view.getContext()).load(url).placeholder(R.drawable.pic_error)
                    .error(R.drawable.pic_error).into(view);


        }
    }

    // 下载一个图片 设置到ImageView
    @BindingAdapter({"imageRoundUrl"})
    public static void loadRoundImage(final ImageView view, final String url) {
        if (url == null || view.getContext() == null) {
            view.setImageResource(R.drawable.pic_error);
        } else {
//            view.setBackgroundResource(R.drawable.pic_error);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
                    Glide.with(view.getContext()).load(new File(url)).centerCrop().dontAnimate().placeholder(R.drawable.pic_error)
                            .transform(new CenterCrop(AdApplcation.getContext()), new GlideRoundTransform(AdApplcation.getContext(),4))
                            .error(R.drawable.pic_error).into(view);
//                }
//            },500);




        }
    }




    @BindingAdapter({"imageLoacl"})
    public static void loadFilePic(ImageView view, String url) {
        if (url == null || view.getContext() == null) {
            view.setImageResource(R.drawable.pic_error);
        } else {
            // Glide代替Volley

            if(url.contains("gif")){
                Glide.with(view.getContext()).load(new File(url)).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(view);
            }else {
                Glide.with(view.getContext()).load(new File(url)).centerCrop().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.pic_error)

                        .error(R.drawable.pic_error).into(view);
            }

        }
    }


    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
