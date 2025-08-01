package com.tiketku.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tiketku.EditProfileActivity;
import com.tiketku.MainActivity;
import com.tiketku.R;
import com.tiketku.RiwayatActivity;
import com.tiketku.model.UserModel;
import com.tiketku.shared.SaveShared;

public class ProfileFragment extends Fragment {
    private View view;
    private LinearLayout btnLogout, btnEdit;
    private SaveShared shared;
    private TextView textName;
    private LinearLayout llRiwayat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        shared = new SaveShared(getContext());
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            shared.setKey("email","");

            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        });
        
        llRiwayat = view.findViewById(R.id.llRiwayat);
        llRiwayat.setOnClickListener(v->{
            startActivity(new Intent(getContext(), RiwayatActivity.class));
            getActivity();
        });
        textName = view.findViewById(R.id.textName);
        btnEdit = view.findViewById(R.id.btnEditProfile);
        btnEdit.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), EditProfileActivity.class));
            getActivity();
        });
        loadProfile();
        return view;
    }

    private void loadProfile() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("email").equalTo(shared.getKey("email"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserModel userModel = snapshot.getValue(UserModel.class);
                                if (userModel != null) {
                                    textName.setText(userModel.name);
                                }
                                break; // karena hanya satu user yang cocok
                            }
                        } else {
                            Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Tidak ada koneksi", Toast.LENGTH_SHORT).show();
                        databaseError.toException().printStackTrace();
                    }
                });
    }

}
