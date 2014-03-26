package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Message;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by absurd on 14-3-26.
 */
public class MessageAdapter1 extends BeanAdapter<Message> {

    //private BitmapDrawable mAvatarDefaultBitmap = (BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.ic_launcher);

    public MessageAdapter1(Context context, List<Message> items) {
        super(context, items);
    }

    public MessageAdapter1(Context context){
        super(context);
    }

    private class ViewHolder{
        ImageView avatarIv;
        TextView createdTv;
        TextView usernameTv;
        TextView contentTv;
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

            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        if(message.getUser().getAvatar() != null) {
            holder.avatarRequest = RequestManager.loadImage(message.getUser().getAvatar(),
                    RequestManager.getImageListener(holder.avatarIv, null, null));
            holder.usernameTv.setText(message.getUser().getName());
        }
        holder.contentTv.setText(message.getContent());
        holder.createdTv.setText(message.getDate().toString());
        if(message.getMediaUrl() != null){
            holder.mediaRequest = RequestManager.loadImage(message.getMediaUrl(),
                    RequestManager.getImageListener(holder.mediaIv, null, null));
        }
        return view;
    }
}
