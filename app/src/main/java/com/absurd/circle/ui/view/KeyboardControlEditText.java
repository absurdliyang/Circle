package com.absurd.circle.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by absurd on 14-5-3.
 */
public class KeyboardControlEditText extends AutoCompleteTextView{
    private boolean mShowKeyboard = true;

    public void setShowKeyboard(boolean value) {
        mShowKeyboard = value;
    }

    public KeyboardControlEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return mShowKeyboard;
    }
}
