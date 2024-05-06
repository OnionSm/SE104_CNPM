package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragment.TrangChuFragment;
import fragment.ViewPager2Adapter;

public class MainScreenNew extends AppCompatActivity
{
    private BottomNavigationView bottom_navigation_view;
    private ViewPager2 viewpager2;

    ViewPager2Adapter view_pager_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainscreennew), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottom_navigation_view = findViewById(R.id.main_scr_bottom_navigation_view);

        viewpager2 = findViewById(R.id.main_scr_view_pager);
        view_pager_adapter = new ViewPager2Adapter(this);
        viewpager2.setAdapter(view_pager_adapter);
        viewpager2.setPageTransformer(new DepthPageTransformer());
        bottom_navigation_view.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_thongbao:
                        viewpager2.setCurrentItem(0);
                        break;
                    case R.id.menu_trangchu:
                        viewpager2.setCurrentItem(1);
                        break;
                    case R.id.menu_hoso:
                        viewpager2.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });


    }

}