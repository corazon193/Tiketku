package com.tiketku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tiketku.model.UserModel;
import com.tiketku.shared.SaveShared;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etEmail, etAlamat, etKota, etProvinsi, etTelpon, etPostCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAlamat = findViewById(R.id.etAlamat);
        etKota = findViewById(R.id.etKota);
        etProvinsi = findViewById(R.id.etProvinsi);
        etPostCode = findViewById(R.id.etPostcode);
        etTelpon = findViewById(R.id.etTelpon);


        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v->updateProfile());

        loadProfile();
    }

    private void updateProfile() {
        SaveShared saveShared = new SaveShared(this);
        String email = saveShared.getKey("email");
        String name = etName.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();
        String kota = etKota.getText().toString().trim();
        String provinsi = etProvinsi.getText().toString().trim();
        String telpon = etTelpon.getText().toString().trim();
        String postCode = etPostCode.getText().toString().trim();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Dapatkan key node user
                        String userKey = snapshot.getKey();

                        // Buat objek user baru
                        UserModel updatedUser = new UserModel(name, email, alamat, kota, provinsi, telpon, postCode);

                        // Update ke Firebase
                        databaseReference.child(userKey).setValue(updatedUser)
                                .addOnSuccessListener(aVoid -> {
                                    // Tampilkan pesan atau finish activity
                                    finish(); // atau Toast
                                })
                                .addOnFailureListener(e -> {
                                    e.printStackTrace();
                                });

                        break; // hanya satu data yang cocok
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }
    private void loadProfile() {
        SaveShared saveShared = new SaveShared(this);
        String email = saveShared.getKey("email");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserModel userModel = snapshot.getValue(UserModel.class);
                                if (userModel != null) {
                                    etName.setText(userModel.name);
                                    etEmail.setText(userModel.email);
                                    etAlamat.setText("null".equals(userModel.alamat) ? "" : userModel.alamat);
                                    etKota.setText("null".equals(userModel.kota) ? "" : userModel.kota);
                                    etProvinsi.setText("null".equals(userModel.provinsi) ? "" : userModel.provinsi);
                                    etPostCode.setText("null".equals(userModel.postCode) ? "" : userModel.postCode);
                                    etTelpon.setText("null".equals(userModel.telpon) ? "" : userModel.telpon);
                                }
                                break; // hanya satu data user
                            }
                        } else {
                            System.out.println("tidak ada data");
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                        finish();
                    }
                });
    }

}
