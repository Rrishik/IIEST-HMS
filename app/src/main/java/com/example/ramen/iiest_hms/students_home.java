package com.example.ramen.iiest_hms;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.ramen.iiest_hms.helper.PageParser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class students_home extends AppCompatActivity {

    private BottomBar bottomBar;
    private TextView mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students_home);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mContent = (TextView) findViewById(R.id.tv_text);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_dashboard){
                    if (PageParser.checkDues())
                        mContent.setText("Oops You have Pending Dues! \n Please pay your dues at the earliest!");
                    else
                        mContent.setText("You have no dues Pending! :)");
                }
            }
        });

    }
}
