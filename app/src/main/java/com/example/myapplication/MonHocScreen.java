package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MonHocScreen extends AppCompatActivity
{
    ArrayList<MONHOC> mylist;
    DsMonHocAdapter adapter;

    RecyclerView monhoc_rcv;

    ImageButton quay_lai_cau_hoi_screen;
    private BottomSheetDialog bottom_sheet_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mon_hoc_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mylist = new ArrayList<>();
        quay_lai_cau_hoi_screen = findViewById(R.id.ds_danh_sach_mon_hoc_icon_back);
        quay_lai_cau_hoi_screen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        setupBottomSheetDialog();
        GetListCauHoi();
        GetDataMonHocFromFirebase();
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
    private void GetDataMonHocFromFirebase()
    {
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        db_monhoc.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                MONHOC monhoc = snapshot.getValue(MONHOC.class);
                mylist.add(monhoc);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                // Lấy đối tượng môn học vừa được thay đổi
                MONHOC updatedMonhoc = snapshot.getValue(MONHOC.class);
                String key = snapshot.getKey();

                // Tìm và cập nhật môn học trong danh sách
                for (int i = 0; i < mylist.size(); i++)
                {
                    if (mylist.get(i).getMaMH().equals(key))
                    {
                        mylist.set(i, updatedMonhoc);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                // Lấy đối tượng môn học vừa bị xóa
                MONHOC removedMonhoc = snapshot.getValue(MONHOC.class);
                String key = snapshot.getKey();

                // Tìm và xóa môn học khỏi danh sách
                for (int i = 0; i < mylist.size(); i++) {
                    if (mylist.get(i).getMaMH().equals(key))
                    {
                        mylist.remove(i);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                // Tùy vào cách bạn sắp xếp danh sách, bạn có thể cập nhật danh sách khi thứ tự của môn học thay đổi
                MONHOC movedMonhoc = snapshot.getValue(MONHOC.class);
                String key = snapshot.getKey();

                // Xóa môn học cũ
                for (int i = 0; i < mylist.size(); i++)
                {
                    if (mylist.get(i).getMaMH().equals(key))
                    {
                        mylist.remove(i);
                        break;
                    }
                }

                // Thêm môn học mới vào vị trí thích hợp
                if (previousChildName == null)
                {
                    mylist.add(0, movedMonhoc); // Nếu previousChildName là null, thêm vào đầu danh sách
                }
                else
                {
                    for (int i = 0; i < mylist.size(); i++)
                    {
                        if (mylist.get(i).getMaMH().equals(previousChildName))
                        {
                            mylist.add(i + 1, movedMonhoc); // Thêm vào sau phần tử trước đó
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(MonHocScreen.this, "Không tìm thấy dữ liệu môn học", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void GetListCauHoi()
    {
        monhoc_rcv = findViewById(R.id.danh_sach_mon_hoc_recycle_view);
        // Set GridLayoutManager with 1 column
        GridLayoutManager grid_layout_manager = new GridLayoutManager(MonHocScreen.this, 1);
        monhoc_rcv.setLayoutManager(grid_layout_manager);
        adapter = new DsMonHocAdapter(mylist, new OnClickMonHocCallback()
        {
            @Override
            public void onClickMonHocCallback(MONHOC monhoc)
            {
                bottom_sheet_dialog.show();
            }
        });
        monhoc_rcv.setAdapter(adapter);
        DividerItemDecoration item_decoration = new DividerItemDecoration(MonHocScreen.this, DividerItemDecoration.VERTICAL);
        monhoc_rcv.addItemDecoration(item_decoration);

    }
    private void setupBottomSheetDialog()
    {
        bottom_sheet_dialog = new BottomSheetDialog(MonHocScreen.this);
        View bottom_sheet_view = getLayoutInflater().inflate(R.layout.bottom_sheet_mon_hoc, null);
        bottom_sheet_dialog.setContentView(bottom_sheet_view);
        ImageButton button_sua_mon_hoc = bottom_sheet_view.findViewById(R.id.button_sua_mon_hoc);
        ImageButton button_xoa_mon_hoc = bottom_sheet_view.findViewById(R.id.button_xoa_mon_hoc);
        button_sua_mon_hoc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //updateCauHoiToDatabase();
            }
        });
        button_xoa_mon_hoc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

            }
        });
    }
    interface OnClickMonHocCallback
    {
        void onClickMonHocCallback(MONHOC monhoc);
    }
}