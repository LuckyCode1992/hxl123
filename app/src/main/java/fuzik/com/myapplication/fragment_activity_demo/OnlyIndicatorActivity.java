package fuzik.com.myapplication.fragment_activity_demo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import fuzik.com.myapplication.R;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.MagicIndicator;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.ViewPagerHelper;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.buildins.UIUtil;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.buildins.commonnavigator.CommonNavigator;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.buildins.commonnavigator.abs.IPagerIndicator;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.buildins.commonnavigator.abs.IPagerTitleView;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.buildins.commonnavigator.indicators.LinePagerIndicator;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import fuzik.com.myapplication.fragment_activity_demo.fragment_formwork.buildins.commonnavigator.titles.DummyPagerTitleView;

public class OnlyIndicatorActivity extends AppCompatActivity {

    private static final String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "NOUGAT", "DONUT"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mDataList);

    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_indicator);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mExamplePagerAdapter);

        initMagicIndicator1();
        initMagicIndicator2();
    }

    private void initMagicIndicator1() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator1);
        magicIndicator.setBackgroundColor(Color.LTGRAY);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                return new DummyPagerTitleView(context);
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float lineHeight = context.getResources().getDimension(R.dimen.small_navigator_height);
                indicator.setLineHeight(lineHeight);
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initMagicIndicator2() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator2);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                return new DummyPagerTitleView(context);
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                TriangularPagerIndicator indicator = new TriangularPagerIndicator(context);
                indicator.setReverse(true);
                float smallNavigatorHeight = context.getResources().getDimension(R.dimen.small_navigator_height);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setTriangleHeight((int) smallNavigatorHeight);
                indicator.setLineColor(Color.parseColor("#e94220"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
}
