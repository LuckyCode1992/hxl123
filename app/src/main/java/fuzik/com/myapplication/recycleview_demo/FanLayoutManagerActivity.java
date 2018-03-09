package fuzik.com.myapplication.recycleview_demo;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fuzik.com.myapplication.R;
import fuzik.com.myapplication.recycleview_demo.fanlayoutmanager.FanLayoutManager;
import fuzik.com.myapplication.recycleview_demo.fanlayoutmanager.FanLayoutManagerSettings;
import fuzik.com.myapplication.recycleview_demo.fanlayoutmanager.adapter.SportCardsAdapter;
import fuzik.com.myapplication.recycleview_demo.fanlayoutmanager.adapter.SportCardsUtils;
import fuzik.com.myapplication.recycleview_demo.fanlayoutmanager.callbacks.FanChildDrawingOrderCallback;

public class FanLayoutManagerActivity extends AppCompatActivity {

    @BindView(R.id.logoBg)
    ImageView logoBg;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.rvCards)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_main)
    RelativeLayout fragmentMain;

    private FanLayoutManager mFanLayoutManager;
    private SportCardsAdapter mAdapter;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_layout_manager);
        unbinder = ButterKnife.bind(this);

        //构建FanLayoutManager
        FanLayoutManagerSettings fanLayoutManagerSettings = FanLayoutManagerSettings
                .newBuilder(this)
                .withFanRadius(true)
                .withAngleItemBounce(5)
                .withViewHeightDp(160)
                .withViewWidthDp(120)
                .build();
        mFanLayoutManager = new FanLayoutManager(this, fanLayoutManagerSettings);

        //recycleview 添加layoutmanger
        recyclerView.setLayoutManager(mFanLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //构建好adapter
        mAdapter = new SportCardsAdapter(this);
        mAdapter.addAll(SportCardsUtils.generateSportCards());
        mAdapter.setOnItemClickListener(new SportCardsAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, final View view) {
                if (mFanLayoutManager.getSelectedItemPosition() != itemPosition) {
                    mFanLayoutManager.switchItem(recyclerView, itemPosition);
                } else {
                    mFanLayoutManager.straightenSelectedItem(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                //onClick(view, mFanLayoutManager.getSelectedItemPosition());
                                onClick(mFanLayoutManager.getSelectedItemPosition());
                            } else {
                                onClick(mFanLayoutManager.getSelectedItemPosition());
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                }
            }
        });

        //设置adapter
        recyclerView.setAdapter(mAdapter);

        recyclerView.setChildDrawingOrderCallback(new FanChildDrawingOrderCallback(mFanLayoutManager));

        (findViewById(R.id.logo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFanLayoutManager.collapseViews();
            }
        });

    }

    public void onClick(int pos) {
        Toast.makeText(FanLayoutManagerActivity.this,"点击了"+pos,Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
