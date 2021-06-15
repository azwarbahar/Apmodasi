package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.DetailBayiActivity;

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

        if (position==2){
            holder.rl_container.setBackground(ContextCompat.getDrawable(context, R.color.white3));
            holder.rl_imunisasi.setVisibility(View.GONE);
            holder.rl_menunggu.setVisibility(View.VISIBLE);
        } else {
            holder.rl_container.setBackground(ContextCompat.getDrawable(context, R.color.purple_5002));
            holder.rl_imunisasi.setVisibility(View.VISIBLE);
            holder.rl_menunggu.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DetailBayiActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private RelativeLayout rl_container;
        private RelativeLayout rl_menunggu;
        private RelativeLayout rl_imunisasi;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            rl_container = itemView.findViewById(R.id.rl_container);
            rl_menunggu = itemView.findViewById(R.id.rl_menunggu);
            rl_imunisasi = itemView.findViewById(R.id.rl_imunisasi);
        }
    }
}
