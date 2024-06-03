package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.background_splash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                if (sessionManager.isLoggedIn())
                {
                    // Chuyển đến màn hình chính
                    String user_account = sessionManager.getUsername();
                    if(user_account.equals("000000"))
                    {
                        startActivity(new Intent(MainActivity.this, MainScreenAdmin.class));
                    }
                    else
                    {
                        startActivity(new Intent(MainActivity.this, MainScreenNew.class));
                    }
                    finish();
                } else
                {
                    // Chuyển đến màn hình đăng nhập
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2500);

    }
}