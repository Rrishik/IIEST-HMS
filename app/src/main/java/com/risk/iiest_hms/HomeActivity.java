package com.risk.iiest_hms;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.risk.iiest_hms.Fragments.DashboardFragment;
import com.risk.iiest_hms.Fragments.HistoryFragment;
import com.risk.iiest_hms.Fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private Fragment fragment;

    private BottomNavigationView bottomNavigationView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Bundle extras = getIntent().getExtras();

        fragment = new DashboardFragment();
        fragment.setArguments(extras);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                fragment = null;
                switch (item.getItemId()) {
                    case R.id.bb_dashboard:
                        fragment = new DashboardFragment();
                        fragment.setArguments(extras);
                        break;

                    case R.id.bb_profile:
                        fragment = new ProfileFragment();
                        fragment.setArguments(extras);
                        break;

                    case R.id.bb_history:
                        fragment = HistoryFragment.getInstance();
                        break;
                }

                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }
}
