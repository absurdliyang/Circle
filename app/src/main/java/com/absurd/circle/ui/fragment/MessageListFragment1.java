package com.absurd.circle.ui.fragment;

import com.absurd.circle.data.model.Message;
import com.absurd.circle.ui.adapter.BeanAdapter;
import com.absurd.circle.ui.adapter.MessageAdapter1;

import java.util.List;

/**
 * Created by absurd on 14-3-26.
 */
public class MessageListFragment1 extends PagedItemFragment<Message> {


    @Override
    protected BeanAdapter getAdapter() {
        return null;
    }

    @Override
    protected boolean hasNext() {
        return false;
    }

    @Override
    protected List<Message> loadData(int index) {
        return null;
    }
}
