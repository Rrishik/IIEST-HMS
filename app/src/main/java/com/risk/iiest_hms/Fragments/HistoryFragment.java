package com.risk.iiest_hms.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.risk.iiest_hms.Adapter.HistoryAdapter;
import com.risk.iiest_hms.Helper.AsyncTasks;
import com.risk.iiest_hms.Helper.PageParser;
import com.risk.iiest_hms.R;

public class HistoryFragment extends Fragment implements HistoryAdapter.RecyclerViewClickListener {
    private String TAG = getClass().getSimpleName();

    private AsyncTasks mLoadTasks = null;

    private String ledger_link = "http://iiesthostel.iiests.ac.in/students/ledger";

    private HistoryAdapter mHistoryAdapter;
    private RecyclerView mHistoryList;
    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHistoryList = (RecyclerView) getView().findViewById(R.id.rv_history);

        progressBar = (ProgressBar) getView().findViewById(R.id.progressBarLedger);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mHistoryList.setLayoutManager(linearLayoutManager);
        mHistoryList.setHasFixedSize(true);

        mHistoryAdapter = new HistoryAdapter(getActivity(), this);
        mHistoryList.setAdapter(mHistoryAdapter);
        loadData();
    }

    private void loadData() {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("sid", sharedPreferences.getString(getString(R.string.saved_sid), null))
                .appendQueryParameter("password", sharedPreferences.getString(getString(R.string.saved_password), null))
                .appendQueryParameter("type", "2")
                .appendQueryParameter("login", "");
        String query = builder.build().getEncodedQuery();
        String type = "application/x-www-form-urlencoded";
        Log.d("params", query);
        mLoadTasks = new AsyncTasks(query, type, new AsyncTasks.AsyncTasksListener() {
            @Override
            public void onPostExecute(String response) {
                mLoadTasks = null;
                PageParser p = new PageParser(response);
                mHistoryAdapter.setmDataset(p.getLedger());
                showProgress(false);
            }

            @Override
            public void onCancelled() {
                mLoadTasks = null;
                showProgress(false);
            }
        });
        showProgress(true);
        mLoadTasks.execute(ledger_link);
    }

    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            mHistoryList.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            mHistoryList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickListener(String ledger) {

    }
}
