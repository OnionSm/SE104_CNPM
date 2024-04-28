package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ThemCauHoi extends AppCompatActivity {

    DatabaseReference dbRef;
    ArrayList<String> tenMonHocList = new ArrayList<>();
    Spinner spinner_mon_hoc;

    DatabaseReference dbRef_2;
    ArrayList<String> do_kho_list = new ArrayList<>();
    Spinner spinner_do_kho;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_cau_hoi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.them_cau_hoi), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView quay_lai_cau_hoi = findViewById(R.id.them_cau_hoi_icon_back);
        quay_lai_cau_hoi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(ThemCauHoi.this, CauHoiScreen.class);
                startActivity(quay_lai_trang_chu_intent);
            }
        });
        dbRef = FirebaseDatabase.getInstance().getReference("MONHOC");
        spinner_mon_hoc = findViewById(R.id.tao_cau_hoi_mon_hoc_spiner);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tenMonHocList.clear();
                for (DataSnapshot monHocSnapshot : dataSnapshot.getChildren()) {
                    String tenMonHoc = monHocSnapshot.child("tenMH").getValue(String.class);
                    tenMonHocList.add(tenMonHoc);
                }
                updateSpinner();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Lỗi", databaseError.toException());
            }
        });
        dbRef_2 = FirebaseDatabase.getInstance().getReference("DOKHO");
        spinner_do_kho = findViewById(R.id.tao_cau_hoi_do_kho_spiner);
        dbRef_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                do_kho_list.clear();
                for (DataSnapshot doKhoSnapshot : dataSnapshot.getChildren()) {
                    String doKho = doKhoSnapshot.child("TenDK").getValue(String.class);
                    do_kho_list.add(doKho);
                }
                updateSpinner1();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Lỗi", databaseError.toException());
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenMonHocList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mon_hoc.setAdapter(adapter);
        ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, do_kho_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_do_kho.setAdapter(adapter_2);

    }
    private void updateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ThemCauHoi.this, android.R.layout.simple_spinner_item, tenMonHocList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mon_hoc.setAdapter(adapter);
    }
    private void updateSpinner1() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ThemCauHoi.this, android.R.layout.simple_spinner_item, do_kho_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_do_kho.setAdapter(adapter);
    }
}