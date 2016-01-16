package cn.andrewlu.app.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by andrewlu on 16-1-16.
 */
public class RatioFrameLayout extends FrameLayout {
    private float ratio_w_h = -1;
    private float ratio_h_w = -1;

    public RatioFrameLayout(Context context) {
        super(context);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context c, AttributeSet as) {
        TypedArray array = c.obtainStyledAttributes(as, R.styleable.RatioLayout);
        ratio_w_h = array.getFloat(R.styleable.RatioLayout_ratio_w_h, -1);
        ratio_h_w = array.getFloat(R.styleable.RatioLayout_ratio_h_w, -1);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wS = MeasureSpec.getSize(widthMeasureSpec);
        int hS = MeasureSpec.getSize(heightMeasureSpec);
        int wM = MeasureSpec.getMode(widthMeasureSpec);
        int hM = MeasureSpec.getMode(heightMeasureSpec);

        if (ratio_w_h > 0) {//优先宽高比. w/s = ratio_w_h
            hS = (int) (wS / ratio_w_h);
        } else if (ratio_h_w > 0) {//如果没有设置宽高比,才检测高宽比.
            wS = (int) (hS / ratio_h_w);
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(wS, wM), MeasureSpec.makeMeasureSpec(hS, hM));

    }


}
