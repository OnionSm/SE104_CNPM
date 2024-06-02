package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThemMonHocScreen extends AppCompatActivity
{

    EditText mamh_edt;
    EditText tenmh_edt;
    EditText noidung_edt;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_mon_hoc_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mamh_edt = findViewById(R.id.ma_mon_hoc);
        tenmh_edt = findViewById(R.id.ten_mon_hoc);
        noidung_edt = findViewById(R.id.noi_dung_mon_hoc);

        ImageButton back_button = findViewById(R.id.them_mon_hoc_icon_back);
        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        ImageButton luu_mon_hoc_button = findViewById(R.id.luu_mon_hoc_button);
        luu_mon_hoc_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckValidMonHoc();
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
                if (isEnabled())
                {
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
    private void CheckValidMonHoc()
    {
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        String ma_mon_hoc = mamh_edt.getText().toString();
        String ten_mon_hoc = tenmh_edt.getText().toString();
        String mo_ta = noidung_edt.getText().toString();
        if(ma_mon_hoc.isEmpty() || ten_mon_hoc.isEmpty() || mo_ta.isEmpty())
        {
            Toast.makeText(ThemMonHocScreen.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
        else
        {
            db_monhoc.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for(DataSnapshot data : snapshot.getChildren())
                    {
                        if(data.getKey().equals(ma_mon_hoc) || data.child("tenMH").getValue(String.class).equals(ten_mon_hoc))
                        {
                            Toast.makeText(ThemMonHocScreen.this, "Môn học này đã tồn tại", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    MONHOC monhoc = new MONHOC(ma_mon_hoc, ten_mon_hoc,mo_ta);
                    db_monhoc.child(ma_mon_hoc).setValue(monhoc).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            Toast.makeText(ThemMonHocScreen.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(ThemMonHocScreen.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    Toast.makeText(ThemMonHocScreen.this, "Không tìm thấy dữ liệu môn học", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}