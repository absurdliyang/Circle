package com.absurd.circle.ui.activity;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by absurd on 14-3-19.
 */
public interface RefreshableActivity {

    public PullToRefreshAttacher getAttacher();
}
