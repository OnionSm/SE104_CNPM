package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    private BottomSheetDialog bottom_sheet_dialog;
    ArrayList<cauhoiitem> mylist;
    DsCauHoiDaTaoAdapter adapter;

    RecyclerView cauhoi_rcv;

    ImageButton quay_lai_cau_hoi_screen;

    ShimmerFrameLayout shimmer_layout;
    private cauhoiitem selectedCauhoi;
    SessionManager sessionManager;
    String user_account;


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
        sessionManager = new SessionManager(getApplicationContext());
        user_account = sessionManager.getUsername();

        adapter = new DsCauHoiDaTaoAdapter(mylist);
        setupBottomSheetDialog();

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
                finish();
            }
        });
        GetDataFromFireBase();
        GetListCauHoi();
        setupOnBackPressed();

    }

    private void setupBottomSheetDialog()
    {
        bottom_sheet_dialog = new BottomSheetDialog(DsCauHoiDaTao.this);
        View bottom_sheet_view = getLayoutInflater().inflate(R.layout.bottom_sheet_sua_xoa, null);
        bottom_sheet_dialog.setContentView(bottom_sheet_view);
        ImageButton button_sua_cau_hoi = bottom_sheet_view.findViewById(R.id.button_sua_cau_hoi);
        ImageButton button_xoa_cau_hoi = bottom_sheet_view.findViewById(R.id.button_xoa_cau_hoi);
        button_sua_cau_hoi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateCauHoiToDatabase();
            }
        });
        button_xoa_cau_hoi.setOnClickListener(v -> showPopupXoa());
    }

    private void updateCauHoiToDatabase()
    {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_sua_cau_hoi_pop_up);

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
        EditText noiDungEditText = dialog.findViewById(R.id.noi_dung_cau_hoi_edt);
        EditText doKhoEditText = dialog.findViewById(R.id.do_kho_edt);
        ImageButton thayDoiButton = dialog.findViewById(R.id.thay_doi_cau_hoi_button);
        if (selectedCauhoi != null)
        {
            noiDungEditText.setText(selectedCauhoi.getMo_ta());
            doKhoEditText.setText(selectedCauhoi.getDo_kho());
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        thayDoiButton.setOnClickListener(v ->
        {
            DatabaseReference dbCauhoi = FirebaseDatabase.getInstance().getReference("CAUHOI");
            DatabaseReference dbDokho = FirebaseDatabase.getInstance().getReference("DOKHO");
            DatabaseReference dbDeThiCauHoi = FirebaseDatabase.getInstance().getReference("DETHICAUHOI");
            dbDeThiCauHoi.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for(DataSnapshot data : snapshot.getChildren())
                    {
                        if(data.child("maCH").getValue(String.class).equals(selectedCauhoi.getMach()))
                        {
                            Toast.makeText(DsCauHoiDaTao.this, "Không thể sửa vì câu hỏi đã được sử dụng ở đề thi khác", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            bottom_sheet_dialog.dismiss();
                            return;
                        }
                    }
                    String newNoiDung = noiDungEditText.getText().toString();
                    String newDoKho = doKhoEditText.getText().toString();

                    if (!newNoiDung.isEmpty() && !newDoKho.isEmpty())
                    {
                        if (newDoKho.equals("Dễ") || newDoKho.equals("Trung bình") || newDoKho.equals("Khó") || newDoKho.equals("Phức tạp"))
                        {
                            dbDokho.orderByChild("TenDK").equalTo(newDoKho).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    if (snapshot.exists())
                                    {
                                        for (DataSnapshot data : snapshot.getChildren())
                                        {
                                            String newMaDoKho = data.getKey();
                                            dbCauhoi.child(selectedCauhoi.getMach()).child("noiDung").setValue(newNoiDung);
                                            dbCauhoi.child(selectedCauhoi.getMach()).child("maDoKho").setValue(newMaDoKho)
                                                    .addOnSuccessListener(aVoid -> {
                                                        selectedCauhoi.setMo_ta(newNoiDung);
                                                        selectedCauhoi.setDo_kho(newDoKho);
                                                        adapter.notifyDataSetChanged();
                                                        dialog.dismiss();
                                                        bottom_sheet_dialog.dismiss();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(DsCauHoiDaTao.this, "Failed to update question", Toast.LENGTH_SHORT).show();
                                                    });
                                            break;
                                        }
                                    } else {
                                        Toast.makeText(DsCauHoiDaTao.this, "Invalid difficulty level", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(DsCauHoiDaTao.this, "Failed to update question", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(DsCauHoiDaTao.this, "Invalid difficulty level", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(DsCauHoiDaTao.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });

        });
    }



    private void showPopupXoa()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(DsCauHoiDaTao.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pop_up_xoa, null);
        builder.setView(dialogView);
        Button button_dong_y = dialogView.findViewById(R.id.button_dong_y_xoa);
        Button button_huy = dialogView.findViewById(R.id.button_huy_xoa);
        AlertDialog dialog = builder.create();
        button_dong_y.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference dbDeThiCauHoi = FirebaseDatabase.getInstance().getReference("DETHICAUHOI");
                DatabaseReference dbCauHoi = FirebaseDatabase.getInstance().getReference("CAUHOI");
                dbDeThiCauHoi.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            if(data.child("maCH").getValue(String.class).equals(selectedCauhoi.getMach()))
                            {
                                Toast.makeText(DsCauHoiDaTao.this, "Không thể xóa vì câu hỏi đã được sử dụng ở đề thi khác", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                bottom_sheet_dialog.dismiss();
                                return;
                            }
                        }
                        dbCauHoi.child(selectedCauhoi.getMach()).removeValue();
                        mylist.remove(selectedCauhoi);
                        for(int i = 0; i<mylist.size();i++)
                        {
                            mylist.get(i).setStt(String.valueOf(i+1));
                        }
                        dialog.dismiss();
                        bottom_sheet_dialog.dismiss();
                        Toast.makeText(DsCauHoiDaTao.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
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

    private void GetDataFromFireBase()
    {
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
                String mach = snapshot.child("maCH").getValue(String.class);
                String mamh = snapshot.child("maMH").getValue(String.class);
                String madokho = snapshot.child("maDoKho").getValue(String.class);
                String magv = snapshot.child("maGV").getValue(String.class);
                String ngaytao = snapshot.child("ngaytao").getValue(String.class);
                String noidung_origin = snapshot.child("noiDung").getValue(String.class);
                String noidung = noidung_origin.length() > 200 ? noidung_origin.substring(0, 200) : noidung_origin;

                if (user_account.equals(magv))
                {
                    // Fetch subject name and difficulty level in parallel
                    db_monhoc.child(mamh).addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot monhocSnapshot) {
                            String tenmon = monhocSnapshot.child("tenMH").getValue(String.class);

                            db_dokho.child(madokho).addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dokhoSnapshot)
                                {
                                    String dokho = dokhoSnapshot.child("TenDK").getValue(String.class);
                                    mylist.add(0, new cauhoiitem(String.valueOf(stt), mach, tenmon, noidung, dokho, ngaytao));
                                    stt++;
                                    // Update the serial numbers
                                    for (int i = 0; i < mylist.size(); i++)
                                    {
                                        cauhoiitem item = mylist.get(i);
                                        item.setStt(String.valueOf(i + 1));
                                    }
                                    adapter.notifyDataSetChanged(); // Notify the adapter of dataset changes
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {
                                    // Handle error
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {
                            // Handle error
                        }
                    });
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                String mach = snapshot.child("maCH").getValue(String.class);
                for (cauhoiitem item : mylist) {
                    if (item.getMach().equals(mach)) {
                        // Update the item fields
                        String mamh = snapshot.child("maMH").getValue(String.class);
                        String madokho = snapshot.child("maDoKho").getValue(String.class);
                        String ngaytao = snapshot.child("ngaytao").getValue(String.class);
                        String noidung_origin = snapshot.child("noiDung").getValue(String.class);
                        String noidung = noidung_origin.length() > 200 ? noidung_origin.substring(0, 200) : noidung_origin;

                        item.setMo_ta(noidung);
                        item.setNgay_tao(ngaytao);

                        db_monhoc.child(mamh).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot monhocSnapshot) {
                                String tenmon = monhocSnapshot.child("tenMH").getValue(String.class);
                                item.setMon_hoc(tenmon);

                                db_dokho.child(madokho).addListenerForSingleValueEvent(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dokhoSnapshot) {
                                        String dokho = dokhoSnapshot.child("TenDK").getValue(String.class);
                                        item.setDo_kho(dokho);
                                        adapter.notifyDataSetChanged();
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
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                String mach = snapshot.child("maCH").getValue(String.class);
                for (int i = 0; i < mylist.size(); i++) {
                    if (mylist.get(i).getMach().equals(mach)) {
                        mylist.remove(i);
                        // Update the serial numbers
                        for (int j = 0; j < mylist.size(); j++) {
                            mylist.get(j).setStt(String.valueOf(j + 1));
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                // This usually indicates a reordering of elements. Handle if necessary.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Handle error
            }
        });
    }



    private void GetListCauHoi()
    {
        cauhoi_rcv = findViewById(R.id.danh_sach_cau_hoi_recycle_view);
        // Set GridLayoutManager with 1 column
        GridLayoutManager grid_layout_manager = new GridLayoutManager(DsCauHoiDaTao.this, 1);
        cauhoi_rcv.setLayoutManager(grid_layout_manager);
        adapter = new DsCauHoiDaTaoAdapter(mylist);
        cauhoi_rcv.setAdapter(adapter);
        DividerItemDecoration item_decoration = new DividerItemDecoration(DsCauHoiDaTao.this, DividerItemDecoration.VERTICAL);
        cauhoi_rcv.addItemDecoration(item_decoration);
        adapter.setOnItemClickListener(new DsCauHoiDaTaoAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(cauhoiitem cauhoi)
            {
                selectedCauhoi = cauhoi;
                bottom_sheet_dialog.show();

            }
        });
    }

    private void setupOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled())
                {
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    setEnabled(false);
                    finish();
                }
            }
        });
    }
}

