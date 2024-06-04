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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TraCuuScreen extends AppCompatActivity {

    ArrayList<dethitracuuitem> mylist;
    DeThiTraCuuAdapter adapter;
    RecyclerView dethi_rcv;
    SearchView searview;
    ImageButton quay_lai_main_screen;
    dethitracuuitem dethi_selected;
    BottomSheetDialog bottom_sheet_dialog;
    SessionManager sessionManager;
    String user_account;

    String made;
    String ngaythi;
    String monhoc;
    String thoiluong;
    String hocky;
    String namhoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tra_cuu_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tra_cuu_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(getApplicationContext());
        user_account = sessionManager.getUsername();

        mylist = new ArrayList<>();

        quay_lai_main_screen = findViewById(R.id.tra_cuu_back_button);
        quay_lai_main_screen.setOnClickListener(v -> startActivity(new Intent(TraCuuScreen.this, MainScreenNew.class)));

        ImageButton bo_loc_mo_rong = findViewById(R.id.tra_cuu_mo_rong_button);
        bo_loc_mo_rong.setOnClickListener(v -> CreatePopUpFilter());

        setupBottomSheetDialog();
        setupRecyclerView();
        setupSearchView();
        setupOnBackPressed();
        GetDataFromFireBase();
    }

    private void CreatePopUpFilter() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_bo_loc_tra_cuu_screen);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams window_attributes = window.getAttributes();
        window_attributes.gravity = Gravity.CENTER;
        window.setAttributes(window_attributes);

        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
        for(int i = 0; i < mylist.size(); i++)
        {
            Log.e("MYLIST",mylist.get(i).toString());
        }

        EditText madt_edt = dialog.findViewById(R.id.bo_loc_edt_ma_de);
        EditText ngaythi_edt = dialog.findViewById(R.id.bo_loc_edt_ngay_thi);
        EditText mh_edt = dialog.findViewById(R.id.bo_loc_edt_mon_hoc);
        EditText thoiluong_edt = dialog.findViewById(R.id.bo_loc_edt_thoi_luong);
        EditText hk_edt = dialog.findViewById(R.id.bo_loc_edt_hoc_ky2);
        EditText namhoc_edt = dialog.findViewById(R.id.bo_loc_edt_nam_hoc);
        ImageButton tra_cuu_btn = dialog.findViewById(R.id.bo_loc_button_tra_cuu);

        if(made!=null)
        {
            madt_edt.setText(made);
        }
        if(ngaythi!=null)
        {
            ngaythi_edt.setText(ngaythi);
        }
        if(monhoc!=null)
        {
            mh_edt.setText(monhoc);
        }
        if(thoiluong!= null)
        {
            thoiluong_edt.setText(thoiluong);
        }
        if(hocky != null)
        {
            hk_edt.setText(hocky);
        }
        if(namhoc!= null)
        {
            namhoc_edt.setText(namhoc);
        }

        tra_cuu_btn.setOnClickListener(v -> {
            made = madt_edt.getText().toString();
            ngaythi = ngaythi_edt.getText().toString();
            monhoc = mh_edt.getText().toString();
            thoiluong = thoiluong_edt.getText().toString();
            hocky = hk_edt.getText().toString();
            namhoc = namhoc_edt.getText().toString();
            adapter.getFilterByMultipleAttributes(made,hocky,monhoc,thoiluong,ngaythi,namhoc);
            dialog.dismiss();
        });
    }

    private void GetDataFromFireBase() {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");

        db_dethi.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                processChild(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                processChild(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String ma_DT = snapshot.child("maDT").getValue(String.class);
                for (int i = 0; i < mylist.size(); i++) {
                    if (mylist.get(i).getMa_de().equals(ma_DT)) {
                        mylist.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle child moved event if needed. Generally, Firebase doesn't move data like this.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }

            private void processChild(DataSnapshot snapshot) {
                DETHI dethi = snapshot.getValue(DETHI.class);
                if (dethi == null) return;

                db_hknh.child(dethi.getMaHKNH()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot hknhSnapshot) {
                        HKNH hknh = hknhSnapshot.getValue(HKNH.class);
                        if (hknh == null) return;

                        db_monhoc.child(dethi.getMaMH()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot monhocSnapshot) {
                                String tenmon = monhocSnapshot.child("tenMH").getValue(String.class);
                                if (tenmon == null) return;

                                dethitracuuitem item = new dethitracuuitem(
                                        tenmon, dethi.getMaGV(), dethi.getMaHKNH(),
                                        String.valueOf(hknh.getHocKy()), String.valueOf(hknh.getNam1()),
                                        String.valueOf(hknh.getNam2()), dethi.getNgayThi(), dethi.getMaDT(),
                                        String.valueOf(dethi.getThoiLuong())
                                );

                                if (user_account.equals("000000") || user_account.equals(dethi.getMaGV()))
                                {
                                    int index = -1;
                                    for (int i = 0; i < mylist.size(); i++) {
                                        if (mylist.get(i).getMa_de().equals(dethi.getMaDT()))
                                        {
                                            index = i;
                                            break;
                                        }
                                    }

                                    if (index != -1) {
                                        mylist.set(index, item); // Update the item if it exists
                                    } else
                                    {
                                        mylist.add(item); // Add the item if it's new
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error)
                            {
                                // Handle possible errors.
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
            }
        });
    }

    private void setupRecyclerView()
    {
        dethi_rcv = findViewById(R.id.ds_de_thi_rcv);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(TraCuuScreen.this);
        dethi_rcv.setLayoutManager(ln_layout_manager);
        adapter = new DeThiTraCuuAdapter(mylist, dethi -> {
            dethi_selected = dethi;
            bottom_sheet_dialog.show();
        });
        dethi_rcv.setAdapter(adapter);

        RecyclerView.ItemDecoration item_decoration = new DividerItemDecoration(TraCuuScreen.this, DividerItemDecoration.VERTICAL);
        dethi_rcv.addItemDecoration(item_decoration);
    }

    private void setupSearchView()
    {
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
    }

    private void setupOnBackPressed()
    {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed()
            {
                if (isEnabled())
                {
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }

    private void setupBottomSheetDialog() {
        bottom_sheet_dialog = new BottomSheetDialog(TraCuuScreen.this);
        View bottom_sheet_view = getLayoutInflater().inflate(R.layout.bottom_xem_sua_xoa_de_thi, null);
        bottom_sheet_dialog.setContentView(bottom_sheet_view);

        ImageButton button_xem_de_thi = bottom_sheet_view.findViewById(R.id.button_chi_tiet_de_thi);
        ImageButton button_sua_de_thi = bottom_sheet_view.findViewById(R.id.button_sua_de_thi);
        ImageButton button_xoa_de_thi = bottom_sheet_view.findViewById(R.id.button_xoa_de_thi);

        button_xem_de_thi.setOnClickListener(v -> {
            Intent intent = new Intent(TraCuuScreen.this, ChiTietDeThiScreen.class);
            Bundle bundle = new Bundle();
            bundle.putString("madethi", dethi_selected.getMa_de());
            intent.putExtra("data_madethi", bundle);
            startActivity(intent);
        });

        button_sua_de_thi.setOnClickListener(v -> {
            Intent intent = new Intent(TraCuuScreen.this, SuaDeThiScreen.class);
            Bundle bundle = new Bundle();
            bundle.putString("madethi", dethi_selected.getMa_de());
            intent.putExtra("data", bundle);
            startActivity(intent);
        });

        button_xoa_de_thi.setOnClickListener(v -> showPopupXoa());
    }

    private void showPopupXoa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TraCuuScreen.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pop_up_xoa_de_thi, null);
        builder.setView(dialogView);

        Button button_dong_y = dialogView.findViewById(R.id.button_dong_y_xoa);
        Button button_huy = dialogView.findViewById(R.id.button_huy_xoa);
        AlertDialog dialog = builder.create();

        button_dong_y.setOnClickListener(v -> {
            DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
            DatabaseReference db_dethi_cauhoi = FirebaseDatabase.getInstance().getReference("DETHICAUHOI");
            db_dethi_cauhoi.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (data.child("maDT").getValue(String.class).equals(dethi_selected.getMa_de())) {
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
                    // Handle possible errors.
                }
            });
        });

        button_huy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
