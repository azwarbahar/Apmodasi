package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;

public class BayiAdapter extends RecyclerView.Adapter<BayiAdapter.MyHolderView> {

    private Context context;

    public BayiAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BayiAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bayi, parent, false);
        BayiAdapter.MyHolderView myHolderView = new BayiAdapter.MyHolderView(view);

        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull BayiAdapter.MyHolderView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        public MyHolderView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
