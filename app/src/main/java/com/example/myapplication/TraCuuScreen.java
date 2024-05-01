package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TraCuuScreen extends AppCompatActivity {

    String ten_mon[] = {"Nhập môn lập trình","Toán cho khoa học máy tính", "Nhập môn Công nghệ phần mềm"};

    String ngay_tao[]  = {"23/4//2024", "24/4/2024", "25/4/2024"};

    String ma_de[] = {"001", "002", "003"};
    ArrayList<dethitracuuitem> mylist;
    DeThiTraCuuAdapter adapter;
    RecyclerView dethi_rcv;

    SearchView searview;

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

        dethi_rcv = findViewById(R.id.ds_de_thi_rcv);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(this);
        dethi_rcv.setLayoutManager(ln_layout_manager);
        adapter = new DeThiTraCuuAdapter(mylist);
        dethi_rcv.setAdapter(adapter);

        RecyclerView.ItemDecoration item_decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dethi_rcv.addItemDecoration(item_decoration);
        searview = findViewById(R.id.tra_cuu_search_view);
        searview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


}