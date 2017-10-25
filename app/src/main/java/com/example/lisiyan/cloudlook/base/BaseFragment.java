package com.example.lisiyan.cloudlook.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.lisiyan.cloudlook.R;

/**
 * Created by lisiyan on 2017/10/25.
 */

public abstract  class BaseFragment<SV extends ViewDataBinding> extends Fragment {

    protected SV bindingView;
    protected RelativeLayout mContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base,null);
        bindingView = DataBindingUtil.inflate(getActivity().getLayoutInflater(),setContent(),null,false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        mContainer = view.findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        return mContainer;
    }



    public abstract int setContent();
}
