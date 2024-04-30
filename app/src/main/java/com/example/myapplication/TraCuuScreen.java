package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class TraCuuScreen extends AppCompatActivity {

    String ten_mon[] = {"Nhập môn lập trình","Toán cho khoa học máy tính", "Nhập môn Công nghệ phần mềm"};

    String ngay_tao[]  = {"23/4//2024", "24/4/2024", "25/4/2024"};

    String ma_de[] = {"001", "002", "003"};
    ArrayList<dethitracuuitem> mylist;
    DeThiTraCuuAdapter adapter;
    ListView listview;

    ImageButton quay_lai_main_screen;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tra_cuu_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tra_cuu_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quay_lai_main_screen = findViewById(R.id.tra_cuu_back_button);

        quay_lai_main_screen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(TraCuuScreen.this, MainScreen.class));
            }
        });

        mylist = new ArrayList<>();
        for (int i = 0; i <ten_mon.length;i++)
        {
            mylist.add(new dethitracuuitem(ten_mon[i], ngay_tao[i], ma_de[i]));
        }

        listview =  findViewById(R.id.ds_de_thi_lv);
        adapter = new DeThiTraCuuAdapter(this, R.layout.activity_tra_cuu_de_thi_list_view, mylist);
        listview.setAdapter(adapter);

    }
}