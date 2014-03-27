package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.absurd.circle.app.R;
import com.absurd.circle.core.bean.Message;
import com.absurd.circle.ui.adapter.BeanAdapter;
import com.absurd.circle.ui.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-19.
 */
public class NotificationListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification,null);
        return rootView;
    }
}
