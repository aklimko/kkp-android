package pl.adamklimko.kkpandroid.fragment;

import android.support.v4.widget.SwipeRefreshLayout;

public interface FragmentManageable {
    void redrawContent();
    void improveSwipeLayout(SwipeRefreshLayout swipeRefreshLayout);
}
