package fuzik.com.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class MainActivity extends UIBaseActivity {

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_main);
    }
}
