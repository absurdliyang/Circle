package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.ui.activity.EditCommentActivity;
import com.absurd.circle.ui.activity.LoadOriginImaegAcitivty;
import com.absurd.circle.ui.activity.UserProfileActivity;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.util.FacesUtil;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.TimeUtil;
import com.android.volley.toolbox.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by absurd on 14-3-14.
 */
public class CommentAdapter extends BeanAdapter<Comment> {

    private FragmentActivity mActivity;


    public CommentAdapter(Context context, List<Comment> items) {
        super(context, items);
    }

    public CommentAdapter(FragmentActivity activity){
        super(activity);
        mActivity = activity;
    }

    private class ViewHolder{
        ImageView userAvatarIv;
        TextView usernameTv;
        TextView commentCreatedTv;
        TextView commentContentTv;
        ImageView replyIconIv;
        ImageView mediaIv;
        ImageLoader.ImageContainer avatarRequest;
        ImageLoader.ImageContainer mediaRequest;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Comment comment = (Comment)getItem(i);
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_comment,null);
            holder = new ViewHolder();
            holder.userAvatarIv = (ImageView)view.findViewById(R.id.iv_comment_user_avatar);
            holder.usernameTv = (TextView)view.findViewById(R.id.tv_comment_username);
            holder.commentCreatedTv = (TextView)view.findViewById(R.id.tv_comment_created);
            holder.commentContentTv = (TextView)view.findViewById(R.id.tv_comment_content);
            holder.mediaIv = (ImageView)view.findViewById(R.id.iv_comment_media);
            holder.replyIconIv = (ImageView)view.findViewById(R.id.iv_comment_reply_icon);
            view.setTag(holder);;
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

        if(comment.getUser() != null) {
            String avatarStr = "";
            if(StringUtil.isEmpty(comment.getUser().getAvatar())){
                avatarStr = "https://annonymous";
            }else{
                avatarStr = comment.getUser().getAvatar();
            }
            if(avatarStr != null && StringUtil.isUrl(avatarStr) ) {
                holder.avatarRequest = RequestManager.loadImage(avatarStr,
                        RequestManager.getImageListener(holder.userAvatarIv, mAvatarDefaultBitmap, mAvatarDefaultBitmap,new BitmapFilter() {
                            @Override
                            public Bitmap filter(Bitmap bitmap) {
                                return ImageUtil.roundBitmap(bitmap);
                            }
                        }));
            }
            holder.usernameTv.setText(comment.getUser().getName());
            holder.userAvatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.startActivity(mActivity,UserProfileActivity.class,"user",comment.getUser());
                }
            });
        }
        holder.commentCreatedTv.setText(TimeUtil.formatShowTime(comment.getDate()));
        String content = "";
        if(comment.getParentId() != 0 && comment.getParentUserName() != null){
            content = "回复 " + comment.getParentUserName() + ":";
        }
        content += comment.getContent();
        holder.commentContentTv.setText(FacesUtil.parseFaceByText(mContext, content));
        holder.replyIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(mActivity, EditCommentActivity.class, "parentComment", comment);
            }
        });
        if(!StringUtil.isEmpty(comment.getMediaUrl())){
            final String thumbnailUrl = ImageUtil.getThumbnailUrl(comment.getMediaUrl(), 200, true);
            holder.mediaIv.setVisibility(View.VISIBLE);
            holder.mediaIv.setTag("loading");
            holder.mediaRequest = RequestManager.loadImage(thumbnailUrl,
                    RequestManager.getImageListener(holder.mediaIv, mMediaDefaultBitmap, mMediaDefaultBitmap,null));

            final Bitmap bitmap;
            if(holder.mediaIv.getTag().equals("loading")){
                bitmap = null;
            }else{
                bitmap = ((BitmapDrawable)holder.mediaIv.getDrawable()).getBitmap();
            }
            holder.mediaIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, LoadOriginImaegAcitivty.class);
                    intent.putExtra("mediaUrl", comment.getMediaUrl());
                    if(bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("thumbnailBitmap", bytes);
                    }
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_in);
                }
            });
        }else{
            holder.mediaIv.setVisibility(View.GONE);
        }
        return view;
    }

}
