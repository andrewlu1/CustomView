package cn.andrewlu.app.customview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * Created by andrewlu on 16-1-15.
 * 通过向activity的结构中注入一层能够拦截事件的View布局,从而可以让内容页跟着手指移动.
 */
public final class FlipHelper {
    public static void inject(Activity activity) {
        ViewGroup root = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        if (root == null || root.getChildAt(0) == null) {
            throw new RuntimeException("inject method must called after setContentView");
        }

        //向其中注入一层布局.
        HDraggableLayout container = new HDraggableLayout(activity);
        // container.setBackgroundColor(Color.argb(100, 0, 0, 0));
        container.setContentDescription("the injected view.");
        View child = root.getChildAt(0);
        child.setBackgroundColor(Color.WHITE);
        ViewGroup.LayoutParams p = child.getLayoutParams(); //new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.addView(container, p);
        root.removeView(child);
        container.addView(child);
    }
}


/**
 * Created by andrewlu on 16-1-15.
 * 可以在水平方向拖动的水平滚动布局.表现效果为:当手指向右滑动时,内容会根据手指位置向右一起滑动.
 * 以后可能会扩展向左滑动功能.现在不作讨论.
 * 只能通过new创建出来.因此只实现一个构造函数.
 */
class HDraggableLayout extends FrameLayout {
    private SimpleGestureListener onGestureListener = new SimpleGestureListener();

    public HDraggableLayout(Context context) {
        super(context);
        View shadowView = new ShadowView(context);
        LayoutParams p = new LayoutParams(40, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.LEFT | Gravity.TOP;
        p.leftMargin = -40;
        addView(shadowView, p);
    }

    //实现定向/定量的滚动事件拦截.
    private PointF mTouchPoint = new PointF();
    private PointF mTouchPointDist = new PointF();
    private final static int MIN_SLOP = 24;
    private long eventTime = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mTouchPoint.x = ev.getRawX();
                mTouchPoint.y = ev.getRawY();
                eventTime = ev.getEventTime();
                onGestureListener.onDown(ev);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mTouchPointDist.x = ev.getRawX() - mTouchPoint.x;
                mTouchPointDist.y = ev.getRawY() - mTouchPoint.y;

                //只负责起始时的拦截.拦截后,事件就完全交给touch来处理了.
                if (Math.abs(mTouchPointDist.y) > Math.abs(mTouchPointDist.x)) break;
                if (mTouchPointDist.x < MIN_SLOP) break;
                if (ev.getRawX() > 100) break;//手指在左边100以内滑动触发.
                Log.i("onInterceptTouchEvent", String.format("dx:%f,dy:%f", mTouchPointDist.x, mTouchPointDist.y));
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                mTouchPointDist.x = ev.getRawX() - mTouchPoint.x;
                mTouchPointDist.y = ev.getRawY() - mTouchPoint.y;
                //负责滑动距离的计算.
                if (getScrollX() - mTouchPointDist.x > 0) {
                    mTouchPointDist.x = getScrollX();
                }
                mTouchPoint.x = ev.getRawX();
                mTouchPoint.y = ev.getRawY();
                onGestureListener.onScroll(mTouchPointDist.x, mTouchPointDist.y);
                onGestureListener.onFling(mTouchPointDist.x * 1000 / (ev.getEventTime() - eventTime), mTouchPointDist.y * 1000 / (ev.getEventTime() - eventTime));
                eventTime = ev.getEventTime();
                break;
            }
            case MotionEvent.ACTION_UP: {
                onGestureListener.onUp();
                break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private ValueAnimator backAnim, finishAnim;

    //回滚
    private void scrollBack() {
        backAnim = ObjectAnimator.ofInt(this, "scrollX", getScrollX(), 0).setDuration(200);
        backAnim.start();
    }

    //滚动到结束.
    private void scrollFinish() {
        finishAnim = ObjectAnimator.ofInt(this, "scrollX", getScrollX(), -getWidth()).setDuration(200);
        finishAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Context c = getContext();
                if (c instanceof Activity) {
                    ((Activity) c).onBackPressed();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        finishAnim.start();
    }

    private class SimpleGestureListener {
        public boolean onDown(MotionEvent ev) {

            return false;
        }

        public boolean onScroll(float distanceX, float distanceY) {
            scrollBy(-(int) distanceX, 0);
            return true;
        }

        public boolean onFling(float velocityX, float velocityY) {
            Log.i("onFling", String.format("vX:%f,vY:%f", velocityX, velocityY));
            return false;
        }

        public void onUp() {
            Log.i("onUp", "=====================");
            if (Math.abs(getScrollX()) < getWidth() / 2) {
                scrollBack();
            } else {
                scrollFinish();
            }
        }
    }
}

//实现一个带阴影的控件.阴影呈现在左边.
class ShadowView extends View {
    private Paint shadowPaint = new Paint();

    public ShadowView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Shader mShader = new LinearGradient(getWidth(), 0, 0, 0,
                new int[]{Color.argb(50, 100, 100, 100), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
        shadowPaint.setShader(mShader);
        canvas.drawRect(0, 0, getWidth(), getHeight(), shadowPaint);
    }
}
