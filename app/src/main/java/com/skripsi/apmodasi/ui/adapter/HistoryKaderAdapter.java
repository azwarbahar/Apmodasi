package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;

public class HistoryKaderAdapter extends RecyclerView.Adapter<HistoryKaderAdapter.MyHolderView> {

    private Context context;

    public HistoryKaderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryKaderAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat_kader, parent, false);
        HistoryKaderAdapter.MyHolderView myHolderView = new HistoryKaderAdapter.MyHolderView(view);

        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryKaderAdapter.MyHolderView holder, int position) {

        if (position % 2 == 0){
            holder.tv1.setText("Mengukur Bayi");
        } else {
            holder.tv1.setText("Menimbang Bayi");
        }

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private TextView tv1;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv1);

        }
    }
}
