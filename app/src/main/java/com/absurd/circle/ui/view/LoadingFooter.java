package com.absurd.circle.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.absurd.circle.app.R;

/**
 * Created by absurd on 14-3-12.
 */
public class LoadingFooter {
    protected View mLoadingFooter;

    protected TextView mLoadingText;

    protected State mState = State.Idle;

    private ProgressBar mProgress;

    private long mAnimationDuration;

    public static enum State {
        Idle, TheEnd, Loading
    }

    public LoadingFooter(Context context) {
        mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.footer_loading, null);
        mLoadingFooter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 屏蔽点击
            }
        });
        mProgress = (ProgressBar) mLoadingFooter.findViewById(R.id.pb_loading_bar);
        mLoadingText = (TextView) mLoadingFooter.findViewById(R.id.tv_loading);
        mAnimationDuration = context.getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        setState(State.Idle);
    }

    public View getView() {
        return mLoadingFooter;
    }

    public State getState() {
        return mState;
    }

    public void setState(final State state, long delay) {
        mLoadingFooter.postDelayed(new Runnable() {

            @Override
            public void run() {
                setState(state);
            }
        }, delay);
    }

    public void setState(State status) {
        if (mState == status) {
            return;
        }
        mState = status;

        mLoadingFooter.setVisibility(View.VISIBLE);

        switch (status) {
            case Loading:
                mLoadingText.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                break;
            case TheEnd:
                mLoadingText.setVisibility(View.VISIBLE);
                //mLoadingText.animate().withLayer().alpha(1).setDuration(mAnimationDuration);
                mProgress.setVisibility(View.GONE);
                break;
            default:
                mLoadingFooter.setVisibility(View.GONE);
                break;
        }
    }
}
