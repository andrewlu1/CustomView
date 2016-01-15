# CustomView
包含一个实现手指向右滑动关闭Activity的组件.
使用方式:
public class FlipActivity extends Activity {

    @Override
    protected void onCreate(android.os.Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_flip);
        ViewUtils.inject(this);
        FlipHelper.inject(this);//一定要在setContentView之后调用.否则没有任何效果.
    }
}

缺陷:
因为默认的Activity的背景是不透明样式,想在代码中实现背景透明似乎没效果,只能在Manifest.xml中手动设置Activity的样式透明.
样式如下:
    <style name="myTransparent" parent="AppBaseTheme">
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowAnimationStyle">@android:style/Animation.Translucent</item>
    </style>
然后在Manifest.xml中添加样式:
      <activity
            android:name=".sample.FlexActivity"
            android:theme="@style/myTransparent" />


另包含一个下拉刷新/上拉加载更多的容器控件,比之前一个版本做了点击优化.可在本人git上找上一个版本进行对比.
本控件可以包含基本所有控件类型.比如:ScrollView,ListView,RecyclerView,XXXLayout,XXXView,XXXButton.等等.
有兴趣可以留言讨论.
