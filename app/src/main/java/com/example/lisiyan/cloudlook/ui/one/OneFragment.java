package com.example.lisiyan.cloudlook.ui.one;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.lisiyan.cloudlook.MainActivity;
import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.adapter.OneAdapter;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.databinding.FragmentOneBinding;

/**
 * Created by lisiyan on 2017/10/26.
 */

public class OneFragment extends BaseFragment<FragmentOneBinding>{

    private MainActivity activity;
    @Override
    public int setContent() {
        return R.layout.fragment_one;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();

    }

    @Override
    protected void loadData() {
        super.loadData();
    }
}
