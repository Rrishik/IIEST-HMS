package com.risk.iiest_hms.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.risk.iiest_hms.Helper.Constants;
import com.risk.iiest_hms.Helper.PageParser;
import com.risk.iiest_hms.R;

public class DashboardFragment extends Fragment {

    private final String loginUrl = "http://iiesthostel.iiests.ac.in/students/login_students";

    private final String TAG = getClass().getSimpleName();

    private TextView dash, openInWeb;
    private String dashboard_source;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dash = (TextView) getView().findViewById(R.id.tv_dashboard);
        openInWeb = (TextView) getView().findViewById(R.id.goto_website);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("HOSTEL MANAGEMENT SYSTEM");

        final Bundle extras = this.getArguments();
        if (extras != null) {
            dashboard_source = extras.getString(Constants.Intent.LOGIN_PAGE_SOURCE);
        }

        PageParser p = new PageParser(dashboard_source);
        if (!p.checkDues()) {
            dash.setText("You have no pending Dues or Bills! :)\nCome back later..");
        } else
            dash.setText("You have pending Dues! \nPlease pay your dues at the earliest to avoid fines.");

        openInWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(loginUrl));
                startActivity(i);
            }
        });
    }
}
