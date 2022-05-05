package com.qihe.imagecompression.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.util.Utils;
import com.qihe.imagecompression.view.GlideRoundTransform;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AddVideoAdapter extends RecyclerView.Adapter<AddVideoAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private List<String> list1;
    private SimpleDateFormat dateFormat;
  //  private MediaMetadataRetriever media;


    public AddVideoAdapter(Context context, List<String> list, List<String> list1) {
        this.context = context;
        this.list = list;
        this.list1 = list1;

    }

    // 条目的点击事件
    private ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public AddVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_add_image, viewGroup, false);
        ViewHolder myViewHolder = new ViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {


        if (i == 0) {
            viewHolder.camera.setVisibility(View.VISIBLE);
        } else {
            if (list.get(i) != null && !list.get(i).equals("")) {

               /* try {
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(list.get(i));

                    String duration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
*/




                viewHolder.camera.setVisibility(View.GONE);
                  /*  if (media.getFrameAtTime() != null) {
                        viewHolder.image.setImageBitmap(media.getFrameAtTime());
                    } else {
                        viewHolder.image.setImageDrawable(context.getResources().getDrawable((R.drawable.loadpicture_icon)));
                    }*/

              /*  Glide.with(context)
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(4000000)
                                        .centerCrop()
                                        .error(R.mipmap.eeeee)//可以忽略
                                        .placeholder(R.mipmap.ppppp)//可以忽略
                        )
                        .load(list.get(i))
                        .into(viewHolder.image);*/

               /* media = new MediaMetadataRetriever();

                media.setDataSource(list.get(i));
                viewHolder.image.setImageBitmap(media.getFrameAtTime());*/

                //  return  media.getFrameAtTime();


             try {
                    Glide.with(context)
                            .load(list.get(i))
                            .placeholder(R.drawable.loadpicture_icon)
                            .error(R.drawable.loadpicture_icon)
                            .thumbnail(0.1f)
                           // .transform(new CenterCrop(context), new GlideRoundTransform(context, 8))
                            .into(viewHolder.image);

                long l = Long.parseLong(list1.get(i));
                String time = dateFormat.format(new Date(l));
                viewHolder.size.setText(time);
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.image.setImageDrawable(context.getResources().getDrawable((R.drawable.loadpicture_icon)));
                }



               /* } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }*/
            }


        }


        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(i);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView size;
        private final LinearLayout camera;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            size = itemView.findViewById(R.id.size);
            camera = itemView.findViewById(R.id.camera);
        }
    }
}
