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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tiketku.OpenActivity;
import com.tiketku.R;
import com.tiketku.adapter.AdapterProduct;
import com.tiketku.model.TiketModel;
import com.tiketku.shared.SaveShared;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private AdapterProduct adapterProduct;
    ArrayList<TiketModel> array;
    private SaveShared shared;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        shared = new SaveShared(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        array = new ArrayList<>();
        adapterProduct = new AdapterProduct(getContext(), array, new AdapterProduct.OnClickListener() {
            @Override
            public void onClick(int position, TiketModel productModel) {
                Intent intent = new Intent(getContext(), OpenActivity.class);
                intent.putExtra("key", productModel.getKey());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapterProduct);
        loadTiket();
        return view;
    }

    private void loadTiket() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tickets");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                array.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TiketModel tiketModel = snapshot.getValue(TiketModel.class);

                        if (tiketModel != null) {
                            tiketModel.setKey(snapshot.getKey()); // simpan key push dari Firebase
                            array.add(tiketModel);
                        }
                    }
                    adapterProduct.notifyDataSetChanged(); // dipanggil setelah semua data masuk
                } else {
                    Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Tidak ada koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
