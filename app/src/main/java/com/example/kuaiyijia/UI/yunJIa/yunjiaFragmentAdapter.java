package com.example.kuaiyijia.UI.yunJIa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.kuaiyijia.UI.yunJIa.all.allFragment;
import com.example.kuaiyijia.UI.yunJIa.canceled.canceledFragment;
import com.example.kuaiyijia.UI.yunJIa.filter.filterFragment;
import com.example.kuaiyijia.UI.yunJIa.finished.finishedFragment;
import com.example.kuaiyijia.UI.yunJIa.ongoing.ongoingFragment;

import java.util.ArrayList;
import java.util.List;

public class yunjiaFragmentAdapter extends FragmentStatePagerAdapter {
    private List<String> TAB_TITLES = new ArrayList<>();
//    private  List<Fragment> subFragments = new ArrayList<>();

    public yunjiaFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment =  new ongoingFragment();;
        switch (position){
            case 0:
                fragment = new ongoingFragment();
                break;
            case 1 :
                fragment = new finishedFragment();
                break;
            case 2:
                fragment = new canceledFragment();
                break;
            case 3:
                fragment = new allFragment();
                break;
            case 4:
                fragment = new filterFragment();
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
