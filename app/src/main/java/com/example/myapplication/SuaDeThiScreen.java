package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fragment.TaoDeThiViewPagerAdapter2;
import my_interface.IPassingData;

public class SuaDeThiScreen extends AppCompatActivity implements IPassingData {

    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager;
    private TaoDeThiViewPagerAdapter2 viewPagerAdapter;
    private ArrayList<taodethicauhoiitem> mylist;
    private String madethi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sua_de_thi_screen);

        initializeUI();
        getDataFromIntent();

        setupBottomNavigationView();
        setupViewPager();
        setupOnBackPressed();
    }

    private void initializeUI() {
        // Adjust insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mylist = new ArrayList<>();
        ImageButton backButton = findViewById(R.id.sua_de_thi_icon_back);
        backButton.setOnClickListener(view -> finish());

        ImageButton saveButton = findViewById(R.id.luu_de_thi_button);
        saveButton.setOnClickListener(view -> saveDataToFirebase());
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            madethi = bundle.getString("madethi");
        }
    }

    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.sua_de_thi_bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_ngan_hang_cau_hoi:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.menu_cau_hoi_da_chon:
                    viewPager.setCurrentItem(1);
                    break;
            }
            return true;
        });
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.sua_de_thi_view_pager);
        viewPagerAdapter = new TaoDeThiViewPagerAdapter2(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.menu_ngan_hang_cau_hoi);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.menu_cau_hoi_da_chon);
                        break;
                }
            }
        });
    }

    private void setupOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });
    }

    private void saveDataToFirebase() {
        DatabaseReference dbDeThiCauHoi = FirebaseDatabase.getInstance().getReference("DETHICAUHOI");
        dbDeThiCauHoi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (madethi != null && !mylist.isEmpty()) {
                    updateOrAddQuestions(snapshot, dbDeThiCauHoi);
                    removeUnusedQuestions(snapshot, dbDeThiCauHoi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });
    }

    private void updateOrAddQuestions(DataSnapshot snapshot, DatabaseReference dbDeThiCauHoi) {
        for (taodethicauhoiitem item : mylist) {
            boolean exists = false;
            for (DataSnapshot data : snapshot.getChildren()) {
                if (data.child("maCH").getValue(String.class).equals(item.getMacauhoi())
                        && data.child("maDT").getValue(String.class).equals(madethi)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                String key = dbDeThiCauHoi.push().getKey();
                DETHICAUHOI dtCh = new DETHICAUHOI(key, madethi, item.getMacauhoi());
                dbDeThiCauHoi.child(key).setValue(dtCh);
            }
        }
    }

    private void removeUnusedQuestions(DataSnapshot snapshot, DatabaseReference dbDeThiCauHoi) {
        for (DataSnapshot data : snapshot.getChildren()) {
            if (data.child("maDT").getValue(String.class).equals(madethi)) {
                boolean found = false;
                for (taodethicauhoiitem item : mylist) {
                    if (data.child("maCH").getValue(String.class).equals(item.getMacauhoi())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    dbDeThiCauHoi.child(data.getKey()).removeValue();
                }
            }
        }
    }

    @Override
    public void PassData(ArrayList<taodethicauhoiitem> listCauHoi) {
        mylist = listCauHoi;
        for (taodethicauhoiitem item : mylist) {
            Log.e("Activity Data", item.getMacauhoi() + "   " + item.getNoidung());
        }
    }

    public String AccessMaDeThi() {
        return madethi;
    }
}
