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

public class DeThi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dethi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.de_thi), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton tao_de_thi = findViewById(R.id.de_thi_tao_de_thi_button);
        tao_de_thi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent tao_de_thi_intent = new Intent(DeThi.this, TaoDeThi.class);
                startActivity(tao_de_thi_intent);
            }
        });
        ImageButton quay_lai_trang_chu = findViewById(R.id.de_thi_icon_back);
        quay_lai_trang_chu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(DeThi.this, MainScreen.class);
                startActivity(quay_lai_trang_chu_intent);
            }
        });
    }
}