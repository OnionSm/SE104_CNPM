package com.example.myapplication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.m;

import fragment.TrangChuFragment;
import fragment.ViewPager2Adapter;

public class MainScreenNew extends AppCompatActivity
{
    private BottomNavigationView bottom_navigation_view;
    private ViewPager2 viewpager2;

    ViewPager2Adapter view_pager_adapter;
    private long lastBackPressedTime = 0;
    private static final long BACK_PRESS_INTERVAL = 2000;


    private Toast m_toast;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainscreennew), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult( ActivityResult result ) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                {
                    if (Environment.isExternalStorageManager())
                        Toast.makeText(MainScreenNew.this,"We have permission",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainScreenNew.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainScreenNew.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (checkPermission())
        {
            Toast.makeText(MainScreenNew.this,"We have permission",Toast.LENGTH_SHORT).show();   // WE have a permission just start your work.
        } else {
            requestPermission(); // Request Permission
        }

        bottom_navigation_view = findViewById(R.id.main_scr_bottom_navigation_view);

        viewpager2 = findViewById(R.id.main_scr_view_pager);
        view_pager_adapter = new ViewPager2Adapter(this);
        viewpager2.setAdapter(view_pager_adapter);
        viewpager2.setPageTransformer(new DepthPageTransformer());
        bottom_navigation_view.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_trangchu:
                        viewpager2.setCurrentItem(0);
                        break;
                    case R.id.menu_thongbao:
                        viewpager2.setCurrentItem(1);
                        break;
                    case R.id.menu_hoso:
                        viewpager2.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        setupOnBackPressed();
    }


    private void setupOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                long currentTime = System.currentTimeMillis();
                if (lastBackPressedTime + BACK_PRESS_INTERVAL > currentTime) {
                    m_toast.cancel();
                    finish();
                    System.exit(0);
                } else {
                    m_toast = Toast.makeText(MainScreenNew.this, "Nhấn 1 lần nữa để thoát", Toast.LENGTH_SHORT);
                    m_toast.show();
                }
                lastBackPressedTime = currentTime;
            }
        });
    }
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readCheck = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED && writeCheck == PackageManager.PERMISSION_GRANTED;
        }
    }
    private String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new AlertDialog.Builder(MainScreenNew.this)
                    .setTitle("Permission")
                    .setMessage("Please give the Storage permission")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                                activityResultLauncher.launch(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                activityResultLauncher.launch(intent);
                            }
                        }
                    })
                    .setCancelable(false)
                    .show();

        } else
        {

            ActivityCompat.requestPermissions(MainScreenNew.this, permissions, 30);
        }
    }
}