package com.absurd.circle.ui.adapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by absurd on 14-3-11.
 */
public class BeanAdapter<E> extends BaseAdapter {
    protected Bitmap mAvatarDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_avatar)).getBitmap();
    protected Bitmap mMediaDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_media)).getBitmap();


    protected Context mContext;
    protected List<E> mItems = Collections.emptyList();


    public BeanAdapter(Context context,List<E> items){
        this(context);
        this.mItems = items;
    }

    public BeanAdapter(Context context){
        this.mContext = context;
    }

    public void setItems(final List<E> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void addItems(final List<E> addItems){
        this.mItems.addAll(addItems);
        notifyDataSetChanged();
    }

    public void addItem(final E item){
        this.mItems.add(item);
        notifyDataSetChanged();
    }

    public void deleteAllItems(){
        this.mItems.clear();
        notifyDataSetChanged();
    }

    public List<E> getItems(){
        return mItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
