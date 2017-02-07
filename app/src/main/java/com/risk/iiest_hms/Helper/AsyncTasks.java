package com.risk.iiest_hms.Helper;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncTasks extends AsyncTask<String, String, String> {

    private String query;
    private String type;
    private String response;

    private AsyncTasksListener listener;

    public AsyncTasks(String query, String type, AsyncTasksListener listener) {
        this.query = query;
        this.type = type;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        try {
            response = NetworkUtils.okHttpPostRequest(url, query, type);
        } catch (Exception e) {
            e.printStackTrace();
            response = null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {

        if (listener != null) {
            if (s == null) {
                Log.d("AsyncTasks", "onPostExecute: respnonse null!!");
                listener.onPostExecute(null);
            } else
                listener.onPostExecute(s);
        }

    }

    @Override
    protected void onCancelled() {
        if (listener != null)
            listener.onCancelled();
    }

    public interface AsyncTasksListener {
        void onPostExecute(String response);

        void onCancelled();
    }
}
