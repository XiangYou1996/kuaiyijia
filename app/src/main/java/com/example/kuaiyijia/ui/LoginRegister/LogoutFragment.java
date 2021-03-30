package com.example.kuaiyijia.ui.LoginRegister;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuaiyijia.R;
import com.example.kuaiyijia.ui.MainActivity;

public class LogoutFragment extends Fragment {

    //退出的广播频段
    private static final String EXITACTION = "action2exit";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().sendBroadcast(new Intent(EXITACTION));
    }
}