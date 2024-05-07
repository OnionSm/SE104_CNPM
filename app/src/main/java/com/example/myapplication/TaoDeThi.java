package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaoDeThi extends AppCompatActivity {
    DatabaseReference dbRef;
    ArrayList<String> tenMonHocList = new ArrayList<>();
    Spinner spinnerMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_de_thi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tao_de_thi), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton quay_lai_de_thi = findViewById(R.id.tao_de_thi_icon_back);
        quay_lai_de_thi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(TaoDeThi.this, DeThiScreen.class);
                startActivity(quay_lai_trang_chu_intent);
                finish();
            }
        });

        dbRef = FirebaseDatabase.getInstance().getReference("MONHOC");

        spinnerMonHoc = findViewById(R.id.tao_de_thi_mon_hoc_spinner);

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

        spinnerMonHoc = findViewById(R.id.tao_de_thi_mon_hoc_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenMonHocList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonHoc.setAdapter(adapter);

        setupOnBackPressed();
    }

    private void updateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(TaoDeThi.this, android.R.layout.simple_spinner_item, tenMonHocList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonHoc.setAdapter(adapter);
    }
    private void setupOnBackPressed()
    {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if(isEnabled())
                {
                    startActivity(new Intent(TaoDeThi.this, DeThiScreen.class));
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
}
