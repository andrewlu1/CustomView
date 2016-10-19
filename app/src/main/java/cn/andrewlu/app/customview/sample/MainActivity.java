package cn.andrewlu.app.customview.sample;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.andrewlu.app.customview.R;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(android.os.Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.refreshBtn, R.id.flipBtn, R.id.customView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refreshBtn: {
                Intent i = new Intent(this, RefreshActivity.class);
                startActivity(i);
                break;
            }
            case R.id.flipBtn: {
                Intent i = new Intent(this, FlipActivity.class);
                startActivity(i);
                break;
            }
            case R.id.customView: {
                Intent i = new Intent(this, ActivityRatio.class);
                startActivity(i);
                break;
            }
        }
    }
}
