package com.risk.iiest_hms.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.risk.iiest_hms.Helper.Constants;
import com.risk.iiest_hms.Helper.WebUtils;
import com.risk.iiest_hms.R;

public class ReceiptView extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    private ProgressBar mProgressBar;
    private WebView mWebPage;
    private String mLink;
    private WebUtils mWebViewUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Money Receipt");
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarReceipt);

        mWebPage = (WebView) findViewById(R.id.wv_page);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e(TAG, "No data specified for webview");
            return;
        }
        mLink = extras.getString(Constants.Intent.SELECTED_LINK);
        if (mLink == null || mLink.trim().isEmpty()) {
            Log.e(TAG, "No URL specified for webview");
            return;
        }
        mWebViewUtil = new WebUtils(ReceiptView.this, mLink, mWebPage, mProgressBar);
        mWebViewUtil.openInWebView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.download:
                mWebViewUtil.download();
                return true;
            case R.id.copy_link:
                mWebViewUtil.copyLink();
                return true;
            case R.id.share_link:
                mWebViewUtil.shareLink();
                return true;
            case R.id.page_refresh:
                mWebViewUtil.refresh();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
