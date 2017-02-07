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

    private final RecyclerViewClickListener mOnClickListener;
    private List<AdapterData> mDataset;
    private Context mContext;

    public HistoryAdapter(Context context, RecyclerViewClickListener listener) {
        mOnClickListener = listener;
        mContext = context;
        mDataset = new ArrayList<>();
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
    }

    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        return mDataset.size();
    }

    public void clear() {
        mDataset.clear();
        notifyDataSetChanged();
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

        HistoryAdapterViewHolder(View view) {
            super(view);
            mLedgerTv = (TextView) view.findViewById(R.id.tv_ledger);
            mDateTv = (TextView) view.findViewById(R.id.tv_date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String ledger = mDataset.get(getAdapterPosition()).mLedger;
            mOnClickListener.onClickListener(ledger);
        }
    }
}
