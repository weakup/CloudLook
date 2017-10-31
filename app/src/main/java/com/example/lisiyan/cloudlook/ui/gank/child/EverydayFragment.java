package com.example.lisiyan.cloudlook.ui.gank.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.databinding.FragmentEverydayBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lisiyan on 2017/10/30.
 */

public class EverydayFragment extends BaseFragment<FragmentEverydayBinding> {

    private RotateAnimation animation;

    @Override
    public int setContent() {
        return R.layout.fragment_everyday;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        bindingView.llLoading.setVisibility(View.VISIBLE);
        //fromDegrees和toDegrees：这两个分别是旋转的起始角度和结束角度。
        //pivotXType和pivotYType：X,Y轴的伸缩模式，定义了pivotXValue和pivotYValue怎么被使用
        //pivotXValue和pivotYValue：在X,Y方向的位置,自身的x的一般，y的一半
        animation = new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(3000);//持续时间
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.setRepeatCount(10);
        bindingView.ivLoading.setAnimation(animation);
        animation.startNow();




    }

//    /**
//     * 获取当天日期
//     */
//    private List<String> getTodayTime() {
//        String data = TimeUtil.getData();
//        String[] split = data.split("-");
//        String year = split[0];
//        String month = split[1];
//        String day = split[2];
//        List<String> list = new ArrayList<>();
//        list.add(year);
//        list.add(month);
//        list.add(day);
//        return list;
//    }

}
