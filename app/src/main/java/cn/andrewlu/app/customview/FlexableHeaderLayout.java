package cn.andrewlu.app.customview;

import android.app.usage.UsageEvents.Event;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 控件功能描述: 一个带可自动伸缩头部的布局. 当布局中的子控件向下拉时,优先显示出头部View. 当头部完全显示时,才允许子控件滚动事件.
 * 当布局中的子控件向上推时,优先隐藏头部View. 当头部完全隐藏时,才允许子控件滚动事件. 目前的方案主要有: 1.
 * 用LinearLayout+ScrollView. 但只有将控件拖动到顶部才能显示出头部View.
 * 而我们想要的时,一旦想要滚动子控件,就要优先判断头部是否显示/隐藏,而不是优先让其他控件进行滚动.
 *
 * @author andrewlu
 */

public class FlexableHeaderLayout extends FrameLayout {
    private View mHeaderView;

    public FlexableHeaderLayout(Context context) {
        super(context);
        init(context, null);
    }

    public FlexableHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlexableHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setHeaderView(View header) {
        if (mHeaderView != null && mHeaderView.getParent() != null) {
            ((ViewGroup) mHeaderView.getParent()).removeView(mHeaderView);
        }
        this.mHeaderView = header;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        measureHeader();
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlexableHeaderLayout);
            int headLayout = array.getResourceId(R.styleable.FlexableHeaderLayout_header, 0);
            if (headLayout != 0) {
                mHeaderView = LayoutInflater.from(context).inflate(headLayout, this, false);
                addView(mHeaderView);
            }
            array.recycle();
        }
    }

    private void measureHeader() {
        if (mHeaderView == null) return;
        LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.TOP;
        p.topMargin = -mHeaderView.getHeight();
        mHeaderView.setLayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureHeader();
    }

    private PointF mDowntPoint = new PointF();

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mDowntPoint.y = ev.getRawY();
                mDowntPoint.x = ev.getRawX();
                return false;
            }

            case MotionEvent.ACTION_MOVE: {
                return willIntercept(ev.getRawX() - mDowntPoint.x, ev.getRawY() - mDowntPoint.y);
            }
            case MotionEvent.ACTION_UP:
                // 返回true,子控件将收不到点击事件,false,子控件可以确认点击.
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                return true;
            }
            case MotionEvent.ACTION_UP: {

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (getScrollY() >= -300 && getScrollY() <= 0) {
                    int dy = (int) ((mDowntPoint.y - ev.getRawY()) * 0.4f) + getScrollY();
                    if (dy > 0) dy = 0;
                    else if (dy < -300) dy = -300;
                    scrollTo(0, dy);

                    mDowntPoint.y = ev.getRawY();
                }
                break;
            }
            default:
                break;
        }
        return false;
    }

    private boolean willIntercept(float dx, float dy) {
        Log.i("willIntercept", String.format("scrollY:%d,dy:%f", getScrollY(), dy));

        if (mHeaderView == null || mHeaderView.getHeight() <= 0) {
            return false;
        }

        // 如果横向距离大于纵向距离,则放行.
        if (Math.abs(dx) > Math.abs(dy)) {
            return false;
        }
        if (getScrollY() > -mHeaderView.getHeight() && getScrollY() < 0) {
            return true;
        }

        if ((getScrollY() <= -mHeaderView.getMeasuredHeight() && dy < 0) || (getScrollY() == 0 && dy > 0)) {
            return true;
        }

        return false;
    }
}
