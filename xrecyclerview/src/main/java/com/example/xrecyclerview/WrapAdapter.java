package com.example.xrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lisiyan on 2017/11/1.
 */

public class WrapAdapter extends RecyclerView.Adapter {

    private static final int TYPE_REFRESH_HEADER = -5;
    private static final int TYPE_HEADER = -4;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = -3;

    private RecyclerView.Adapter mAdapter;

    private SparseArray<View> mHeaderViews;

    private SparseArray<View> mFootViews;

    private int headerPosition = 1;

    public WrapAdapter(SparseArray<View> headerViews, SparseArray<View> footViews, RecyclerView.Adapter adapter){

        this.mAdapter = adapter;
        this.mHeaderViews = headerViews;
        this.mFootViews = footViews;
    }

    //GridLayout 头部适配  getSpanSize  如果是头部和尾部 占满。
    //如果是其他的位置占一个格子
    //

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager){
            final GridLayoutManager gridManger = (GridLayoutManager) manager;
            gridManger.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position)|| isFooter(position)
                            ? gridManger.getSpanCount() : 1);
                }
            });
        }
    }

    //瀑布流布局 需要LayoutParams 的setFullSpan 方法来设置占位宽度 在这里处理

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp !=null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.getLayoutPosition())|| isFooter(holder.getLayoutPosition()))){
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }


    public boolean isRefreshHeader(int position){

        return position ==0;
    }

    public boolean isHeader(int position) {
        return position >= 0 && position < mHeaderViews.size();
    }

    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - mFootViews.size();
    }

    public int getHeadersCount(){

        return mHeaderViews.size();
    }

    public int getFootersCount() {

        return mFootViews.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_REFRESH_HEADER){
            return new SimpleViewHolder(mHeaderViews.get(0));
        }else if (viewType == TYPE_HEADER){
            return new SimpleViewHolder(mHeaderViews.get(headerPosition++));
        }else if (viewType == TYPE_FOOTER){
            return new SimpleViewHolder(mFootViews.get(0));
        }
        return mAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position)){
            return;
        }
        int adjPosition = position - getHeadersCount();
        int adapterCount ;
        if (mAdapter != null){
            adapterCount = mAdapter.getItemCount();

            if (adjPosition < adapterCount){
                mAdapter.onBindViewHolder(holder,adjPosition);
                return;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isRefreshHeader(position)){
            return TYPE_REFRESH_HEADER;
        }
        if (isHeader(position)){
            return TYPE_HEADER;
        }

        if (isFooter(position)){
            return TYPE_FOOTER;
        }

        //内容
        int adjPosition = position - getHeadersCount();
        int adapterCount;
        if(mAdapter != null){
            adapterCount = mAdapter.getItemCount();
            if (adjPosition > adapterCount){
                return mAdapter.getItemViewType(adjPosition);
            }
        }

        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {

        if (mAdapter !=null){
            return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
        }else {
            return getHeadersCount() + getFootersCount();
        }
    }

    @Override
    public long getItemId(int position) {
        if (mAdapter != null && position >= getHeadersCount()){
            int adjPosition = position - getHeadersCount();
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount){
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (mAdapter !=null){
            mAdapter.unregisterAdapterDataObserver(observer);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (mAdapter !=null){
            mAdapter.registerAdapterDataObserver(observer);
        }
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder{

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
