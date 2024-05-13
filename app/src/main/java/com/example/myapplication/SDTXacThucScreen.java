package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SDTXacThucScreen extends AppCompatActivity {
    EditText sdt_xac_thuc;
    ImageButton gui_ma_xac_thuc;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdtxac_thuc_screen);
        sdt_xac_thuc = findViewById(R.id.sdt_xac_thuc);
        gui_ma_xac_thuc = findViewById(R.id.gui_ma_otp_button);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sdt_xac_thuc_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
// quay tro lai trang login
        ImageButton icon_back_login = findViewById(R.id.sdt_xac_thuc_icon_back);
        icon_back_login.setOnClickListener(view -> {
            Intent quay_lai_trang_login = new Intent(SDTXacThucScreen.this, Login.class);
            startActivity(quay_lai_trang_login);
            finish();
        });
// quay tro lai trang login
        ImageButton dang_nhap_text = findViewById(R.id.dang_nhap_sdt);
        dang_nhap_text.setOnClickListener(view -> {
            Intent dang_nhap_intent = new Intent(SDTXacThucScreen.this, Login.class);
            startActivity(dang_nhap_intent);
            finish();
        });

        gui_ma_xac_thuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sdt_xac_thuc.getText().toString().isEmpty())
                {
                    if (sdt_xac_thuc.getText().toString().trim().length() == 9) {
                        progressBar.setVisibility(View.VISIBLE);
                        gui_ma_xac_thuc.setVisibility(View.INVISIBLE);
                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(mAuth)
                                        .setPhoneNumber("+84"+sdt_xac_thuc.getText().toString())
                                        .setTimeout(60L, TimeUnit.SECONDS)
                                        .setActivity(SDTXacThucScreen.this)
                                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                final String code = phoneAuthCredential.getSmsCode();
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
                                                intent.putExtra("phone_number", sdt_xac_thuc.getText().toString());
                                                intent.putExtra("verificationId", verificationId);
                                                startActivity(intent);

                                            }
                                        })
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);
                        Intent intent = new Intent(getApplicationContext(), GuiMaXacThucScreen.class);
                        intent.putExtra("phone_number", sdt_xac_thuc.getText().toString());
                        startActivity(intent);
                    }
                 else{
                    Toast.makeText(SDTXacThucScreen.this, "Nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();

                }
                }
                else{
                    Toast.makeText(SDTXacThucScreen.this, "Nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
