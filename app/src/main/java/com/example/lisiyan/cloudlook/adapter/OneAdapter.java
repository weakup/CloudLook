package com.example.lisiyan.cloudlook.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecycleViewHolder;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecyclerViewAdapter;
import com.example.lisiyan.cloudlook.bean.moviechild.SubjectsBean;
import com.example.lisiyan.cloudlook.databinding.ItemOneBinding;
import com.example.lisiyan.cloudlook.ui.one.OneMovieDetailActivity;
import com.example.lisiyan.cloudlook.utils.CommonUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by lisiyan on 2017/10/30.
 */

public class OneAdapter extends BaseRecyclerViewAdapter {

    private Activity mActivity;

    public OneAdapter(Activity activity){

        this.mActivity = activity;
    }
    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(parent, R.layout.item_one);
    }

    private class ViewHolder extends BaseRecycleViewHolder<SubjectsBean,ItemOneBinding>{


        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final SubjectsBean subjectsBean, int posotion) {

            if (subjectsBean != null){
                binding.setSubjectsBean(subjectsBean);
                //
                binding.viewColor.setBackgroundColor(CommonUtils.randomColor());


                ViewHelper.setScaleX(itemView,0.8f);
                ViewHelper.setScaleY(itemView,0.8f);
                ViewPropertyAnimator.animate(itemView).scaleX(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();
                ViewPropertyAnimator.animate(itemView).scaleY(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();

                RxView.clicks(binding.llOneItem)
                        .throttleFirst(1000, TimeUnit.MILLISECONDS)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                OneMovieDetailActivity.start(mActivity, subjectsBean, binding.ivOnePhoto);

                            }
                        });
            }
        }
    }
}
