package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ThayDoiThamSoScreen extends AppCompatActivity
{

    ImageButton back_button;
    EditText diem_toi_thieu_edt;
    EditText diem_toi_da_edt;
    EditText so_cau_toi_da_edt;
    EditText thoi_luong_toi_thieu_edt;
    EditText thoi_luong_toi_da_edt;
    ImageButton luu_button;

    ImageButton dtt_update;
    ImageButton dtt_ok;
    ImageButton dtd_update;
    ImageButton dtd_ok;
    ImageButton sctd_update;
    ImageButton sctd_ok;
    ImageButton tltt_update;
    ImageButton tltt_ok;
    ImageButton tltd_update;
    ImageButton tltd_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thay_doi_tham_so_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.thay_doi_tham_so), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back_button = findViewById(R.id.thay_doi_tham_so_icon_back);
        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        diem_toi_thieu_edt = findViewById(R.id.diem_toi_thieu_edt);
        diem_toi_da_edt = findViewById(R.id.diem_toi_da_edt);
        so_cau_toi_da_edt = findViewById(R.id.so_cau_toi_da_edt);
        thoi_luong_toi_thieu_edt = findViewById(R.id.thoi_luong_toi_thieu_edt);
        thoi_luong_toi_da_edt = findViewById(R.id.thoi_luong_toi_da_edt);
        luu_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        dtt_update = findViewById(R.id.dtt_update);
        dtt_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

            }
        });
        dtt_ok = findViewById(R.id.dtt_ok);
        dtt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dtd_update = findViewById(R.id.dtd_update);
        dtd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dtd_ok = findViewById(R.id.dtt_ok);
        dtd_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sctd_update = findViewById(R.id.sctd_update);
        sctd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sctd_ok = findViewById(R.id.sctd_ok);
        sctd_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        tltt_update = findViewById(R.id.tltt_update);
        tltt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tltt_ok = findViewById(R.id.tltt_ok);
        tltt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tltd_update = findViewById(R.id.tltd_update);
        tltd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tltd_ok = findViewById(R.id.tltt_ok);
        tltd_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
}