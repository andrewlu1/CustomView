package cn.andrewlu.app.customview.sample;

import android.app.Activity;

import com.lidroid.xutils.ViewUtils;

import cn.andrewlu.app.customview.FlipHelper;
import cn.andrewlu.app.customview.R;

public class FlipActivity extends Activity {

    @Override
    protected void onCreate(android.os.Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_flip);
        ViewUtils.inject(this);
        FlipHelper.inject(this);
        
    }
}
