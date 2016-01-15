package cn.andrewlu.app.customview.sample;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
