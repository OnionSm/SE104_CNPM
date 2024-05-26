package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import fragment.BaoCaoViewPagerAdapter;
import fragment.TaoDeThiViewPagerAdapter;

public class BaoCaoNamScreen extends AppCompatActivity
{
    private BottomNavigationView bottom_navigation_view;
    private ViewPager2 viewpager2;
    BaoCaoViewPagerAdapter view_pager_adapter;
    String namhoc;
    String nam1;
    String nam2;
    int tongsodethi;
    int tongsobaicham;


    ArrayList<baocaomonhocitem> list_monhoc;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bao_cao_nam_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        tongsodethi = bundle.getInt("tongsodethi");
        tongsobaicham = bundle.getInt("tongsobaicham");
        nam1 = bundle.getString("nam1");
        nam2 = bundle.getString("nam2");
        list_monhoc = bundle.getParcelableArrayList("data_list");
        Log.e("THISSS", "THISSS");


        ImageButton quay_lai_de_thi = findViewById(R.id.bao_cao_icon_back);
        quay_lai_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(BaoCaoNamScreen.this, MainScreenNew.class);
                startActivity(quay_lai_trang_chu_intent);
                finish();
            }
        });

        ImageButton xuat_file = findViewById(R.id.xuat_file_excel_button);
        xuat_file.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ExportFileExcel();
            }
        });

        bottom_navigation_view = findViewById(R.id.bao_cao_bottom_navigation_view);

        viewpager2 = findViewById(R.id.bao_cao_view_pager);
        view_pager_adapter = new BaoCaoViewPagerAdapter(this);
        viewpager2.setAdapter(view_pager_adapter);
        viewpager2.setPageTransformer(new DepthPageTransformer());

        bottom_navigation_view.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_baocao:
                        viewpager2.setCurrentItem(0);
                        break;
                    case R.id.menu_dethi:
                        viewpager2.setCurrentItem(1);
                        break;
                    case R.id.menu_baicham:
                        viewpager2.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        bottom_navigation_view.setSelectedItemId(R.id.menu_baocao);
                        break;
                    case 1:
                        bottom_navigation_view.setSelectedItemId(R.id.menu_dethi);
                        break;
                    case 2:
                        bottom_navigation_view.setSelectedItemId(R.id.menu_baicham);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        //GetDataCauHoiFromFireBase();
        setupOnBackPressed();
    }



    private void setupOnBackPressed()
    {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if(isEnabled())
                {
                    startActivity(new Intent(BaoCaoNamScreen.this, BaoCaoNhapNamScreen.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }

    public String AccessDataNam()
    {
        return nam1+"/"+ nam2;
    }
    public ArrayList<baocaomonhocitem> AccessDataList()
    {
        return list_monhoc;
    }
    public int AccessDataTongSoDeThi()
    {
        return tongsodethi;
    }
    public int AccessDataTongSoBaiCham()
    {
        return tongsobaicham;
    }

    private void ExportFileExcel() {
        XSSFWorkbook file_excel = new XSSFWorkbook();
        XSSFSheet excel_sheet = file_excel.createSheet("Sheet1");
        XSSFRow sheet_row = excel_sheet.createRow(0);
        XSSFCell cell_row = sheet_row.createCell(0);
        cell_row.setCellValue("BÁO CÁO NĂM");

        XSSFRow sheet_row1 = excel_sheet.createRow(1);
        XSSFCell cell_row1 = sheet_row1.createCell(0);
        cell_row1.setCellValue("Năm: " + namhoc);

        XSSFRow sheet_row2 = excel_sheet.createRow(2);
        XSSFCell cell_row2 = sheet_row2.createCell(0);
        cell_row2.setCellValue("Tổng số đề thi: " + tongsodethi);
        XSSFCell cell_row3 = sheet_row2.createCell(1);
        cell_row3.setCellValue("Tổng số bài chấm: " + tongsobaicham);

        XSSFRow sheet_row3 = excel_sheet.createRow(3);
        XSSFCell cell_row3_0 = sheet_row3.createCell(0);
        XSSFCell cell_row3_1 = sheet_row3.createCell(1);
        XSSFCell cell_row3_2 = sheet_row3.createCell(2);
        XSSFCell cell_row3_3 = sheet_row3.createCell(3);
        XSSFCell cell_row3_4 = sheet_row3.createCell(4);
        XSSFCell cell_row3_5 = sheet_row3.createCell(5);
        cell_row3_0.setCellValue("STT");
        cell_row3_1.setCellValue("Tên môn");
        cell_row3_2.setCellValue("Số lượng đề thi");
        cell_row3_3.setCellValue("Số lượng bài chấm");
        cell_row3_4.setCellValue("Tỉ lệ đề thi");
        cell_row3_5.setCellValue("Tỉ lệ bài chấm");

        for (int i = 0; i < list_monhoc.size(); i++) {
            XSSFRow row = excel_sheet.createRow(4 + i);
            XSSFCell cell0 = row.createCell(0);
            XSSFCell cell1 = row.createCell(1);
            XSSFCell cell2 = row.createCell(2);
            XSSFCell cell3 = row.createCell(3);
            XSSFCell cell4 = row.createCell(4);
            XSSFCell cell5 = row.createCell(5);
            cell0.setCellValue(i + 1);
            cell1.setCellValue(list_monhoc.get(i).getTenmon());
            cell2.setCellValue(list_monhoc.get(i).getSoluongdethi());
            cell3.setCellValue(list_monhoc.get(i).getSoluongbaicham());
            cell4.setCellValue(list_monhoc.get(i).getTiledethi());
            cell5.setCellValue(list_monhoc.get(i).getTilebaicham());
        }

        // Ensure you have WRITE_EXTERNAL_STORAGE permission in the AndroidManifest.xml
        // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/baocao.xlsx";
            File file_output = new File(path);
            file_output.getParentFile().mkdirs(); // Create directories if they don't exist

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file_output);
                file_excel.write(fileOutputStream);
                fileOutputStream.close();
                Toast.makeText(BaoCaoNamScreen.this, "Đã lưu file excel", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(BaoCaoNamScreen.this, "File not found: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(BaoCaoNamScreen.this, "Error writing file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(BaoCaoNamScreen.this, "External storage not available", Toast.LENGTH_SHORT).show();
        }
    }
}