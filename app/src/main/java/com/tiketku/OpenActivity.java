package com.tiketku;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tiketku.model.CartModel;
import com.tiketku.model.TiketModel;
import com.tiketku.shared.SaveShared;
import java.util.UUID;

public class OpenActivity extends AppCompatActivity {
    TextView textJudul, textHarga, textDescription;
    ImageView imgProduct;
    AppCompatButton appButtonKeranjang;
    private SaveShared shared;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        shared = new SaveShared(this);

        textJudul = findViewById(R.id.textJudul);
        textHarga = findViewById(R.id.textHarga);
        textDescription = findViewById(R.id.textDescription);
        imgProduct = findViewById(R.id.imgProduct);
        appButtonKeranjang = findViewById(R.id.appButtonKeranjang);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String key = bundle.getString("key");
            loadData(key);
        }

    }

    private void loadData(String key) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tickets/" + key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Ambil data langsung jadi TiketModel
                    TiketModel tiket = dataSnapshot.getValue(TiketModel.class);

                    if (tiket != null) {
                        // Tampilkan ke UI
                        textJudul.setText(tiket.getTitle());
                        textHarga.setText(new com.tiketku.format.FormatRupiah().formatRupiah(tiket.getPrice()));
                        // Gabungkan semua deskripsi termasuk kategori, rating, favorit
                        String deskripsiLengkap = tiket.getDescription() + "\n\n"
                                + "🎟️ Kategori: " + tiket.getCategory() + "\n"
                                + "⭐ Rating: " + tiket.getRating() + "\n";

                        textDescription.setText(deskripsiLengkap);
                        Glide.with(OpenActivity.this).load(tiket.getUrlImage()).into(imgProduct);

                        // Tambahkan ke keranjang saat tombol ditekan
                        appButtonKeranjang.setOnClickListener(v -> {
                            showBottomSheetSuccess(tiket, dataSnapshot.child("id").getValue(String.class));
                        });
                    }

                } else {
                    Toast.makeText(OpenActivity.this, "Tiket tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
                Toast.makeText(OpenActivity.this, "Tidak ada koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addCart(UUID uuid,String id,  String description, String key, String name, int price, String urlImage) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cart/"+uuid);
        CartModel cartModel = new CartModel(String.valueOf(uuid), id, key, description, shared.getKey("email"), name, price, urlImage);
        databaseReference.setValue(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(OpenActivity.this, "Berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OpenActivity.this, "Gagal ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRiwayat(UUID uuid,String id,  String description, String key, String name, int price, String urlImage) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("history/"+uuid);
        CartModel cartModel = new CartModel(String.valueOf(uuid), id, key, description, shared.getKey("email"), name, price, urlImage);
        databaseReference.setValue(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("AplikasiTiketKu", "Berhasil ditambahkan ke keranjang");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AplikasiTiketKu", "Gagal ditambahkan ke keranjang", e);
                Toast.makeText(OpenActivity.this, "Gagal ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBottomSheetSuccess(TiketModel tiket, String id) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(OpenActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_success);
        bottomSheetDialog.setCancelable(true);

        TextView textMessage = bottomSheetDialog.findViewById(R.id.textMessage);
        AppCompatButton btnLihatKeranjang = bottomSheetDialog.findViewById(R.id.btnLanjut);
        AppCompatButton btnTutup = bottomSheetDialog.findViewById(R.id.btnBatal);

        if (textMessage != null)
            textMessage.setText("Tiket berhasil ditambahkan ke keranjang 🎉");

        if (btnLihatKeranjang != null) {
            btnLihatKeranjang.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                addRiwayat(UUID.randomUUID(), id, tiket.getDescription(), tiket.getKey(), tiket.getTitle(), tiket.getPrice(), tiket.getUrlImage());
                addCart(UUID.randomUUID(), id, tiket.getDescription(), tiket.getKey(), tiket.getTitle(), tiket.getPrice(), tiket.getUrlImage());
            });
        }

        if (btnTutup != null) {
            btnTutup.setOnClickListener(v -> bottomSheetDialog.dismiss());
        }

        bottomSheetDialog.show();
    }

}
