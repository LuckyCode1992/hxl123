package fuzik.com.myapplication.recycleview_demo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import fuzik.com.myapplication.R;
import fuzik.com.myapplication.recycleview_demo.vlayoutmanager.OnePlusNLayoutActivity;
import fuzik.com.myapplication.recycleview_demo.vlayoutmanager.VLayoutActivity;

public class VlayoutActivity extends ListActivity {

    private Class[] mActivities = new Class[]{
            VLayoutActivity.class,
            OnePlusNLayoutActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlayout);
        String[] mTitles = new String[]{
                VLayoutActivity.class.getSimpleName(),
                OnePlusNLayoutActivity.class.getSimpleName()
        };
       setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, mTitles));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(new Intent(this, mActivities[position]));
    }
}
