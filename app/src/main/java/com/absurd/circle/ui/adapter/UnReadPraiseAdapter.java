package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.ui.adapter.base.NotificationAdapter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.TimeUtil;

/**
 * Created by absurd on 14-3-29.
 */
public class UnReadPraiseAdapter extends NotificationAdapter<Praise> {
    public UnReadPraiseAdapter(Context context) {
        super(context);
    }

    @Override
    protected void handleView(ViewHolder holder, Praise item) {
        holder.timeView.setText(TimeUtil.formatShowTime(item.getDate()));
        holder.descView.setText("赞了我的微博:");
        holder.contentView.setText(item.getParentText());
        if(item.getUser() != null) {
            holder.usernameView.setText(item.getUser().getName());
            RequestManager.loadImage(item.getUser().getAvatar(), RequestManager.getImageListener(holder.avatarView,
                    mAvatarDefaultBitmap, mAvatarDefaultBitmap, new BitmapFilter() {
                        @Override
                        public Bitmap filter(Bitmap bitmap) {
                            return ImageUtil.roundBitmap(bitmap);
                        }
                    }
            ));
        }
    }
}
