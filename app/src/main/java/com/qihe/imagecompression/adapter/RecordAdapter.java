package com.qihe.imagecompression.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.util.MediaFile;
import com.qihe.imagecompression.util.Utils;
import com.qihe.imagecompression.view.GlideRoundTransform;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private Context context;
    private List<String> list;

    private boolean isShowDelete = false;

    private List<String> selectList = new ArrayList<>();
    private BitmapFactory.Options opts;

    public RecordAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    // 条目的点击事件
    private AddImageAdapter.ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AddImageAdapter.ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    // 条目更多的点击事件
    private AddImageAdapter.ItemClickListener mItemMoreClickListener;

    public interface ItemMoreClickListener {
        void onItemClick(int position);
    }

    public void setOnItemMoreClickListener(AddImageAdapter.ItemClickListener itemMoreClickListener) {
        this.mItemMoreClickListener = itemMoreClickListener;
    }


    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_record, viewGroup, false);
        ViewHolder myViewHolder = new ViewHolder(inflater);
        return myViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        /*Glide.with(context)
                .load(list.get(i))
                .asBitmap()
                .placeholder(R.drawable.loadpicture_icon)
                .error(R.drawable.loadpicture_icon)
                .thumbnail(0.1f)

                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        viewHolder.image.setImageBitmap(resource);
                        if (Utils.getFileType(list.get(i)) == 0) {
                            viewHolder.size1.setVisibility(View.VISIBLE);
                            int height = resource.getHeight();
                            int width = resource.getWidth();
                            viewHolder.size1.setText(width + "" + " x " + height + "" + "px");
                        } else {
                            viewHolder.size1.setVisibility(View.GONE);
                        }
                    }
                });*/

       /* Glide.with(context)
                .load(list.get(i))
                .asBitmap()
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (Utils.getFileType(list.get(i)) == 0) {
                            viewHolder.size1.setVisibility(View.VISIBLE);
                            int height = resource.getHeight();
                            int width = resource.getWidth();
                            viewHolder.size1.setText(width + "" + " x " + height + "" + "px");
                        } else {
                            viewHolder.size1.setVisibility(View.GONE);
                        }
                    }
                });*/

        if (Utils.getFileType(list.get(i)) == 0) {
            viewHolder.size1.setVisibility(View.VISIBLE);
            opts = new BitmapFactory.Options();
            //只请求图片宽高，不解析图片像素(请求图片属性但不申请内存，解析bitmap对象，该对象不占内存)
            opts.inJustDecodeBounds = true;
            //String path = Environment.getExternalStorageDirectory() + "/dog.jpg";
            BitmapFactory.decodeFile(list.get(i), opts);
            int width = opts.outWidth;
            int height = opts.outHeight;
            viewHolder.size1.setText(width + "" + " x " + height + "" + "px");
        } else {
            viewHolder.size1.setVisibility(View.GONE);
        }



        Glide.with(context)
                .load(list.get(i))
                .placeholder(R.drawable.loadpicture_icon)
                .error(R.drawable.loadpicture_icon)
                .thumbnail(0.1f)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, 8))
                .into(viewHolder.image);

        String substring = list.get(i).substring(0, list.get(i).lastIndexOf("/") + 1);
        viewHolder.name.setText(list.get(i).replace(substring, ""));
        try {
            viewHolder.size.setText(Utils.getFileSize(new File(list.get(i))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String substring1 = list.get(i).substring(0, list.get(i).lastIndexOf(".") + 1);
        viewHolder.format.setText(list.get(i).replace(substring1, ""));
        if (mItemMoreClickListener != null) {
            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemMoreClickListener.onItemClick(i);

                }
            });
        }

        if(isShowDelete){
            viewHolder.delete.setVisibility(View.VISIBLE);
            if(selectList.contains(i+"")){
                viewHolder.delete.setImageDrawable(context.getDrawable(R.drawable.selected_icon2));
            }else{
                viewHolder.delete.setImageDrawable(context.getDrawable(R.drawable.unselected_icon2));
            }

        }else{
            viewHolder.delete.setVisibility(View.GONE);
        }





        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(i);
                    if(isShowDelete){
                        if(selectList.contains(i+"")){
                            selectList.remove(i+"");
                        }else{
                            selectList.add(i+"");
                        }
                        notifyItemChanged(i);
                    }
                }
            });
        }
    }

    public void showDelect() {
        if (isShowDelete) {
            isShowDelete = false;
        } else {
            isShowDelete = true;
        }
        notifyDataSetChanged();
    }

    public void setClear() {
        selectList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView size;
        private final TextView name;
        private final ImageView more;
        private final TextView format;
        private final TextView size1;
        private final ImageView delete;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            size = itemView.findViewById(R.id.size);
            format = itemView.findViewById(R.id.format);
            name = itemView.findViewById(R.id.name);
            more = itemView.findViewById(R.id.more);
            size1 = itemView.findViewById(R.id.size1);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
