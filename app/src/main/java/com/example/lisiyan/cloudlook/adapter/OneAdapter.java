package com.example.lisiyan.cloudlook.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecycleViewHolder;
import com.example.lisiyan.cloudlook.base.baseadapter.BaseRecyclerViewAdapter;

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

        return null;
    }

    private class ViewHolder extends BaseRecycleViewHolder{


        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(Object object, int posotion) {

            if (object !=null){

            }
        }
    }
}
