package com.qihe.imagecompression.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lipei on 2018/3/19.
 */

public class MainPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> mListFragment=null;
    private List<String> tableTiles=null;
    public MainPageAdapter(FragmentManager fm, List<Fragment> mListFragment, List<String> tableTiles) {
        super(fm);
        this.mListFragment=mListFragment;
        this.tableTiles=tableTiles;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tableTiles.get(position);
    }
}
