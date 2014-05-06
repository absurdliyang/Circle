package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.util.FacesUtil;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.TimeUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by absurd on 14-4-16.
 */
public class ChatAdapter extends BeanAdapter<UserMessage> {

    private User mToUser;

    public ChatAdapter(Context context, User user) {
        super(context);
        mToUser = user;
    }

    public ChatAdapter(Context context, List<UserMessage> items, User user) {
        super(context, items);
        mToUser = user;
    }

    public class ViewHolder{
        View fromUserView;
        ImageView fromUserAvatar;
        TextView fromUserContent;
        TextView fromUserDate;

        View toUserView;
        ImageView toUserAvatar;
        TextView toUserContent;
        TextView toUserDate;

        ImageLoader.ImageContainer avatarLoader;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        UserMessage userMessage = (UserMessage)getItem(i);
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_chat,null);
            holder = new ViewHolder();
            holder.fromUserView = view.findViewById(R.id.rtly_from_user_part);
            holder.fromUserAvatar = (ImageView)view.findViewById(R.id.iv_from_user_avatar);
            holder.fromUserContent = (TextView)view.findViewById(R.id.tv_from_content);
            holder.fromUserDate = (TextView)view.findViewById(R.id.tv_from_date);

            holder.toUserView = view.findViewById(R.id.rtly_to_user_part);
            holder.toUserAvatar = (ImageView)view.findViewById(R.id.iv_to_user_avatar);
            holder.toUserContent = (TextView)view.findViewById(R.id.tv_to_content);
            holder.toUserDate = (TextView)view.findViewById(R.id.tv_to_date);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        if(holder.avatarLoader != null){
            holder.avatarLoader.cancelRequest();
        }
        if(userMessage.getFromUserId().equals(AppContext.auth.getUserId())){
            holder.fromUserView.setVisibility(View.VISIBLE);
            holder.toUserView.setVisibility(View.GONE);
            holder.fromUserContent.setText(FacesUtil.parseFaceByText(mContext,userMessage.getContent()));
            holder.fromUserDate.setText(TimeUtil.formatShowTime(userMessage.getDate()));
            if(!StringUtil.isEmpty(AppContext.auth.getAvatar()) && StringUtil.isUrl(AppContext.auth.getAvatar())){
                holder.avatarLoader = RequestManager.loadImage(AppContext.auth.getAvatar(), RequestManager.getImageListener(holder.fromUserAvatar,
                        mAvatarDefaultBitmap, mAvatarDefaultBitmap, new BitmapFilter() {
                            @Override
                            public Bitmap filter(Bitmap bitmap) {
                                return ImageUtil.roundBitmap(bitmap);
                            }
                        }
                ));
            }
        }else{
            holder.fromUserView.setVisibility(View.GONE);
            holder.toUserView.setVisibility(View.VISIBLE);
            holder.toUserContent.setText(FacesUtil.parseFaceByText(mContext, userMessage.getContent()));
            holder.toUserDate.setText(TimeUtil.formatShowTime(userMessage.getDate()));
            if(!StringUtil.isEmpty(mToUser.getAvatar()) && StringUtil.isUrl(mToUser.getAvatar())){
                holder.avatarLoader = RequestManager.loadImage(mToUser.getAvatar(),RequestManager.getImageListener(holder.toUserAvatar,
                        mAvatarDefaultBitmap,mAvatarDefaultBitmap,new BitmapFilter() {
                            @Override
                            public Bitmap filter(Bitmap bitmap) {
                                return ImageUtil.roundBitmap(bitmap);
                            }
                        }));
            }
        }
        return view;
    }
}
