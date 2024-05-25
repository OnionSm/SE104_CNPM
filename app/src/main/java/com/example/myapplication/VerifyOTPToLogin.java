package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTPToLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText inputcode1, inputcode2, inputcode3, inputcode4, inputcode5, inputcode6;
    private ImageButton xac_thuc_otp_button;
    private ProgressBar progressBarverify;
    private String getotpbackend;
    private String numberphone;
    private String ten_user, email, pass, msgv;
    private TextView gui_lai_ma_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otpto_login);

        initViews();
        setupWindowInsets();
        setupOTPInputs();

        mAuth = FirebaseAuth.getInstance();

        getotpbackend = getIntent().getStringExtra("verificationId");
        numberphone = getIntent().getStringExtra("phone_number");
        ten_user = getIntent().getStringExtra("ten_user");
        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("pass");
        msgv = getIntent().getStringExtra("msgv");

        if (msgv == null || msgv.isEmpty()) {
            showToast("Mã giảng viên không hợp lệ");
            return;
        }

        xac_thuc_otp_button.setOnClickListener(v -> verifyOTP());
        gui_lai_ma_click.setOnClickListener(v -> resendOTP());
    }

    private void initViews() {
        xac_thuc_otp_button = findViewById(R.id.xac_thuc_otp_button);
        inputcode1 = findViewById(R.id.otp_input_1);
        inputcode2 = findViewById(R.id.otp_input_2);
        inputcode3 = findViewById(R.id.otp_input_3);
        inputcode4 = findViewById(R.id.otp_input_4);
        inputcode5 = findViewById(R.id.otp_input_5);
        inputcode6 = findViewById(R.id.otp_input_6);
        progressBarverify = findViewById(R.id.progress_bar);
        gui_lai_ma_click = findViewById(R.id.gui_lai_ma_click);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gui_ma_xac_thuc_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupOTPInputs() {
        setupOTPTextWatcher(inputcode1, inputcode2);
        setupOTPTextWatcher(inputcode2, inputcode3);
        setupOTPTextWatcher(inputcode3, inputcode4);
        setupOTPTextWatcher(inputcode4, inputcode5);
        setupOTPTextWatcher(inputcode5, inputcode6);
        setupOTPTextWatcher(inputcode6, null);
    }

    private void setupOTPTextWatcher(EditText current, EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && next != null) {
                    next.requestFocus();
                } else if (s.length() == 0 && current != inputcode1) {
                    current.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void verifyOTP() {
        try {
            String code = inputcode1.getText().toString() +
                    inputcode2.getText().toString() +
                    inputcode3.getText().toString() +
                    inputcode4.getText().toString() +
                    inputcode5.getText().toString() +
                    inputcode6.getText().toString();

            if (code.length() == 6 && getotpbackend != null) {
                progressBarverify.setVisibility(View.VISIBLE);
                xac_thuc_otp_button.setVisibility(View.INVISIBLE);

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getotpbackend, code);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, task -> {
                    progressBarverify.setVisibility(View.GONE);
                    xac_thuc_otp_button.setVisibility(View.VISIBLE);

                    if (task.isSuccessful()) {
                        registerUser();
                    } else {
                        showToast("Mã xác thực không chính xác");
                    }
                });
            } else {
                showToast("Vui lòng nhập toàn bộ mã xác thực");
            }
        } catch (Exception e) {
            showToast("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    private void registerUser() {
        try {
            if (msgv == null || msgv.isEmpty()) {
                showToast("Mã giảng viên không hợp lệ");
                return;
            }

            DatabaseReference giangVienRef = FirebaseDatabase.getInstance().getReference("GIANGVIEN");
            GIANGVIEN giangvien = new GIANGVIEN(
                    "Unknown",
                    ten_user,
                    msgv,
                    "Unknown",
                    pass,
                    "Unknown",
                    numberphone
            );

            giangVienRef.child(msgv).setValue(giangvien).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showToast("Đăng ký thành công");
                    Intent intent = new Intent(VerifyOTPToLogin.this, Login.class);
                    intent.putExtra("phone_number", numberphone);
                    startActivity(intent);
                } else {
                    showToast("Đăng ký thất bại");
                }
            });
        } catch (Exception e) {
            showToast("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    private void resendOTP() {
        try {
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                    .setPhoneNumber("+84" + numberphone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                            // Tùy chọn: Tự động đọc mã từ tin nhắn SMS
                            final String code = credential.getSmsCode();
                            if (code != null) {
                                inputcode1.setText(String.valueOf(code.charAt(0)));
                                inputcode2.setText(String.valueOf(code.charAt(1)));
                                inputcode3.setText(String.valueOf(code.charAt(2)));
                                inputcode4.setText(String.valueOf(code.charAt(3)));
                                inputcode5.setText(String.valueOf(code.charAt(4)));
                                inputcode6.setText(String.valueOf(code.charAt(5)));
                                verifyOTP();
                            }
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            showToast(e.getMessage());
                        }

                        @Override
                        public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                            getotpbackend = newVerificationId;
                            showToast("Mã xác thực đã được gửi lại");
                        }
                    }).build();

            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            showToast("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(VerifyOTPToLogin.this, message, Toast.LENGTH_SHORT).show();
    }
}
