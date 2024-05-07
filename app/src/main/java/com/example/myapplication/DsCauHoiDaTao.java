package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DsCauHoiDaTao extends AppCompatActivity
{
    String stt[] = {"1","2","3"};

    String ten_mon[] = {"Nhập môn lập trình","Toán cho khoa học máy tính", "Nhập môn Công nghệ phần mềm"};
    String mo_ta[] = {"Môn này nên học", "Học rồi không biết đã học hay chưa", "Rất là okela"};

    String do_kho[] = {"Dễ", "Khó vcl", "Khó"};

    String ngay_tao[]  = {"23/4//2024", "24/4/2024", "25/4/2024"};
    ArrayList<cauhoiitem> mylist;
    DsCauHoiDaTaoAdapter adapter;
    RecyclerView cauhoi_rcv;

    ImageButton quay_lai_cau_hoi_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ds_cau_hoi_da_tao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ds_cau_hoi_da_tao), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        quay_lai_cau_hoi_screen = findViewById(R.id.ds_danh_sach_cau_hoi_icon_back);
        quay_lai_cau_hoi_screen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(DsCauHoiDaTao.this, CauHoiScreen.class));
                finish();
            }
        });
        GetDataFromFireBase();
        setupOnBackPressed();

    }

    private void GetDataFromFireBase()
    {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        DatabaseReference db_cauhoi = FirebaseDatabase.getInstance().getReference("CAUHOI");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        DatabaseReference db_dokho = FirebaseDatabase.getInstance().getReference("DOKHO");

        mylist = new ArrayList<>();


        db_cauhoi.addChildEventListener(new ChildEventListener()
        {
            int stt = 1;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

                String mamh = snapshot.child("maMH").getValue(String.class);
                String madokho = snapshot.child("maDoKho").getValue(String.class);
                String magv = snapshot.child("maGV").getValue(String.class);
                String ngaytao = snapshot.child("ngaytao").getValue(String.class);
                String noidung = snapshot.child("noiDung").getValue(String.class);
                db_pdn.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String taikhoan = snapshot.child("account").getValue(String.class);
                        if(taikhoan.equals(magv))
                        {
                            db_monhoc.addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {

                                    String tenmon = snapshot.child(mamh).child("tenMH").getValue(String.class);
                                    db_dokho.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot)
                                        {
                                            String dokho = snapshot.child(madokho).child("TenDK").getValue(String.class);
                                            mylist.add(0,new cauhoiitem(String.valueOf(stt),tenmon,noidung,dokho,ngaytao));
                                            stt = stt +1;
                                            GetListCauHoi();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
    private void GetListCauHoi()
    {
        cauhoi_rcv = findViewById(R.id.danh_sach_cau_hoi_recycle_view);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(DsCauHoiDaTao.this);
        cauhoi_rcv.setLayoutManager(ln_layout_manager);
        adapter = new DsCauHoiDaTaoAdapter(mylist);
        cauhoi_rcv.setAdapter(adapter);

        RecyclerView.ItemDecoration item_decoration = new DividerItemDecoration(DsCauHoiDaTao.this, DividerItemDecoration.VERTICAL);
        cauhoi_rcv.addItemDecoration(item_decoration);
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
                    startActivity(new Intent(DsCauHoiDaTao.this, CauHoiScreen.class));
                    setEnabled(false);
                    finish();
                }
            }
        });
    }


}