package com.example.myapplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class DsCauHoiDaTao extends AppCompatActivity {
    String stt[] = {"1","2","3"};

    String ten_mon[] = {"Nhập môn lập trình","Toán cho khoa học máy tính", "Nhập môn Công nghệ phần mềm"};
    String mo_ta[] = {"Môn này nên học", "Học rồi không biết đã học hay chưa", "Rất là okela"};

    String do_kho[] = {"Dễ", "Khó vcl", "Khó"};

    String ngay_tao[]  = {"23/4//2024", "24/4/2024", "25/4/2024"};

    ArrayList<cauhoiitem> mylist;
    CustomListViewBase custom_list_view_base;
    ListView listview;

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
        mylist = new ArrayList<>();
        for (int i = 0; i <stt.length;i++)
        {
            mylist.add(new cauhoiitem(stt[i],ten_mon[i],mo_ta[i], do_kho[i],ngay_tao[i]));
        }

        listview =  findViewById(R.id.danh_sach_cau_hoi_list_view);

        custom_list_view_base = new CustomListViewBase(this, R.layout.activity_custom_list_view, mylist);
        listview.setAdapter(custom_list_view_base);
    }
}