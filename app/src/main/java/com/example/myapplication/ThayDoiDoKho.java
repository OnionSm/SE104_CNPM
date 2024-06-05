package com.example.myapplication;

import android.app.Dialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThayDoiDoKho extends AppCompatActivity
{
    RecyclerView do_kho_recycle_view;
    DsDoKhoAdapter adapter;
    ArrayList<DOKHO> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thay_doi_do_kho);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mylist = new ArrayList<>();

        ImageButton back_button = findViewById(R.id.icon_back);
        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        ImageButton them_do_kho = findViewById(R.id.them_do_kho_button);
        them_do_kho.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateDoKhoToDatabase();
            }
        });
        do_kho_recycle_view = findViewById(R.id.do_kho_recycle_view);

        GetListDoKho();
        GetDataDoKhoFromFirebase();
        setupOnBackPressed();
    }
    private void setupOnBackPressed()
    {
        OnBackPressedCallback callback = new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
    private void GetListDoKho()
    {
        do_kho_recycle_view = findViewById(R.id.do_kho_recycle_view);
        // Set GridLayoutManager with 1 column
        GridLayoutManager grid_layout_manager = new GridLayoutManager(ThayDoiDoKho.this, 1);
        do_kho_recycle_view.setLayoutManager(grid_layout_manager);
        adapter = new DsDoKhoAdapter(mylist);
        do_kho_recycle_view.setAdapter(adapter);
        DividerItemDecoration item_decoration = new DividerItemDecoration(ThayDoiDoKho.this, DividerItemDecoration.VERTICAL);
        do_kho_recycle_view.addItemDecoration(item_decoration);

    }

    private void GetDataDoKhoFromFirebase()
    {
        DatabaseReference db_dokho = FirebaseDatabase.getInstance().getReference("DOKHO");
        db_dokho.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                DOKHO dokho = snapshot.getValue(DOKHO.class);
                mylist.add(dokho);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                DOKHO dokho = snapshot.getValue(DOKHO.class);
                String key = snapshot.getKey();
                for (int i = 0; i < mylist.size(); i++) {
                    if (mylist.get(i).getMaDoKho().equals(key))
                    {
                        mylist.set(i, dokho);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getKey();
                for (int i = 0; i < mylist.size(); i++) {
                    if (mylist.get(i).getMaDoKho().equals(key)) {
                        mylist.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle if necessary
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });
    }

    private void updateDoKhoToDatabase()
    {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.them_do_kho_pop_up);

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
        EditText tendk_edt = dialog.findViewById(R.id.ten_dk_edt);
        ImageButton thayDoiButton = dialog.findViewById(R.id.thay_doi_button);

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        thayDoiButton.setOnClickListener(v ->
        {
            DatabaseReference db_dokho = FirebaseDatabase.getInstance().getReference("DOKHO");
            String tenDK = tendk_edt.getText().toString();
            if(tenDK.isEmpty())
            {
                Toast.makeText(ThayDoiDoKho.this, "Tên độ khó không được trống", Toast.LENGTH_SHORT).show();
            }
            db_dokho.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    boolean check = true;
                    for(DataSnapshot data : snapshot.getChildren())
                    {
                        if(data.child("tenDK").getValue(String.class).equals(tenDK))
                        {
                            check = false;
                        }
                    }
                    if(check)
                    {
                        String key = db_dokho.push().getKey();
                        DOKHO dokho = new DOKHO(key,tenDK);
                        db_dokho.child(key).setValue(dokho).addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                Toast.makeText(ThayDoiDoKho.this, "Thêm độ khó thành công", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(ThayDoiDoKho.this, "Thêm độ khó thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(ThayDoiDoKho.this , "Độ khó này đã tồn tại", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });

        });
    }

}