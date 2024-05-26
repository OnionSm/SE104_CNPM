package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThongTinDeThiScreen extends AppCompatActivity
{
    EditText mon_hoc_edt;
    ArrayList<String> list_mon_hoc;

    EditText hoc_ky_edt;
    EditText nam_hoc_edt;
    EditText thoi_luong_edt;
    ImageButton tao_de_thi_button;


    String monhoc;
    String hocky;
    String namhoc;
    String thoiluong;

    int thoiluongtoithieu;
    int thoiluongtoida;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_tin_de_thi_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.thongtindethi), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        thoiluongtoithieu = -1;
        thoiluongtoida = -1;


        ImageButton back_button = findViewById(R.id.tao_de_thi_icon_back);
        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ThongTinDeThiScreen.this, DeThiScreen.class));
            }
        });

        mon_hoc_edt = findViewById(R.id.tenmonhoc_edt);


        hoc_ky_edt = findViewById(R.id.hoc_ky_edt);
        nam_hoc_edt = findViewById(R.id.nam_hoc_edt);
        thoi_luong_edt = findViewById(R.id.thoi_luong_edt);
        tao_de_thi_button = findViewById(R.id.tao_de_thi_button);



        tao_de_thi_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TriggerThoiLuong(new OnCompleteGetData()
                {
                    @Override
                    public void onCompleteGetData()
                    {
                        if(CheckValidInput())
                        {

                            Log.e("đã check", "OK");
                            Intent intent = new Intent(ThongTinDeThiScreen.this, TaoDeThi.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("monhoc", monhoc);
                            bundle.putString("hocky", hocky);
                            bundle.putString("namhoc", namhoc);
                            bundle.putString("thoiluong", thoiluong);
                            intent.putExtra("thongtinmonhoc",bundle);
                            startActivity(intent);
                        }
                    }
                });

            }
        });

        setupOnBackPressed();
    }


    private Boolean CheckValidInput()
    {
        monhoc = mon_hoc_edt.getText().toString();
        hocky = hoc_ky_edt.getText().toString();
        namhoc = nam_hoc_edt.getText().toString();
        thoiluong = thoi_luong_edt.getText().toString();
        if(hocky.equals("") || namhoc.equals("") || thoiluong.equals(""))
        {
            Toast.makeText(ThongTinDeThiScreen.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
            return false;
        }
        if(hocky.equals("1") == false && hocky.equals("2") == false)
        {
            Toast.makeText(ThongTinDeThiScreen.this, "Học kỳ phải là 1 hoặc 2",Toast.LENGTH_SHORT).show();
            Log.e("hocky", hocky);
            return false;
        }
        if (!namhoc.matches("\\d{4}/\\d{4}"))
        {
            Toast.makeText(ThongTinDeThiScreen.this, "Năm học phải có định dạng xxxx/xxxx", Toast.LENGTH_SHORT).show();
            Log.e("namhoc", namhoc);
            return false;
        }
        if(Integer.parseInt(thoiluong) < thoiluongtoithieu || Integer.parseInt(thoiluong) > thoiluongtoida)
        {
            Toast.makeText(ThongTinDeThiScreen.this, "Thời lượng phải lớn hơn " + String.valueOf(thoiluongtoithieu) + " và bé hơn " + String.valueOf(thoiluongtoida) ,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void TriggerThoiLuong(OnCompleteGetData callback)
    {
        DatabaseReference db_thamso = FirebaseDatabase.getInstance().getReference("THAMSO");
        db_thamso.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    if (data.getKey().equals("thoiluongthitoithieu"))
                    {
                        thoiluongtoithieu = data.child("giaTri").getValue(Integer.class);
                        Log.e("tối thiểu", String.valueOf(thoiluongtoithieu));
                    }
                    if (data.getKey().equals("thoiluongthitoida"))
                    {
                        thoiluongtoida = data.child("giaTri").getValue(Integer.class);
                        Log.e("tối đa", String.valueOf(thoiluongtoida));
                    }
                    if(thoiluongtoithieu != -1 && thoiluongtoida != -1)
                    {
                        Log.e("Ok","OK");
                        callback.onCompleteGetData();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
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
                    startActivity(new Intent(ThongTinDeThiScreen.this, DeThiScreen.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                }
            }
        });
    }

    interface OnCompleteGetData
    {
        void onCompleteGetData();
    }

}