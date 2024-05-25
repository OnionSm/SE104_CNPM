package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.SearchView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TraCuuScreen extends AppCompatActivity
{

    ArrayList<dethitracuuitem> mylist;
    DeThiTraCuuAdapter adapter;
    RecyclerView dethi_rcv;

    SearchView searview;

    ImageButton quay_lai_main_screen;
    dethitracuuitem dethi_selected;
    BottomSheetDialog bottom_sheet_dialog;


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
                startActivity(new Intent(TraCuuScreen.this, MainScreenNew.class));
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
        setupBottomSheetDialog();
        GetDataFromFireBase();
        GetListDeThi();
        setupOnBackPressed();
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
        EditText hk_edt = dialog.findViewById(R.id.bo_loc_edt_hoc_ky2);
        EditText mh_edt = dialog.findViewById(R.id.bo_loc_edt_mon_hoc);
        EditText thoiluong_edt = dialog.findViewById(R.id.bo_loc_edt_thoi_luong);
        EditText ngaythi_edt = dialog.findViewById(R.id.bo_loc_edt_ngay_thi);
        EditText namhoc_edt = dialog.findViewById(R.id.bo_loc_edt_nam_hoc);
        ImageButton tra_cuu_btn = dialog.findViewById(R.id.bo_loc_button_tra_cuu);




        tra_cuu_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String made = madt_edt.getText().toString();
                adapter.getFilterMaDe().filter(made);
                String ngaythi = ngaythi_edt.getText().toString();
                adapter.getFilterNgayThi().filter(ngaythi);
                String monhoc = mh_edt.getText().toString();
                adapter.getFilterMonHoc().filter(monhoc);
                String thoiluong = thoiluong_edt.getText().toString();
                adapter.getFilterThoiLuong().filter(thoiluong);
                String hocky = hk_edt.getText().toString();
                adapter.getFilterHocKy().filter(hocky);
                String namhoc = namhoc_edt.getText().toString();
                adapter.getFilterNam().filter(namhoc);
                dialog.dismiss();
            }
        });
    }

    private void GetDataFromFireBase()
    {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        Query query = db_dethi.limitToLast(20);
        mylist = new ArrayList<>();
        query.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                String ma_DT = snapshot.child("maDT").getValue(String.class);
                String ngay_Thi = snapshot.child("ngayThi").getValue(String.class);
                String ma_MH_dethi = snapshot.child("maMH").getValue(String.class);
                String thoiluong = String.valueOf(snapshot.child("thoiLuong").getValue(Integer.class));

                db_monhoc.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String tenmon = snapshot.child(ma_MH_dethi).child("tenMH").getValue(String.class);
                        mylist.add(new dethitracuuitem(tenmon, ngay_Thi, ma_DT, thoiluong));
                        adapter.notifyDataSetChanged();

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
        adapter = new DeThiTraCuuAdapter(mylist, new DeThiTraCuuAdapter.DeThiTraCuuClickCallBack()
        {
            @Override
            public void deThiTraCuuCallBack(dethitracuuitem dethi)
            {
                dethi_selected = dethi;
                bottom_sheet_dialog.show();
            }
        });
        dethi_rcv.setAdapter(adapter);

        RecyclerView.ItemDecoration item_decoration = new DividerItemDecoration(TraCuuScreen.this, DividerItemDecoration.VERTICAL);
        dethi_rcv.addItemDecoration(item_decoration);
        searview = findViewById(R.id.tra_cuu_search_view);
        searview.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                adapter.getFilterMaDe().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilterMaDe().filter(newText);
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
    private void setupOnBackPressed()
    {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if(isEnabled())
                {
                    startActivity(new Intent(TraCuuScreen.this, MainScreenNew.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
    private void setupBottomSheetDialog()
    {
        bottom_sheet_dialog = new BottomSheetDialog(TraCuuScreen.this);
        View bottom_sheet_view = getLayoutInflater().inflate(R.layout.bottom_xem_sua_xoa_de_thi, null);
        bottom_sheet_dialog.setContentView(bottom_sheet_view);
        ImageButton button_xem_de_thi = bottom_sheet_view.findViewById(R.id.button_chi_tiet_de_thi);
        ImageButton button_sua_de_thi = bottom_sheet_view.findViewById(R.id.button_sua_de_thi);
        ImageButton button_xoa_de_thi = bottom_sheet_view.findViewById(R.id.button_xoa_de_thi);
        button_xem_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(TraCuuScreen.this, ChiTietDeThiScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("madethi", dethi_selected.getMa_de());
                intent.putExtra("data_madethi", bundle);
                startActivity(intent);
            }
        });
        button_sua_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(TraCuuScreen.this, SuaDeThiScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("madethi",dethi_selected.getMa_de());
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        button_xoa_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopupXoa();
            }
        });

    }
    private void showPopupXoa()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(TraCuuScreen.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pop_up_xoa_de_thi, null);
        builder.setView(dialogView);
        Button button_dong_y = dialogView.findViewById(R.id.button_dong_y_xoa);
        Button button_huy = dialogView.findViewById(R.id.button_huy_xoa);
        AlertDialog dialog = builder.create();
        button_dong_y.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
                DatabaseReference db_dethi_cauhoi = FirebaseDatabase.getInstance().getReference("DETHICAUHOI");
                db_dethi_cauhoi.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            if(data.child("maDT").getValue(String.class).equals(dethi_selected.getMa_de()))
                            {
                                db_dethi_cauhoi.child(data.getKey()).removeValue();
                            }
                        }
                        db_dethi.child(dethi_selected.getMa_de()).removeValue();
                        Toast.makeText(TraCuuScreen.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        bottom_sheet_dialog.dismiss();
                        mylist.remove(dethi_selected);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        button_huy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}