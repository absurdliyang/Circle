package com.absurd.circle.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.absurd.circle.app.R;
import com.absurd.circle.core.bean.PageBase;
import com.absurd.circle.core.service.MessageService;
import com.absurd.circle.ui.activity.BaseActivity;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.adapter.BeanAdapter;

import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

/**
 * Created by absurd on 14-3-12.
 */
public abstract class PagedItemFragment<E> extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
