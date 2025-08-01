package com.tiketku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tiketku.model.UserModel;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etPasswordConfirm;
    private Button btnSubmit;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v ->
                registerAccount(
                        etName.getText().toString().trim(),
                        etEmail.getText().toString().trim(),
                        etPassword.getText().toString(),
                        etPasswordConfirm.getText().toString()
                )
        );
    }

    private void registerAccount(String name, String email, String password, String passConfirm) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passConfirm.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(passConfirm)) {
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            etPassword.setError("Password tidak sama");
            etPasswordConfirm.setError("Password tidak sama");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Simpan nama ke Realtime Database
                            String uid = user.getUid();

                            UserModel userModel = new UserModel(name, email, "null", "null", "null", "null", "null");


                            dbRef.child(uid).setValue(userModel)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Gagal menyimpan nama: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Pendaftaran gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


}
