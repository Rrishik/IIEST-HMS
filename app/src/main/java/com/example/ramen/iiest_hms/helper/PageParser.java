package com.example.ramen.iiest_hms.helper;


import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParser {

    private Context mContext;
    private Document mDocument;

    public PageParser(Context context, String responsePage) {
        mContext = context;
        mDocument = Jsoup.parse(responsePage);
    }

    public boolean checkLogin() {

        Elements username = mDocument.getElementsByClass("username");
        Log.d("parser  ", "checkLogin: "+ username.toString());

        if (username.toString().length() != 0) {;
            return (true);
        }
        return false;
    }
}
