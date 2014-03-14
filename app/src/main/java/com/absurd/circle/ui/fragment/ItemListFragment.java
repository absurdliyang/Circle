package com.absurd.circle.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.adapter.BeanAdapter;


/**
 * Created by absurd on 14-3-11.
 */
public abstract class ItemListFragment<E> extends Fragment{

    /**
     * ListView
     */
    protected ListView mListView;

    /**
     * Empty TextView
     */
    protected TextView mEmptyText;
    protected View mEmptyView;

    /**
     * Is the list currently shown
     */
    protected boolean mListShown;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.item_list,null);
        mListView = (ListView) view.findViewById(R.layout.item_list);
        return view;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        mListShown = false;
        mEmptyText = null;
        mListView = null;

        super.onDestroyView();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                onListItemClick(arg0,arg1,arg2,arg3);
            }
        });
        setListAdapter(createAdapter(new ArrayList<E>()));
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub
                return onListItemLongClick(arg0, arg1, arg2, arg3);
            }
        });
        mEmptyText = (TextView)view.findViewById(R.id.tv_empty);
        mEmptyView = view.findViewById(R.id.rlyt_empty);
    }

    protected void onListItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){

    }
    protected boolean onListItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
        return true;
    }

    protected void setEmptyViewShow(boolean visibility){
        if(visibility)
            mEmptyView.setVisibility(View.VISIBLE);
        else
            mEmptyView.setVisibility(View.INVISIBLE);
    }

    /**
     * is fragment usable in UI-Thread
     * @return
     */
    protected boolean isUsable(){
        return getActivity() != null;
    }

    protected abstract BeanAdapter<E> createAdapter(final List<E> items);


    protected void setItems(List<E> items){
        if(mListView.getAdapter() != null){
            ((BeanAdapter)mListView.getAdapter()).setItems(items);
        }
    }

    protected void setListAdapter(final BeanAdapter<E> adapter) {
        if (mListView != null)
            mListView.setAdapter(adapter);
    }

    protected void setEmptyText(final String message){
        if(mEmptyText != null)
            mEmptyText.setText(message);
    }

}
