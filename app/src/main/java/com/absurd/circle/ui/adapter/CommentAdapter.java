package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Comment;

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
        ImageView deleteIconIv;
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
            holder.deleteIconIv = (ImageView)view.findViewById(R.id.iv_comment_delete_icon);
            view.setTag(holder);;
        }else{
            holder = (ViewHolder)view.getTag();
        }
        holder.usernameTv.setText(comment.getUser().getName());
        holder.commentContentTv.setText(comment.getContent());

        return view;
    }

}
