package com.risk.iiest_hms.helper;


import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetworkUtils {

    private CookieManager cookieManager;
    private OkHttpClient client;

    public NetworkUtils() {

        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
    }

    public String okHttpPostRequest(String url, String params) {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            String response = client.newCall(request).execute().body().string();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
