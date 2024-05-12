package com.example.myapplication;

import static androidx.tracing.Trace.isEnabled;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    ArrayList<CHITIETLOP> chamdiemdatalist;

    String filepath;
    ImageButton taode;
    DatabaseReference db_chitietlop;

    TextView ma_lop_text_view;
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

        setupOnBackPressed();

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

        taode = findViewById(R.id.cham_thi_tao_de);
        ma_lop_text_view = findViewById(R.id.cham_diem_ma_lop);
        db_chitietlop = FirebaseDatabase.getInstance().getReference("CHITIETLOP");
        taode.setOnClickListener(new View.OnClickListener()
        {
            int count = 0;
            @Override
            public void onClick(View v)
            {
                int item_count = adapter.getItemCount();

                for(int i = 0;i<item_count;i++)
                {
                    String malop = ma_lop_text_view.getText().toString().split(":")[1];
                    String masv = ((EditText) nhap_diem_rcv.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.cham_thi_mssv_textview)).getText().toString();
                    String hoten = ((EditText) nhap_diem_rcv.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.cham_thi_ho_va_ten_textview)).getText().toString();
                    int diem = Integer.parseInt(((EditText) nhap_diem_rcv.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.cham_thi_diem_text)).getText().toString());
                    String diemchu = ((EditText) nhap_diem_rcv.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.cham_thi_diem_chu_text)).getText().toString();
                    String ghichu = ((EditText) nhap_diem_rcv.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.cham_thi_ghi_chu_text_view)).getText().toString();
                    CHITIETLOP sinhvien = new CHITIETLOP(malop,masv,hoten,diem,diemchu,ghichu);
                    String key = db_chitietlop.push().getKey();
                    db_chitietlop.child(key).setValue(sinhvien).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                count++;
                            }
                        }
                    });
                }
                if(count!= 0)
                {
                    Toast.makeText(NhapDiemScreen.this,"Đã lưu điểm "+count+" sinh viên",Toast.LENGTH_SHORT).show();
                }
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
        DividerItemDecoration item_decoration = new DividerItemDecoration(NhapDiemScreen.this, DividerItemDecoration.VERTICAL);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.divider_nhap_diem, null);
        item_decoration.setDrawable(drawable);
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
    private void setupOnBackPressed()
    {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if(isEnabled())
                {
                    startActivity(new Intent(NhapDiemScreen.this, ChamDiemScreen.class));
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
}