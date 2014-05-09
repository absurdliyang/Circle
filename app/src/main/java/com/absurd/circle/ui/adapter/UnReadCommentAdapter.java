package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.adapter.base.NotificationAdapter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.TimeUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-29.
 */
public class UnReadCommentAdapter extends NotificationAdapter<Comment> {

    public UnReadCommentAdapter(Context context) {
        super(context);
    }

    @Override
    protected void handleView(final ViewHolder holder, Comment item) {
        holder.timeView.setText(TimeUtil.formatShowTime(item.getDate()));
        holder.descView.setText("评论了我的微博：");
        holder.contentView.setText(item.getContent());

        if(item.getUser() != null) {
            bindUserInfo(holder,item.getUser());
        }else {
            User user = AppContext.cacheService.userDBManager.getUser(item.getUserId());
            if(user != null){
                bindUserInfo(holder, user);
            }else {
                UserService service = new UserService();
                service.getUser(item.getUserId(), new TableQueryCallback<User>() {
                    @Override
                    public void onCompleted(List<User> result, int count, Exception exception, ServiceFilterResponse response) {
                        if (result == null || result.isEmpty()) {
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        } else {
                            AppContext.cacheService.userDBManager.insertUser(result.get(0));
                            bindUserInfo(holder, result.get(0));
                        }
                    }
                });
            }
        }
    }


    private void bindUserInfo(ViewHolder holder, User user){
        holder.usernameView.setText(user.getName());
        RequestManager.loadImage(user.getAvatar(), RequestManager.getImageListener(holder.avatarView,
                mAvatarDefaultBitmap, mAvatarDefaultBitmap, new BitmapFilter() {
                    @Override
                    public Bitmap filter(Bitmap bitmap) {
                        return ImageUtil.roundBitmap(bitmap);
                    }
                }
        ));
    }
}
