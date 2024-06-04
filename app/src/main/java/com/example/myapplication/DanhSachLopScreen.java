package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DanhSachLopScreen extends AppCompatActivity
{
    ArrayList<lopitemrcv> mylist;
    DsLopAdapter adapter;

    RecyclerView lop_rcv;

    ImageButton quay_lai;

    BottomSheetDialog bottom_sheet_dialog;

    lopitemrcv selected_lop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danh_sach_lop_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lop_hoc), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mylist = new ArrayList<>();
        quay_lai = findViewById(R.id.icon_back);
        quay_lai.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        ImageButton them_lop = findViewById(R.id.them_lop_button);
        them_lop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(DanhSachLopScreen.this, ThemLopScreen.class));
            }
        });

        setupBottomSheetDialog();
        GetListCauHoi();
        GetDataLopFromFirebase();
        setupOnBackPressed();
    }
    private void setupOnBackPressed()
    {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
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
    private void GetDataLopFromFirebase() {
        DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");

        db_lop.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LOP lop = snapshot.getValue(LOP.class);
                db_hknh.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getKey().equals(lop.getMaHKNH())) {
                                String hocky = String.valueOf(data.child("hocKy").getValue(Integer.class));
                                String nam1 = String.valueOf(data.child("nam1").getValue(Integer.class));
                                String nam2 = String.valueOf(data.child("nam2").getValue(Integer.class));
                                lopitemrcv lop_item = new lopitemrcv(
                                        lop.getMaLop(),
                                        lop.getTenLop(),
                                        lop.getMaHKNH(),
                                        hocky,
                                        nam1 + nam2,
                                        lop.getSiSo(),
                                        lop.getMaMH(),
                                        lop.getMaGVCham()
                                );
                                mylist.add(lop_item);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LOP lop = snapshot.getValue(LOP.class);
                for (lopitemrcv item : mylist) {
                    if (item.getMaLop().equals(lop.getMaLop())) {
                        // Update the item details
                        item.setTenLop(lop.getTenLop());
                        item.setSiSo(lop.getSiSo());
                        item.setMaMH(lop.getMaMH());
                        item.setMaGVCham(lop.getMaGVCham());
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LOP lop = snapshot.getValue(LOP.class);
                for (int i = 0; i < mylist.size(); i++) {
                    if (mylist.get(i).getMaLop().equals(lop.getMaLop())) {
                        mylist.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle any specific actions if needed when a child is moved within the list.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
    private void GetListCauHoi()
    {
        lop_rcv = findViewById(R.id.lop_hoc_recycle_view);
        // Set GridLayoutManager with 1 column
        GridLayoutManager grid_layout_manager = new GridLayoutManager(DanhSachLopScreen.this, 1);
        lop_rcv.setLayoutManager(grid_layout_manager);
        adapter = new DsLopAdapter(mylist, new OnClickLopHocListener()
        {
            @Override
            public void onClickCallBack(lopitemrcv lop)
            {
                selected_lop = lop;
                bottom_sheet_dialog.show();
            }
        });
        lop_rcv.setAdapter(adapter);
        DividerItemDecoration item_decoration = new DividerItemDecoration(DanhSachLopScreen.this, DividerItemDecoration.VERTICAL);
        lop_rcv.addItemDecoration(item_decoration);

    }
    private void setupBottomSheetDialog()
    {
        bottom_sheet_dialog = new BottomSheetDialog(DanhSachLopScreen.this);
        View bottom_sheet_view = getLayoutInflater().inflate(R.layout.bottom_sheet_sua_xoa_lop, null);
        bottom_sheet_dialog.setContentView(bottom_sheet_view);
        ImageButton button_sua_mon_hoc = bottom_sheet_view.findViewById(R.id.button_sua_lop);
        ImageButton button_xoa_mon_hoc = bottom_sheet_view.findViewById(R.id.button_xoa_lop);
        button_sua_mon_hoc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateLopToDatabase();
            }
        });
        button_xoa_mon_hoc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopupXoa();
            }
        });
    }
    private void updateLopToDatabase()
    {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sua_lop_pop_up);

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
        EditText tenlop_edt = dialog.findViewById(R.id.ten_lop_edt);
        EditText siso_edt = dialog.findViewById(R.id.si_so_edt);
        ImageButton thayDoiButton = dialog.findViewById(R.id.thay_doi_button);
        if (selected_lop != null)
        {
            tenlop_edt.setText(selected_lop.getTenLop());
            siso_edt.setText(String.valueOf(selected_lop.getSiSo()));
        }

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        thayDoiButton.setOnClickListener(v ->
        {
            DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
            DatabaseReference db_ctlop = FirebaseDatabase.getInstance().getReference("CHITIETLOP");
            String tenlop = tenlop_edt.getText().toString();
            String siso = siso_edt.getText().toString();
            db_ctlop.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    int count = 0;
                    for(DataSnapshot data : snapshot.getChildren())
                    {
                        if(data.child("maLop").getValue(String.class).equals(selected_lop.getMaLop()))
                        {
                            count += 1;
                            break;
                        }
                    }
                    if(count == 0)
                    {
                        db_lop.child(selected_lop.getMaLop()).child("tenLop").setValue(tenlop);
                        db_lop.child(selected_lop.getMaLop()).child("siSo").setValue(Integer.parseInt(siso)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                Toast.makeText(DanhSachLopScreen.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                selected_lop.setTenLop(tenlop);
                                selected_lop.setSiSo(Integer.parseInt(siso));
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                                bottom_sheet_dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DanhSachLopScreen.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(DanhSachLopScreen.this, "Không thể sửa", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        bottom_sheet_dialog.dismiss();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachLopScreen.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pop_up_xoa_lop, null);
        builder.setView(dialogView);
        Button button_dong_y = dialogView.findViewById(R.id.button_dong_y_xoa);
        Button button_huy = dialogView.findViewById(R.id.button_huy_xoa);
        AlertDialog dialog = builder.create();
        button_dong_y.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
                DatabaseReference db_ctlop = FirebaseDatabase.getInstance().getReference("CHITIETLOP");
                db_ctlop.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        int count = 0;
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            if(data.child("maLop").getValue(String.class).equals(selected_lop.getMaLop()))
                            {
                                count += 1;
                                break;
                            }
                        }
                        if(count == 0)
                        {
                            db_lop.child(selected_lop.getMaLop()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Toast.makeText(DanhSachLopScreen.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    mylist.remove(selected_lop);
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    bottom_sheet_dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Toast.makeText(DanhSachLopScreen.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    bottom_sheet_dialog.dismiss();
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(DanhSachLopScreen.this, "Không thể xóa", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            bottom_sheet_dialog.dismiss();
                        }
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

    interface OnClickLopHocListener
    {
        void onClickCallBack(lopitemrcv lop);
    }
}