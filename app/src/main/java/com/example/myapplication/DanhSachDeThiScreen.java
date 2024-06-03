package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    dethidataoitem selected_dethi;
    BottomSheetDialog bottom_sheet_dialog;
    SessionManager sessionManager;
    String user_account;
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
        // Khởi tạo list dữ liệu cho recycleview
        mylist = new ArrayList<>();

        // Khởi tạo biến dùng để truy xuất user_login;
        sessionManager = new SessionManager(getApplicationContext());
        user_account = sessionManager.getUsername();


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
        setupBottomSheetDialog();

        shimmer_layout = findViewById(R.id.ds_cau_hoi_shimmer_layout);
        shimmer_layout.startShimmer();
        Handler handler = new Handler();
        handler.postDelayed(() ->
        {
            de_thi_rcv.setVisibility(View.VISIBLE);
            shimmer_layout.stopShimmer();
            shimmer_layout.setVisibility(View.INVISIBLE);
        },3000);
        GetListDeThi();
        GetDataDeThiFromFireBase();


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
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
    private void setupBottomSheetDialog()
    {
        bottom_sheet_dialog = new BottomSheetDialog(DanhSachDeThiScreen.this);
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
                Intent intent = new Intent(DanhSachDeThiScreen.this, ChiTietDeThiScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("madethi",selected_dethi.getMade());
                intent.putExtra("data_madethi", bundle);
                startActivity(intent);
            }
        });
        button_sua_de_thi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DanhSachDeThiScreen.this, SuaDeThiScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("madethi",selected_dethi.getMade());
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
    private void GetListDeThi()
    {
        de_thi_rcv = findViewById(R.id.danh_sach_cau_hoi_recycle_view);
        GridLayoutManager grid_layout_manager = new GridLayoutManager(DanhSachDeThiScreen.this, 1);
        de_thi_rcv.setLayoutManager(grid_layout_manager);
        adapter = new DsDeThiDaTaoAdapter(mylist, new DsDeThiDaTaoAdapter.DsDeThiClickCallBack()
        {
            @Override
            public void dsDeThiCallBack(dethidataoitem dethi)
            {
                selected_dethi = dethi;
                bottom_sheet_dialog.show();
            }
        });
        de_thi_rcv.setAdapter(adapter);
        DividerItemDecoration item_decoration = new DividerItemDecoration(DanhSachDeThiScreen.this, DividerItemDecoration.VERTICAL);
        de_thi_rcv.addItemDecoration(item_decoration);
    }
    private void GetDataDeThiFromFireBase()
    {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        db_dethi.addChildEventListener(new ChildEventListener()
        {
            int stt = 1;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                DETHI dethi = snapshot.getValue(DETHI.class);

                if(dethi.getMaGV().equals(user_account))
                {
                    db_monhoc.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            String monhoc = snapshot.child(dethi.getMaMH()).child("tenMH").getValue(String.class);
                            db_hknh.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    String hocky = String.valueOf(snapshot.child(dethi.getMaHKNH()).child("hocKy").getValue(Integer.class));
                                    String nam1 = String.valueOf(snapshot.child(dethi.getMaHKNH()).child("nam1").getValue(Integer.class));
                                    String nam2 = String.valueOf(snapshot.child(dethi.getMaHKNH()).child("nam2").getValue(Integer.class));
                                    mylist.add(0, new dethidataoitem(String.valueOf(stt), dethi.getMaDT(), monhoc,
                                            hocky, nam1 +  "/" + nam2, String.valueOf(dethi.getThoiLuong()), dethi.getNgayThi()));
                                    stt++;
                                    for (int i = 0; i < mylist.size(); i++)
                                    {
                                        dethidataoitem item = mylist.get(i);
                                        item.setStt(String.valueOf(i + 1));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {

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
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                DETHI dethi = snapshot.getValue(DETHI.class);

                if(dethi.getMaGV().equals(user_account))
                {
                    for (dethidataoitem item : mylist)
                    {
                        if (item.getMade().equals(dethi.getMaDT()))
                        {
                            db_monhoc.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    String monhoc = snapshot.child(dethi.getMaMH()).child("tenMH").getValue(String.class);
                                    db_hknh.addListenerForSingleValueEvent(new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot)
                                        {
                                            String hocky = String.valueOf(snapshot.child(dethi.getMaHKNH()).child("hocKy").getValue(Integer.class));
                                            String nam1 = String.valueOf(snapshot.child(dethi.getMaHKNH()).child("nam1").getValue(Integer.class));
                                            String nam2 = String.valueOf(snapshot.child(dethi.getMaHKNH()).child("nam2").getValue(Integer.class));
                                            item.setTenmon(monhoc);
                                            item.setHocky(hocky);
                                            item.setNamhoc(nam1 + "/" + nam2);
                                            item.setThoiluong(String.valueOf(dethi.getThoiLuong()));
                                            item.setNgaytao(dethi.getNgayThi());
                                            adapter.notifyDataSetChanged();
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error)
                                        {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                DETHI dethi = snapshot.getValue(DETHI.class);

                if(dethi.getMaGV().equals(user_account))
                {
                    for (int i = 0; i < mylist.size(); i++)
                    {
                        if (mylist.get(i).getMade().equals(dethi.getMaDT()))
                        {
                            mylist.remove(i);
                            for (int j = i; j < mylist.size(); j++)
                            {
                                mylist.get(j).setStt(String.valueOf(j + 1));
                            }
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                // This typically occurs when the order of the items changes.
                // In our scenario, we'll just update the list.
                // In most Firebase scenarios, this isn't necessary unless a specific order is being used.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Handle possible errors.
            }
        });
    }

    private void showPopupXoa()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachDeThiScreen.this);
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
                            if(data.child("maDT").getValue(String.class).equals(selected_dethi.getMade()))
                            {
                                db_dethi_cauhoi.child(data.getKey()).removeValue();
                            }
                        }
                        db_dethi.child(selected_dethi.getMade()).removeValue();
                        Toast.makeText(DanhSachDeThiScreen.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        bottom_sheet_dialog.dismiss();
                        mylist.remove(selected_dethi);
                        for(int i = 0; i<mylist.size();i++)
                        {
                            mylist.get(i).setStt(String.valueOf(i+1));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });

            }
        });
        button_huy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}