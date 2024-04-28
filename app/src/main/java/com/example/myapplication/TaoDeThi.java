package com.example.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TaoDeThi extends AppCompatActivity {
    String[] mon_hoc_list = {"Nhập môn lập trình",
            "Lập trình hướng đối tượng", "Cấu trúc dữ liệu và giải thuật",
    "Kiến trúc máy tính", "Hệ điều hành", "Nhập môn mạng máy tính",
            "Cơ sở dữ liệu", "Tổ chức và cấu trúc máy tính", "Đại số tuyến tính",
        "Cấu trúc rời rạc", "Xác suất thống kê", "Giải tích", "Tư tưởng Hồ Chí Minh",
        "Pháp luật đại cương", "Triết học Mác - Lenin", "Kinh tế chính trị Mác - Lenin",
        "Chủ nghĩa xã hội khoa học", "Lịch sử Đảng Cộng sản Việt Nam"};
    Spinner spinner_mon_hoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
            }
        });
        spinner_mon_hoc = findViewById(R.id.tao_de_thi_mon_hoc_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mon_hoc_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mon_hoc.setAdapter(adapter);
    }
}