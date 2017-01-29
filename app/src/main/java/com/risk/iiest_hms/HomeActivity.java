package com.risk.iiest_hms;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.risk.iiest_hms.adapter.HistoryAdapter;
import com.risk.iiest_hms.helper.Constants;
import com.risk.iiest_hms.helper.PageParser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class HomeActivity extends AppCompatActivity /*implements HistoryAdapter.RecyclerViewClickListener*/ {

    private String TAG = getClass().getSimpleName();

    private BottomBar bottomBar;

    private CardView cDashboardProfile;
    private TextView mDashBoardText;
    private ImageView iImage;
    private TextView mProfileText;

    private CardView cChangePasswd;
    private TextView mChangePassword;

    private CardView cLogout;
    private TextView mLogout;

    //private RecyclerView mHistoryList;

    private String login_page_source;
    private String ledger_page_source;
    private PageParser pLogin, pLedger;
    //private HistoryAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        cDashboardProfile = (CardView) findViewById(R.id.cv_db_p);
        mDashBoardText = (TextView) findViewById(R.id.tv_dashboard);

        iImage = (ImageView) findViewById(R.id.iv_image);
        mProfileText = (TextView) findViewById(R.id.tv_profile);

        cChangePasswd = (CardView) findViewById(R.id.cv_change_password);
        mChangePassword = (TextView) findViewById(R.id.tv_change_password);

        cLogout = (CardView) findViewById(R.id.cv_logout);
        mLogout = (TextView) findViewById(R.id.tv_logout);

        // mHistoryList = (RecyclerView) findViewById(R.id.rv_history);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //mHistoryList.setLayoutManager(linearLayoutManager);
        // mHistoryList.setHasFixedSize(true);

        //mAdapter = new HistoryAdapter(HomeActivity.this, this);
        //mHistoryList.setAdapter(mAdapter);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Log.e(TAG, "Page Source is empty!");
        } else {
            login_page_source = extras.getString(Constants.Intent.LOGIN_PAGE_SOURCE);
            ledger_page_source = extras.getString(Constants.Intent.LEDGER_PAGE_SOURCE);
        }

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                pLogin = new PageParser(HomeActivity.this, login_page_source);
                pLedger = new PageParser(HomeActivity.this, ledger_page_source);

                if (tabId == R.id.tab_dashboard) {
                    showDashboard();

                    if (pLogin.checkDues())
                        mDashBoardText.setText("Oops You have Pending Dues! \n Please pay your dues at the earliest!");
                    else
                        mDashBoardText.setText("You have no dues Pending!");
                }

                if (tabId == R.id.tab_profile) {
                    showProfile();
                    Log.d(TAG, "onTabSelected: " + pLedger.getUserName());
                    mProfileText.setText(pLedger.getUserName());
                }

                if (tabId == R.id.tab_history) {

                    //showHistory();
                }
            }
        });


    }

    private void showDashboard() {
        cDashboardProfile.setVisibility(View.VISIBLE);
        mDashBoardText.setVisibility(View.VISIBLE);
        iImage.setVisibility(View.GONE);
        mProfileText.setVisibility(View.GONE);
        cChangePasswd.setVisibility(View.GONE);
        cLogout.setVisibility(View.GONE);
        //mHistoryList.setVisibility(View.GONE);
    }

    private void showProfile() {
        cDashboardProfile.setVisibility(View.VISIBLE);
        mDashBoardText.setVisibility(View.GONE);
        iImage.setVisibility(View.VISIBLE);
        mProfileText.setVisibility(View.VISIBLE);
        cChangePasswd.setVisibility(View.VISIBLE);
        cLogout.setVisibility(View.VISIBLE);
        mChangePassword.setVisibility(View.VISIBLE);
        mLogout.setVisibility(View.VISIBLE);
        // mHistoryList.setVisibility(View.GONE);
    }
}
