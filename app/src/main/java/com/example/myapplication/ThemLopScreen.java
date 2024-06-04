package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThemLopScreen extends AppCompatActivity
{

    ArrayList<String> tenMonHocList = new ArrayList<>();
    Spinner spinner_mon_hoc;
    DatabaseReference db_monhoc;
    ArrayAdapter<String> adapter;
    EditText ten_lop ;
    EditText hoc_ky ;
    EditText nam_hoc ;
    EditText si_so ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_lop_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");

        ImageButton back_button = findViewById(R.id.icon_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        ten_lop = findViewById(R.id.ten_lop);
        hoc_ky = findViewById(R.id.hoc_ky);
        nam_hoc = findViewById(R.id.nam_hoc);
        si_so = findViewById(R.id.si_so);
        spinner_mon_hoc = findViewById(R.id.them_lop_spiner);
        ImageButton luu_lop = findViewById(R.id.luu_lop_button);
        luu_lop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UpLopToDataBase();
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenMonHocList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mon_hoc.setAdapter(adapter);

        GetDataMonHoc();
    }
    private void GetDataMonHoc()
    {
        db_monhoc.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    tenMonHocList.add(data.child("tenMH").getValue(String.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void UpLopToDataBase()
    {
        DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        String tenlop = ten_lop.getText().toString();
        String hocky = hoc_ky.getText().toString();
        String namhoc = nam_hoc.getText().toString();
        String siso = si_so.getText().toString();
        String monhoc = spinner_mon_hoc.getSelectedItem().toString();
        db_hknh.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    //if(String.valueOf(data.child("hocKy").getValue(Integer.class)).equals(hocky))
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}