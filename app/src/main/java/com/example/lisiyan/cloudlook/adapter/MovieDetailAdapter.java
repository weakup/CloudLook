package com.example.lisiyan.cloudlook.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecycleViewHolder;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.lisiyan.cloudlook.bean.moviechild.PersonBean;
import com.example.lisiyan.cloudlook.databinding.ItemMovieDetailPersonBinding;
import com.example.lisiyan.cloudlook.view.webview.WebViewActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by lisiyan on 2017/11/27.
 */

public class MovieDetailAdapter extends BaseRecyclerViewAdapter<PersonBean> {

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_movie_detail_person);
    }

    private class ViewHolder extends BaseRecycleViewHolder<PersonBean,ItemMovieDetailPersonBinding>{


        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final PersonBean bean, int posotion) {

            binding.setPersonBean(bean);

            RxView.clicks(binding.llItem)
                  .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if (bean != null && !TextUtils.isEmpty(bean.getAlt())) {
                                WebViewActivity.loadUrl(binding.llItem.getContext(), bean.getAlt(), bean.getName());
                            }

                        }
                    });

        }
    }
}
