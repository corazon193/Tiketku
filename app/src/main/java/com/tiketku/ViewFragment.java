package com.tiketku;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tiketku.fragment.CartFragment;
import com.tiketku.fragment.HomeFragment;
import com.tiketku.fragment.ProfileFragment;
import com.tiketku.shared.SaveShared;
import com.nafis.bottomnavigation.NafisBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ViewFragment extends AppCompatActivity {
    private NafisBottomNavigation bottomNavifs;
    private static final int ID_HOME = 1;
    private static final int ID_CART = 2;
    private static final int ID_PERSON = 3;
    private FrameLayout frameLayout;
    private SaveShared shared;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfragment);

        shared = new SaveShared(this);
        loadCountCart();
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavifs = findViewById(R.id.bottomNafis);
        bottomNavifs.setSelectedIconColor(ContextCompat.getColor(this, R.color.blue));
        bottomNavifs.add(new NafisBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        bottomNavifs.add(new NafisBottomNavigation.Model(ID_CART, R.drawable.ic_cart));
        bottomNavifs.add(new NafisBottomNavigation.Model(ID_PERSON, R.drawable.ic_person));

        bottomNavifs.setOnClickMenuListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                loadReplaceFragment(model.getId());
                return null;
            }
        });

        bottomNavifs.setOnReselectListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                Toast.makeText(ViewFragment.this, "Item "+model.getId()+" reselected", Toast.LENGTH_SHORT).show();
                return null;
            }
        });
        loadReplaceFragment(ID_HOME);
        bottomNavifs.show(ID_HOME, true);
    }

    private void loadReplaceFragment(int item) {
        Fragment selectedFragment = null;

        switch (item) {
            case ID_HOME:
                selectedFragment = new HomeFragment();
                break;
            case ID_CART:
                // Tambahkan CartFragment kalau sudah ada
                selectedFragment = new CartFragment(); // Ganti dengan CartFragment()
                break;
            case ID_PERSON:
                // Tambahkan PersonFragment kalau sudah ada
                selectedFragment = new ProfileFragment(); // Ganti dengan PersonFragment()
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, selectedFragment)
                    .commit();

        }


    }

    private void loadCountCart() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cart"); // ubah ke path cart yang benar

        String currentUserEmail = shared.getKey("email");

        databaseReference.orderByChild("email").equalTo(currentUserEmail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = (int) snapshot.getChildrenCount(); // bisa langsung pakai ini
                        bottomNavifs.setCount(ID_CART, String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Tidak ada koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}