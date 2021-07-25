package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseImunisasi;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.data.model.Bunda;
import com.skripsi.apmodasi.data.model.Imunisasi;
import com.skripsi.apmodasi.ui.activity.kader.DetailBayiKaderActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BayiKaderAdapter extends RecyclerView.Adapter<BayiKaderAdapter.MyHolderView> {

    private Context context;
    private ArrayList<Bayi> bayis;
    private ArrayList<Imunisasi> imunisasis;

    public BayiKaderAdapter(Context context, ArrayList<Bayi> bayis) {
        this.context = context;
        this.bayis = bayis;
    }

    public void filterList(ArrayList<Bayi> bayi) {
        bayis = bayi;
        notifyDataSetChanged();
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


        String id_bayi = bayis.get(position).getIdBayi();
        String nama_bayi = bayis.get(position).getNamaBayi();
        String yourDate = bayis.get(position).getTanggalLahirBayi();
        String status_bayi = bayis.get(position).getStatusBayi();
        String foto_bayi = bayis.get(position).getFotoBayi();
        if (foto_bayi.equals("-")) {
            Glide.with(context)
                    .load(R.drawable.img_baby)
                    .into(holder.foto_bayi);
        } else {
            Glide.with(context)
                    .load(Constanta.URL_IMG_BAYI + foto_bayi)
                    .into(holder.foto_bayi);
        }

        // get Imunisasi Bayi
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseImunisasi> responseImunisasiCall = apiInterface.getImunisasiBayi(id_bayi);
        responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
            @Override
            public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        imunisasis = (ArrayList<Imunisasi>) response.body().getImunisasi_bayi();
                        if (imunisasis.size() < 1) {
                            holder.tv_jumlah_imunisasi.setText("0/11");
                        } else {
                            int jumlah_sudah = 0;
                            for (int a = 0; a < imunisasis.size(); a++) {
                                String status_imunisasi = imunisasis.get(a).getStatus_imunisasi();
                                String keterangan_imunisasi = imunisasis.get(a).getKeterangan_imunisasi();
                                if (status_imunisasi.equals("Sudah") && keterangan_imunisasi.equals("Wajib")) {
                                    jumlah_sudah = jumlah_sudah + 1;
                                }
                            }
                            holder.tv_jumlah_imunisasi.setText(jumlah_sudah + "/11");
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
            }
        });


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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailBayiKaderActivity.class);
                intent.putExtra("ID_BAYI", bayis.get(position).getIdBayi());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bayis.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {


        private TextView tv_nama_bayi;
        private TextView tv_usia;
        private TextView tv_jumlah_imunisasi;
        private ImageView foto_bayi;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            foto_bayi = itemView.findViewById(R.id.foto_bayi);
            tv_nama_bayi = itemView.findViewById(R.id.tv_nama_bayi);
            tv_usia = itemView.findViewById(R.id.tv_usia);
            tv_jumlah_imunisasi = itemView.findViewById(R.id.tv_jumlah_imunisasi);
        }
    }
}
