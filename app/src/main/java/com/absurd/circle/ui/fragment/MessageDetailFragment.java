package com.absurd.circle.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.service.CommentService;
import com.absurd.circle.ui.activity.EditCommentActivity;
import com.absurd.circle.ui.activity.ImageDetailActivity;
import com.absurd.circle.ui.activity.MessageDetailActivity;
import com.absurd.circle.ui.activity.UserProfileActivity;
import com.absurd.circle.ui.adapter.CommentAdapter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.TimeUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-14.
 */
public class MessageDetailFragment extends Fragment{
    private Bitmap mAvatarDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_avatar)).getBitmap();
    private Bitmap mMediaDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_media)).getBitmap();


    private ListView mContentLv;
    private TextView mEmptyTv;

    private TextView mPraiseCountTv;
    private TextView mCommentCountTv;

    private MessageDetailActivity mMessageDetailActivity;

    private CommentService mCommentService;

    private int mCurrentPageIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMessageDetailActivity = (MessageDetailActivity)getActivity();
        mCommentService = new CommentService();
        View rootView = inflater.inflate(R.layout.fragment_message_detail,null);

        initCommentList(inflater, rootView);
        initBottomBar(rootView);
        refresh();

        return rootView;
    }

    private void initCommentList(LayoutInflater inflater, View rootView){
        View headerView = inflater.inflate(R.layout.header_message_detail,null);
        ((TextView)headerView.findViewById(R.id.tv_header_content)).setText(mMessageDetailActivity.message.getContent());
        ((TextView)headerView.findViewById(R.id.tv_header_title_created)).setText(TimeUtil.formatShowTime(mMessageDetailActivity.message.getDate()));
        ImageView headerAvaterView = (ImageView)headerView.findViewById(R.id.iv_header_title_avatar);
        ImageView headerMediaView = (ImageView)headerView.findViewById(R.id.iv_header_media);
        if(mMessageDetailActivity.message != null && mMessageDetailActivity.message.getUser() != null){
            ((TextView)headerView.findViewById(R.id.tv_header_title_username)).setText(mMessageDetailActivity.message.getUser().getName());
            ((TextView)headerView.findViewById(R.id.tv_header_title_description)).setText(mMessageDetailActivity.message.getUser().getDescription());
            if(!StringUtil.isEmpty(mMessageDetailActivity.message.getUser().getAvatar()) && StringUtil.isUrl(mMessageDetailActivity.message.getUser().getAvatar())) {
                RequestManager.loadImage(mMessageDetailActivity.message.getUser().getAvatar(), RequestManager.getImageListener(headerAvaterView,
                        mAvatarDefaultBitmap, mAvatarDefaultBitmap, new BitmapFilter() {
                            @Override
                            public Bitmap filter(Bitmap bitmap) {
                                return ImageUtil.roundBitmap(bitmap);
                            }
                        }
                ));
            }
            headerAvaterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.startActivity(mMessageDetailActivity,
                            UserProfileActivity.class, "user",
                            mMessageDetailActivity.message.getUser());
                }
            });
            headerView.findViewById(R.id.tv_header_title_username).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.startActivity(mMessageDetailActivity,
                            UserProfileActivity.class, "user",
                            mMessageDetailActivity.message.getUser());
                }
            });
        }
        if(!StringUtil.isEmpty(mMessageDetailActivity.message.getMediaUrl()) && StringUtil.isUrl(mMessageDetailActivity.message.getMediaUrl())){
            RequestManager.loadImage(mMessageDetailActivity.message.getMediaUrl(),RequestManager.getImageListener(headerMediaView,
                    mMediaDefaultBitmap,mMediaDefaultBitmap,null));
            headerMediaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.startActivity(mMessageDetailActivity,ImageDetailActivity.class,"mediaUrl",mMessageDetailActivity.message.getMediaUrl());
                }
            });
        }else{
            headerMediaView.setVisibility(View.GONE);
        }

        mContentLv = (ListView)rootView.findViewById(R.id.lv_comment_content);
        CommentAdapter adapter = new CommentAdapter(getActivity());
        mContentLv.addHeaderView(headerView);
        mContentLv.setAdapter(adapter);

        //Scroll down get more comments
        mContentLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(absListView.getLastVisiblePosition() == absListView.getCount() - 1 && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    nextPage();
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });
        mEmptyTv = (TextView)rootView.findViewById(R.id.tv_empty);

        //Init comment list on start
        refresh();
    }

    private void initBottomBar(View rootView){
        mCommentCountTv = (TextView)rootView.findViewById(R.id.tv_comment_count);
        mPraiseCountTv = (TextView)rootView.findViewById(R.id.tv_praise_count);
        mCommentCountTv.setText(mMessageDetailActivity.message.getCommentCount() + "");
        mPraiseCountTv.setText(mMessageDetailActivity.message.getPraiseCount() + "");
        rootView.findViewById(R.id.llyt_bar_btn_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(mMessageDetailActivity, EditCommentActivity.class, "message", mMessageDetailActivity.message);
            }
        });
        rootView.findViewById(R.id.llyt_bar_btn_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void refresh(){
        mCurrentPageIndex = 0;
        mCommentService.getComments(mMessageDetailActivity.message.getId(), mCurrentPageIndex,10,true,new TableQueryCallback<Comment>() {
            @Override
            public void onCompleted(List<Comment> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null){
                    if(exception != null){
                        exception.printStackTrace();
                        Toast.makeText(MessageDetailFragment.this.getActivity(),"get Comment failed!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    // HeaderViewListAdapter can not cast to BaseAdapter directly
                    HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) mContentLv.getAdapter();
                    ((CommentAdapter) headerAdapter.getWrappedAdapter()).setItems(result);
                }
            }
        });
    }

    private void nextPage(){
        mCurrentPageIndex ++;
        mCommentService.getComments(mMessageDetailActivity.message.getId(),mCurrentPageIndex,10,true,new TableQueryCallback<Comment>(){

            @Override
            public void onCompleted(List<Comment> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null){
                    if(exception != null){
                        exception.printStackTrace();
                        Toast.makeText(MessageDetailFragment.this.getActivity(),"get Comment failed!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) mContentLv.getAdapter();
                    ((CommentAdapter)headerAdapter.getWrappedAdapter()).addItems(result);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }


}
