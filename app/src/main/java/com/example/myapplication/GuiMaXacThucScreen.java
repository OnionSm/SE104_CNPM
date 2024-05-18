package com.example.myapplication;

import android.content.Intent;
import android.media.Image;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class GuiMaXacThucScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    private EditText inputcode1, inputcode2, inputcode3, inputcode4, inputcode5, inputcode6;
    ImageButton xac_thuc_otp_button;
    String getotpbackend;
    TextView gui_lai_ma_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        gui_lai_ma_click = findViewById(R.id.gui_lai_ma_click);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gui_ma_xac_thuc_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gui_ma_xac_thuc), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        xac_thuc_otp_button = findViewById(R.id.xac_thuc_otp_button);
        inputcode1 = findViewById(R.id.otp_input_1);
        inputcode2 = findViewById(R.id.otp_input_2);
        inputcode3 = findViewById(R.id.otp_input_3);
        inputcode4 = findViewById(R.id.otp_input_4);
        inputcode5 = findViewById(R.id.otp_input_5);
        inputcode6 = findViewById(R.id.otp_input_6);
        mAuth = FirebaseAuth.getInstance();
//        gui_lai_ma_click.setText(String.format("+84-%s", getIntent().getStringExtra("phone_number")));
        getotpbackend = getIntent().getStringExtra("verificationId");
        final ProgressBar progressBarverify = findViewById(R.id.progress_bar);
        xac_thuc_otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputcode1.getText().toString().trim().isEmpty() && !inputcode2.getText().toString().trim().isEmpty() && !inputcode3.getText().toString().trim().isEmpty() && !inputcode4.getText().toString().trim().isEmpty() && !inputcode5.getText().toString().trim().isEmpty() && !inputcode6.getText().toString().trim().isEmpty()) {
                    String entercodeotp = inputcode1.getText().toString() +
                            inputcode2.getText().toString() +
                            inputcode3.getText().toString() +
                            inputcode4.getText().toString() +
                            inputcode5.getText().toString() +
                            inputcode6.getText().toString();
                    if (getotpbackend != null) {
                        progressBarverify.setVisibility(View.VISIBLE);
                        xac_thuc_otp_button.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getotpbackend, entercodeotp);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBarverify.setVisibility(View.GONE);
                                        xac_thuc_otp_button.setVisibility(View.VISIBLE);
                                        if (task.isSuccessful()) {
                                            // Nếu xác thực OTP đúng, chuyển hướng người dùng đến màn hình nhập lại mật khẩu mới
                                            Intent intent = new Intent(GuiMaXacThucScreen.this, QuenMatKhauScreen.class);
                                            startActivity(intent);
                                        } else {
                                            // Nếu xác thực OTP sai, hiển thị thông báo lỗi
                                            Toast.makeText(GuiMaXacThucScreen.this, "Mã xác thực không chính xác", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
                    } else {

                        Toast.makeText(GuiMaXacThucScreen.this, "Vui lòng kiểm tra kết nối Internet", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GuiMaXacThucScreen.this, "Vui lòng nhập toàn bộ mã xác thực", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setupOTPInputs();

        TextView gui_lai_ma_click = findViewById(R.id.gui_lai_ma_click);
        gui_lai_ma_click.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    PhoneAuthOptions options =
                                                            PhoneAuthOptions.newBuilder(mAuth)
                                                                    .setPhoneNumber("+84" + getIntent().getStringExtra("phone_number"))
                                                                    .setTimeout(60L, TimeUnit.SECONDS)
                                                                    .setActivity(GuiMaXacThucScreen.this)
                                                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                                        @Override
                                                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                                            final String code = phoneAuthCredential.getSmsCode();

                                                                        }

                                                                        @Override
                                                                        public void onVerificationFailed(@NonNull FirebaseException e) {

                                                                            Toast.makeText(GuiMaXacThucScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }

                                                                        @Override
                                                                        public void onCodeSent(@NonNull String newverificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                                            getotpbackend = newverificationId;
                                                                            Toast.makeText(GuiMaXacThucScreen.this, "Mã xác thực đã được gửi lại", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    })
                                                                    .build();
                                                    PhoneAuthProvider.verifyPhoneNumber(options);

                                                }
                                            }
        );
    }

    // ma xac thuc
    private void setupOTPInputs() {
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1){
                    inputcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputcode1.requestFocus();
                }
            }
        });
        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    inputcode1.requestFocus();
                } else if (s.length() == 1) {
                    inputcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputcode2.requestFocus();
                }

            }
        });
        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    inputcode2.requestFocus();
                } else if (s.length() == 1) {
                    inputcode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputcode3.requestFocus();
                }
            }
        });
        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    inputcode3.requestFocus();
                } else if (s.length() == 1) {
                    inputcode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputcode4.requestFocus();
                }


            }
        });
        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    inputcode4.requestFocus();
                } else if (s.length() == 1) {
                    inputcode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    inputcode5.requestFocus();
                }
            }
        });
    }
}