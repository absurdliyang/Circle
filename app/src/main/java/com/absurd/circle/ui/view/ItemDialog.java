package com.absurd.circle.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.absurd.circle.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-4-8.
 */
public class ItemDialog extends AlertDialog {

    private ListView mItemLv;
    private List<String> mItems;
    private AdapterView.OnItemClickListener mListener;
    public ItemDialog(Context context) {
        super(context);
    }

    public ItemDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ItemDialog(Context context, int theme) {
        super(context, theme);
    }


    public ItemDialog(Context context,List<String> items){
        super(context);
        this.mItems = items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = this.getLayoutInflater();
        View parent = inflater.inflate(R.layout.dia_items,null);
        mItemLv = (ListView)parent.findViewById(R.id.lv_dia_items);
        mItemLv.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_dia, R.id.tv_dia_item, mItems));
        if(mListener != null)
            mItemLv.setOnItemClickListener(mListener);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        this.setContentView(parent);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mListener = listener;
    }
}
