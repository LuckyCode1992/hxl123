package fuzik.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fuzik.com.myapplication.recycleview_demo.CarouselLayoutManagerActivity;
import fuzik.com.myapplication.recycleview_demo.ChipsLayoutManagerActivity;
import fuzik.com.myapplication.recycleview_demo.FanLayoutManagerActivity;
import fuzik.com.myapplication.recycleview_demo.HiveLayoutManagerActivity;
import fuzik.com.myapplication.recycleview_demo.LondonEyeLayoutManagerActivity;
import fuzik.com.myapplication.recycleview_demo.normal.NorRecycleviewActivity;
import fuzik.com.myapplication.recycleview_demo.VlayoutActivity;

public class RecycleViewActivity extends AppCompatActivity {
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_nor, R.id.btn_FanLayoutManager, R.id.btn_CarouselLayoutManager, R.id.ChipsLayoutManager, R.id.btn_HiveLayoutManager, R.id.btn_vlayout, R.id.btn_LondonEyeLayoutManager})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_nor:
                skip(NorRecycleviewActivity.class);
                break;
            case R.id.btn_FanLayoutManager:
                skip(FanLayoutManagerActivity.class);
                break;
            case R.id.btn_CarouselLayoutManager:
                skip(CarouselLayoutManagerActivity.class);
                break;
            case R.id.ChipsLayoutManager:
                skip(ChipsLayoutManagerActivity.class);
                break;
            case R.id.btn_HiveLayoutManager:
                skip(HiveLayoutManagerActivity.class);
                break;
            case R.id.btn_vlayout:
                skip(VlayoutActivity.class);
                break;
            case R.id.btn_LondonEyeLayoutManager:
                skip(LondonEyeLayoutManagerActivity.class);
                break;
        }
    }

    private void skip(Class cls) {
        startActivity(new Intent(RecycleViewActivity.this, cls));
    }
}
