package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChiTietDeThiScreen extends AppCompatActivity
{
    RecyclerView chi_tiet_de_thi_recycleview;
    ChiTietDeThiAdapter adapter;
    ImageButton back_button;
    ArrayList<cauhoiitem> mylist;
    String madt;
    interface GetListCauHoiCallBack
    {
        void getListCauHoiCallBack(ArrayList<cauhoiitem> mylist);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_de_thi_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mylist = new ArrayList<>();
        back_button= findViewById(R.id.chi_tiet_de_thi_icon_back);
        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        setupOnBackPressed();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data_madethi");
        madt = bundle.getString("madethi");
        if (madt != null && !madt.isEmpty())
        {
            GetDataFromFirebase(new GetListCauHoiCallBack()
            {
                @Override
                public void getListCauHoiCallBack(ArrayList<cauhoiitem> mylist)
                {
                    Log.e("Kết quả", "Đã call back my list");
                    Log.e("kết quả", String.valueOf(mylist.size()));
                    GetListCauHoi();
                }
            });
        }
    }

    private void GetListCauHoi()
    {
        chi_tiet_de_thi_recycleview = findViewById(R.id.chi_tiet_de_thi_recycle_view);
        GridLayoutManager grid_layout_manager = new GridLayoutManager(ChiTietDeThiScreen.this, 1);
        chi_tiet_de_thi_recycleview.setLayoutManager(grid_layout_manager);
        adapter = new ChiTietDeThiAdapter(mylist);
        chi_tiet_de_thi_recycleview.setAdapter(adapter);
        DividerItemDecoration item_decoration = new DividerItemDecoration(ChiTietDeThiScreen.this, DividerItemDecoration.VERTICAL);
        chi_tiet_de_thi_recycleview.addItemDecoration(item_decoration);

    }

    private void GetDataFromFirebase(GetListCauHoiCallBack callback)
    {
        DatabaseReference db_dethi_cauhoi = FirebaseDatabase.getInstance().getReference("DETHICAUHOI");
        DatabaseReference db_cauhoi = FirebaseDatabase.getInstance().getReference("CAUHOI");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        DatabaseReference db_dokho = FirebaseDatabase.getInstance().getReference("DOKHO");

        db_dethi_cauhoi.addListenerForSingleValueEvent(new ValueEventListener()
        {
            int stt = 1;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                int max = (int)snapshot.getChildrenCount();
                int count = 0;
                for(DataSnapshot data : snapshot.getChildren())
                {
                    if(data.child("maDT").getValue(String.class).equals(madt))
                    {
                        String mach = data.child("maCH").getValue(String.class);
                        db_cauhoi.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for(DataSnapshot data : snapshot.getChildren())
                                {
                                    if(data.getKey().equals(mach))
                                    {
                                        String madk = data.child("maDoKho").getValue(String.class);
                                        String mamh = data.child("maMH").getValue(String.class);
                                        String ngaytao = data.child("ngaytao").getValue(String.class);
                                        String noidung = data.child("noiDung").getValue(String.class);
                                        db_dokho.addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                for(DataSnapshot data : snapshot.getChildren())
                                                {
                                                    if(madk.equals(data.getKey()))
                                                    {
                                                        String tendokho = data.child("TenDK").getValue(String.class);
                                                        db_monhoc.addListenerForSingleValueEvent(new ValueEventListener()
                                                        {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                                            {
                                                                for(DataSnapshot data : snapshot.getChildren())
                                                                {
                                                                    if(data.getKey().equals(mamh))
                                                                    {
                                                                        String tenmon = data.child("tenMH").getValue(String.class);
                                                                        cauhoiitem cauhoi = new cauhoiitem(String.valueOf(stt),mach,tenmon,noidung,tendokho,ngaytao);
                                                                        mylist.add(cauhoi);
                                                                        stt++;
                                                                        // Update the serial numbers
                                                                        for (int i = 0; i < mylist.size(); i++)
                                                                        {
                                                                            cauhoiitem item = mylist.get(i);
                                                                            item.setStt(String.valueOf(i + 1));
                                                                        }
                                                                        adapter.notifyDataSetChanged(); // Notify the adapter of dataset changes
                                                                        GetListCauHoi();

                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error)
                                                            {

                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    count++;
                    if(count == max)
                    {
                        callback.getListCauHoiCallBack(mylist);
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

}