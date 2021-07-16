package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseKader;
import com.skripsi.apmodasi.data.model.Imunisasi;
import com.skripsi.apmodasi.data.model.Kader;
import com.skripsi.apmodasi.data.model.RiwayatKader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatBayiAdapter extends RecyclerView.Adapter<RiwayatBayiAdapter.MyHolderView> {

    private Context context;
    private ArrayList<RiwayatKader> riwayatKaders;
    private ArrayList<Kader> kaders;

    String[] imunisasi_array = {"HB", "BCG", "Polio 1", "DPT-HB-Hib 1", "Polio 2", "DPT-HB-Hib 2", "Polio 3", "DPT-HB-Hib 3", "Polio 4", "IPV", "CAMPAK"};

    public RiwayatBayiAdapter(Context context, ArrayList<RiwayatKader> riwayatKaders) {
        this.context = context;
        this.riwayatKaders = riwayatKaders;
    }

    @NonNull
    @Override
    public RiwayatBayiAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat_bayi, parent, false);
        RiwayatBayiAdapter.MyHolderView myHolderView = new RiwayatBayiAdapter.MyHolderView(view);

        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatBayiAdapter.MyHolderView holder, int position) {

        String jenis = riwayatKaders.get(position).getJenis_input();
        String value = riwayatKaders.get(position).getValue_input();
        if (jenis.equals("Tinggi")){
            holder.tv1.setText("Mengukur Tinggi");
            holder.tv_title.setText(value + " Cm");
        } else if (jenis.equals("Berat")){
            holder.tv1.setText("Menimbang Berat");
            holder.tv_title.setText(value + " Kg");
        } else if (jenis.equals("Vaksin")){
            holder.tv1.setText("Imunisasi ");
            holder.tv_title.setText(imunisasi_array[Integer.parseInt(value)-1]);
        }
        holder.tv_catatan.setText(riwayatKaders.get(position).getCatatan());
        holder.tv_tanggal.setText(parseDateToddMMyyyy(riwayatKaders.get(position).getTanggal_riwayat()));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseKader> responseKaderCall = apiInterface.getKaderId(riwayatKaders.get(position).getKader_id());
        responseKaderCall.enqueue(new Callback<ResponseKader>() {
            @Override
            public void onResponse(Call<ResponseKader> call, Response<ResponseKader> response) {
                holder.tv_kader.setText("Kader : " + response.body().getKader().getNama_kader());
            }

            @Override
            public void onFailure(Call<ResponseKader> call, Throwable t) {
                holder.tv_kader.setText("Kader : Nama Kader");
            }
        });

    }


    private String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMMM yyyy";
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

    @Override
    public int getItemCount() {
        return riwayatKaders.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private TextView tv1;
        private TextView tv_title;
        private TextView tv_kader;
        private TextView tv_catatan;
        private TextView tv_tanggal;


        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv1);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_kader = itemView.findViewById(R.id.tv_kader);
            tv_catatan = itemView.findViewById(R.id.tv_catatan);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);

        }
    }
}
