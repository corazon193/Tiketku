package com.tiketku.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tiketku.DetailActivity;
import com.tiketku.OpenActivity;
import com.tiketku.R;
import com.tiketku.adapter.AdapterCart;
import com.tiketku.model.CartModel;
import com.tiketku.shared.SaveShared;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    private View view;
    private ArrayList<CartModel> arrayCart;
    private AdapterCart adapterCart;
    private RecyclerView recyclerCart;
    private DatabaseReference cartRef;
    private SaveShared shared;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cart_fragment, container, false);
        arrayCart = new ArrayList<>();

        recyclerCart = view.findViewById(R.id.recyclerCart);
        recyclerCart.setHasFixedSize(true);
        recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterCart = new AdapterCart(getContext(), arrayCart, new AdapterCart.OnClickListener() {
            @Override
            public void onClick(int position, CartModel cartModel) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("id", cartModel.getId());
                startActivity(intent);
            }

            @Override
            public void remove(int position, CartModel cartModel) {
                cartRef.child(cartModel.getKey()).removeValue()
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getContext(), "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Gagal Dihapus", Toast.LENGTH_SHORT).show()
                        );
            }

        });

        recyclerCart.setAdapter(adapterCart);

        cartRef = FirebaseDatabase.getInstance().getReference("cart");

        loadCart();

        return view;
    }

    private void loadCart() {
        shared = new SaveShared(getContext());
        String emailUser = shared.getKey("email");

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart");
        cartRef.orderByChild("email").equalTo(emailUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayCart.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                CartModel cartModel = data.getValue(CartModel.class);
                                if (cartModel != null) {
                                    cartModel.setKey(data.getKey());
                                    arrayCart.add(cartModel);
                                }
                            }
                            adapterCart.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Kesalahan koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
