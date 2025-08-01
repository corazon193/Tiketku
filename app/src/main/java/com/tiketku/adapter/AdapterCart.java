package com.tiketku.adapter;

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
import com.tiketku.model.CartModel;

import java.util.ArrayList;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {

    Context context;
    ArrayList<CartModel> cartModels;
    OnClickListener onClickListener;

    public AdapterCart(Context context, ArrayList<CartModel> arrayList, OnClickListener onClickListener) {
        this.context = context;
        this.cartModels = arrayList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CartModel cartModel = cartModels.get(i);
        viewHolder.textJudul.setText(cartModel.getName());
        viewHolder.textHarga.setText(new com.tiketku.format.FormatRupiah().formatRupiah(cartModel.getPrice()));
        viewHolder.textTanggal.setText(cartModel.getDate());
        Glide.with(context).load(cartModel.getUrlImage()).into(viewHolder.imgProduct);
        viewHolder.itemView.setOnClickListener(v->onClickListener.onClick(i, cartModel));
        viewHolder.imgDelete.setOnClickListener(v->onClickListener.remove(i, cartModel));
    }
    public interface OnClickListener {
        void onClick(int position, CartModel cartModel);
        void remove(int position, CartModel cartModel);
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    class ViewHolder  extends RecyclerView.ViewHolder {

        ImageView imgProduct, imgDelete;
        TextView textJudul, textHarga, textTanggal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            textJudul = itemView.findViewById(R.id.textJudul);
            textHarga = itemView.findViewById(R.id.textHarga);
            textTanggal = itemView.findViewById(R.id.textTanggal);
        }
    }
}
