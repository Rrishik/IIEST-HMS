package com.example.ramen.iiest_hms.helper;


import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageParser {

    private Context mContext;
    private Document mDocument;
    private boolean mLoggedIn = false;

    public PageParser(Context context, String responsePage) {
        mContext = context;
        mDocument = Jsoup.parse(responsePage);
    }

    public boolean checkLogin() {
        boolean chk = mDocument.hasClass("top-nav notification-row");
        return chk;
    }
}
