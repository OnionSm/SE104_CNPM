package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DsCauHoiDaTao extends AppCompatActivity
{
    ArrayList<cauhoiitem> mylist;
    DsCauHoiDaTaoAdapter adapter;
    RecyclerView cauhoi_rcv;

    ImageButton quay_lai_cau_hoi_screen;

    ShimmerFrameLayout shimmer_layout;

    LinearLayout data_layout;
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


        shimmer_layout = findViewById(R.id.ds_cau_hoi_shimmer_layout);
        shimmer_layout.startShimmer();
        Handler handler = new Handler();
        handler.postDelayed(() ->
        {
            cauhoi_rcv.setVisibility(View.VISIBLE);
            shimmer_layout.stopShimmer();
            shimmer_layout.setVisibility(View.INVISIBLE);
        },3000);




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
        GetListCauHoi();
        setupOnBackPressed();

    }

    private void GetDataFromFireBase() {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        DatabaseReference db_cauhoi = FirebaseDatabase.getInstance().getReference("CAUHOI");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        DatabaseReference db_dokho = FirebaseDatabase.getInstance().getReference("DOKHO");

        mylist = new ArrayList<>();

        db_cauhoi.addChildEventListener(new ChildEventListener() {
            int stt = 1;

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String mamh = snapshot.child("maMH").getValue(String.class);
                String madokho = snapshot.child("maDoKho").getValue(String.class);
                String magv = snapshot.child("maGV").getValue(String.class);
                String ngaytao = snapshot.child("ngaytao").getValue(String.class);
                String noidung_origin = snapshot.child("noiDung").getValue(String.class);
                String noidung = noidung_origin.length() > 200 ? noidung_origin.substring(0, 200) : noidung_origin;

                db_pdn.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot pdnSnapshot) {
                        String taikhoan = pdnSnapshot.child("account").getValue(String.class);
                        if (taikhoan.equals(magv)) {
                            // Fetch subject name and difficulty level in parallel
                            db_monhoc.child(mamh).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot monhocSnapshot) {
                                    String tenmon = monhocSnapshot.child("tenMH").getValue(String.class);

                                    db_dokho.child(madokho).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dokhoSnapshot) {
                                            String dokho = dokhoSnapshot.child("TenDK").getValue(String.class);

                                            if (mylist.size() > 50) {
                                                mylist.remove(mylist.size() - 1);
                                            }
                                            mylist.add(0, new cauhoiitem(String.valueOf(stt), tenmon, noidung, dokho, ngaytao));
                                            stt++;

                                            // Update the serial numbers
                                            for (int i = 0; i < mylist.size(); i++) {
                                                cauhoiitem item = mylist.get(i);
                                                item.setStt(String.valueOf(i + 1));
                                            }
                                            adapter.notifyDataSetChanged(); // Notify the adapter of dataset changes
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle error
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle child changed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle child removed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle child moved
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }









    /*private void GetDataFromFireBase()
    {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        DatabaseReference db_cauhoi = FirebaseDatabase.getInstance().getReference("CAUHOI");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        DatabaseReference db_dokho = FirebaseDatabase.getInstance().getReference("DOKHO");

        mylist = new ArrayList<>();
        for(int i = 0;i<20;i++)
        {
            mylist.add(0, new cauhoiitem(String.valueOf(i+1), "test môn", "test nội dung", "dễ", "15/5/2024"));
        }
        GetListCauHoi();

    }*/
    private void GetListCauHoi() {
        cauhoi_rcv = findViewById(R.id.danh_sach_cau_hoi_recycle_view);
        // Set GridLayoutManager with 1 column
        GridLayoutManager grid_layout_manager = new GridLayoutManager(DsCauHoiDaTao.this, 1);
        cauhoi_rcv.setLayoutManager(grid_layout_manager);
        adapter = new DsCauHoiDaTaoAdapter(mylist);
        cauhoi_rcv.setAdapter(adapter);
        DividerItemDecoration item_decoration = new DividerItemDecoration(DsCauHoiDaTao.this, DividerItemDecoration.VERTICAL);
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