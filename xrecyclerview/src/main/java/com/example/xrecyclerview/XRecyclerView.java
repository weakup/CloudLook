package com.example.xrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lisiyan on 2017/10/25.
 */

public class XRecyclerView extends RecyclerView{

    private SparseArray<View> mHeaderViews = new SparseArray<View>();
    private SparseArray<View> mFootViews = new SparseArray<View>();
    private boolean pullRefreshEnabled = true;
    private YunRefreshHeader mRefreshHeader;
    private boolean isLoadingData;
    // 是否是额外添加FooterView
    private boolean isOther = false;
    public boolean isnomore;
    public int previousTotal;

    public XRecyclerView(Context context) {
        this(context,null);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init(Context context){

        if (pullRefreshEnabled){
            YunRefreshHeader refreshHeader = new YunRefreshHeader(context);
            mHeaderViews.put(0,refreshHeader);
            mRefreshHeader = refreshHeader;
        }

        LoadingMoreFooter footView = new LoadingMoreFooter(context);
        addFootView(footView,false);
        mFootViews.get(0).setVisibility(GONE);

    }

    public void addHeaderView(View view){

        if (pullRefreshEnabled && !(mHeaderViews.get(0) instanceof YunRefreshHeader)){
            YunRefreshHeader refreshHeader = new YunRefreshHeader(getContext());
            mHeaderViews.put(0,refreshHeader);
            mRefreshHeader = refreshHeader;
        }

        mHeaderViews.put(mHeaderViews.size(),view);
    }

    /**
     * 改为公有。供外添加view使用,使用标识
     * 注意：使用后不能使用 上拉加载，否则添加无效
     * 使用时 isOther 传入 true，然后调用 noMoreLoading即可。
     */
    public void addFootView(final View view,boolean isOther){

        mFootViews.clear();
        mFootViews.put(0,view);
        this.isOther = isOther;

    }

    /**
     * 相当于加一个空白头布局：
     * 只有一个目的：为了滚动条显示在最顶端
     * 因为默认加了刷新头布局，不处理滚动条会下移。
     * 和 setPullRefreshEnabled(false) 一块儿使用
     * 使用下拉头时，此方法不应被使用！
     */

    public void clearHeader(){
        mHeaderViews.clear();
        //Density（密度）
//      这个是指屏幕上每平方英寸（2.54 ^ 2 平方厘米）中含有的像素点数量。
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int height = (int) (1.0f * scale + 0.5f);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        View view = new View(getContext());
        view.setLayoutParams(params);
        mHeaderViews.put(0,view);
    }

    private void loadMoreComplete(){
        isLoadingData = false;
        View footView = mFootViews.get(0);
        if (previousTotal <= getLayoutManager().getItemCount()){
            if (footView instanceof  LoadingMoreFooter){
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_COMPLETE);
            }else {
                footView.setVisibility(GONE);
            }
        }else{
            if (footView instanceof  LoadingMoreFooter){

                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
            } else {

                footView.setVisibility(GONE);
            }

            isnomore = true;
        }

        previousTotal = getLayoutManager().getItemCount();
    }


    public void noMoreLoading(){

        isLoadingData = false;
        final View footView = mFootViews.get(0);
        isnomore = true;
        if (footView instanceof LoadingMoreFooter){
            ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
        }else {
            footView.setVisibility(GONE);
        }

        if (isOther){

            footView.setVisibility(GONE);
        }

    }

    public void refreshComplete(){

        if (isLoadingData){

            loadMoreComplete();

        }else {

            mRefreshHeader.refreshComplate();
        }

    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }
}
