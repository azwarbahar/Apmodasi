package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.kader.DetailBayiKaderActivity;

public class BayiKaderAdapter extends RecyclerView.Adapter<BayiKaderAdapter.MyHolderView> {

    private Context context;

    public BayiKaderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BayiKaderAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bayi_kader, parent, false);
        BayiKaderAdapter.MyHolderView myHolderView = new BayiKaderAdapter.MyHolderView(view);

        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull BayiKaderAdapter.MyHolderView holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DetailBayiKaderActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        public MyHolderView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
