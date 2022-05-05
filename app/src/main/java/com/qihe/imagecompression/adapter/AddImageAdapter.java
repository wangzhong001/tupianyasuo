package com.qihe.imagecompression.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.qihe.imagecompression.R;
import com.qihe.imagecompression.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private List<String> addList;


    public AddImageAdapter(Context context, List<String> list,List<String> addList) {
        this.context = context;
        this.list = list;
        this.addList = addList;
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
    public AddImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_add_image, viewGroup, false);
        ViewHolder myViewHolder = new ViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AddImageAdapter.ViewHolder viewHolder, final int i) {
        if(i == 0){
            viewHolder.camera.setVisibility(View.VISIBLE);
        }else{
            viewHolder.camera.setVisibility(View.GONE);
            Glide.with(context)
                    .load(list.get(i))
                    .placeholder(R.drawable.loadpicture_icon)
                    .error(R.drawable.loadpicture_icon)
                    // .thumbnail(0.1f)
                    .into(viewHolder.image);

            try {
                viewHolder.size.setText(Utils.getFileSize(new File(list.get(i))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(addList.contains(list.get(i))){
            viewHolder.xuanze.setVisibility(View.VISIBLE);
        }else{
            viewHolder.xuanze.setVisibility(View.GONE);
        }

        if(mItemClickListener != null){
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
        private final RelativeLayout xuanze;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            size = itemView.findViewById(R.id.size);
            camera = itemView.findViewById(R.id.camera);
            xuanze = itemView.findViewById(R.id.xuanze);

        }

    }
}
