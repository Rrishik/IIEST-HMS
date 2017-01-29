package com.risk.iiest_hms.helper;


import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class PageParser {

    private Context mContext;
    private Document mDocument;

    public PageParser(Context context, String responsePage) {
        mContext = context;
        mDocument = Jsoup.parse(responsePage);
    }

    public boolean checkLogin() {

        Elements username = mDocument.getElementsByClass("username");
        Log.d("parser  ", "checkLogin: " + username.toString());

        if (username.toString().length() != 0) {
            ;
            return (true);
        }
        return false;
    }

    public boolean checkDues() {

        Elements spaceunder = mDocument.getElementsByClass("spaceUnder");
        Element dues_tr = spaceunder.get(1);
        Elements dues_td = dues_tr.getElementsByTag("td");
        Log.d("CheckDues", dues_td.toString());
        if (dues_td.text().length() != 0) {
            return false;
        }
        return true;
    }

    public String getUserName() {

        Elements username = mDocument.getElementsByClass("username");
        return username.text();
    }

    //public List<String>
}
