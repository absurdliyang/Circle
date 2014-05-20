package com.absurd.circle.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.absurd.circle.app.AppConfig;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.model.ReportMessage;
import com.absurd.circle.data.service.CommentService;
import com.absurd.circle.data.service.MessageService;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.ui.activity.LoadOriginImaegAcitivty;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.activity.EditCommentActivity;
import com.absurd.circle.ui.activity.MessageDetailActivity;
import com.absurd.circle.ui.activity.UserProfileActivity;
import com.absurd.circle.ui.adapter.CommentAdapter;
import com.absurd.circle.ui.fragment.base.MessageListFragment;
import com.absurd.circle.ui.view.ItemDialog;
import com.absurd.circle.util.FacesUtil;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.TimeUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import org.jivesoftware.smack.Chat;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by absurd on 14-3-14.
 */
public class MessageDetailFragment extends Fragment{
    private Bitmap mAvatarDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_avatar)).getBitmap();
    private Bitmap mMediaDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_media)).getBitmap();


    private ListView mContentLv;

    private TextView mPraiseDescTv;
    private TextView mPraiseCountTv;
    private TextView mCommentCountTv;

    private ProgressBar mPraiseLodingPb;


    private MessageDetailActivity mMessageDetailActivity;

    private CommentService mCommentService;
    private MessageService mMessageService;

    private int mCurrentPageIndex = 0;
    private boolean mQueryOrder = false;

    protected boolean mIsBusy = false;

    private Chat mChat;

    private Praise mPraise = new Praise(-1);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMessageDetailActivity = (MessageDetailActivity)getActivity();
        mCommentService = new CommentService();
        mMessageService = new MessageService();
        View rootView = inflater.inflate(R.layout.fragment_message_detail,null);

        initCommentList(inflater, rootView);
        initBottomBar(rootView);

        if(AppContext.xmppConnectionManager.getConnection() != null) {
            mChat = AppContext.xmppConnectionManager.initChat(MessageDetailActivity.message.getUser().getId() + "");
        }

        return rootView;
    }

    private void initCommentList(LayoutInflater inflater, View rootView){
        View headerView = inflater.inflate(R.layout.header_message_detail,null);
        ((TextView)headerView.findViewById(R.id.tv_header_content)).setText(FacesUtil.parseFaceByText(mMessageDetailActivity,MessageDetailActivity.message.getContent()));
        ((TextView)headerView.findViewById(R.id.tv_header_title_created)).setText(TimeUtil.formatShowTime(MessageDetailActivity.message.getDate()));
        ImageView headerAvaterView = (ImageView)headerView.findViewById(R.id.iv_header_title_avatar);
        ImageView headerMediaView = (ImageView)headerView.findViewById(R.id.iv_header_media);
        if(MessageDetailActivity.message != null && MessageDetailActivity.message.getUser() != null){
            ((TextView)headerView.findViewById(R.id.tv_header_title_username)).setText(MessageDetailActivity.message.getUser().getName());
            ((TextView)headerView.findViewById(R.id.tv_header_title_description)).setText(MessageDetailActivity.message.getUser().getDescription());
            if(!StringUtil.isEmpty(MessageDetailActivity.message.getUser().getAvatar()) && StringUtil.isUrl(MessageDetailActivity.message.getUser().getAvatar())) {
                RequestManager.loadImage(MessageDetailActivity.message.getUser().getAvatar(), RequestManager.getImageListener(headerAvaterView,
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
                            MessageDetailActivity.message.getUser());
                }
            });
            headerView.findViewById(R.id.tv_header_title_username).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.startActivity(mMessageDetailActivity,
                            UserProfileActivity.class, "user",
                            MessageDetailActivity.message.getUser());
                }
            });
        }
        if(!StringUtil.isEmpty(MessageDetailActivity.message.getMediaUrl()) && StringUtil.isUrl(MessageDetailActivity.message.getMediaUrl())){
            headerMediaView.setTag("loading");
            RequestManager.loadImage(MessageDetailActivity.message.getMediaUrl(),RequestManager.getImageListener(headerMediaView,
                    mMediaDefaultBitmap,mMediaDefaultBitmap,null));

            final Bitmap thumbnailBitmap;
            if(headerMediaView.getTag().equals("loading")){
                thumbnailBitmap = null;
            }else{
                thumbnailBitmap = ((BitmapDrawable)headerMediaView.getDrawable()).getBitmap();
            }
            headerMediaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MessageDetailFragment.this.getActivity(), LoadOriginImaegAcitivty.class);
                    intent.putExtra("mediaUrl", MessageDetailActivity.message.getMediaUrl());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if(thumbnailBitmap != null) {
                        thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("thumbnailBitmap", bytes);
                    }
                    MessageDetailFragment.this.getActivity().startActivity(intent);
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
                    mMessageDetailActivity.setBusy(true);
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });

    }


    private void initBottomBar(View rootView){
        mPraiseDescTv = (TextView)rootView.findViewById(R.id.tv_btn_praise_text);
        mCommentCountTv = (TextView)rootView.findViewById(R.id.tv_comment_count);
        mPraiseLodingPb = (ProgressBar)rootView.findViewById(R.id.pb_praise_loading);
        mPraiseLodingPb.setVisibility(View.VISIBLE);
        mPraiseCountTv = (TextView)rootView.findViewById(R.id.tv_praise_count);
        mCommentCountTv.setText(MessageDetailActivity.message.getCommentCount() + "");
        mPraiseCountTv.setText(MessageDetailActivity.message.getPraiseCount() + "");
        MessageService messageService = new MessageService();
        if(AppContext.userId != null) {
            messageService.isPraised(AppContext.userId, MessageDetailActivity.message.getId(),
                    new TableQueryCallback<Praise>() {
                        @Override
                        public void onCompleted(List<Praise> result, int count, Exception exception, ServiceFilterResponse response) {
                            mPraiseLodingPb.setVisibility(View.INVISIBLE);
                            if (result == null) {
                                if (exception != null) {
                                    exception.printStackTrace();
                                }
                            } else {
                                if (!result.isEmpty()) {
                                    mPraise = result.get(0);
                                    mPraiseDescTv.setText("取消赞");
                                }else{
                                    mPraise = null;
                                    mPraiseDescTv.setText("赞");
                                }
                            }
                        }
                    }
            );
        }
        rootView.findViewById(R.id.llyt_bar_btn_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(mMessageDetailActivity, EditCommentActivity.class);
            }
        });
        rootView.findViewById(R.id.llyt_bar_btn_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPraiseLodingPb.setVisibility(View.VISIBLE);
                praiseMessage();
            }
        });
    }

    private void praiseMessage(){
        if(mPraise != null && mPraise.getId() == -1){
            mPraiseLodingPb.setVisibility(View.INVISIBLE);
            return;
        }else{
            if (mPraise != null) {
                mMessageService.deletePraise(mPraise,new TableDeleteCallback() {
                    @Override
                    public void onCompleted(Exception exception, ServiceFilterResponse response) {
                        mPraiseLodingPb.setVisibility(View.INVISIBLE);
                        if(exception != null){
                            exception.printStackTrace();
                        }else{
                            AppContext.commonLog.i("Delete praise success");
                            MessageDetailActivity.message.decPraiseCount();
                            mPraise = null;
                            mPraiseDescTv.setText("赞");
                            refreshBottomBar();
                        }
                    }
                });
            } else {
                if(mChat == null) {
                    mMessageDetailActivity.warning(R.string.chat_not_prepared_warning_send_failed);
                    return;
                }else{
                    mPraise = new Praise();
                    mPraise.setMessageId(MessageDetailActivity.message.getId());
                    mPraise.setUserId(AppContext.auth.getUserId());
                    mPraise.setToUserId(MessageDetailActivity.message.getUserId());
                    mPraise.setDate(Calendar.getInstance().getTime());
                    mPraise.setParentText(MessageDetailActivity.message.getContent());
                    AppContext.xmppConnectionManager.send(mChat, mPraise, MessageDetailActivity.message.getUser().getId() + "");

                    mMessageService.insertPraise(mPraise, new TableOperationCallback<Praise>() {
                        @Override
                        public void onCompleted(Praise entity, Exception exception, ServiceFilterResponse response) {
                            mPraiseLodingPb.setVisibility(View.INVISIBLE);
                            if (entity == null) {
                                if (exception != null) {
                                    exception.printStackTrace();
                                    mMessageDetailActivity.warning(R.string.praise_failed);
                                }
                            } else {
                                AppContext.commonLog.i("Insert Praise success");
                                MessageDetailActivity.message.incPraiseCount();
                                mPraise = entity;
                                mPraiseDescTv.setText("取消赞");
                                refreshBottomBar();
                            }
                        }
                    });
                }
            }
        }
    }

    private void refreshBottomBar(){
        mCommentCountTv.setText(MessageDetailActivity.message.getCommentCount() + "");
        mPraiseCountTv.setText(MessageDetailActivity.message.getPraiseCount() + "");
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void refresh(){
        mMessageDetailActivity.setBusy(true);
        mCurrentPageIndex = 0;
        mCommentService.getComments(MessageDetailActivity.message.getId(), mCurrentPageIndex, AppConfig.CUSTOM_PAGE_SIZE,mQueryOrder,new TableQueryCallback<Comment>() {
            @Override
            public void onCompleted(List<Comment> result, int count, Exception exception, ServiceFilterResponse response) {
                mMessageDetailActivity.setBusy(false);
                if(result == null){
                    if(exception != null){
                        exception.printStackTrace();
                        //Toast.makeText(MessageDetailFragment.this.getActivity(),"get Comment failed!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    // HeaderViewListAdapter can not cast to BaseAdapter directly
                    HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) mContentLv.getAdapter();
                    ((CommentAdapter) headerAdapter.getWrappedAdapter()).setItems(result);
                }
            }
        });
    }

    public void nextPage(){
        if(!mIsBusy) {
            mCurrentPageIndex++;
            mIsBusy = true;
            mCommentService.getComments(MessageDetailActivity.message.getId(), mCurrentPageIndex, 10, mQueryOrder, new TableQueryCallback<Comment>() {

                @Override
                public void onCompleted(List<Comment> result, int count, Exception exception, ServiceFilterResponse response) {
                    mMessageDetailActivity.setBusy(false);
                    mIsBusy = false;
                    if (result == null) {
                        if (exception != null) {
                            exception.printStackTrace();
                            //Toast.makeText(MessageDetailFragment.this.getActivity(),"get Comment failed!",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) mContentLv.getAdapter();
                        ((CommentAdapter) headerAdapter.getWrappedAdapter()).addItems(result);
                    }
                }
            });
        }
    }


    public void onMoreClicked(View view){
        List<String> items = new ArrayList<String>();
        items.add("倒序查看评论");
        items.add("举报该信息");
        items.add("复制信息");
        if(MessageDetailActivity.message.getUserId().equals(AppContext.auth.getUserId())){
            items.add("删除");
        }
        final ItemDialog dialog = new ItemDialog(mMessageDetailActivity,items);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        mQueryOrder  = !mQueryOrder;
                        refresh();
                        mMessageDetailActivity.setBusy(true);
                        break;
                    case 1:
                        reportMessage();
                        mMessageDetailActivity.setBusy(true);
                        break;
                    case 2:
                        copyClipboard();
                        break;
                    case 3:
                        deleteMessage();
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }


    private void copyClipboard(){
        ClipboardManager copy = (ClipboardManager) AppContext.getContext()
                .getSystemService(AppContext.getContext().CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("content", mMessageDetailActivity.message.getContent());
        copy.setPrimaryClip(clip);
        ((BaseActivity)this.getActivity()).warning(R.string.copy_clip_board_success);
    }


    private void reportMessage(){
        NotificationService service = new NotificationService();
        ReportMessage rm = new ReportMessage();
        rm.setFromUserId(AppContext.userId);
        rm.setFromUserName(AppContext.auth.getName());
        rm.setContent(mMessageDetailActivity.message.getContent());
        rm.setMessageId(mMessageDetailActivity.message.getId());
        rm.setToUserId(mMessageDetailActivity.message.getUserId());
        rm.setDeviceId(mMessageDetailActivity.message.getUser().getQq());
        service.insertReportMessage(rm, new TableOperationCallback<ReportMessage>() {
            @Override
            public void onCompleted(ReportMessage entity, Exception exception, ServiceFilterResponse response) {
                mMessageDetailActivity.setBusy(false);
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                }else{
                    mMessageDetailActivity.warning(R.string.report_message_success);;
                }
            }
        });
    }

    public void deleteMessage(){
        mMessageService.deleteMessage(MessageDetailActivity.message, new TableDeleteCallback() {
            @Override
            public void onCompleted(Exception exception, ServiceFilterResponse response) {
                //mMessageDetailActivity.warning("");
                if(exception != null){
                    exception.printStackTrace();
                    return;
                }
                AppContext.commonLog.i("Delate message success");
                if(MessageListFragment.messages != null)
                    MessageListFragment.messages.remove(mMessageDetailActivity.mIndexId);
                mMessageDetailActivity.finish();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        refreshBottomBar();
    }


}
