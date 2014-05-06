package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.adapter.base.NotificationAdapter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.TimeUtil;
import com.android.volley.toolbox.ImageLoader;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-29.
 */
public class UserMessageAdapter extends NotificationAdapter<UserMessage> {

    private User mUser;

    public UserMessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected void handleView(ViewHolder holder, UserMessage item) {
        holder.usernameView .setText(item.getFromUserName());
        holder.timeView.setText(TimeUtil.formatShowTime(item.getDate()));
        holder.descView.setVisibility(View.GONE);
        holder.contentView.setText(item.getContent());

        // Where coming a extra fucking space!???shit!!!
        User fromUser = AppContext.cacheService.userDBManager.getUser(item.getFromUserId());
        if(fromUser != null){
            AppContext.commonLog.i("get from user " + fromUser.toString());
            holder.avatarLoader = RequestManager.loadImage(fromUser.getAvatar(),
                    RequestManager.getImageListener(holder.avatarView, mAvatarDefaultBitmap, mAvatarDefaultBitmap, new BitmapFilter() {
                        @Override
                        public Bitmap filter(Bitmap bitmap) {
                            return ImageUtil.roundBitmap(bitmap);
                        }
                    })
            );
        }else{
            getUserInfo(holder, item.getFromUserId());
        }
    }


    private void getUserInfo(final ViewHolder holder, String userId){
        UserService userService = new UserService();
        userService.getUser(userId, new TableQueryCallback<User>() {
            @Override
            public void onCompleted(List<User> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                }else {
                    if (result.size() > 0) {
                        mUser = result.get(0);
                        AppContext.cacheService.userDBManager.insertUser(mUser);
                        holder.avatarLoader = RequestManager.loadImage(mUser.getAvatar(),
                                RequestManager.getImageListener(holder.avatarView, mAvatarDefaultBitmap, mAvatarDefaultBitmap, new BitmapFilter() {
                                    @Override
                                    public Bitmap filter(Bitmap bitmap) {
                                        return ImageUtil.roundBitmap(bitmap);
                                    }
                                })
                        );
                    }
                }
            }
        });
    }

}
