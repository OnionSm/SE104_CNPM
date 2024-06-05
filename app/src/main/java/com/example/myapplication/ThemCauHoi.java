package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import fragment.HoSoFragment;
import fragment.NganHangCauHoiFragment;
import fragment.TrangChuFragment;

public class ThemCauHoi extends AppCompatActivity
{

    DatabaseReference dbRef;
    ArrayList<String> tenMonHocList = new ArrayList<>();
    Spinner spinner_mon_hoc;
    DatabaseReference dbRef_3;
    DatabaseReference dbRef_2;
    DatabaseReference dbRef_4;
    ArrayList<String> do_kho_list = new ArrayList<>();
    Spinner spinner_do_kho;
    EditText noi_dung_cau_hoi;
    TextView ngay_dang_tai;
    TextView ten_giang_vien;

    DatabaseReference db_pdn;

    CircleImageView user_dang_tai;

    ImageButton them_cau_hoi_button;
    SessionManager sessionManager;
    String user_account;

    int code;
    ArrayList<taodethicauhoiitem> list_cau_hoi_duoc_chon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cau_hoi);

        them_cau_hoi_button = findViewById(R.id.them_mon_hoc_button);
        them_cau_hoi_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ThemCauHoi.this, ThemMonHocScreen.class));
            }
        });

        // Khởi tạo biến chia sẻ dữ liệu
        sessionManager = new SessionManager(getApplicationContext());
        user_account = sessionManager.getUsername();

        noi_dung_cau_hoi = findViewById(R.id.noi_dung_cau_hoi);
        ngay_dang_tai = findViewById(R.id.ngay_dang_tai);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentDate = new Date();
        String ngayHienTai = sdf.format(currentDate);
        ngay_dang_tai.setText(ngayHienTai);

        dbRef_3 = FirebaseDatabase.getInstance().getReference("CAUHOI");
        dbRef_4 = FirebaseDatabase.getInstance().getReference("GIANGVIEN");
        dbRef = FirebaseDatabase.getInstance().getReference("MONHOC");

        spinner_mon_hoc = findViewById(R.id.tao_cau_hoi_mon_hoc_spiner);
        dbRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                tenMonHocList.clear();
                for (DataSnapshot monHocSnapshot : dataSnapshot.getChildren())
                {
                    String tenMonHoc = monHocSnapshot.child("tenMH").getValue(String.class);
                    tenMonHocList.add(tenMonHoc);
                }
                updateSpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.w(TAG, "Lỗi", databaseError.toException());
            }
        });

        dbRef_2 = FirebaseDatabase.getInstance().getReference("DOKHO");
        spinner_do_kho = findViewById(R.id.tao_cau_hoi_do_kho_spiner);
        dbRef_2.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                do_kho_list.clear();
                for (DataSnapshot doKhoSnapshot : dataSnapshot.getChildren()) {
                    String doKho = doKhoSnapshot.child("tenDK").getValue(String.class);
                    do_kho_list.add(doKho);
                }
                updateSpinner1();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.w(TAG, "Lỗi", databaseError.toException());
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenMonHocList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mon_hoc.setAdapter(adapter);
        ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, do_kho_list);
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_do_kho.setAdapter(adapter_2);


        /*Lấy tên giảng viên từ database và set vào phần đăng tải bởi*/
        GetTenNguoiThemCauHoi();
        SetUserImage();


        ImageButton tao_cau_hoi = findViewById(R.id.tao_cau_hoi_button);
        tao_cau_hoi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                SaveTaoCauHoi();
            }
        });


        ImageButton quay_lai_cau_hoi = findViewById(R.id.them_cau_hoi_icon_back);
        quay_lai_cau_hoi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        setupOnBackPressed();
    }




    private void updateSpinner()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ThemCauHoi.this, android.R.layout.simple_spinner_item, tenMonHocList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mon_hoc.setAdapter(adapter);
    }

    private void updateSpinner1()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ThemCauHoi.this, android.R.layout.simple_spinner_item, do_kho_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_do_kho.setAdapter(adapter);
    }

    private void SaveTaoCauHoi()
    {
        String tenMonHoc = spinner_mon_hoc.getSelectedItem().toString();
        String tenDoKho = spinner_do_kho.getSelectedItem().toString();
        getMaMonHoc(tenMonHoc, new OnMaMonHocCallback()
        {
            @Override
            public void onMaMonHocReceived(String maMonHoc)
            {
                if (maMonHoc != null)
                {
                    getMaDoKho(tenDoKho, new OnMaDoKhoCallback()
                    {
                        @Override
                        public void onMaDoKhoReceived(String maDoKho)
                        {
                            if (maDoKho != null)
                            {
                                String noiDung = noi_dung_cau_hoi.getText().toString();
                                String maCH = dbRef_3.push().getKey();

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String dateString = dateFormat.format(calendar.getTime());

                                if(!noiDung.isEmpty())
                                {
                                    CAUHOI chiTietCauHoi = new CAUHOI(maCH, maDoKho, noiDung, maMonHoc, user_account, dateString);
                                    dbRef_3.child(maCH).setValue(chiTietCauHoi).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(ThemCauHoi.this, "Câu hỏi đã được tạo", Toast.LENGTH_LONG).show();
                                                noi_dung_cau_hoi.setText("");

                                            } else {
                                                Toast.makeText(ThemCauHoi.this, "Lỗi", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(ThemCauHoi.this, "Nội dung đang còn trống", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(ThemCauHoi.this, "Không tìm thấy mã độ khó", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(ThemCauHoi.this, "Không tìm thấy mã môn học", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getMaMonHoc(String tenMonHoc, OnMaMonHocCallback callback)
    {
        dbRef.orderByChild("tenMH").equalTo(tenMonHoc).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String maMonHoc = null;
                for (DataSnapshot monHocSnapshot : dataSnapshot.getChildren()) {
                    maMonHoc = monHocSnapshot.getKey();
                    break;
                }
                callback.onMaMonHocReceived(maMonHoc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Lỗi", databaseError.toException());
                callback.onMaMonHocReceived(null);
            }
        });
    }

    private void getMaDoKho(String tenDoKho, OnMaDoKhoCallback callback)
    {
        dbRef_2.orderByChild("tenDK").equalTo(tenDoKho).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String maDoKho = null;
                for (DataSnapshot doKhoSnapshot : dataSnapshot.getChildren())
                {
                    maDoKho = doKhoSnapshot.getKey();
                    break;
                }
                callback.onMaDoKhoReceived(maDoKho);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.w(TAG, "Lỗi", databaseError.toException());
                callback.onMaDoKhoReceived(null);
            }
        });
    }


    private void SetUserImage()
    {
        DatabaseReference db_userimage = FirebaseDatabase.getInstance().getReference("USERIMAGE");
        user_dang_tai = findViewById(R.id.anh_dai_dien_nguoi_them_cau_hoi);
        db_userimage.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot data : snapshot.getChildren())
                {
                    if(data.getKey().equals(user_account))
                    {
                        String file_image = data.child("fileImage").getValue(String.class);
                        if (file_image != null)
                        {
                            Glide.with(ThemCauHoi.this).load(file_image).into(user_dang_tai);
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(ThemCauHoi.this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void GetTenNguoiThemCauHoi()
    {
        ten_giang_vien = findViewById(R.id.ten_nguoi_them_cau_hoi);
        dbRef_4.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String ten_gv = snapshot.child(user_account).child("hoTenGV").getValue(String.class);
                ten_giang_vien.setText(ten_gv);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    interface OnMaMonHocCallback {
        void onMaMonHocReceived(String maMonHoc);
    }

    interface OnMaDoKhoCallback {
        void onMaDoKhoReceived(String maDoKho);
    }

    interface OnMaGiangVienCallback {
        void onMaGiangVienReceived(String maGiangVien);
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
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }

}
