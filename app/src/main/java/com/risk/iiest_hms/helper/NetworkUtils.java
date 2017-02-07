package com.risk.iiest_hms.Helper;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetworkUtils {

    public static String okHttpPostRequest(String url, String params, String type) {

        MediaType mediaType = MediaType.parse(type);
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", type)
                .addHeader("cache-control", "no-cache")
                .build();

        String responseString;

        try {
            responseString = NetworkClient.getInstance().getResponse(request).body().string();
        } catch (IOException e) {
            e.printStackTrace();
            responseString = null;
        }

        return responseString;
    }
}
