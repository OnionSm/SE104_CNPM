package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DanhSachDeThiScreen extends AppCompatActivity
{
    ArrayList<dethidataoitem> mylist;
    DsDeThiDaTaoAdapter adapter;
    RecyclerView de_thi_rcv;

    ImageButton back_button;
    ShimmerFrameLayout shimmer_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danh_sach_de_thi_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back_button = findViewById(R.id.ds_danh_sach_cau_hoi_icon_back);
        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(DanhSachDeThiScreen.this, DeThiScreen.class));
                finish();
            }
        });
        shimmer_layout = findViewById(R.id.ds_cau_hoi_shimmer_layout);
        shimmer_layout.startShimmer();
        Handler handler = new Handler();
        handler.postDelayed(() ->
        {
            de_thi_rcv.setVisibility(View.VISIBLE);
            shimmer_layout.stopShimmer();
            shimmer_layout.setVisibility(View.INVISIBLE);
        },3000);

        GetDataDeThiFromFireBase();
        GetListDeThi();


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
                    startActivity(new Intent(DanhSachDeThiScreen.this, DeThiScreen.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }

    private void GetListDeThi()
    {
        de_thi_rcv = findViewById(R.id.danh_sach_cau_hoi_recycle_view);
        GridLayoutManager grid_layout_manager = new GridLayoutManager(DanhSachDeThiScreen.this, 1);
        de_thi_rcv.setLayoutManager(grid_layout_manager);
        adapter = new DsDeThiDaTaoAdapter(mylist);
        de_thi_rcv.setAdapter(adapter);
        DividerItemDecoration item_decoration = new DividerItemDecoration(DanhSachDeThiScreen.this, DividerItemDecoration.VERTICAL);
        de_thi_rcv.addItemDecoration(item_decoration);
    }
    private void GetDataDeThiFromFireBase()
    {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        mylist = new ArrayList<>();
        db_dethi.addChildEventListener(new ChildEventListener()
        {
            int stt = 1;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                String madt = snapshot.child("maDT").getValue(String.class);
                String magv = snapshot.child("maGV").getValue(String.class);
                String mahknh = snapshot.child("maHKNH").getValue(String.class);
                String ngaythi = snapshot.child("ngayThi").getValue(String.class);
                String thoiluong = String.valueOf(snapshot.child("thoiLuong").getValue(Integer.class));
                String mamh = snapshot.child("maMH").getValue(String.class);
                db_pdn.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String taikhoan = snapshot.child("account").getValue(String.class);
                        if(magv.equals(taikhoan))
                        {
                            db_monhoc.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String monhoc = snapshot.child(mamh).child("tenMH").getValue(String.class);
                                    db_hknh.addValueEventListener(new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot)
                                        {
                                            String hocky = String.valueOf(snapshot.child(mahknh).child("hocKy").getValue(Integer.class));
                                            String nam1 = String.valueOf(snapshot.child(mahknh).child("nam1").getValue(Integer.class));
                                            String nam2 = String.valueOf(snapshot.child(mahknh).child("nam2").getValue(Integer.class));
                                            if (mylist.size() > 50)
                                            {
                                                mylist.remove(mylist.size() - 1);
                                            }
                                            mylist.add(0, new dethidataoitem(String.valueOf(stt), madt, monhoc,
                                                    hocky, nam1 +  "/" + nam2, thoiluong, ngaythi));
                                            stt++;
                                            for (int i = 0; i < mylist.size(); i++) {
                                                dethidataoitem item = mylist.get(i);
                                                item.setStt(String.valueOf(i + 1));
                                            }
                                            adapter.notifyDataSetChanged();

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
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}