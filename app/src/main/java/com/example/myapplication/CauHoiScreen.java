package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CauHoiScreen extends AppCompatActivity {

    private ImageButton ds_cau_hoi_da_tao_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cau_hoi_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cau_hoi), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton tao_cau_hoi = findViewById(R.id.cau_hoi_tao_cau_hoi_button);
        tao_cau_hoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent them_cau_hoi_intent = new Intent(CauHoiScreen.this, ThemCauHoi.class);
                startActivity(them_cau_hoi_intent);
            }
        });
        ImageButton quay_lai_trang_chu = findViewById(R.id.tao_cau_hoi_icon_back);
        quay_lai_trang_chu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(CauHoiScreen.this, MainScreen.class);
                startActivity(quay_lai_trang_chu_intent);
            }
        });

        ds_cau_hoi_da_tao_button = (ImageButton) findViewById(R.id.tao_cau_hoi_danh_sach_button);
        ds_cau_hoi_da_tao_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(CauHoiScreen.this, DsCauHoiDaTao.class));
            }
        });


    }
}