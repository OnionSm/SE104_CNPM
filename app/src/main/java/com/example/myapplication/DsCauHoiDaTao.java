package com.example.myapplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DsCauHoiDaTao extends AppCompatActivity {

    private String ten_mon[] = {"Nhập môn lập trình","Toán cho khoa học máy tính", "Nhập môn Công nghệ phần mềm"};
    private String mota[] = {"Môn này nên học", "Học rồi không biết đã học hay chưa", "Rất là okela"};

    private String do_kho[] = {"Dễ", "Khó vcl", "Khó"};

    private String ngay_tao[] = {"23/4//2024", "24/4/2024", "25/4/2024"};

    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ds_cau_hoi_da_tao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ds_cau_hoi_da_tao), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listview = (ListView) findViewById(R.id.ds_cau_hoi_da_tao);
        CustomListViewBase custom_list_view_base = new CustomListViewBase(getApplicationContext(),ten_mon, mota, do_kho, ngay_tao);
        listview.setAdapter(custom_list_view_base);
    }
}