package com.example.lisiyan.cloudlook.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecycleViewHolder;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.lisiyan.cloudlook.bean.moviechild.SubjectsBean;
import com.example.lisiyan.cloudlook.databinding.ItemDoubanTopBinding;
import com.example.lisiyan.cloudlook.ui.one.OneMovieDetailActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * Created by lisiyan on 2017/11/27.
 */

public class DouBanTopAdapter extends BaseRecyclerViewAdapter<SubjectsBean> {

    private Activity activity;

    public DouBanTopAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_douban_top);
    }

    class ViewHolder extends BaseRecycleViewHolder<SubjectsBean,ItemDoubanTopBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final SubjectsBean bean,final int position) {
            binding.setBean(bean);

            binding.executePendingBindings();
            RxView.clicks(binding.llItemTop)
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> OneMovieDetailActivity.start(activity,bean,binding.ivTopPhoto));

            binding.llItemTop.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = View.inflate(v.getContext(),R.layout.title_douban_top,null);
                TextView titleTop = view.findViewById(R.id.title_top);
                titleTop.setText("Top" + (position + 1) + ": " + bean.getTitle());
                builder.setCustomTitle(view);
                builder.setPositiveButton("查看详情", (dialog, which) ->
                        OneMovieDetailActivity.start(activity, bean, binding.ivTopPhoto));
                builder.show();

                return false;
            });

        }
    }
}
