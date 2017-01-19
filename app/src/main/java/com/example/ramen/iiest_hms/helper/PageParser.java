package com.example.ramen.iiest_hms.helper;


import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParser {

    private Context mContext;
    private Document mDocument;
    private boolean mLoggedIn = false;

    public PageParser(Context context, String responsePage) {
        mContext = context;
        mDocument = Jsoup.parse(responsePage);
    }

    public boolean checkLogin(){
        Elements chk = mDocument.getElementsByClass("top-nav notification-row");
        if (chk == null){
            mLoggedIn = false;
        }
        else {
            mLoggedIn = true;
        }
        return mLoggedIn;
    }
}
