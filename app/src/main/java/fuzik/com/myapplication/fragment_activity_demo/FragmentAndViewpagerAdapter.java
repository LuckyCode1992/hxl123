package fuzik.com.myapplication.fragment_activity_demo;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

public class FragmentAndViewpagerAdapter extends FragmentStatePagerAdapter {
    private FragmentManager mfragmentManager;
    private List<Fragment> mlist;
    public FragmentAndViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAndViewpagerAdapter(FragmentManager fm ,List<Fragment> mlist) {
        super(fm);
        this.mfragmentManager = fm;
        this.mlist = mlist;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("FragmentAndViewpager","getItem:"+mlist.get(position));
        return mlist.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }
    @Override
    public int getCount() {
        Log.d("FragmentAndViewpager","getCount:"+mlist.size());
        return mlist.size();
    }

    @Override
    public void notifyDataSetChanged() {
        Log.d("FragmentAndViewpager","notifyDataSetChanged:");
        super.notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("FragmentAndViewpager","instantiateItem:"+container+"**"+position);
        return super.instantiateItem(container, position);
    }
}
