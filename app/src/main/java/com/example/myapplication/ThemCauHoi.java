package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ThemCauHoi extends AppCompatActivity {

    String[] mon_hoc_list = {"Nhập môn lập trình",
            "Lập trình hướng đối tượng",
            "Toán cho khoa học máy tính",
            "Phân tích và thiết kế thuật toán",
            "Nhập môn công nghệ phần mềm"};

    String[] do_kho_list = {"Dễ", "Trung Bình", "Phức tạp", "Khó"};
    Spinner spinner_mon_hoc;

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

        spinner_mon_hoc = findViewById(R.id.tao_cau_hoi_mon_hoc_spiner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mon_hoc_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mon_hoc.setAdapter(adapter);

        spinner_do_kho = findViewById(R.id.tao_cau_hoi_do_kho_spiner);
        ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, do_kho_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_do_kho.setAdapter(adapter_2);

    }
}