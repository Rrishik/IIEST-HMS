package com.risk.iiest_hms.Fragments;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import static android.content.Intent.ACTION_VIEW;

public class HistoryFragment extends Fragment implements HistoryAdapter.RecyclerViewClickListener {
    private static HistoryFragment mInstance;
    private String TAG = getClass().getSimpleName();
    private AsyncTasks mLoadTasks = null;

    private String ledger_link = "http://iiesthostel.iiests.ac.in/students/ledger";

    private HistoryAdapter mHistoryAdapter;
    private RecyclerView mHistoryList;
    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;

    public static HistoryFragment getInstance() {
        if (mInstance == null) {
            mInstance = new HistoryFragment();
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHistoryList = (RecyclerView) getView().findViewById(R.id.rv_history);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Payment History");

        progressBar = (ProgressBar) getView().findViewById(R.id.progressBarLedger);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mHistoryList.setLayoutManager(linearLayoutManager);
        mHistoryList.setHasFixedSize(true);

        mHistoryAdapter = HistoryAdapter.getInstance(getActivity(), this);
        mHistoryList.setAdapter(mHistoryAdapter);
        if (mHistoryAdapter.getmDataset() == null)
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
                showProgress(false);
                if (response != null) {
                    PageParser p = new PageParser(response);
                    mHistoryAdapter.setmDataset(p.getLedger());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Could not fetch details!\nTry again later")
                            .setTitle("Network Error!")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }
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
    public void onClickListener(String rcpt_link) {
        Intent intent = new Intent(ACTION_VIEW);
        intent.setDataAndType(Uri.parse(rcpt_link), "application/pdf");
        getActivity().startActivity(intent);
    }
}
