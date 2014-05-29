package com.absurd.circle.ui.adapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.StringUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by absurd on 14-3-29.
 */
public abstract class NotificationAdapter<V> extends BeanAdapter<V> {

    public NotificationAdapter(Context context) {
        super(context);
    }

    public NotificationAdapter(Context context, List<V> items) {
        super(context, items);
    }

    public class ViewHolder{
        public ImageView avatarView;
        public TextView usernameView;
        public TextView timeView;
        public TextView descView;
        public TextView contentView;
        public TextView notificationNumView;
        public ImageView iconMoreView;
        public LinearLayout contentLinearLayoutView;
        public ImageLoader.ImageContainer avatarLoader;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final V item = (V)getItem(i);
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_notification,null);
            holder = new ViewHolder();
            holder.avatarView = (ImageView)view.findViewById(R.id.iv_notify_item_avatar);
            holder.usernameView = (TextView)view.findViewById(R.id.tv_notify_item_username);
            holder.timeView = (TextView)view.findViewById(R.id.tv_notify_item_time);
            holder.descView = (TextView)view.findViewById(R.id.tv_notify_item_desc);
            holder.contentView = (TextView)view.findViewById(R.id.tv_notify_item_content);
            holder.notificationNumView = (TextView)view.findViewById(R.id.tv_usermesssagee_notification_num);
            holder.iconMoreView = (ImageView)view.findViewById(R.id.iv_icon_more);
            holder.contentLinearLayoutView = (LinearLayout)view.findViewById(R.id.llyt_notification_content);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
            if(holder.avatarLoader != null){
                holder.avatarLoader.cancelRequest();
            }
        }

        handleView(holder,item);
        return view;
    }

    protected abstract void handleView(ViewHolder holder,V item);
}
