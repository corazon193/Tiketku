package com.tiketku;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tiketku.adapter.AdapterHistory;
import com.tiketku.model.CartModel;
import com.tiketku.shared.SaveShared;

import java.util.ArrayList;

public class RiwayatActivity extends AppCompatActivity {

    private ArrayList<CartModel> array;
    private RecyclerView recyclerCart;
    private AdapterHistory adapterCart;
    private DatabaseReference cartRef;
    private SaveShared shared;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        array = new ArrayList<>();

        recyclerCart = findViewById(R.id.recyclerView);
        recyclerCart.setHasFixedSize(true);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        adapterCart = new AdapterHistory(this, array, new AdapterHistory.OnClickListener() {
            @Override
            public void onClick(int position, CartModel cartModel) {
                // Tambahkan aksi jika item diklik (jika perlu)
            }
        });

        recyclerCart.setAdapter(adapterCart);

        // Ambil data dari 'history' berdasarkan email
        loadHistoryByEmail();
    }

    private void loadHistoryByEmail() {
        shared = new SaveShared(this);
        String emailUser = shared.getKey("email");

        cartRef = FirebaseDatabase.getInstance().getReference("history");
        cartRef.orderByChild("email").equalTo(emailUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        array.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                CartModel cartModel = data.getValue(CartModel.class);
                                if (cartModel != null) {
                                    cartModel.setKey(data.getKey());
                                    array.add(cartModel);
                                }
                            }
                            adapterCart.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "Riwayat kosong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Kesalahan koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
