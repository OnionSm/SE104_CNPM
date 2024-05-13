package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaoDeThi extends AppCompatActivity
{

    DatabaseReference db_monhoc;
    DatabaseReference db_cauhoi;

    String hocky[] = {"1","2"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        db_cauhoi = FirebaseDatabase.getInstance().getReference("CAUHOI");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_de_thi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tao_de_thi), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton quay_lai_de_thi = findViewById(R.id.tao_de_thi_icon_back);
        quay_lai_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(TaoDeThi.this, DeThiScreen.class);
                startActivity(quay_lai_trang_chu_intent);
                finish();
            }
        });
        ImageButton thongtin = findViewById(R.id.mo_rong_button);
        thongtin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CreatePopUpThongTinMonHoc();
            }
        });
        setupOnBackPressed();
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
    private void CreatePopUpThongTinMonHoc()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_thong_tin_mon_hoc);

        Window window = dialog.getWindow();
        if(window == null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams window_attributes = window.getAttributes();
        window_attributes.gravity = Gravity.CENTER;
        window.setAttributes(window_attributes);

        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
        Spinner mon_hoc_spiner = dialog.findViewById(R.id.ten_mon_hoc_spiner);
        Spinner hoc_ky_spiner = dialog.findViewById(R.id.hoc_ky_spiner);
        EditText nam_hoc = dialog.findViewById(R.id.nam_hoc_edt);
        EditText thoi_luong_edt = dialog.findViewById(R.id.thoi_luong_edt);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(TaoDeThi.this, android.R.layout.simple_spinner_item, hocky);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hoc_ky_spiner.setAdapter(adapter);

        ArrayList<String> monhoc_list = new ArrayList<>();
        db_monhoc.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                String tenmon = snapshot.child("tenMH").getValue(String.class);
                monhoc_list.add(tenmon);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(TaoDeThi.this, android.R.layout.simple_spinner_item, monhoc_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mon_hoc_spiner.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
