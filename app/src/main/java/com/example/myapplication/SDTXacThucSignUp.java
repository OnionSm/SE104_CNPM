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

public class SDTXacThucSignUp extends AppCompatActivity {
    EditText sdt_xac_thuc;
    ImageButton gui_ma_xac_thuc;
    FirebaseAuth mAuth;
    DatabaseReference giangVienRef;
    String ten_user, email, pass, msgv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdtxac_thuc_sign_up);
        ten_user = getIntent().getStringExtra("ten_user");
        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("pass");
        msgv = getIntent().getStringExtra("msgv");
        sdt_xac_thuc = findViewById(R.id.sdt_xac_thuc);
        gui_ma_xac_thuc = findViewById(R.id.gui_ma_otp_button);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
        giangVienRef = FirebaseDatabase.getInstance().getReference("GIANGVIEN");

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sdt_xac_thuc_sign_up), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
                        Log.d("SDTXacThucToSignup", "Full Phone Number: " + fullPhoneNumber);
                        // Gửi mã xác thực
                        sendVerificationCode("+84" + phoneNumber, progressBar);
                    } else {
                        Toast.makeText(SDTXacThucSignUp.this, "Nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SDTXacThucSignUp.this, "Nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendVerificationCode(String phoneNumber, ProgressBar progressBar) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(SDTXacThucSignUp.this)
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
                                Toast.makeText(SDTXacThucSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                gui_ma_xac_thuc.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(), VerifyOTPToLogin.class);
                                intent.putExtra("phone_number", phoneNumber);
                                intent.putExtra("verificationId", verificationId);
                                intent.putExtra("ten_user", ten_user);
                                intent.putExtra("email", email);
                                intent.putExtra("pass", pass);
                                intent.putExtra("msgv", msgv);
                                startActivity(intent);

                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
