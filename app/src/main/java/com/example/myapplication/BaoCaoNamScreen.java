package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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

import fragment.BaoCaoViewPagerAdapter;
import fragment.TaoDeThiViewPagerAdapter;

public class BaoCaoNamScreen extends AppCompatActivity
{
    private BottomNavigationView bottom_navigation_view;
    private ViewPager2 viewpager2;
    BaoCaoViewPagerAdapter view_pager_adapter;
    String namhoc;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bao_cao_nam_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        namhoc = "2023/2024";

        ImageButton quay_lai_de_thi = findViewById(R.id.bao_cao_icon_back);
        quay_lai_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(BaoCaoNamScreen.this, MainScreenNew.class);
                startActivity(quay_lai_trang_chu_intent);
                finish();
            }
        });

        bottom_navigation_view = findViewById(R.id.bao_cao_bottom_navigation_view);

        viewpager2 = findViewById(R.id.bao_cao_view_pager);
        view_pager_adapter = new BaoCaoViewPagerAdapter(this);
        viewpager2.setAdapter(view_pager_adapter);
        viewpager2.setPageTransformer(new DepthPageTransformer());

        bottom_navigation_view.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_baocao:
                        viewpager2.setCurrentItem(0);
                        break;
                    case R.id.menu_dethi:
                        viewpager2.setCurrentItem(1);
                        break;
                    case R.id.menu_baicham:
                        viewpager2.setCurrentItem(2);
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
                        bottom_navigation_view.setSelectedItemId(R.id.menu_baocao);
                        break;
                    case 1:
                        bottom_navigation_view.setSelectedItemId(R.id.menu_dethi);
                        break;
                    case 2:
                        bottom_navigation_view.setSelectedItemId(R.id.menu_baicham);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        //GetDataCauHoiFromFireBase();
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
                    startActivity(new Intent(BaoCaoNamScreen.this, MainScreenNew.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }

    public String AccessData()
    {
        return namhoc;
    }
}