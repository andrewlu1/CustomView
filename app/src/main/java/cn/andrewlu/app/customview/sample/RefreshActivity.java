package cn.andrewlu.app.customview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.sql.Ref;

import cn.andrewlu.app.customview.FlipHelper;
import cn.andrewlu.app.customview.R;
import cn.andrewlu.app.customview.SuperRefreshLayout;

public class RefreshActivity extends FragmentActivity {
    @ViewInject(R.id.container)
    private ListView listView;

    @ViewInject(R.id.refreshLayout)
    private SuperRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_refresh);
        ViewUtils.inject(this);
        FlipHelper.inject(this);

        String[] data = new String[200];
        for (int i = 0; i < data.length; i++) {
            data[i] = System.currentTimeMillis() + "";
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                android.widget.Toast.makeText(RefreshActivity.this, "x:" + position, android.widget.Toast.LENGTH_SHORT)
                        .show();
                Intent i = new Intent(RefreshActivity.this, FlipActivity.class);
                startActivity(i);
            }
        });
        refreshLayout.setOnRefreshListener(new SuperRefreshLayout.OnRefreshListener() {

            @Override
            public void OnRefresh(SuperRefreshLayout refresher, boolean up) {
                refresher.finishRefresh();
                Toast.makeText(RefreshActivity.this, up ? "上拉加载完毕" : "下拉刷新完毕", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
