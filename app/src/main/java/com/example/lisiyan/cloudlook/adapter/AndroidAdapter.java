package com.example.lisiyan.cloudlook.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecycleViewHolder;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.lisiyan.cloudlook.bean.GankIoDataBean;
import com.example.lisiyan.cloudlook.databinding.ItemAndroidBinding;
import com.example.lisiyan.cloudlook.utils.ImgLoadUtil;
import com.example.lisiyan.cloudlook.view.webview.WebViewActivity;

/**
 * Created by lisiyan on 2017/11/21.
 */

public class AndroidAdapter extends BaseRecyclerViewAdapter<GankIoDataBean.ResultBean> {

    private boolean isAll = false;

    public void setAllType(boolean all) {
         isAll = all;
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_android);
    }

    private class ViewHolder extends BaseRecycleViewHolder<GankIoDataBean.ResultBean, ItemAndroidBinding>{


        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final GankIoDataBean.ResultBean object, int posotion) {

            if (isAll && "福利".equals(object.getType())){
                binding.ivAllWelfare.setVisibility(View.VISIBLE);
                binding.llWelfareOther.setVisibility(View.GONE);
                ImgLoadUtil.displayEspImage(object.getUrl(),binding.ivAllWelfare,1);
            }else {
                binding.ivAllWelfare.setVisibility(View.GONE);
                binding.llWelfareOther.setVisibility(View.VISIBLE);
            }

            if (isAll){
                binding.tvContentType.setVisibility(View.VISIBLE);
                binding.tvContentType.setText(" · " + object.getType());
            }else {
                binding.tvContentType.setVisibility(View.GONE);
            }

            binding.setResultsBean(object);
            binding.executePendingBindings();

            //gif 换成静态图
            if (object.getImages() !=null
                    && object.getImages().size() > 0
                    && !TextUtils.isEmpty(object.getImages().get(0))){
                binding.ivAndroidPic.setVisibility(View.VISIBLE);
                ImgLoadUtil.displayGif(object.getImages().get(0),binding.ivAndroidPic);
            }else {
                binding.ivAndroidPic.setVisibility(View.GONE);
            }

            binding.llAll.setOnClickListener(v -> WebViewActivity.loadUrl(v.getContext(), object.getUrl(), "加载中..."));
        }
    }

}
