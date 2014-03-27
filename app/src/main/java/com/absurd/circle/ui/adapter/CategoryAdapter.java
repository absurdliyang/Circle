package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by absurd on 14-3-14.
 */
public class CategoryAdapter extends BaseAdapter{

    private Context mContext;
    private List<Integer> mSelectedItems;
    private List<Integer> mItems;
    private Map<Integer,String> mItemsMap;

    public CategoryAdapter(Context context, Map<Integer,String> items, List<Integer> selected){
        this.mContext = context;
        mItemsMap = items;
        mSelectedItems = selected;
        mItems = new ArrayList<Integer>();
        for(Integer keyItem :mItemsMap.keySet()){
            mItems.add(keyItem);
        }
        // Sort the mItems
        int i;
        for(i = 0; i < mItems.size(); i++){
            int j;
            int min = i;
            for(j = i + 1; j < mItems.size(); j++){
                if(mItems.get(j) < mItems.get(min)){
                    min = j;
                }
            }
            int tmp = mItems.get(min);
            mItems.set(min,mItems.get(i));
            mItems.set(i,tmp);
        }
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

    public void setSelectedItems(List<Integer> selectedItems){
        this.mSelectedItems = selectedItems;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Integer category = (Integer)getItem(i);
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_category,null);
        }
        ((TextView) view.findViewById(R.id.tv_category_name)).setText(mItemsMap.get(category));
        if(mSelectedItems.contains(category)) {
            view.setSelected(true);
        }else{
            view.setSelected(false);
        }
        return view;
    }
}
