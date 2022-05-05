package com.qihe.imagecompression.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.qihe.imagecompression.R;
import com.qihe.imagecompression.model.WorkBean;
import com.qihe.imagecompression.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class MineWorkAdapter extends RecyclerView.Adapter<MineWorkAdapter.ViewHolder> {
    private List<WorkBean> list = new ArrayList<>();
    private Context context;

    public MineWorkAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 3; i++) {
            WorkBean workBean = new WorkBean();
            switch (i) {
                case 0:
                    workBean.context = "再多文件也能快速压缩、解压";
                    workBean.title = "解压缩工厂";
                    workBean.iconRes = R.mipmap.zip_icon;
                    workBean.packageName = "com.qihe.zipking";
                    break;
                case 1:
                    workBean.context = "超齐全的证件类型";
                    workBean.title = "最美一寸证件照";
                    workBean.iconRes = R.mipmap.zzj_icon;
                    workBean.packageName = "com.qihe.zzj";
                    break;
                case 2:
                    workBean.context = "视频音频提取器";
                    workBean.title = "视频音频提取器";
                    workBean.iconRes = R.mipmap.yptq_icon;
                    workBean.packageName = "com.qihakeji.videoparsemusic";
                    break;
            }

            list.add(workBean);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mine_work_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final WorkBean bean = list.get(position);
        holder.icon_iv.setImageResource(R.mipmap.logo);
        holder.title_tv.setText(bean.title);
        holder.content_tv.setText(bean.context);
        holder.icon_iv.setImageResource(bean.iconRes);
        holder.right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.jumpToReview(context, bean.packageName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon_iv;
        private TextView title_tv;
        private TextView content_tv;
        private TextView right_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_iv = itemView.findViewById(R.id.icon_iv);
            title_tv = itemView.findViewById(R.id.title_tv);
            content_tv = itemView.findViewById(R.id.content_tv);
            right_tv = itemView.findViewById(R.id.right_tv);
        }
    }
}
