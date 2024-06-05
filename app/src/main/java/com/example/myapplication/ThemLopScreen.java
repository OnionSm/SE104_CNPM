package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemLopScreen extends AppCompatActivity {

    ArrayList<String> tenMonHocList = new ArrayList<>();
    Spinner spinner_mon_hoc;
    DatabaseReference db_monhoc;
    ArrayAdapter<String> adapter;
    EditText ten_lop;
    EditText hoc_ky;
    EditText nam_hoc;
    EditText si_so;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        back_button.setOnClickListener(v -> finish());

        ten_lop = findViewById(R.id.ten_lop);
        hoc_ky = findViewById(R.id.hoc_ky);
        nam_hoc = findViewById(R.id.nam_hoc);
        si_so = findViewById(R.id.si_so);
        spinner_mon_hoc = findViewById(R.id.them_lop_spiner);

        ImageButton luu_lop = findViewById(R.id.luu_lop_button);
        luu_lop.setOnClickListener(v -> UpLopToDataBase());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenMonHocList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mon_hoc.setAdapter(adapter);

        GetDataMonHoc();
        setupOnBackPressed();
    }

    // Lấy dữ liệu môn học cho spinner
    private void GetDataMonHoc()
    {
        db_monhoc.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenMonHocList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot data : snapshot.getChildren()) {
                    String tenMH = data.child("tenMH").getValue(String.class);
                    if (tenMH != null) {
                        tenMonHocList.add(tenMH);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThemLopScreen.this, "Failed to load subjects", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpLopToDataBase() {
        DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");

        String tenlop = ten_lop.getText().toString().trim();
        String hocky = hoc_ky.getText().toString().trim();
        String namhoc = nam_hoc.getText().toString().trim();
        String siso = si_so.getText().toString().trim();
        String monhoc = spinner_mon_hoc.getSelectedItem().toString();

        if (tenlop.isEmpty() || hocky.isEmpty() || namhoc.isEmpty() || siso.isEmpty() || monhoc.isEmpty()) {
            Toast.makeText(ThemLopScreen.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!hocky.equals("1") && !hocky.equals("2")) {
            Toast.makeText(ThemLopScreen.this, "Học kỳ phải là 1 hoặc 2", Toast.LENGTH_SHORT).show();
            return;
        }

        String regex = "^\\d{4}/\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(namhoc);
        if (!matcher.matches()) {
            Toast.makeText(ThemLopScreen.this, "Năm học không đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        }

        int siSoValue;
        try {
            siSoValue = Integer.parseInt(siso);
        } catch (NumberFormatException e) {
            Toast.makeText(ThemLopScreen.this, "Sỉ số không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (siSoValue < 0) {
            Toast.makeText(ThemLopScreen.this, "Sỉ số không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] namHocParts = namhoc.split("/");
        String nam1 = namHocParts[0];
        String nam2 = namHocParts[1];

        db_hknh.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean foundHKNH = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    String mahknh = data.getKey();
                    if (mahknh != null &&
                            String.valueOf(data.child("hocKy").getValue(Integer.class)).equals(hocky) &&
                            String.valueOf(data.child("nam1").getValue(Integer.class)).equals(nam1) &&
                            String.valueOf(data.child("nam2").getValue(Integer.class)).equals(nam2)) {
                        foundHKNH = true;
                        db_monhoc.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean foundMonHoc = false;
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    String tenmon = data.child("tenMH").getValue(String.class);
                                    if (tenmon != null && tenmon.equals(monhoc)) {
                                        foundMonHoc = true;
                                        String mamh = data.getKey();
                                        String key = db_lop.push().getKey();
                                        if (key == null) {
                                            Toast.makeText(ThemLopScreen.this, "Failed to generate class key", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        LOP lop = new LOP(key, tenlop, mahknh, siSoValue, mamh, "");
                                        db_lop.child(key).setValue(lop).addOnCompleteListener(task -> {
                                            Toast.makeText(ThemLopScreen.this, "Thêm lớp thành công", Toast.LENGTH_SHORT).show();
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(ThemLopScreen.this, "Thêm lớp thất bại", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                }
                                if (!foundMonHoc) {
                                    Toast.makeText(ThemLopScreen.this, "Môn học không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ThemLopScreen.this, "Failed to read subject data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                if (!foundHKNH) {
                    Toast.makeText(ThemLopScreen.this, "Học kỳ năm học không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThemLopScreen.this, "Failed to read semester data", Toast.LENGTH_SHORT).show();
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
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
}
