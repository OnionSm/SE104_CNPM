package com.example.myapplication;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import fragment.TaoDeThiViewPagerAdapter;
import my_interface.IPassingData;

public class TaoDeThi extends AppCompatActivity implements IPassingData
{

    private BottomNavigationView bottom_navigation_view;
    private ViewPager2 viewpager2;

    TaoDeThiViewPagerAdapter view_pager_adapter;
    DatabaseReference db_monhoc;
    String monhoc;
    String hocky;
    String namhoc;
    String thoiluong;

    ArrayList<taodethicauhoiitem> mylist;

    String key_mamh;
    String key_user;
    String key_mahknh;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_de_thi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tao_de_thi), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");


        ImageButton quay_lai_de_thi = findViewById(R.id.tao_de_thi_icon_back);
        quay_lai_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(TaoDeThi.this, ThongTinDeThiScreen.class);
                startActivity(quay_lai_trang_chu_intent);
                finish();
            }
        });


        GetDataFromIntent();
        GetMaMH();
        GetUser();
        GetMaHK();


        ImageButton tao_de_thi = findViewById(R.id.tao_de_button);
        tao_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
                DatabaseReference db_dethi_cauhoi = FirebaseDatabase.getInstance().getReference("DETHICAUHOI");

                String key_dt = db_dethi.push().getKey();
                DETHI dt = new DETHI(key_dt,Integer.parseInt(thoiluong),key_mahknh,"",key_mamh,key_user);
                db_dethi.child(key_dt).setValue(dt);
                for(int i = 0 ;i < mylist.size(); i++)
                {
                    DETHICAUHOI dt_ch = new DETHICAUHOI(key_dt,mylist.get(i).getMacauhoi());
                    String key_dtch = db_dethi_cauhoi.push().getKey();
                    db_dethi_cauhoi.child(key_dtch).setValue(dt_ch);
                }
            }
        });

        bottom_navigation_view = findViewById(R.id.tao_de_thi_bottom_navigation_view);

        viewpager2 = findViewById(R.id.tao_de_thi_view_pager);
        view_pager_adapter = new TaoDeThiViewPagerAdapter(this);
        viewpager2.setAdapter(view_pager_adapter);
        viewpager2.setPageTransformer(new ZoomOutPageTransformer());

        bottom_navigation_view.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_ngan_hang_cau_hoi:
                        viewpager2.setCurrentItem(0);
                        break;
                    case R.id.menu_cau_hoi_da_chon:
                        viewpager2.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        bottom_navigation_view.setSelectedItemId(R.id.menu_ngan_hang_cau_hoi);
                        break;
                    case 1:
                        bottom_navigation_view.setSelectedItemId(R.id.menu_cau_hoi_da_chon);
                        break;
                }
                super.onPageSelected(position);
            }
        });
        setupOnBackPressed();
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
                    startActivity(new Intent(TaoDeThi.this, ThongTinDeThiScreen.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }

    private void GetDataFromIntent()
    {
        Intent thong_tin_de_thi_intent = getIntent();
        Bundle bundle_get = new Bundle();
        bundle_get = thong_tin_de_thi_intent.getBundleExtra("thongtinmonhoc");
        monhoc = bundle_get.getString("monhoc");
        hocky = bundle_get.getString("hocky");
        namhoc = bundle_get.getString("namhoc");
        thoiluong = bundle_get.getString("thoiluong");
    }

    public String AccessData()
    {
        return monhoc;
    }



    @Override
    public void PassData(ArrayList<taodethicauhoiitem> list_cau_hoi)
    {
        mylist = list_cau_hoi;
        for(int i = 0 ;i < mylist.size(); i++)
        {
            Log.e("Giá trị trong activity", String.valueOf(mylist.get(i).getMacauhoi())+ "   " +String.valueOf(mylist.get(i).getNoidung()));
        }

    }

    private void GetMaMH()
    {
        monhoc = monhoc.toLowerCase();
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        db_monhoc.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    String tenmh = data.child("tenMH").getValue(String.class).toLowerCase();
                    if(tenmh.contains(monhoc))
                    {
                        key_mamh = data.getKey().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
    private void GetMaHK()
    {
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        db_hknh.addValueEventListener(new ValueEventListener()
        {
            String nam1 = namhoc.split("/")[0];
            String nam2 = namhoc.split("/")[1];
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    if (String.valueOf(data.child("hocKy").getValue(Integer.class)).equals(hocky) &&
                            String.valueOf(data.child("nam1").getValue(Integer.class)).equals(nam1) &&
                            String.valueOf(data.child("nam2").getValue(Integer.class)).equals(nam2))
                    {
                        key_mahknh = data.getKey().toString();
                        return;
                    }
                }
                String ma_hknh = db_hknh.push().getKey().toString();
                HKNH hk = new HKNH(ma_hknh,Integer.parseInt(hocky),Integer.parseInt(nam1),Integer.parseInt(nam2));
                db_hknh.child(ma_hknh).setValue(hk);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetUser()
    {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        db_pdn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                key_user = snapshot.child("account").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
