package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.activity.ImageDetailActivity;
import com.absurd.circle.ui.activity.MyProfileActivity;
import com.absurd.circle.ui.activity.UserDynamicActivity;
import com.absurd.circle.ui.activity.UserProfileActivity;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.util.DistanceUtil;
import com.absurd.circle.util.FacesUtil;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.TimeUtil;
import com.absurd.circle.util.StringUtil;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.util.List;

/**
 * Created by absurd on 14-3-26.
 */
public class MessageAdapter extends BeanAdapter<Message> {

    private Bitmap mMediaDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_media)).getBitmap();
    public MessageAdapter(Context context, List<Message> items) {
        super(context, items);
    }

    public MessageAdapter(Context context){
        super(context);
    }

    private class ViewHolder{
        ImageView avatarIv;
        TextView createdTv;
        TextView usernameTv;
        TextView contentTv;
        TextView locationTv;
        TextView praiseCommentTv;
        ImageView mediaIv;
        ImageLoader.ImageContainer avatarRequest;
        ImageLoader.ImageContainer mediaRequest;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Message message = (Message) getItem(i);
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_message,null);
            holder = new ViewHolder();
            holder.avatarIv = (ImageView)view.findViewById(R.id.iv_title_avatar);
            holder.createdTv = (TextView)view.findViewById(R.id.tv_title_created);
            holder.usernameTv = (TextView)view.findViewById(R.id.tv_title_username);
            holder.contentTv = (TextView)view.findViewById(R.id.tv_content);
            holder.mediaIv = (ImageView)view.findViewById(R.id.iv_media);
            holder.locationTv = (TextView)view.findViewById(R.id.tv_location);
            holder.praiseCommentTv = (TextView)view.findViewById(R.id.tv_praise_comment);

            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
            //Cancle Request in order to fobidden the delay of image downloading
            if(holder.avatarRequest != null){
                holder.avatarRequest.cancelRequest();
            }
            if(holder.mediaRequest != null){
                holder.mediaRequest.cancelRequest();
            }
        }
        if(message.getUser() != null) {
            if(message.getUser().getAvatar() != null && StringUtil.isUrl(message.getUser().getAvatar()) ) {
                //AppContext.commonLog.i(message.getUser().getName()+ "---------" + message.getUser().getAvatar());
                holder.avatarRequest = RequestManager.loadImage(message.getUser().getAvatar(),
                        RequestManager.getImageListener(holder.avatarIv, mAvatarDefaultBitmap, mAvatarDefaultBitmap,new BitmapFilter() {
                            @Override
                            public Bitmap filter(Bitmap bitmap) {
                                return ImageUtil.roundBitmap(bitmap);
                            }
                        }));
                holder.avatarIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(message.getUserId().equals(AppContext.userId)){
                            IntentUtil.startActivity(mContext, MyProfileActivity.class);
                        }else {
                            IntentUtil.startActivity(mContext, UserProfileActivity.class, "user", message.getUser());
                        }
                    }
                });
            }
            holder.usernameTv.setText(message.getUser().getName());
        }

        holder.contentTv.setText(FacesUtil.parseFaceByText(mContext,message.getContent()));
        if(mContext instanceof HomeActivity){
            holder.createdTv.setText(TimeUtil.formatShowTime(message.getCommentDate()));
        }else {
            holder.createdTv.setText(TimeUtil.formatShowTime(message.getDate()));
        }

        if(AppContext.lastPosition != null) {
            holder.locationTv.setText(StringUtil.parseDistance(DistanceUtil.caculate(AppContext.lastPosition.getLatitude(),AppContext.lastPosition.getLongitude()
                                              ,message.getLatitude(),message.getLongitude())));
        }
        holder.praiseCommentTv.setText("赞 " + message.getPraiseCount() + " 评论 " + message.getCommentCount());
        if(message.getMediaUrl() != null){
            holder.mediaIv.setVisibility(View.VISIBLE);
            holder.mediaRequest = RequestManager.loadImage(message.getMediaUrl(),
                    RequestManager.getImageListener(holder.mediaIv, mMediaDefaultBitmap, mMediaDefaultBitmap,null));
            holder.mediaIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.startActivity(mContext,ImageDetailActivity.class,"mediaUrl",message.getMediaUrl());
                }
            });
        }else{
            holder.mediaIv.setVisibility(View.GONE);
        }
        return view;
    }

}
