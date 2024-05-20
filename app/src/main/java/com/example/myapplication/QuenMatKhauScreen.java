package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QuenMatKhauScreen extends AppCompatActivity {
    EditText edtNhapMatKhauMoi;
    EditText edtNhapLaiMatKhauMoi;
    ImageButton btnDatLaiMatKhau;

    FirebaseAuth mAuth;
    DatabaseReference giangVienRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau_screen);

        edtNhapMatKhauMoi = findViewById(R.id.edt_nhap_mat_khau_moi);
        edtNhapLaiMatKhauMoi = findViewById(R.id.edt_nhap_lai_mat_khau_moi);
        btnDatLaiMatKhau = findViewById(R.id.button_dat_lai_mat_khau);

        mAuth = FirebaseAuth.getInstance();
        giangVienRef = FirebaseDatabase.getInstance().getReference("GIANGVIEN");

        String phoneNumber = getIntent().getStringExtra("phone_number");

        btnDatLaiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edtNhapMatKhauMoi.getText().toString().trim();
                String confirmPassword = edtNhapLaiMatKhauMoi.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(QuenMatKhauScreen.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(QuenMatKhauScreen.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(phoneNumber, newPassword);
                }
            }
        });
    }

    private void resetPassword(String phoneNumber, String newPassword) {
        Query query = giangVienRef.orderByChild("sdt").equalTo(phoneNumber);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        userSnapshot.getRef().child("matKhau").setValue(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(QuenMatKhauScreen.this, "Mật khẩu đã được cập nhật", Toast.LENGTH_SHORT).show();
                                            // Chuyển hướng về trang đăng nhập hoặc trang thích hợp khác
                                            Intent intent = new Intent(QuenMatKhauScreen.this, Login.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(QuenMatKhauScreen.this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuenMatKhauScreen.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
