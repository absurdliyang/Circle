package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.absurd.circle.ui.activity.UserProfileActivity;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.TimeUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by absurd on 14-3-14.
 */
public class CommentAdapter extends BeanAdapter<Comment> {


    public CommentAdapter(Context context, List<Comment> items) {
        super(context, items);
    }

    public CommentAdapter(Context context){
        super(context);
    }

    private class ViewHolder{
        ImageView userAvatarIv;
        TextView usernameTv;
        TextView commentCreatedTv;
        TextView commentContentTv;
        ImageView replyIconIv;
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
            if(comment.getUser().getAvatar() != null && StringUtil.isUrl(comment.getUser().getAvatar()) ) {
                holder.avatarRequest = RequestManager.loadImage(comment.getUser().getAvatar(),
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
                    IntentUtil.startActivity(mContext,UserProfileActivity.class,"user",comment.getUser());
                }
            });
        }
        holder.commentCreatedTv.setText(TimeUtil.formatShowTime(comment.getDate()));
        String content = "";
        if(comment.getParentId() != 0){
            String name = "";
            for(Comment c : getItems()){
                if(c.getId() == comment.getParentId()){
                    name = c.getUser().getName();
                }
            }
            content = "回复 " + name + ": ";
        }
        content += comment.getContent();
        holder.commentContentTv.setText(content);
        holder.replyIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(mContext, EditCommentActivity.class, "parentComment", comment);
            }
        });
        return view;
    }

}
