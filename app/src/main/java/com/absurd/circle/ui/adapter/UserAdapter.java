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
import com.absurd.circle.data.model.User;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.StringUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by absurd on 14-3-28.
 */
public class UserAdapter extends BeanAdapter<User> {

    public UserAdapter(Context context){
        super(context);
    }
    public UserAdapter(Context context, List<User> items) {
        super(context, items);
    }

    private class ViewHolder{
        ImageView avatarView;
        TextView usernameView;
        TextView descView;
        ImageLoader.ImageContainer avatarLoader;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        final User user = (User)getItem(i);
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_user,null);
            holder = new ViewHolder();
            holder.avatarView = (ImageView)view.findViewById(R.id.iv_user_item_avatar);
            holder.usernameView = (TextView)view.findViewById(R.id.tv_user_item_username);
            holder.descView = (TextView)view.findViewById(R.id.tv_user_item_desc);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        if(holder.avatarLoader != null){
            holder.avatarLoader.cancelRequest();
        }
        if(!StringUtil.isEmpty(user.getAvatar()) && StringUtil.isUrl(user.getAvatar())){
            holder.avatarLoader = RequestManager.loadImage(user.getAvatar(),RequestManager.getImageListener(holder.avatarView,
                    mAvatarDefaultBitmap,mAvatarDefaultBitmap,new BitmapFilter() {
                        @Override
                        public Bitmap filter(Bitmap bitmap) {
                            return ImageUtil.roundBitmap(bitmap);
                        }
                    }));
        }
        holder.usernameView.setText(user.getName());;
        holder.descView.setText(user.getDescription());
        return view;
    }
}
