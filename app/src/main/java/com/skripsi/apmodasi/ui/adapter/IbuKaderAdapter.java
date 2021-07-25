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
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.Bunda;
import com.skripsi.apmodasi.ui.activity.kader.DetailBundaActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class IbuKaderAdapter extends RecyclerView.Adapter<IbuKaderAdapter.MyHolderView> {

    private Context context;
    private ArrayList<Bunda> bundas;

    public IbuKaderAdapter(Context context, ArrayList<Bunda> bundas) {
        this.context = context;
        this.bundas = bundas;
    }

    @NonNull
    @Override
    public IbuKaderAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ibu, parent, false);
        IbuKaderAdapter.MyHolderView myHolderView = new IbuKaderAdapter.MyHolderView(view);

        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull IbuKaderAdapter.MyHolderView holder, int position) {

        holder.tv_nama.setText(bundas.get(position).getNamaBunda());
        holder.tv_telpon.setText(bundas.get(position).getKontakBunda());
        Glide.with(context)
                .load(Constanta.URL_IMG_BUNDA + bundas.get(position).getFotoBunda())
                .into(holder.foto_bayi);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailBundaActivity.class);
                intent.putExtra("ID_BUNDA", bundas.get(position).getIdBunda());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bundas.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private ImageView foto_bayi;
        private TextView tv_nama;
        private TextView tv_telpon;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            foto_bayi = itemView.findViewById(R.id.foto_bayi);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_telpon = itemView.findViewById(R.id.tv_telpon);

        }
    }
    public void filterList(ArrayList<Bunda> bunda) {
        bundas = bunda;
        notifyDataSetChanged();
    }
}
