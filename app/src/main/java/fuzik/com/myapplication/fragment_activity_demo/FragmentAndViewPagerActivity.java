package fuzik.com.myapplication.fragment_activity_demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import fuzik.com.myapplication.R;
import rx.functions.Action1;

public class FragmentAndViewPagerActivity extends AppCompatActivity {
    ViewPager viewPager;
    RadioGroup radioGroup;
    FragmentAndViewpagerAdapter adapter;
    DemoFragment fragment1, fragment2, fragment3;
    List<Fragment> fragments = new ArrayList<>();
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_and_view_pager);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        radioGroup = (RadioGroup) findViewById(R.id.rg_viewpager_main);


        /**以下部分，可以用for循环操作，但是，为了看清楚，就分开写了*/
        //创建fragment 放入集合
        fragment1 = new DemoFragment(new Action1() {
            @Override
            public void call(Object o) {
                fragments.remove(viewPager.getCurrentItem());//移除当前fragemnt
                adapter.notifyDataSetChanged();

            }
        });
        fragments.add(fragment1);
        fragment2 = new DemoFragment(new Action1() {
            @Override
            public void call(Object o) {
                fragments.remove(viewPager.getCurrentItem());//移除当前fragemnt
                adapter.notifyDataSetChanged();
            }
        });
        fragments.add(fragment2);
        fragment3 = new DemoFragment(new Action1() {
            @Override
            public void call(Object o) {
                fragments.remove(viewPager.getCurrentItem());//移除当前fragemnt
                adapter.notifyDataSetChanged();
            }
        });
        fragments.add(fragment3);
        fm = getSupportFragmentManager();
        adapter = new FragmentAndViewpagerAdapter(fm, fragments);
        viewPager.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_viewpager1:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_viewpager2:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_viewpager3:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("FragmentAndViewpager", "onPageScrolled:" + position + "**" + positionOffset + "**" + positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("FragmentAndViewpager", "onPageSelected:" + position);
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rb_viewpager1);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_viewpager2);
                        break;
                    case 2:
                        radioGroup.check(R.id.rb_viewpager3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("FragmentAndViewpager", "onPageScrollStateChanged:" + state);
            }
        });
    }
}
