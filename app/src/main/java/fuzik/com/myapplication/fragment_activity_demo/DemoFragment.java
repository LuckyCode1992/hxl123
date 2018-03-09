package fuzik.com.myapplication.fragment_activity_demo;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fuzik.com.myapplication.R;
import rx.functions.Action1;


public class DemoFragment extends Fragment {
    static final String TAG = "DemoFragment";
    TextView tv;
    Button btn,btndel;
    EditText et;

    Action1 action;

    @SuppressLint("ValidFragment")
    public DemoFragment(Action1 action) {
        this.action = action;
    }

    public DemoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            Log.d(TAG, "onCreateView");
            view = inflater.inflate(R.layout.fragment_demo, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        tv = view.findViewById(R.id.tv_show_change);
        btn = view.findViewById(R.id.btn_show_change);
        et = view.findViewById(R.id.et_show_change);
        btndel = view.findViewById(R.id.btn_show_del);
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.call("回调");
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable text = et.getText();
                if (text != null) {
                    tv.setText(text.toString());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged:" + hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "isVisibleToUser:" + isVisibleToUser);
    }


}
