package com.example.kuaiyijia.UI.orderManage;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {


    private  List<String> TAB_TITLES = new ArrayList<>();
//    private  List<Fragment> subFragments = new ArrayList<>();

    public SectionsPagerAdapter( FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment =  new FinisedOrderFragment();;
        switch (position){
            case 0:
                fragment = new GoingOrderFragment();
                break;
            case 1 :
                fragment = new FinisedOrderFragment();
                break;
            case 2:
                fragment = new DaishoukuanOrderFragment();
                break;
            case 3:
                fragment = new ScanLackFragment();
                break;
            case 4:
                fragment = new RetentionOrderFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES.get(position);
    }

    @Override
    public int getCount() {
        // Show  total pages.
        return 5;
    }

    public void SetSubFragments( List<String> subTitles){
        this.TAB_TITLES = subTitles;
    }
}