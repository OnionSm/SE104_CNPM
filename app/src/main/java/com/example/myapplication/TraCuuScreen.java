package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.SearchView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TraCuuScreen extends AppCompatActivity {

    ArrayList<dethitracuuitem> mylist;
    DeThiTraCuuAdapter adapter;
    RecyclerView dethi_rcv;

    SearchView searview;

    ImageButton quay_lai_main_screen;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tra_cuu_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tra_cuu_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quay_lai_main_screen = findViewById(R.id.tra_cuu_back_button);

        quay_lai_main_screen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(TraCuuScreen.this, MainScreen.class));
            }
        });

        ImageButton bo_loc_mo_rong = findViewById(R.id.tra_cuu_mo_rong_button);
        bo_loc_mo_rong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CreatePopUpFilter();
            }
        });

        GetDataFromFireBase();
    }




    private void CreatePopUpFilter()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_bo_loc_tra_cuu_screen);

        Window window = dialog.getWindow();
        if(window == null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams window_attributes = window.getAttributes();
        window_attributes.gravity = Gravity.CENTER;
        window.setAttributes(window_attributes);

        dialog.setCanceledOnTouchOutside(true);

        dialog.show();

        EditText madt_edt = dialog.findViewById(R.id.bo_loc_edt_ma_de);
        EditText mahk_edt = dialog.findViewById(R.id.bo_loc_edt_nam_hoc);
        EditText mamh_edt = dialog.findViewById(R.id.bo_loc_edt_mon_hoc);
        EditText thoiluong_edt = dialog.findViewById(R.id.bo_loc_edt_thoi_luong);
        EditText ngaythi_edt = dialog.findViewById(R.id.bo_loc_edt_ngay_thi);
        EditText magv_edt = dialog.findViewById(R.id.bo_loc_edt_ma_gv);
        ImageButton tra_cuu_btn = dialog.findViewById(R.id.bo_loc_button_tra_cuu);

        String madt = madt_edt.toString();
        String mahk = mahk_edt.toString();
        String mamh = mamh_edt.toString();
        String thoiluong = thoiluong_edt.toString();
        String ngaythi = ngaythi_edt.toString();
        String magv = magv_edt.toString();

        tra_cuu_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                dialog.dismiss();
            }
        });
    }

    private void GetDataFromFireBase()
    {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        Query query = db_dethi.limitToLast(10);
        mylist = new ArrayList<>();
        query.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                String ma_DT = snapshot.child("maDT").getValue(String.class);
                String ngay_Thi = snapshot.child("ngayThi").getValue(String.class);
                String ma_MH_dethi = snapshot.child("maMH").getValue(String.class);
                if (ma_DT != null && ngay_Thi != null && ma_MH_dethi != null)
                {
                    Log.e("Thông tin", "MaDT: " + ma_DT + ", NgayThi: " + ngay_Thi + ", MaMH: " + ma_MH_dethi);
                }
                db_monhoc.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String tenmon = snapshot.child(ma_MH_dethi).child("tenMH").getValue(String.class);
                        Log.e("môn học", tenmon);
                        mylist.add(new dethitracuuitem(tenmon, ngay_Thi, ma_DT));
                        GetListDeThi();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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

    private void GetListDeThi()
    {
        dethi_rcv = findViewById(R.id.ds_de_thi_rcv);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(TraCuuScreen.this);
        dethi_rcv.setLayoutManager(ln_layout_manager);
        adapter = new DeThiTraCuuAdapter(mylist);
        dethi_rcv.setAdapter(adapter);

        RecyclerView.ItemDecoration item_decoration = new DividerItemDecoration(TraCuuScreen.this, DividerItemDecoration.VERTICAL);
        dethi_rcv.addItemDecoration(item_decoration);
        searview = findViewById(R.id.tra_cuu_search_view);
        searview.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        // Sau khi đã thêm dữ liệu mới vào danh sách, cập nhật giao diện nếu cần
        // Ví dụ: Nếu bạn sử dụng RecyclerView, bạn có thể gọi notifyDataSetChanged()
        // adapter.notifyDataSetChanged();
    }

    private void GetDataSorted()
    {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
    }

}