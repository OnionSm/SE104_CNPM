package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NhapDiemScreen extends AppCompatActivity
{
    RecyclerView nhap_diem_rcv;
    ImageButton quay_lai_cham_diem_screen;
    ChiTietLopAdapter adapter;

    ArrayList<CHITIETLOP> mylist;

    String filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nhap_diem_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nhapdiemscreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quay_lai_cham_diem_screen = findViewById(R.id.nhap_diem_icon_back);
        quay_lai_cham_diem_screen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(NhapDiemScreen.this,ChamDiemScreen.class));
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mypackage");
        filepath = bundle.getString("filepath");
        Toast.makeText(NhapDiemScreen.this,filepath,Toast.LENGTH_LONG).show();
        GetData(filepath);
    }
    private void GetListCauHoi()
    {
        nhap_diem_rcv = findViewById(R.id.cham_thi_recycle_view);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(NhapDiemScreen.this);
        nhap_diem_rcv.setLayoutManager(ln_layout_manager);
        adapter = new ChiTietLopAdapter(mylist);
        nhap_diem_rcv.setAdapter(adapter);
        RecyclerView.ItemDecoration item_decoration = new DividerItemDecoration(NhapDiemScreen.this, DividerItemDecoration.VERTICAL);
        nhap_diem_rcv.addItemDecoration(item_decoration);
    }
    private void GetData(String absoulute_path)
    {
        File file_absolute =new File(absoulute_path);
        mylist = new ArrayList<>();
        try
        {
            FileInputStream inputStream = new FileInputStream(file_absolute);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet s = workbook.getSheet("Sheet1");
            DataFormatter formatter = new DataFormatter();
            int row_count = s.getLastRowNum() + 1;
            int col_count = s.getRow(0).getLastCellNum(); // Số lượng cột trong hàng đầu tiên

            for (int i = 1; i <= row_count; i++)
            {
                Row r = s.getRow(i);
                if (r != null)
                { // Kiểm tra xem hàng có tồn tại không
                    // Kiểm tra xem ô có giá trị hợp lệ không trước khi chuyển đổi thành số nguyên
                    String maSV = "";
                    if (r.getCell(1) != null)
                    {
                        maSV = formatter.formatCellValue(r.getCell(1));
                    }
                    String tenSV = "";
                    if (r.getCell(2) != null)
                    {
                        tenSV = formatter.formatCellValue(r.getCell(2));
                    }
                    int diem = 0;
                    if (r.getCell(3) != null && r.getCell(3).getCellType() == CellType.NUMERIC)
                    {
                        diem = (int) r.getCell(3).getNumericCellValue();
                    }
                    String diemChu = "";
                    if (r.getCell(4) != null)
                    {
                        diemChu = formatter.formatCellValue(r.getCell(4));
                    }
                    String ghiChu = "";
                    if (r.getCell(5) != null)
                    {
                        ghiChu = formatter.formatCellValue(r.getCell(5));
                    }

                    // Tạo đối tượng CHITIETLOP từ dữ liệu cột của hàng
                    CHITIETLOP sv = new CHITIETLOP(" ", maSV, tenSV, diem, diemChu, ghiChu);
                    mylist.add(sv);
                    Log.e("CHI TIẾT LỚP ND", sv.getMaLop() + "   " + sv.getMaSV()+ "   " + sv.getTenSV()+ "   " + sv.getDiem() + "   "+ sv.getDiemChu()+ "   " + sv.getGhiChu());
                }
                GetListCauHoi();
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