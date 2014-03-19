package com.absurd.circle.ui.fragment;

import com.absurd.circle.core.bean.Message;
import com.absurd.circle.ui.adapter.BeanAdapter;
import com.absurd.circle.ui.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-19.
 */
public class NotificationListFragment extends PagedItemFragment<Message> {
    @Override
    protected boolean hasNext() {
        return true;
    }

    @Override
    protected List<Message> loadData(int index) {
        List<Message> datas = new ArrayList<Message>();
        Message m1 = new Message();
        m1.setContent("djslfkjslfjskldfjkl");
        datas.add(m1);
        return datas;
    }

    @Override
    protected BeanAdapter getAdapter() {
        return new MessageAdapter(getActivity());
    }
}
