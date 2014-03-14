package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-13.
 */
public class CategoryFragment extends Fragment {

    private GridView mCategoryGv;

    private static CategoryFragment mCategoryFragment;

    public static CategoryFragment getInstance(){
        if(mCategoryFragment == null)
            mCategoryFragment = new CategoryFragment();
        return mCategoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category,null);
        mCategoryGv = (GridView)rootView.findViewById(R.id.gv_category);
        List<String> categories = new ArrayList<String>();
        categories.add("Category1");
        categories.add("Category2");
        categories.add("Category3");
        categories.add("Category4");
        categories.add("Category5");
        categories.add("Category6");
        CategoryAdapter adapter = new CategoryAdapter(CategoryFragment.this.getActivity(),categories);
        mCategoryGv.setAdapter(adapter);
        mCategoryGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return rootView;
    }
}
