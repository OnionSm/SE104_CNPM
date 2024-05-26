package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpScreen extends AppCompatActivity {

    EditText ten_user_edt;
    EditText ms_gv;
    EditText email_edt;
    EditText password_edt;
    EditText password2_edt;
    ImageButton dang_ki_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ten_user_edt = findViewById(R.id.nhap_ten);
        email_edt = findViewById(R.id.nhap_email);
        password_edt = findViewById(R.id.nhap_mk);
        password2_edt = findViewById(R.id.nhap_lai_mk);
        dang_ki_button = findViewById(R.id.button_dangky);
        ms_gv = findViewById(R.id.nhap_msgv);

        dang_ki_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckValidInput(new OnCompleteDataGV()
                {
                    @Override
                    public void onCompleteData(String ten_user, String email, String pass, String msgv)
                    {
                        Intent intent = new Intent(SignUpScreen.this, SDTXacThucSignUp.class);
                        intent.putExtra("ten_user", ten_user);
                        intent.putExtra("email", email);
                        intent.putExtra("pass", pass);
                        intent.putExtra("msgv", msgv);
                        startActivity(intent);
                    }
                });
            }
        });

        Button loginButton = findViewById(R.id.button_dangnhap);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpScreen.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void CheckValidInput(OnCompleteDataGV callback)
    {
        String ten_user = ten_user_edt.getText().toString();
        String email = email_edt.getText().toString();
        String pass = password_edt.getText().toString();
        String pass2 = password2_edt.getText().toString();
        String msgv = ms_gv.getText().toString();

        if (ten_user.isEmpty() || email.isEmpty() || pass.isEmpty() || pass2.isEmpty() || msgv.isEmpty()) {
            Toast.makeText(SignUpScreen.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                Toast.makeText(SignUpScreen.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(pass2)) {
                Toast.makeText(SignUpScreen.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference db_gv = FirebaseDatabase.getInstance().getReference("GIANGVIEN");
            db_gv.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    int max = (int)snapshot.getChildrenCount();
                    int count = 0;
                    for(DataSnapshot data : snapshot.getChildren())
                    {
                        if(data.child("maGV").getValue(String.class).equals(msgv))
                        {
                            Toast.makeText(SignUpScreen.this, "Mã giáo viên đã tồn tại",Toast.LENGTH_SHORT).show();
                        }
                        count ++;
                        if(count==max)
                        {
                            callback.onCompleteData(ten_user, email, pass, msgv);
                            return;
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
    interface OnCompleteDataGV
    {
        void onCompleteData(String ten_user, String email, String pass, String msgv);
    }
}
