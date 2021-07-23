package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.data.model.Imunisasi;

import java.util.ArrayList;

public class DataImunisasiBayiAdapter extends RecyclerView.Adapter<DataImunisasiBayiAdapter.MyHolderView> {

    private Context context;
    private ArrayList<Imunisasi> imunisasis;

    public DataImunisasiBayiAdapter(Context context, ArrayList<Imunisasi> imunisasis) {
        this.context = context;
        this.imunisasis = imunisasis;
    }

    @NonNull
    @Override
    public DataImunisasiBayiAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_imunisasi_bayi, parent, false);
        DataImunisasiBayiAdapter.MyHolderView myHolderView = new DataImunisasiBayiAdapter.MyHolderView(view);

        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull DataImunisasiBayiAdapter.MyHolderView holder, int position) {

        holder.tv_imunisasi.setText(imunisasis.get(position).getNama_imunisasi());
        holder.tv_interval_imunisasi.setText(imunisasis.get(position).getInterval_imunisasi());
        String status_imunisasi = imunisasis.get(position).getStatus_imunisasi();
        if (status_imunisasi.equals("Sudah")){
            holder.tv_status.setText(status_imunisasi);
            holder.rl_container.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_stroke_radius_primary_gret));
        } else {
            holder.tv_status.setText(status_imunisasi);
            holder.rl_container.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_stroke_radius_primary));
        }

    }

    @Override
    public int getItemCount() {
        return imunisasis.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private TextView tv_imunisasi;
        private TextView tv_interval_imunisasi;
        private TextView tv_status;
        private RelativeLayout rl_container;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            rl_container = itemView.findViewById(R.id.rl_container);
            tv_imunisasi = itemView.findViewById(R.id.tv_imunisasi);
            tv_interval_imunisasi = itemView.findViewById(R.id.tv_interval_imunisasi);
            tv_status = itemView.findViewById(R.id.tv_status);

        }
    }
}
