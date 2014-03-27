package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by absurd on 14-3-13.
 */
public class CategoryFragment extends Fragment {

    private HomeActivity mHomeActivity;

    private GridView mCategoryGv;

    private static CategoryFragment mCategoryFragment;

    public static CategoryFragment getInstance(){
        if(mCategoryFragment == null)
            mCategoryFragment = new CategoryFragment();
        return mCategoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeActivity = (HomeActivity)getActivity();
        View rootView = inflater.inflate(R.layout.fragment_category,null);
        mCategoryGv = (GridView)rootView.findViewById(R.id.gv_category);
        Map<Integer,String> itemsMap = new HashMap<Integer, String>();
        itemsMap.put(1,"新鲜事");
        itemsMap.put(6,"美食");
        itemsMap.put(10,"心情");
        itemsMap.put(9,"随手拍");
        itemsMap.put(7,"活动");
        itemsMap.put(8,"交友");
        final CategoryAdapter adapter = new CategoryAdapter(CategoryFragment.this.getActivity(),itemsMap,mHomeActivity.categoryFilter);
        mCategoryGv.setAdapter(adapter);
        mCategoryGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mHomeActivity.categoryFilter.contains(adapter.getItem(i))){
                    mHomeActivity.categoryFilter.remove(adapter.getItem(i));
                    adapter.setSelectedItems(mHomeActivity.categoryFilter);
                    adapter.notifyDataSetChanged();
                }else{
                    mHomeActivity.categoryFilter.add((Integer)adapter.getItem(i));
                    adapter.setSelectedItems(mHomeActivity.categoryFilter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        rootView.findViewById(R.id.iv_distance_flag1).setVisibility(View.VISIBLE);
        return rootView;
    }
}
