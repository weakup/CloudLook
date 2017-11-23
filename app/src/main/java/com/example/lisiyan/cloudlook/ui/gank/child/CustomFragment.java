package com.example.lisiyan.cloudlook.ui.gank.child;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.http.HttpUtils;
import com.example.lisiyan.cloudlook.R;
import com.example.lisiyan.cloudlook.adapter.AndroidAdapter;
import com.example.lisiyan.cloudlook.app.Constants;
import com.example.lisiyan.cloudlook.base.BaseFragment;
import com.example.lisiyan.cloudlook.bean.GankIoDataBean;
import com.example.lisiyan.cloudlook.databinding.FragmentCustomBinding;
import com.example.lisiyan.cloudlook.http.RequestImpl;
import com.example.lisiyan.cloudlook.http.cache.ACache;
import com.example.lisiyan.cloudlook.model.GankOtherModel;
import com.example.lisiyan.cloudlook.utils.SPUtils;
import com.example.lisiyan.cloudlook.utils.ToastUtil;
import com.example.xrecyclerview.XRecyclerView;

import io.reactivex.disposables.Disposable;

/**
 * Created by lisiyan on 2017/11/21.
 */

public class CustomFragment extends BaseFragment<FragmentCustomBinding> {

    private GankIoDataBean mAllBean;
    private GankOtherModel gankOtherModel;
    private AndroidAdapter mAndroidAdapter;
    private ACache mACache;
    private int mPage = 1;
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private View mHeaderView;
    private String mType = "all";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mACache = ACache.get(getContext());
        gankOtherModel = new GankOtherModel();

        // 禁止下拉刷新
        bindingView.xrvCustom.setPullRefreshEnabled(false);
        // 去掉刷新头
        bindingView.xrvCustom.clearHeader();
        mAndroidAdapter = new AndroidAdapter();
        bindingView.xrvCustom.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadCustomData();
            }
        });

        mIsPrepared = true;
    }

    @Override
    public int setContent() {
        return R.layout.fragment_custom;
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }

        mAllBean = (GankIoDataBean) mACache.getAsObject(Constants.GANK_CUSTOM);

        if (mAllBean != null
                && mAllBean.getResults() != null
                && mAllBean.getResults().size() > 0) {
            showContentView();
            setAdapter(mAllBean);
        } else {
            loadCustomData();
        }
    }

    private void loadCustomData() {
        gankOtherModel.setData(mType, mPage, HttpUtils.per_page_more);
        gankOtherModel.getGankIoData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                showContentView();
                GankIoDataBean gankIoDataBean = (GankIoDataBean) object;
                if (mPage == 1) {
                    if (gankIoDataBean != null && gankIoDataBean.getResults() != null &&
                            gankIoDataBean.getResults().size() > 0) {

                        setAdapter(gankIoDataBean);
                        mACache.remove(Constants.GANK_CUSTOM);
                        // 缓存50分钟
                        mACache.put(Constants.GANK_CUSTOM, gankIoDataBean, 30000);
                    }
                } else {

                    if (gankIoDataBean != null && gankIoDataBean.getResults() != null
                            && gankIoDataBean.getResults().size() > 0) {
                        bindingView.xrvCustom.refreshComplete();
                        mAndroidAdapter.addAll(gankIoDataBean.getResults());
                        mAndroidAdapter.notifyDataSetChanged();
                    } else {

                        bindingView.xrvCustom.noMoreLoading();
                    }

                }
            }

            @Override
            public void loadFailed() {
                {
                    showContentView();
                    bindingView.xrvCustom.refreshComplete();
                    if (mAndroidAdapter.getItemCount() == 0) {
                        showError();
                    }
                    if (mPage > 1) {
                        mPage--;
                    }
                }

            }

            @Override
            public void addSubscription(Disposable d) {

                CustomFragment.this.addDisposable(d);
            }
        });
    }

    /**
     * 设置adapter
     */
    private void setAdapter(GankIoDataBean mCustomBean) {
        if (mHeaderView == null) {
            mHeaderView = View.inflate(getContext(), R.layout.header_item_gank_custom, null);
            bindingView.xrvCustom.addHeaderView(mHeaderView);
        }
        initHeader(mHeaderView);

        boolean isAll = SPUtils.getString("gank_cala", "全部").equals("全部");
        mAndroidAdapter.clear();
        mAndroidAdapter.setAllType(isAll);
        mAndroidAdapter.addAll(mCustomBean.getResults());
        bindingView.xrvCustom.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindingView.xrvCustom.setAdapter(mAndroidAdapter);
        mAndroidAdapter.notifyDataSetChanged();

        mIsFirst = false;
    }

    private void initHeader(View mHeaderView) {
        final TextView txName = mHeaderView.findViewById(R.id.tx_name);
        String gankCala = SPUtils.getString("gank_cala", "全部");
        txName.setText(gankCala);
        View view = mHeaderView.findViewById(R.id.ll_choose_catalogue);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new BottomSheet.Builder(getActivity(),R.style.BottomSheet_StyleDialog)
                        .title("选择分类")
                        .sheet(R.menu.gank_bottomsheet)
                        .listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which) {
                                    case R.id.gank_all:
                                        if (isOtherType("全部")) {
                                            txName.setText("全部");
                                            mType = "all";// 全部传 all
                                            mPage = 1;
                                            mAndroidAdapter.clear();
                                            SPUtils.putString("gank_cala", "全部");
                                            showLoading();
                                            loadCustomData();
                                        }
                                        break;
                                    case R.id.gank_ios:
                                        if (isOtherType("IOS")) {
                                            txName.setText("IOS");
                                            mType = "iOS";// 这里有严格大小写
                                            mPage = 1;
                                            mAndroidAdapter.clear();
                                            SPUtils.putString("gank_cala", "IOS");
                                            showLoading();
                                            loadCustomData();
                                        }
                                        break;
                                    case R.id.gank_qian:
                                        if (isOtherType("前端")) {
                                            changeContent(txName, "前端");
                                        }
                                        break;
                                    case R.id.gank_app:
                                        if (isOtherType("App")) {
                                            changeContent(txName, "App");
                                        }
                                        break;
                                    case R.id.gank_movie:
                                        if (isOtherType("休息视频")) {
                                            changeContent(txName, "休息视频");
                                        }
                                        break;
                                    case R.id.gank_resouce:
                                        if (isOtherType("拓展资源")) {
                                            changeContent(txName, "拓展资源");
                                        }
                                        break;
                                }

                            }
                        }).show();
            }
        });
    }


    private void changeContent(TextView textView, String content) {
        textView.setText(content);
        mType = content;
        mPage = 1;
        mAndroidAdapter.clear();
        SPUtils.putString("gank_cala", content);
        showLoading();
        loadCustomData();
    }

    private boolean isOtherType(String selectType) {
        String clickText = SPUtils.getString("gank_cala", "全部");
        if (clickText.equals(selectType)) {
            ToastUtil.showToast("当前已经是" + selectType + "分类");
            return false;
        } else {
            // 重置XRecyclerView状态，解决 如出现刷新到底无内容再切换其他类别后，无法上拉加载的情况
            bindingView.xrvCustom.reset();
            return true;
        }
    }

    @Override
    protected void onRefresh() {
        loadCustomData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
