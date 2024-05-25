package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity
{

    EditText EmailInput;
    EditText PasswordInput;
    ImageButton LoginButton;

    boolean password_visible;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();
        EmailInput = findViewById(R.id.login_email_edit_text);
        PasswordInput = findViewById(R.id.login_pass_edit_text);
        LoginButton = findViewById(R.id.login_button);


        PasswordInput.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                final int right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    if(event.getRawX() >= PasswordInput.getRight()- PasswordInput.getCompoundDrawables()[right].getBounds().width())
                    {
                        int selection = PasswordInput.getSelectionEnd();
                        if(password_visible)
                        {
                            PasswordInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_off_24dp, 0);
                            PasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            password_visible = false;
                        }
                        else
                        {
                            PasswordInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_24dp, 0);
                            PasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            password_visible = true;
                        }
                        PasswordInput.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = EmailInput.getText().toString();
                String password = PasswordInput.getText().toString();
                Log.i("Text Credentials", "Email: " + email + " and Password: " + password);
                ref.child("GIANGVIEN").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.exists()) {
                                    String id = dataSnapshot.child("maGV").getValue(String.class);
                                    String matKhau = dataSnapshot.child("matKhau").getValue(String.class);
                                    if (email.equals(id) && password.equals(matKhau))
                                    {
                                        PHIENDANGNHAP phiendangnhap = new PHIENDANGNHAP(id,matKhau);
                                        ref.child("PHIENDANGNHAP").setValue(phiendangnhap);
                                        startActivity(new Intent(Login.this, MainScreenNew.class));
                                        finish();
                                    }
                                    Log.d("GIANGVIEN", "ID: " + id);
                                    Log.d("GIANGVIEN", "Mật khẩu: " + matKhau);
                                }
                            }
                        }
                    }
                });
            }
        });
        ImageButton dangKyButton = findViewById(R.id.dang_ky_button);
        dangKyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển hướng sang màn hình signup
                Intent intent = new Intent(Login.this, SignUpScreen.class);
                startActivity(intent);
            }
        });
    }



}