package com.example.xrecyclerview;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lisiyan on 2017/10/25.
 */

public class XRecyclerView extends RecyclerView{

    private LoadingListener mLoadingListener;
    private WrapAdapter mWrapAdapter;
    private SparseArray<View> mHeaderViews = new SparseArray<View>();
    private SparseArray<View> mFootViews = new SparseArray<View>();
    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;
    private YunRefreshHeader mRefreshHeader;
    private boolean isLoadingData;

    // 是否是额外添加FooterView
    private boolean isOther = false;
    public boolean isnomore;
    private float mLastY = -1;
    public int previousTotal;
    private static final float DRAG_RATE = 1.75f;

    public XRecyclerView(Context context) {
        this(context,null);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        //若支持下拉刷新则加入Headerview列表，设置加载图标
        if (pullRefreshEnabled){
            YunRefreshHeader refreshHeader = new YunRefreshHeader(context);
            mHeaderViews.put(0,refreshHeader);
            mRefreshHeader = refreshHeader;
        }
        //加载更多无需触发
        LoadingMoreFooter footView = new LoadingMoreFooter(context);
        addFootView(footView,false);
        mFootViews.get(0).setVisibility(GONE);

    }

    /**
     *
     * @param view 添加自定义view
      */
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
        // 额外添加的footView
        if (isOther){

            footView.setVisibility(VISIBLE);
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
        mWrapAdapter = new WrapAdapter(mHeaderViews,mFootViews,adapter);
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(mDataObserver);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
//        当前的recycleView不滑动
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener !=null && !isLoadingData && loadingMoreEnabled ){
                //得到布局模式
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager){
                //获取最后一个可见view的位置
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }else if (layoutManager instanceof StaggeredGridLayoutManager){
                int []into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            }else {

                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() -1
                    && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isnomore && mRefreshHeader.getState() < YunRefreshHeader.STATE_REFRESHING){

                    View footView = mFootViews.get(0);
                    isLoadingData = true;
                    if (footView instanceof LoadingMoreFooter){

                        ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOADING);
                    }else {
                        footView.setVisibility(VISIBLE);
                    }

                    if (isNetWorkConnected(getContext())){
                        mLoadingListener.onLoadMore();
                    }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (mLastY == -1){
            mLastY = e.getRawY();
        }

        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float delatY = e.getRawY() - mLastY;
                mLastY = e.getRawY();
                if (isOnTop() && pullRefreshEnabled){
                    mRefreshHeader.onMove(delatY / DRAG_RATE);
                    if (mRefreshHeader.getVisiableHeight() > 0 && mRefreshHeader.getState() < YunRefreshHeader.STATE_REFRESHING){
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1;//RESET
                if (isOnTop() && pullRefreshEnabled){
                    if (mRefreshHeader.releaseAction()){
                        if (mLoadingListener != null){
                            mLoadingListener.onRefresh();
                            isnomore = false;;
                            previousTotal = 0;
                            final View footView = mFootViews.get(0);
                            if (footView instanceof LoadingMoreFooter){
                                if (footView.getVisibility() != GONE){
                                    footView.setVisibility(GONE);
                                }
                            }
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        this.pullRefreshEnabled = pullRefreshEnabled;
    }

    public void setLoadingMoreEnabled(boolean loadingMoreEnabled) {
        this.loadingMoreEnabled = loadingMoreEnabled;
        if (!loadingMoreEnabled) {
            if (mFootViews != null) {
                mFootViews.remove(0);
            }
        } else {
            if (mFootViews != null) {
                LoadingMoreFooter footView = new LoadingMoreFooter(getContext());
                addFootView(footView, false);
            }
        }
    }


    private int findMax(int[] lastPositions){

        int max = lastPositions[0];
        for (int value : lastPositions){
            if (value > max){
                max = value;
            }
        }

        return max;
    }

    public boolean isOnTop() {
        if (mHeaderViews == null || mHeaderViews.size() == 0) {
            return false;
        }

        View view = mHeaderViews.get(0);
        if (view.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    public void reset() {
        isnomore = false;
        previousTotal = 0;
        final View footView = mFootViews.get(0);
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).reSet();
        }
    }
}
