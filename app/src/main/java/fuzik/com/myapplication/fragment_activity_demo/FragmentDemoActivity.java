package fuzik.com.myapplication.fragment_activity_demo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import fuzik.com.myapplication.R;

public class FragmentDemoActivity extends AppCompatActivity {
    static final String TAG = "FragmentDemoActivity";
    RadioGroup radioGroup;
    DemoFragment fragment1, fragment2, fragment3;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_demo);
        radioGroup = (RadioGroup) findViewById(R.id.rg_main);

       // 初始化fragmen管理器
        fm = getSupportFragmentManager();
        if (fragment1 == null)
            fragment1 = new DemoFragment();
        replace(fragment1);
        //初始化监听
        initListener();


    }

    private void initListener() {
        radioGroup.check(R.id.radio1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d(TAG, "radioGroup.getCheckedRadioButtonId():" + radioGroup.getCheckedRadioButtonId() + "  i:" + i);
                switch (i) {
                    case R.id.radio1:
                        if (fragment1 == null)
                            fragment1 = new DemoFragment();
                        replace(fragment1);
                        break;
                    case R.id.radio2:
                        if (fragment2 == null)
                            fragment2 = new DemoFragment();
                        replace(fragment2);
                        break;
                    case R.id.radio3:
                        if (fragment3 == null)
                            fragment3= new DemoFragment();
                        replace(fragment3);
                        break;

                }
            }
        });


    }

    private void replace(DemoFragment fragment) {
        ft = fm.beginTransaction();
        ft.replace(R.id.fl_main_container, fragment);
        ft.commit();
    }

}
