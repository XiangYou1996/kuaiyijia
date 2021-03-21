package com.example.kuaiyijia.ui.orderManage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.kuaiyijia.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrderManageFragment extends Fragment {
    private List<Fragment> subFragments = new ArrayList<>();
    private List<String> msubTitle = new ArrayList<>();
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_ordermanage,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        int tag = 0;
        msubTitle.add("已完成");
        msubTitle.add("进行中");
        msubTitle.add("代收款");
        msubTitle.add("扫描不全");
        msubTitle.add("滞留订单");
        /*
        if (tag ==0) {
            subFragments.add(new FinisedOrderFragment());
            tag =1;
        }
        if (tag ==1){
            subFragments.add(new GoingOrderFragment());
            tag=2;
        }
        if (tag==2){
            subFragments.add(new DaishoukuanOrderFragment());
            tag = 3 ;
        }
        if (tag==3){
            subFragments.add(new ScanLackFragment());
            tag =4;}
        if (tag == 4) {
            subFragments.add(new RetentionOrderFragment());
        }*/
    }

    private void initView() {
        sectionsPagerAdapter = new SectionsPagerAdapter( getActivity().getSupportFragmentManager());
        sectionsPagerAdapter.SetSubFragments(msubTitle);
        viewPager = getActivity().findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = getActivity().findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}
