package fuzik.com.myapplication.recycleview_demo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

import fuzik.com.myapplication.R;
import fuzik.com.myapplication.recycleview_demo.carousellayoutmanager.CarouselLayoutManager;
import fuzik.com.myapplication.recycleview_demo.carousellayoutmanager.CarouselZoomPostLayoutListener;
import fuzik.com.myapplication.recycleview_demo.carousellayoutmanager.CenterScrollListener;
import fuzik.com.myapplication.recycleview_demo.carousellayoutmanager.DefaultChildSelectionListener;

public class CarouselLayoutManagerActivity extends AppCompatActivity {
    TestAdapter adapter;
    RecyclerView list_vertical, list_horizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel_layout_manager);
        list_vertical = findViewById(R.id.list_vertical);
        list_horizontal = findViewById(R.id.list_horizontal);

        adapter = new TestAdapter(this);

        // create layout manager with needed params: vertical, cycle
        initRecyclerView(list_horizontal, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), adapter);
        initRecyclerView(list_vertical, new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true), adapter);
    }

    private void initRecyclerView(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, final TestAdapter adapter) {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(2);

        recyclerView.setLayoutManager(layoutManager);
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true);
        // sample adapter with random data
        recyclerView.setAdapter(adapter);
        // enable center post scrolling
        recyclerView.addOnScrollListener(new CenterScrollListener());
        // enable center post touching on item and item click listener
        DefaultChildSelectionListener.initCenterItemListener(new DefaultChildSelectionListener.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClicked(@NonNull final RecyclerView recyclerView, @NonNull final CarouselLayoutManager carouselLayoutManager, @NonNull final View v) {
                final int position = recyclerView.getChildLayoutPosition(v);
                final String msg = String.format(Locale.US, "Item %1$d was clicked", position);
                Toast.makeText(CarouselLayoutManagerActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }, recyclerView, layoutManager);

        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {

            @Override
            public void onCenterItemChanged(final int adapterPosition) {
                if (CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
                    final int value = adapter.mPosition[adapterPosition];
/*
                    adapter.mPosition[adapterPosition] = (value % 10) + (value / 10 + 1) * 10;
                    adapter.notifyItemChanged(adapterPosition);
*/
                }
            }
        });
    }


    class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {
        Context context;
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        private final Random mRandom = new Random();
        private final int[] mColors;
        private final int[] mPosition;
        private int mItemsCount = 10;

        public TestAdapter(Context context) {
            this.context = context;
            mColors = new int[mItemsCount];
            mPosition = new int[mItemsCount];
            for (int i = 0; mItemsCount > i; ++i) {
                //noinspection MagicNumber
                mColors[i] = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
                mPosition[i] = i;
            }
        }

        @Override
        public TestViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.carousellayout_item_view, parent,false);
//            view = LayoutInflater.from(context).inflate(R.layout.carousellayout_item_view, null);//这种方法，加载item的布局，宽高会失效
            return new TestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TestViewHolder holder, final int position) {
            holder.cItem1.setText(String.valueOf(mPosition[position]));
            holder.cItem2.setText(String.valueOf(mPosition[position]));
            holder.itemView.setBackgroundColor(mColors[position]);
        }

        @Override
        public int getItemCount() {
            return mItemsCount;
        }
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        public TextView cItem1, cItem2;

        TestViewHolder(View view) {
            super(view);
            cItem1 = view.findViewById(R.id.c_item_1);
            cItem2 = view.findViewById(R.id.c_item_2);
        }
    }
}
