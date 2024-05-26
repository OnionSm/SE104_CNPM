package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SDTXacThucScreen extends AppCompatActivity {
    EditText sdt_xac_thuc;
    ImageButton gui_ma_xac_thuc;
    FirebaseAuth mAuth;
    DatabaseReference giangVienRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdtxac_thuc_screen);
        sdt_xac_thuc = findViewById(R.id.sdt_xac_thuc);
        gui_ma_xac_thuc = findViewById(R.id.gui_ma_otp_button);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
        giangVienRef = FirebaseDatabase.getInstance().getReference("GIANGVIEN");

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sdt_xac_thuc_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton icon_back_login = findViewById(R.id.sdt_xac_thuc_icon_back);
        icon_back_login.setOnClickListener(view -> {
            Intent quay_lai_trang_login = new Intent(SDTXacThucScreen.this, Login.class);
            startActivity(quay_lai_trang_login);
            finish();
        });

        ImageButton dang_nhap_text = findViewById(R.id.dang_nhap_sdt);
        dang_nhap_text.setOnClickListener(view -> {
            Intent dang_nhap_intent = new Intent(SDTXacThucScreen.this, Login.class);
            startActivity(dang_nhap_intent);
            finish();
        });

        gui_ma_xac_thuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = sdt_xac_thuc.getText().toString().trim();
                if (!phoneNumber.isEmpty()) {
                    if (phoneNumber.length() == 9) {
                        progressBar.setVisibility(View.VISIBLE);
                        gui_ma_xac_thuc.setVisibility(View.INVISIBLE);
                        String fullPhoneNumber = "+84" + phoneNumber;
                        Log.d("SDTXacThucScreen", "Full Phone Number: " + fullPhoneNumber);
                        // Kiểm tra số điện thoại trên Firebase trước khi gửi mã xác thực
                        checkPhoneNumberExists(fullPhoneNumber, progressBar);
                    } else {
                        Toast.makeText(SDTXacThucScreen.this, "Nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SDTXacThucScreen.this, "Nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkPhoneNumberExists(String phoneNumber, ProgressBar progressBar) {
        giangVienRef.orderByChild("sdt").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Số điện thoại tồn tại trên Firebase, tiếp tục gửi mã xác thực
                    sendVerificationCode(phoneNumber, progressBar);
                } else {
                    // Số điện thoại không tồn tại trên Firebase
                    progressBar.setVisibility(View.GONE);
                    gui_ma_xac_thuc.setVisibility(View.VISIBLE);
                    Toast.makeText(SDTXacThucScreen.this, "Số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                gui_ma_xac_thuc.setVisibility(View.VISIBLE);
                Toast.makeText(SDTXacThucScreen.this, "Lỗi kiểm tra số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendVerificationCode(String phoneNumber, ProgressBar progressBar) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(SDTXacThucScreen.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                gui_ma_xac_thuc.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                gui_ma_xac_thuc.setVisibility(View.VISIBLE);
                                Toast.makeText(SDTXacThucScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                gui_ma_xac_thuc.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(), GuiMaXacThucScreen.class);
                                intent.putExtra("phone_number", phoneNumber);
                                intent.putExtra("verificationId", verificationId);

                                startActivity(intent);
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
