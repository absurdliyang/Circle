package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.ui.adapter.base.NotificationAdapter;
import com.absurd.circle.util.TimeUtil;

/**
 * Created by absurd on 14-3-29.
 */
public class UserMessageAdapter extends NotificationAdapter<UserMessage> {

    public UserMessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected void handleView(ViewHolder holder, UserMessage item) {
        holder.usernameView .setText(item.getFromUserName());
        holder.timeView.setText(TimeUtil.formatShowTime(item.getDate()));
        holder.descView.setVisibility(View.GONE);
        holder.contentView.setText(item.getContent());
    }
}
