package com.risk.iiest_hms.helper;


import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParser {

    private Context mContext;
    private static Document mDocument;

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

    public static boolean checkDues(){

        Elements spaceunder = mDocument.getElementsByClass("spaceUnder");
        Element dues_tr = spaceunder.get(1);
        Elements dues_td = dues_tr.getElementsByTag("td");
        Log.d("CheckDues", dues_td.toString());
        if (dues_td.text().length() != 0){
            return false;
        }
        return true;
    }
}
