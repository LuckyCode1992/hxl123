package fuzik.com.myapplication.fragment_activity_demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import fuzik.com.myapplication.R;

public class FragmentDemoActivity extends FragmentActivity {

    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_fragment_demo);
        radioGroup = findViewById(R.id.rg_main);
        RadioButton radioButton = new RadioButton(this);
        ViewGroup.LayoutParams layoutParams = radioButton.getLayoutParams();
        layoutParams.width = 0;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;


    }
}
