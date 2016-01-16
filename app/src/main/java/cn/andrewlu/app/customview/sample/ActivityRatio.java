package cn.andrewlu.app.customview.sample;

import android.app.Activity;
import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;

import cn.andrewlu.app.customview.FlipHelper;
import cn.andrewlu.app.customview.R;

/**
 * Created by andrewlu on 16-1-16.
 */
public class ActivityRatio extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratio_layout);
        FlipHelper.inject(this);
    }
}
