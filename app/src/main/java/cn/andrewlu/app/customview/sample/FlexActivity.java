package cn.andrewlu.app.customview.sample;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.andrewlu.app.customview.FlipHelper;
import cn.andrewlu.app.customview.R;

public class FlexActivity extends Activity {

    @Override
    protected void onCreate(android.os.Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_flexheader);
        ViewUtils.inject(this);
        FlipHelper.inject(this);
    }

}
