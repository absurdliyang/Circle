package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.absurd.circle.app.R;

import java.util.List;

/**
 * Created by absurd on 14-3-14.
 */
public class CategoryAdapter extends BaseAdapter{

    private Context mContext;
    private List<String> mItems;

    public CategoryAdapter(Context context, List<String> items){
        this.mContext = context;
        this.mItems = items;
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
        String category = (String)getItem(i);
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_category,null);
        }
        ((TextView)view.findViewById(R.id.tv_category_name)).setText(category);
        return view;
    }
}
