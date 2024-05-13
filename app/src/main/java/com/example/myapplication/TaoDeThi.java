package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fragment.TaoDeThiViewPagerAdapter;
import fragment.ViewPager2Adapter;

public class TaoDeThi extends AppCompatActivity
{

    private BottomNavigationView bottom_navigation_view;
    private ViewPager2 viewpager2;

    TaoDeThiViewPagerAdapter view_pager_adapter;
    DatabaseReference db_monhoc;
    private String monhoc;

    private String hocky;

    String hocky_list[] = {"1","2"};

    ArrayList<taodethicauhoiitem> mylist;


    RecyclerView tao_de_thi_rcv;

    TaoDeThiAdapter adapter;

    SearchView searchview;

    ImageButton ds_cau_hoi_da_chon_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_de_thi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tao_de_thi), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton quay_lai_de_thi = findViewById(R.id.tao_de_thi_icon_back);
        quay_lai_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(TaoDeThi.this, DeThiScreen.class);
                startActivity(quay_lai_trang_chu_intent);
                finish();
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
                    startActivity(new Intent(TaoDeThi.this, DeThiScreen.class));
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
}
