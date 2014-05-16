package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
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
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.util.DistanceUtil;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.StringUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by absurd on 14-5-16.
 */
public class NearbyUserAdapter extends BeanAdapter<User> {

    private Bitmap mFemailBitmap = ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.user_profile_female)).getBitmap();
    private Bitmap mMaleBitmap = ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.user_profile_male)).getBitmap();


    private FragmentActivity mActivity;

    public NearbyUserAdapter(Context context, List<User> items) {
        super(context, items);
    }

    public NearbyUserAdapter(Context context) {
        super(context);
    }

    public NearbyUserAdapter(FragmentActivity activity){
        super(activity);

        this.mActivity = activity;
    }

    private class ViewHolder{
        TextView numView;
        ImageView avatarView;
        TextView usernameView;
        ImageView sexView;
        TextView positionView;
        TextView levelView;
        TextView descView;
        ImageLoader.ImageContainer avatarLoader;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        final User user = (User)getItem(i);
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_nearbyuser,null);
            holder = new ViewHolder();
            holder.numView = (TextView)view.findViewById(R.id.tv_nearbyuser_item_num);
            holder.avatarView = (ImageView)view.findViewById(R.id.iv_nearbyuser_item_avatar);
            holder.sexView = (ImageView)view.findViewById(R.id.iv_nearbyuser_item_sex);
            holder.levelView = (TextView)view.findViewById(R.id.tv_nearbyuser_item_level);
            holder.usernameView = (TextView)view.findViewById(R.id.tv_nearbyuser_item_username);
            holder.positionView = (TextView)view.findViewById(R.id.tv_nearbyuser_item_position);
            holder.descView = (TextView)view.findViewById(R.id.tv_nearbyuser_item_desc);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        if(holder.avatarLoader != null){
            holder.avatarLoader.cancelRequest();
        }
        holder.numView.setText(user.getRowNumber() + "");
        if(!StringUtil.isEmpty(user.getAvatar()) && StringUtil.isUrl(user.getAvatar())){
            holder.avatarLoader = RequestManager.loadImage(user.getAvatar(), RequestManager.getImageListener(holder.avatarView,
                    mAvatarDefaultBitmap, mAvatarDefaultBitmap, new BitmapFilter() {
                        @Override
                        public Bitmap filter(Bitmap bitmap) {
                            return ImageUtil.roundBitmap(bitmap);
                        }
                    }
            ));
        }
        holder.usernameView.setText(user.getName());
        holder.descView.setText(user.getDescription());
        if (user.getSex().equals("m")) {
            holder.sexView.setImageBitmap(mFemailBitmap);
        } else {
            holder.sexView.setImageBitmap(mMaleBitmap);
        }
        holder.levelView.setText("LV." + user.getLevel());
        if(AppContext.lastPosition != null) {
            holder.positionView.setText(StringUtil.parseDistance(DistanceUtil.caculate(AppContext.lastPosition.getLatitude(), AppContext.lastPosition.getLongitude()
                    , user.getLatitude(), user.getLongitude())));
        }
        return view;
    }




}
