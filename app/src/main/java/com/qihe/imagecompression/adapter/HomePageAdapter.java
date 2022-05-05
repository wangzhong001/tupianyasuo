package com.qihe.imagecompression.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lipei on 2018/3/19.
 */

public class HomePageAdapter extends FragmentPagerAdapter {
    private List<Fragment> mListFragment=null;

    public HomePageAdapter(FragmentManager fm, List<Fragment> mListFragment) {
        super(fm);
        this.mListFragment=mListFragment;

    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }


}
