package com.tiketku.adapter;

import static com.tiketku.format.FormatRupiah.formatRupiah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tiketku.R;
import com.tiketku.model.TiketModel;

import java.util.ArrayList;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {

    Context context;
    ArrayList<TiketModel> productModels;
    OnClickListener onClickListener;

    public AdapterProduct(Context context, ArrayList<TiketModel> arrayList, OnClickListener onClickListener) {
        this.context = context;
        this.productModels = arrayList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TiketModel data = productModels.get(i);
        viewHolder.textHarga.setText(formatRupiah(data.getPrice()));
        viewHolder.textName.setText(data.getTitle());
        Glide.with(context)
                .load(data.getUrlImage())
                .into(viewHolder.imgProduct);

        viewHolder.itemView.setOnClickListener(v -> {
            onClickListener.onClick(i, data);
        });
    }

    public interface OnClickListener {
        void onClick(int position, TiketModel tiketModel);

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textHarga, textName;
        ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textHarga = itemView.findViewById(R.id.textHarga);
            textName = itemView.findViewById(R.id.textName);

            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
