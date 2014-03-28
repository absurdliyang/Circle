package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.ui.adapter.base.NotificationAdapter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.TimeUtil;

/**
 * Created by absurd on 14-3-29.
 */
public class UnReadCommentAdapter extends NotificationAdapter<Comment> {

    public UnReadCommentAdapter(Context context) {
        super(context);
    }

    @Override
    protected void handleView(ViewHolder holder, Comment item) {
        holder.timeView.setText(TimeUtil.formatShowTime(item.getDate()));
        holder.descView.setText("评论了我的微博：");
        holder.contentView.setText(item.getContent());
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
