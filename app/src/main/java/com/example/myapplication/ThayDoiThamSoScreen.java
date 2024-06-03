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
        luu_button = findViewById(R.id.luu_button);
        luu_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

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