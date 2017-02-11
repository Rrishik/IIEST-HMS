package com.risk.iiest_hms.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.risk.iiest_hms.R;

public class DashboardFragment extends Fragment {

    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        toolbar = (android.support.v7.widget.Toolbar) getView().findViewById(R.id.toolbar_dashboard);
//        toolbar.setTitle("HOSTEL MANAGEMENT SYSTEM");
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("HOSTEL MANAGEMENT SYSTEM");
    }
}
