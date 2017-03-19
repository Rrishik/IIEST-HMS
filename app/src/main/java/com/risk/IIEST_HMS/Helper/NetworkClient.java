package com.risk.IIEST_HMS.Helper;

import android.app.Application;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkClient extends Application {

    private static NetworkClient mInstance;
    private OkHttpClient mOkHttpClient;

    private NetworkClient() {
        mOkHttpClient = getOkHttpClient();
    }

    public static synchronized NetworkClient getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkClient();
        }
        return mInstance;
    }

    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            mOkHttpClient = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();
        }
        return mOkHttpClient;
    }

    public Response getResponse(Request request) {
        try {
            return mOkHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
