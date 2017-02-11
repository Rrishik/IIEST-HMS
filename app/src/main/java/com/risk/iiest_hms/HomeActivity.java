package com.risk.iiest_hms;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.risk.iiest_hms.Fragments.DashboardFragment;
import com.risk.iiest_hms.Fragments.HistoryFragment;
import com.risk.iiest_hms.Fragments.ProfileFragment;
import com.risk.iiest_hms.Helper.Constants;

public class HomeActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    private String login_page_source;
    private BottomNavigationView bottomNavigationView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Log.e(TAG, "Page Source is empty!");
        } else {
            login_page_source = extras.getString(Constants.Intent.LOGIN_PAGE_SOURCE);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new DashboardFragment());
        fragmentTransaction.commit();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.bb_dashboard:
                        fragment = new DashboardFragment();
                        break;

                    case R.id.bb_profile:
                        fragment = new ProfileFragment();
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
