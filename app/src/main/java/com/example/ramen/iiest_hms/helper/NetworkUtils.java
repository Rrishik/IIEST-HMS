package com.example.ramen.iiest_hms.helper;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static URL urlObj;
    private static HttpURLConnection conn;
    private static DataOutputStream wr;
    private static StringBuilder result;

    public static String httpPostRequest(String url, String params) {
        try {
            urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.connect();
            wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader read = new BufferedReader(new InputStreamReader(in));

            result = new StringBuilder();
            String line;
            while ((line = read.readLine()) != null) {
                result.append(line);
            }

            Log.d("POST_Request", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        return result.toString();
    }
}
