package com.tiketku;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;
import com.tiketku.format.FormatRupiah;
import com.tiketku.format.QRGenerator;
import com.tiketku.model.CartModel;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        String id = getIntent().getStringExtra("id");
        if (!id.isEmpty() && id != null) {
            loadData(id);
        }
    }

    private void loadData(String id) {
        TextView name = findViewById(R.id.textName);
        TextView tanggal = findViewById(R.id.textDate);
        TextView kode = findViewById(R.id.textKey);
        TextView price = findViewById(R.id.textPrice);
        TextView description = findViewById(R.id.textDescription);
        ImageView image_qr = findViewById(R.id.image_qr);

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("cart");
        productRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartModel cart = dataSnapshot.getValue(CartModel.class);
                        name.setText(cart.getName());
                        tanggal.setText(cart.getDate());
                        kode.setText(cart.getKey());
                        price.setText(FormatRupiah.formatRupiah(cart.getPrice()));
                        description.setText(cart.getDescription());

                        try {
                            Bitmap qr = QRGenerator.generateQR(cart.getId(), 500, 500);
                            image_qr.setImageBitmap(qr);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "Kesalahan koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
