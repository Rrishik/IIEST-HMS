package com.risk.IIEST_HMS.Helper;


import com.risk.IIEST_HMS.Adapter.AdapterData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class PageParser {

    private Document mDocument;

    public PageParser(String responsePage) {

        if (responsePage != null) {
            mDocument = Jsoup.parse(responsePage);
        }
    }

    public boolean checkLogin() {

        Elements username = mDocument.getElementsByClass("username");
        return username.toString().length() != 0;
    }

    public boolean checkDues() {

        Elements spaceunder = mDocument.getElementsByClass("spaceUnder");
        Element dues_tr = spaceunder.get(1);
        Elements dues_td = dues_tr.getElementsByTag("td");
        return dues_td.text().length() == 0;
    }

    public String getUserName() {

        Elements username = mDocument.getElementsByClass("username");
        return username.text();
    }

    public List<AdapterData> getLedger() {
        String ledger, fine, date, link;
        List<AdapterData> list = new ArrayList<>();
        Element tbody = mDocument.getElementsByTag("tbody").get(0);
        Elements rows = tbody.getElementsByTag("tr");

        for (Element row : rows) {
            Elements td = row.getElementsByTag("td");
            if (td.get(2).text().trim().equals("Rcpt")) {
                date = td.get(1).text().split("<br>")[0].trim();
                String[] led_amt = td.get(4).text().split("<br>");
                ledger = led_amt[0].substring(0, led_amt[0].indexOf("+"));
                fine = "Fine " + led_amt[0].substring(led_amt[0].indexOf("+") + 2);
                link = td.get(11).getElementsByTag("a").get(1).attr("href");
                AdapterData data = new AdapterData(ledger, date, fine, link);
                list.add(data);
            }
        }
        return list;
    }

}
