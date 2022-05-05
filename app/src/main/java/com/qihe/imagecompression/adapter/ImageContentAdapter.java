package com.qihe.imagecompression.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.qihe.imagecompression.R;
import java.util.List;

public class ImageContentAdapter extends RecyclerView.Adapter<ImageContentAdapter.ViewHolder> {

    private Context context;
    private List<String> list;


    public ImageContentAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

    }


    @Override
    public ImageContentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_image_context, viewGroup, false);
        ViewHolder myViewHolder = new ViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageContentAdapter.ViewHolder viewHolder, int i) {

        viewHolder.content.setText(list.get(i));


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView content;

        public ViewHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);


        }

    }
}
