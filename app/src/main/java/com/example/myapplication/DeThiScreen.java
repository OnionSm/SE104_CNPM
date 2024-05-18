package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fragment.NganHangCauHoiFragment;

public class DeThiScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_de_thi_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.de_thi_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton tao_de_thi = findViewById(R.id.de_thi_tao_de_thi_button);
        tao_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent tao_de_thi_intent = new Intent(DeThiScreen.this, ThongTinDeThiScreen.class);
                startActivity(tao_de_thi_intent);
                finish();
            }
        });

        ImageButton ds_de_thi = findViewById(R.id.tao_de_thi_danh_sach_button);
        ds_de_thi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeThiScreen.this,DanhSachDeThiScreen.class));
                finish();
            }
        });

        ImageButton quay_lai_trang_chu = findViewById(R.id.de_thi_icon_back);
        quay_lai_trang_chu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(DeThiScreen.this, MainScreenNew.class);
                startActivity(quay_lai_trang_chu_intent);
                finish();
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
                    startActivity(new Intent(DeThiScreen.this, MainScreenNew.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
}