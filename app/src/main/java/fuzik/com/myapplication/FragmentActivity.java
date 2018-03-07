package fuzik.com.myapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.OnClick;
import fuzik.com.myapplication.fragment_activity_demo.BageFragmentActivity;
import fuzik.com.myapplication.fragment_activity_demo.ContainerFragmentActivity;
import fuzik.com.myapplication.fragment_activity_demo.CustomLayoutActivity;
import fuzik.com.myapplication.fragment_activity_demo.CustomNavigatorActivity;
import fuzik.com.myapplication.fragment_activity_demo.DynamicActivity;
import fuzik.com.myapplication.fragment_activity_demo.FixedFragmentActivity;
import fuzik.com.myapplication.fragment_activity_demo.OnlyIndicatorActivity;
import fuzik.com.myapplication.fragment_activity_demo.ScollableFragmentActivity;

public class FragmentActivity extends UIBaseActivity {
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_fragment);
    }

    @OnClick({R.id.btn_scrollable_tab, R.id.btn_fixed_tab, R.id.btn_dynamic_tab, R.id.btn_no_tab_only_indicator, R.id.btn_tab_with_badge_view, R.id.btn_work_with_fragment_container, R.id.btn_load_custom_layout, R.id.btn_custom_navigator})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scrollable_tab:
                skip(ScollableFragmentActivity.class);
                break;
            case R.id.btn_fixed_tab:
                skip(FixedFragmentActivity.class);
                break;
            case R.id.btn_dynamic_tab:
                skip(DynamicActivity.class);
                break;
            case R.id.btn_no_tab_only_indicator:
                skip(OnlyIndicatorActivity.class);
                break;
            case R.id.btn_tab_with_badge_view:
                skip(BageFragmentActivity.class);
                break;
            case R.id.btn_work_with_fragment_container:
                skip(ContainerFragmentActivity.class);
                break;
            case R.id.btn_load_custom_layout:
                skip(CustomLayoutActivity.class);
                break;
            case R.id.btn_custom_navigator:
                skip(CustomNavigatorActivity.class);
                break;
        }
    }

    private void skip(Class cls) {
        startActivity(new Intent(FragmentActivity.this, cls));
    }
}
