package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import fragment.TaoDeThiViewPagerAdapter2;
import my_interface.IPassingData;

public class SuaDeThiScreen extends AppCompatActivity implements IPassingData
{

    private BottomNavigationView bottom_navigation_view;
    private ViewPager2 viewpager2;

    TaoDeThiViewPagerAdapter2 view_pager_adapter;
    DatabaseReference db_monhoc;
    String monhoc;
    String hocky;
    String namhoc;
    String thoiluong;

    ArrayList<taodethicauhoiitem> mylist;

    String key_mamh;
    String key_user;
    String key_mahknh;

    String madethi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sua_de_thi_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton quay_lai_de_thi = findViewById(R.id.sua_de_thi_icon_back);
        quay_lai_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        GetDataFromIntent();

        ImageButton luu_de_thi = findViewById(R.id.luu_de_thi_button);
        luu_de_thi.setOnClickListener(new View.OnClickListener()
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


        bottom_navigation_view = findViewById(R.id.sua_de_thi_bottom_navigation_view);

        viewpager2 = findViewById(R.id.sua_de_thi_view_pager );
        view_pager_adapter = new TaoDeThiViewPagerAdapter2(this);
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
        Bundle bundle_get = thong_tin_de_thi_intent.getBundleExtra("data");
        if(bundle_get != null)
        {
            madethi = bundle_get.getString("madethi");
        }
    }

    public String AccessMaDeThi()
    {
        return madethi;
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
}