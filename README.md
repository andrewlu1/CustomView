# FlipHelper
包含一个实现手指向右滑动关闭Activity的组件.
使用方式:
public class FlipActivity extends Activity {
    @Override
    protected void onCreate(android.os.Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_flip);
        FlipHelper.inject(this);//一定要在setContentView之后调用.否则没有任何效果.
    }
}

#缺陷:
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

#SuperRefreshLayout
包含一个下拉刷新/上拉加载更多的容器控件,比之前一个版本做了点击优化.可在本人git上找上一个版本进行对比.
本控件可以包含基本所有控件类型.比如:ScrollView,ListView,RecyclerView,XXXLayout,XXXView,XXXButton.等等.
典型使用方式:

    <cn.andrewlu.app.customview.SuperRefreshLayout
        android:id="@+id/refreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <ListView
            android:id="@+id/container"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </cn.andrewlu.app.customview.SuperRefreshLayout>

```java
    refreshLayout.setOnRefreshListener(new SuperRefreshLayout.OnRefreshListener() {

            @Override
            public void OnRefresh(SuperRefreshLayout refresher, boolean up) {
                refresher.finishRefresh();
                Toast.makeText(RefreshActivity.this, up ? "上拉加载完毕" : "下拉刷新完毕", Toast.LENGTH_SHORT).show();
            }
        });
```

#RatioLinearLayout & RatioFrameLayout
一个简单的可以在XML中设定控件宽高比的布局.分别继承自LinearLayout,FrameLayout.具体表现为当设定ratio_w_h="0.75"时,容器的高度= w/0.75f; 
典型的应用场景是:
1. 九宫格图片显示场景,希望不管几行几列图片,每张图片的宽和高都相等,即正方形.
2. 视频播放器场景,当希望视频播放窗口的宽高比为4:3时,可以简单的在外面套上一个RatioFrameLayout然后xml中设定宽高比的值为1.33


#New 2016-04-17
#新增控件 BlurringView 用于背景模糊的控件实现.
原项目地址:https://github.com/500px/500px-android-blur

#新增控件:RoundView 用于圆角边框的布局,继承自FrameLayout. 可以实现任意布局的圆角化.
与CardView 不同的是,它兼容所有版本的手机, 而CardView则会在4.x以下为内容留出一个边框--有时候无法符合我们的需求.
典型应用: 如果想要圆角图片,则只需在ImageView外边套上RoundView布局.即可正确处理圆角绘制,而不是去修改ImageView的实现.

大家工作中,遇到还需要什么样的控件可以发问题讨论,将尽量给大家实现丰富多彩的自定义控件.
