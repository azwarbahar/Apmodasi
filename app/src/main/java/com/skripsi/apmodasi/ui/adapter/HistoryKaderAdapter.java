package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.data.model.RiwayatKader;
import com.skripsi.apmodasi.ui.activity.kader.DetailBayiKaderActivity;
import com.skripsi.apmodasi.ui.activity.kader.InputDataBayiActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryKaderAdapter extends RecyclerView.Adapter<HistoryKaderAdapter.MyHolderView> {

    private Context context;
    private ArrayList<RiwayatKader> riwayatKaders;
    private Bayi bayi;

    public HistoryKaderAdapter(Context context, ArrayList<RiwayatKader> riwayatKaders) {
        this.context = context;
        this.riwayatKaders = riwayatKaders;
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

        String jenis = riwayatKaders.get(position).getJenis_input();
        String bayi_id = riwayatKaders.get(position).getBayi_id();
        if (jenis.equals("Tinggi")){
            holder.tv1.setText("Mengukur Bayi");
        } else if (jenis.equals("Berat")){
            holder.tv1.setText("Menimbang Bayi");
        } else {
            holder.tv1.setText("Memvaksin Bayi");
        }

        holder.tv_tanggal.setText(parseDateToddMMyyyy(riwayatKaders.get(position).getCrated_at()));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBayi> responseBayiCall = apiInterface.getBayiId(bayi_id);
        responseBayiCall.enqueue(new Callback<ResponseBayi>() {
            @Override
            public void onResponse(Call<ResponseBayi> call, Response<ResponseBayi> response) {
                bayi = response.body().getResult_bayi();
                holder.tv_nama_bayi.setText(bayi.getNamaBayi());
            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {

            }
        });

        holder.tv_nama_bayi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailBayiKaderActivity.class);
                intent.putExtra("ID_BAYI", bayi_id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return riwayatKaders.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private TextView tv1;
        private TextView tv_nama_bayi;
        private TextView tv_tanggal;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv1);
            tv_nama_bayi = itemView.findViewById(R.id.tv_nama_bayi);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);

        }
    }



    private String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMMM yyyy HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
