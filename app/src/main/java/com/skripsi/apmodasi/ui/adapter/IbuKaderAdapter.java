package com.skripsi.apmodasi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;

public class IbuKaderAdapter extends RecyclerView.Adapter<IbuKaderAdapter.MyHolderView> {

    private Context context;

    public IbuKaderAdapter(Context context) {
        this.context = context;
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
