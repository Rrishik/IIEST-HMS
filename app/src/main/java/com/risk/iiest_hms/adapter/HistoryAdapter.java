package com.risk.iiest_hms.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.risk.iiest_hms.R;

import java.util.ArrayList;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryAdapterViewHolder> {

    private static HistoryAdapter mInstance;
    private final RecyclerViewClickListener mOnClickListener;
    private List<AdapterData> mDataset = null;
    private Context mContext;


    public HistoryAdapter(Context context, RecyclerViewClickListener listener) {
        mOnClickListener = listener;
        mContext = context;
        mDataset = new ArrayList<>();
    }

    public static HistoryAdapter getInstance(Context context, RecyclerViewClickListener listener) {
        if (mInstance == null) {
            mInstance = new HistoryAdapter(context, listener);
        }
        return mInstance;
    }

    @Override
    public HistoryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_list, parent, false);
        return new HistoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapterViewHolder holder, int position) {
        holder.mLedgerTv.setText(mDataset.get(position).mLedger);
        holder.mDateTv.setText(mDataset.get(position).mDate);
        holder.mFineTv.setText(mDataset.get(position).mFine);
    }

    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        return mDataset.size();
    }

    public List<AdapterData> getmDataset() {
        if (mDataset.isEmpty()) {
            return null;
        }
        return mDataset;
    }

    public void setmDataset(List<AdapterData> adapterDataList) {
        mDataset.clear();
        mDataset.addAll(adapterDataList);
        notifyDataSetChanged();
    }

    public interface RecyclerViewClickListener {
        void onClickListener(String ledger);
    }

    class HistoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mLedgerTv;
        final TextView mDateTv;
        final TextView mFineTv;

        HistoryAdapterViewHolder(View view) {
            super(view);
            mLedgerTv = (TextView) view.findViewById(R.id.tv_ledger);
            mDateTv = (TextView) view.findViewById(R.id.tv_date);
            mFineTv = (TextView) view.findViewById(R.id.tv_fine);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String rcpt_link = mDataset.get(getAdapterPosition()).mReceiptLink;
            mOnClickListener.onClickListener(rcpt_link);
        }
    }
}
