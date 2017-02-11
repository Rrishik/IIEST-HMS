package com.risk.iiest_hms.Helper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Intent.ACTION_VIEW;

public class WebUtils {

    private Context mContext;
    private String mLink;
    private WebView mWebview;
    private ProgressBar mProgressBar;

    private String TAG = getClass().getSimpleName();

    public WebUtils(Context context, String link, WebView webView, ProgressBar progressBar) {
        mContext = context;
        mLink = link;
        mWebview = webView;
        mProgressBar = progressBar;
        WebSettings mWebsettings = mWebview.getSettings();
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE) {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }

                mProgressBar.setProgress(progress);
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mWebsettings.setJavaScriptEnabled(true);
    }

    public void refresh() {
        mWebview.loadUrl(mLink);
    }

    public void shareLink() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mLink);
        mContext.startActivity(intent);
    }

    public void copyLink() {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("link", mLink);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(mContext, "Link Copied !", Toast.LENGTH_LONG).show();
    }

    public void openInWebView() {
        mWebview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + mLink);
    }

    public void download() {
        Intent intent = new Intent(ACTION_VIEW);
        intent.setDataAndType(Uri.parse(mLink), "application/pdf");
        mContext.startActivity(intent);
    }

}
