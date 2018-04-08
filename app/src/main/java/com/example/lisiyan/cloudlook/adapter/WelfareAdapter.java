package com.example.lisiyan.cloudlook.adapter;

import android.view.ViewGroup;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecycleViewHolder;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.lisiyan.cloudlook.bean.GankIoDataBean;
import com.example.lisiyan.cloudlook.databinding.ItemWelfareBinding;
import com.example.lisiyan.cloudlook.utils.DensityUtil;

/**
 * Created by lisiyan on 2017/11/20.
 */

public class WelfareAdapter extends BaseRecyclerViewAdapter<GankIoDataBean.ResultBean> {

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_welfare);
    }

    private class ViewHolder extends BaseRecycleViewHolder<GankIoDataBean.ResultBean,ItemWelfareBinding>{


        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final GankIoDataBean.ResultBean resultsBean, final int posotion) {

            /**
             * 注意：DensityUtil.setViewMargin(itemView,true,5,3,5,0);
             * 如果这样使用，则每个item的左右边距是不一样的，
             * 这样item不能复用，所以下拉刷新成功后显示会闪一下
             * 换成每个item设置上下左右边距是一样的话，系统就会复用，就消除了图片不能复用 闪跳的情况
             */

            if (posotion % 2 ==0){
                DensityUtil.setViewMargin(itemView,false,12,6,12,0);
            }else {
                DensityUtil.setViewMargin(itemView,false,6,12,12,0);
            }

            binding.setBean(resultsBean);
        // 数据变更调用
            binding.executePendingBindings();
            itemView.setOnClickListener(v -> {

                if (mClickListener != null){

                    mClickListener.onClick(resultsBean,posotion);
                }

            });

        }
    }
}
