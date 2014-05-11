package com.absurd.circle.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by absurd on 14-3-20.
 */
public class ImageDetailActivity extends ActionBarActivity implements View.OnTouchListener{
    private CommonLog mLog = LogFactory.createLog(AppConstant.TAG);

    private static final float MAX_SCALE = 4f;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private int mode = NONE;

    private ImageView mImageDetailIv;
    private Bitmap mBitmap;

    private Matrix mMatrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    private DisplayMetrics mDisplayMetrics;

    private float mMinScaleR;
    private PointF prev = new PointF();
    private PointF mid = new PointF();
    private float dist = 1f;
    private Bitmap mMediaDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_media)).getBitmap();

    private String mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getIntent().getStringExtra("mediaUrl");
        // Show in full screen
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_image_detail);
        mImageDetailIv = (ImageView)findViewById(R.id.iv_image_detail);
        RequestManager.loadImage(mImageUrl,
            new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mMediaDefaultBitmap != null) {
                        mImageDetailIv.setImageBitmap(mMediaDefaultBitmap);
                    }
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        mBitmap = response.getBitmap();
                        if (!isImmediate && mMediaDefaultBitmap!= null) {
                            TransitionDrawable transitionDrawable = new TransitionDrawable(
                                    new Drawable[] {
                                            new BitmapDrawable(AppContext.getContext().getResources(),mMediaDefaultBitmap),
                                            new BitmapDrawable(AppContext.getContext().getResources(),
                                                    response.getBitmap())
                                    });
                            transitionDrawable.setCrossFadeEnabled(true);
                            mImageDetailIv.setImageDrawable(transitionDrawable);
                            transitionDrawable.startTransition(100);
                        } else {
                            mImageDetailIv.setImageBitmap(response.getBitmap());
                        }
                        mImageDetailIv.setOnTouchListener(ImageDetailActivity.this);
                        mDisplayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
                        minZoom();
                        center();
                        mImageDetailIv.setImageMatrix(mMatrix);
                    }
                }
            });



    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 主点按下
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(mMatrix);
                prev.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            // 副点按下
            case MotionEvent.ACTION_POINTER_DOWN:
                dist = spacing(event);
                // 如果连续两点距离大于10，则判定为多点模式
                if (spacing(event) > 10f) {
                    savedMatrix.set(mMatrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    mMatrix.set(savedMatrix);
                    mMatrix.postTranslate(event.getX() - prev.x, event.getY()
                            - prev.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        mMatrix.set(savedMatrix);
                        float tScale = newDist / dist;
                        mMatrix.postScale(tScale, tScale, mid.x, mid.y);
                    }
                }
                break;
        }
        mImageDetailIv.setImageMatrix(mMatrix);
        //checkView();
        return true;
    }

    /**
     * 限制最大最小缩放比例，自动居中
     */
    private void checkView() {
        float p[] = new float[9];
        mMatrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < mMinScaleR) {
                mMatrix.setScale(mMinScaleR, mMinScaleR);
            }
            if (p[0] > MAX_SCALE) {
                mMatrix.set(savedMatrix);
            }
        }
        center();
    }

    private void minZoom(){
        if(mBitmap != null) {
            mMinScaleR = Math.min(
                    (float) mDisplayMetrics.widthPixels / (float) mBitmap.getWidth(),
                    (float) mDisplayMetrics.heightPixels / (float) mBitmap.getHeight());
            if (mMinScaleR < 1.0) {
                mMatrix.postScale(mMinScaleR, mMinScaleR);
            }
        }
    }

    /**
     * 限制最大最小缩放比例，自动居中
     */
    private void CheckView() {
        float p[] = new float[9];
        mMatrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < mMinScaleR) {
                mMatrix.setScale(mMinScaleR, mMinScaleR);
            }
            if (p[0] > MAX_SCALE) {
                mMatrix.set(savedMatrix);
            }
        }
        center();
    }
    private void center() {
        center(true, true);
    }
    /**
     * 横向 纵向 居中
     * @param horizontal
     * @param vertical
     */
    protected void center(boolean horizontal, boolean vertical) {

        Matrix m = new Matrix();
        m.set(mMatrix);
        RectF rect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
            int screenHeight = mDisplayMetrics.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = mImageDetailIv.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = mDisplayMetrics.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 两点距离
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * 两点的中点
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
