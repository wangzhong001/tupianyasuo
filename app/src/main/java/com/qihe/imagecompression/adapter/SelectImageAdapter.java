package com.qihe.imagecompression.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qihe.imagecompression.R;

import java.util.ArrayList;
import java.util.List;

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder> {

    private Context context;
    private List<String> list;

    public SelectImageAdapter(Context context, List<String> list) {
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


    @Override
    public SelectImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_select_image, viewGroup, false);
        ViewHolder myViewHolder = new ViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(SelectImageAdapter.ViewHolder viewHolder, final int i) {

        Glide.with(context)
                .load(list.get(i))
                .placeholder(R.drawable.loadpicture_icon)
                .error(R.drawable.loadpicture_icon)
                // .thumbnail(0.1f)
                .into(viewHolder.image);



        if(mItemClickListener != null){
            viewHolder.imageDelect.setOnClickListener(new View.OnClickListener() {
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
        private final ImageView imageDelect;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageDelect = itemView.findViewById(R.id.image_delect);
        }
    }
}
