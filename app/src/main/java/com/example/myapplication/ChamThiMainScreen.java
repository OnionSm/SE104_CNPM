package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChamThiMainScreen extends AppCompatActivity
{

    ImageButton back_button;
    ImageButton cham_diem_button;
    ImageButton ds_bai_cham_button;
    ImageButton lop_hoc_button;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cham_thi_main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cham_diem_main_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back_button = findViewById(R.id.cham_diem_icon_back);
        ds_bai_cham_button = findViewById(R.id.thong_tin_cham_diem);
        lop_hoc_button = findViewById(R.id.lop_hoc_button);

        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        ds_bai_cham_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        lop_hoc_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ChamThiMainScreen.this, DanhSachLopScreen.class));
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
}