package com.example.myapplication;

import static org.apache.log4j.helpers.Loader.getResource;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class ChamDiemScreen extends AppCompatActivity
{

    ImageButton chamdiem_btn;

    Workbook workbook;
    String filename;
    private ActivityResultLauncher<Intent> m_activity_result = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            String[] filename1;
                            String fn;
                            String filepath=uri.getPath();
                            String filePath1[]=filepath.split(":");
                            filename1 =filepath.split("/");
                            fn=filename1[filename1.length-1];
                            String absoulute_path = Environment.getExternalStorageDirectory().getPath()+"/"+filePath1[1];
                            File file_absolute =new File(absoulute_path);

                            try
                            {
                                ArrayList<CHITIETLOP> ctl  = new ArrayList<>();
                                FileInputStream inputStream = new FileInputStream(file_absolute);
                                Workbook workbook = new XSSFWorkbook(inputStream);
                                Sheet s = workbook.getSheet("Sheet1");
                                DataFormatter formatter = new DataFormatter();
                                int row_count = s.getLastRowNum() + 1;
                                int col_count = s.getRow(0).getLastCellNum(); // Số lượng cột trong hàng đầu tiên

                                for (int i = 1; i <= row_count; i++)
                                {
                                    Row r = s.getRow(i);
                                    if (r != null) { // Kiểm tra xem hàng có tồn tại không
                                        // Kiểm tra xem ô có giá trị hợp lệ không trước khi chuyển đổi thành số nguyên
                                        String maSV = "";
                                        if (r.getCell(1) != null) {
                                            maSV = formatter.formatCellValue(r.getCell(1));
                                        }
                                        String tenSV = "";
                                        if (r.getCell(2) != null) {
                                            tenSV = formatter.formatCellValue(r.getCell(2));
                                        }
                                        int diem = 0;
                                        if (r.getCell(3) != null && r.getCell(3).getCellType() == CellType.NUMERIC) {
                                            diem = (int) r.getCell(3).getNumericCellValue();
                                        }
                                        String diemChu = "";
                                        if (r.getCell(4) != null) {
                                            diemChu = formatter.formatCellValue(r.getCell(4));
                                        }
                                        String ghiChu = "";
                                        if (r.getCell(5) != null) {
                                            ghiChu = formatter.formatCellValue(r.getCell(5));
                                        }

                                        // Tạo đối tượng CHITIETLOP từ dữ liệu cột của hàng
                                        CHITIETLOP sv = new CHITIETLOP("", maSV, tenSV, diem, diemChu, ghiChu);
                                        Log.e("CHI TIẾT LỚP", sv.getMaLop() + "   " + sv.getMaSV()+ "   " + sv.getTenSV()+ "   " + sv.getDiem() + "   "+ sv.getDiemChu()+ "   " + sv.getGhiChu());
                                    }
                                }
                                workbook.close();
                                inputStream.close();
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cham_diem_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chamdiem), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chamdiem_btn = findViewById(R.id.button_cham_diem);
        chamdiem_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickRequestPermission();
            }
        });


    }
    private void onClickRequestPermission()
    {
        /*if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            OpenGallery();
        }*/

            OpenGallery();
    }
    private void OpenGallery()
    {
        String path = Environment.getExternalStorageState()+"/Download";
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setData(uri);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        m_activity_result.launch(intent);

    }


}