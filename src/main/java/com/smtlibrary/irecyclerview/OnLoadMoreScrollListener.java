package com.smtlibrary.irecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.smtlibrary.utils.LogUtils;

/**
 * Created by aspsine on 16/3/13.
 */
public abstract class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        onScrolling(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        boolean triggerCondition = visibleItemCount > 0
                && newState == RecyclerView.SCROLL_STATE_IDLE
                && canTriggerLoadMore(recyclerView);

        if (triggerCondition) {
            onLoadMore(recyclerView);
        } else {
            onScroll(recyclerView);
        }
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            onStartScroll(recyclerView);
        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            onStopScroll(recyclerView);
        }
    }

    public boolean canTriggerLoadMore(RecyclerView recyclerView) {
        View lastChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        int position = recyclerView.getChildLayoutPosition(lastChild);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        return totalItemCount - 1 == position;
    }

    public abstract void onLoadMore(RecyclerView recyclerView);

    public abstract void onScroll(RecyclerView recyclerView);

    public abstract void onScrolling(RecyclerView recyclerView, int dx, int dy);

    public abstract void onStartScroll(RecyclerView recyclerView);

    public abstract void onStopScroll(RecyclerView recyclerView);
}
