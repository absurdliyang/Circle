package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.EditMessageActivity;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.adapter.CategoryAdapter;
import com.absurd.circle.util.IntentUtil;

import java.io.Serializable;
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

    private LinearLayout mContainer;
    private View.OnTouchListener mOnTouchListener;

    public void setOnTouchListener(View.OnTouchListener onTouchListener){
        this.mOnTouchListener = onTouchListener;
    }

    private static CategoryFragment mCategoryFragment;
    // 0 for category selected
    // 1 for edit new message
    private int mStatus = 0;

    public boolean hasChanged = false;

    // Message Filter
    public List<Integer> categoryFilter;
    public int distanceFilter;
    public boolean orderFilter;

    public CategoryFragment(){
        super();
        initDefaultFilter();
    }

    public static CategoryFragment getInstance(){
        if(mCategoryFragment == null)
            mCategoryFragment = new CategoryFragment();
        return mCategoryFragment;
    }


    private void initDefaultFilter(){
        categoryFilter = new ArrayList<Integer>();
        categoryFilter.add(1);
        categoryFilter.add(7);
        categoryFilter.add(8);
        categoryFilter.add(9);
        categoryFilter.add(10);
        orderFilter = true;
        distanceFilter = 5;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Init default message filter
        //initDefaultFilter();
        mHomeActivity = (HomeActivity)getActivity();
        final View rootView = inflater.inflate(R.layout.fragment_category,null);
        mContainer = (LinearLayout)rootView.findViewById(R.id.llyt_category_container);
        mContainer.setOnTouchListener(mOnTouchListener);
        mCategoryGv = (GridView)rootView.findViewById(R.id.gv_category);
        Map<Integer,String> itemsMap = new HashMap<Integer, String>();
        itemsMap.put(1,"新鲜事");
        //itemsMap.put(6,"美食");
        itemsMap.put(10,"心情");
        itemsMap.put(9,"随手拍");
        itemsMap.put(7,"活动");
        itemsMap.put(8,"交友");
        final CategoryAdapter adapter = new CategoryAdapter(CategoryFragment.this.getActivity(),itemsMap,categoryFilter);
        mCategoryGv.setAdapter(adapter);

        rootView.findViewById(R.id.iv_distance_flag1).setVisibility(View.VISIBLE);
        if(mStatus == 1){
            rootView.findViewById(R.id.llyt_cat_distance).setVisibility(View.GONE);
            rootView.findViewById(R.id.llyt_cat_order).setVisibility(View.GONE);
            rootView.findViewById(R.id.v_cat_divider1).setVisibility(View.GONE);
            rootView.findViewById(R.id.v_cat_divider2).setVisibility(View.GONE);
            mCategoryGv.setAdapter(new CategoryAdapter(CategoryFragment.this.getActivity(),itemsMap,null));
            mCategoryGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    IntentUtil.startActivity(mHomeActivity, EditMessageActivity.class,"contentType",(Serializable)adapter.getItem(i));
                    // remove the category fragment from showed view
                    FragmentManager fm = mHomeActivity.getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                    CategoryFragment fragment = CategoryFragment.getInstance();
                    ft.remove(fragment).commit();
                    fm.popBackStack();
                }
            });
        }else{
            mCategoryGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    hasChanged = true;
                    if(categoryFilter.contains(adapter.getItem(i))){
                        categoryFilter.remove(adapter.getItem(i));
                        adapter.setSelectedItems(categoryFilter);
                        adapter.notifyDataSetChanged();
                    }else{
                        categoryFilter.add((Integer)adapter.getItem(i));
                        adapter.setSelectedItems(categoryFilter);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        rootView.findViewById(R.id.iv_distance_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasChanged = true;
                rootView.findViewById(R.id.iv_distance_flag1).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.iv_distance_flag2).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.iv_distance_flag3).setVisibility(View.INVISIBLE);
            }
        });
        rootView.findViewById(R.id.iv_distance_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasChanged = true;
                rootView.findViewById(R.id.iv_distance_flag2).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.iv_distance_flag1).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.iv_distance_flag3).setVisibility(View.INVISIBLE);
            }
        });
        rootView.findViewById(R.id.iv_distance_btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasChanged = true;
                rootView.findViewById(R.id.iv_distance_flag3).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.iv_distance_flag1).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.iv_distance_flag2).setVisibility(View.INVISIBLE);
            }
        });
        rootView.findViewById(R.id.tv_order_last_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasChanged = true;
                orderFilter = true;

            }
        });
        rootView.findViewById(R.id.tv_order_last_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasChanged = true;
                orderFilter = false;
            }
        });
        return rootView;
    }



    public int getStatus(){
        return mStatus;
    }

    public void setStatus(int status){
        this.mStatus = status;
    }
}
