package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.ui.activity.bunda.DetailBayiActivity;
import com.skripsi.apmodasi.ui.activity.kader.InputDataBayiActivity;
import com.skripsi.apmodasi.ui.activity.kader.MenuKaderActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BayiAdapter extends RecyclerView.Adapter<BayiAdapter.MyHolderView> {

    private Context context;
    private ArrayList<Bayi> bayis;

    public BayiAdapter(Context context, ArrayList<Bayi> bayis) {
        this.context = context;
        this.bayis = bayis;
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

        String id_bayi = bayis.get(position).getIdBayi();
        String nama_bayi = bayis.get(position).getNamaBayi();
        String yourDate = bayis.get(position).getTanggalLahirBayi();
        String status_bayi = bayis.get(position).getStatusBayi();

        Calendar currentDate = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date birthdate = null;

        try {
            birthdate = myFormat.parse(yourDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long time = currentDate.getTime().getTime() / 1000 - birthdate.getTime() / 1000;

        int years = Math.round(time) / 31536000;
        int months = Math.round(time - years * 31536000) / 2628000;

        holder.tv_nama_bayi.setText(nama_bayi);
        if (years > 0) {
            holder.tv_usia.setText(String.valueOf(years + " Tahun, " + months + " Bulan"));
        } else {
            holder.tv_usia.setText(String.valueOf(months + " Bulan"));
        }

        if (status_bayi.equals("Menunggu")) {
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
                Intent intent = new Intent(context, DetailBayiActivity.class);
                intent.putExtra("id_bayi", id_bayi);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bayis.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private RelativeLayout rl_container;
        private RelativeLayout rl_menunggu;
        private RelativeLayout rl_imunisasi;
        private TextView tv_nama_bayi;
        private TextView tv_usia;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            rl_container = itemView.findViewById(R.id.rl_container);
            rl_menunggu = itemView.findViewById(R.id.rl_menunggu);
            rl_imunisasi = itemView.findViewById(R.id.rl_imunisasi);
            tv_nama_bayi = itemView.findViewById(R.id.tv_nama_bayi);
            tv_usia = itemView.findViewById(R.id.tv_usia);
        }
    }
}
