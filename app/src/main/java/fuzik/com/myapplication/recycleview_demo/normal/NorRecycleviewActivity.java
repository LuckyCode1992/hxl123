package fuzik.com.myapplication.recycleview_demo.normal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fuzik.com.myapplication.R;

public class NorRecycleviewActivity extends AppCompatActivity {

    @BindView(R.id.recycleview_nor)
    RecyclerView recycleviewNor;
    Unbinder unbinder;
    NorAdapter norAdapter;
    List<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nor_recycleview);
        unbinder = ButterKnife.bind(this);

        for (int i = 1; i < 100; i++) {
            list.add(i);
        }

        //设置LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); //加上这句 就可以设置方向
        recycleviewNor.setLayoutManager(layoutManager);

        //添加Android自带的分割线
        recycleviewNor.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //添加数据
        norAdapter = new NorAdapter(list,this);
        //加入adapter
        recycleviewNor.setAdapter(norAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
