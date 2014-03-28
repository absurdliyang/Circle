package com.absurd.circle.data.test;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.service.CommentService;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-26.
 */
public class CommentServiceTest extends BaseTestCase {
    private CommentService mCommentService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCommentService = new CommentService();
    }

    public void testGetComments() throws Exception {
        final Object lock = new Object();
        mCommentService.getComments(1265611,1,10,true,new TableQueryCallback<Comment>() {
            @Override
            public void onCompleted(List<Comment> comments, int count, Exception exception, ServiceFilterResponse response) {
                if(comments == null){
                    mLog.i("comments is null");
                    if(exception != null){
                        exception.printStackTrace();
                    }
                }else{
                    for(Comment comment :comments){
                        mLog.i(comment.getParentText());
                    }
                }
                synchronized (lock){
                    lock.notify();
                }
            }
        });
        synchronized (lock){
            lock.wait();
        }
    }
}
