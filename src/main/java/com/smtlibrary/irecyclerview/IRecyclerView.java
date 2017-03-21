package com.smtlibrary.irecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smtlibrary.R;

import static com.smtlibrary.R.id.recyclerView;

/**
 * @author gbh
 *         Created by gbh on 16/11/15.
 *         快速开发IRecyclerView
 */

public class IRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OnLoadMoreListener mOnLoadMoreListener;
    private OnRefreshListener mOnRefreshListener;
    private OnScrollerListener mOnScrollerListener;
    private LinearLayout load_layout;
    private RelativeLayout noDataLayout;
    private TextView noDataMsg;

    private OnLoadMoreScrollListener mOnLoadMoreScrollListener;
    private boolean mLoadMoreEnabled;
    private int mDivierHeight = 1;//间距

    public IRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public IRecyclerView(Context context, int divierHeight) {
        super(context);
        mDivierHeight = divierHeight;
        initView(context);
    }


    public IRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray tc = context.obtainStyledAttributes(attrs, R.styleable.IRecyclerView);
        int swipe_size = tc.getInt(R.styleable.IRecyclerView_swipe_size, SwipeRefreshLayout.DEFAULT);
        int type = tc.getInt(R.styleable.IRecyclerView_recyview_manager, 0);
        int count = tc.getInt(R.styleable.IRecyclerView_gridview_count, 1);
        boolean reflesh_enabled = tc.getBoolean(R.styleable.IRecyclerView_refresh_enable, false);
        boolean loadMore_enabled = tc.getBoolean(R.styleable.IRecyclerView_loadMore_enabled, true);
        boolean swipe_enabled = tc.getBoolean(R.styleable.IRecyclerView_swipe_enabled, true);
        int orientation = tc.getInt(R.styleable.IRecyclerView_orientation, 1);
        mDivierHeight = tc.getInt(R.styleable.IRecyclerView_divierHeight, 1);
        tc.recycle();

        mSwipeRefreshLayout.setSize(swipe_size >= 1 ? 1 : 0);
        switch (type) {
            case 0:
                setLinearlayoutmanager(context, orientation);
                break;
            case 1:
                setGridviewlayoutmanager(context, count, orientation);
                break;
            case 2:
                setStaggeredGridLayoutManager(count, orientation);
                break;
        }

        setOnRefresh(reflesh_enabled);
        setSwipeRefreshLayoutEnabled(swipe_enabled);
        setLoadMoreEnabled(loadMore_enabled);
    }

    /**
     * @param context
     */

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_layout, this);
        load_layout = (LinearLayout) view.findViewById(R.id.load_layout);
        noDataLayout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        noDataMsg = (TextView) view.findViewById(R.id.no_data_meg);
        mRecyclerView = (RecyclerView) view.findViewById(recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置间距
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(mDivierHeight));

    }


    /**
     * 设置线性列表
     *
     * @param context
     * @param orientation
     */
    public void setLinearlayoutmanager(Context context, int orientation) {
        if (null == context) new IllegalArgumentException("context is null");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }


    /**
     * 设置表格列表
     *
     * @param context
     * @param count
     * @param orientation
     */
    public void setGridviewlayoutmanager(Context context, int count, int orientation) {
        if (null == context) new IllegalArgumentException("context is null");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, count);
        gridLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(gridLayoutManager);

    }

    /**
     * 流布局
     *
     * @param count
     * @param orientation
     */
    public void setStaggeredGridLayoutManager(int count, int orientation) {
        StaggeredGridLayoutManager stagManager = new StaggeredGridLayoutManager(count, orientation);
        mRecyclerView.setLayoutManager(stagManager);
    }


    /**
     * 选择
     *
     * @param position
     */
    public void setSelect(int position) {
        mRecyclerView.scrollToPosition(position);
    }

    /**
     * 设置mSwipeRefreshLayout size
     *
     * @param swipeSize
     */
    public void setSwipeSize(int swipeSize) {
        mSwipeRefreshLayout.setSize(swipeSize == 0 ? SwipeRefreshLayout.LARGE : SwipeRefreshLayout.DEFAULT);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (null != adapter)
            mRecyclerView.setAdapter(adapter);
        else new IllegalArgumentException("adapter is null");
    }


    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        if (null != mOnRefreshListener) {
            mOnRefreshListener.onRefresh();
        } else new IllegalArgumentException("mOnRefreshListener is null");
    }


    /**
     * 设置是否刷新
     *
     * @param refreshing
     */
    public void setOnRefresh(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    /**
     * 设置是否支持下拉刷新数据 默认:支持
     *
     * @param enabled
     */
    public void setSwipeRefreshLayoutEnabled(boolean enabled) {
        mSwipeRefreshLayout.setEnabled(enabled);
    }

    /**
     * 设置加载界面是否显示
     *
     * @param visable
     */
    public void setLoadLayoutVisible(boolean visable) {
        load_layout.setVisibility(visable ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置是否支持上拉加载更多 默认：支持
     *
     * @param enabled
     */
    public void setLoadMoreEnabled(boolean enabled) {
        this.mLoadMoreEnabled = enabled;
        if (mOnLoadMoreScrollListener == null) {
            mOnLoadMoreScrollListener = new OnLoadMoreScrollListener() {
                @Override
                public void onLoadMore(RecyclerView recyclerView) {
                    if (mLoadMoreEnabled) {
                        if (mOnLoadMoreListener == null) return;
                        setLoadLayoutVisible(true);
                        mOnLoadMoreListener.onLoadMore();
                    }
                }

                @Override
                public void onScroll(RecyclerView recyclerView) {
                    setLoadLayoutVisible(false);
                }

                @Override
                public void onScrolling(RecyclerView recyclerView, int dx, int dy) {
                    if (mOnScrollerListener == null) return;
                    mOnScrollerListener.onScrolling(dx, dy);
                }

                @Override
                public void onStartScroll(RecyclerView recyclerView) {
                    if (mOnScrollerListener == null) return;
                    mOnScrollerListener.onStartScroll();
                }

                @Override
                public void onStopScroll(RecyclerView recyclerView) {
                    if (mOnScrollerListener == null) return;
                    mOnScrollerListener.onStopScroll();
                }
            };
        } else {
            mRecyclerView.removeOnScrollListener(mOnLoadMoreScrollListener);
        }
        mRecyclerView.addOnScrollListener(mOnLoadMoreScrollListener);
    }

    /**
     * 设置无任务界面
     */
    public void setNoDataLayout() {
        noDataLayout.setVisibility(VISIBLE);
        mRecyclerView.setVisibility(GONE);
    }


    /**
     * 设置无任务界面
     */
    public void setNoDataLayout(String msg) {
        noDataLayout.setVisibility(VISIBLE);
        mRecyclerView.setVisibility(GONE);
        noDataMsg.setText(TextUtils.isEmpty(msg) ? "" : msg);
    }

    /**
     * 有数据
     */
    public void setHashDataLayout() {
        noDataLayout.setVisibility(GONE);
        mRecyclerView.setVisibility(VISIBLE);
    }

    /**
     * 设置加载更多监听
     *
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 设置刷新加载更多监听
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    public void setOnScrollerListener(OnScrollerListener mOnScrollerListener) {
        this.mOnScrollerListener = mOnScrollerListener;
    }
}
