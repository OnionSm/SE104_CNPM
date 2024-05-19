package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fragment.BaoCaoSoDo1Fragment;

public class BaoCaoNhapNamScreen extends AppCompatActivity
{

    private String namhoc;
    private String mahk1;
    private String mahk2;
    private String key_user;

    private String ma_bcn_hk1;
    private String ma_bcn_hk2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bao_cao_nhap_nam_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        namhoc = "2023/2024";
        GetUserLogin();


    }

    private void GetUserLogin()
    {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        db_pdn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                key_user = snapshot.child("account").getValue(String.class);
                Log.e("User2", key_user);
                GetListMonHoc2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }
    private void GetMaBCNAM(DatabaseReference db_baocaonam , String mahknh, OnMaBaoCaoNamCallBack callback)
    {
        db_baocaonam.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                boolean found = false;
                Log.e("Mã cần xét",mahknh);
                Log.e("Số phần tử con", String.valueOf(snapshot.getChildrenCount()));
                Log.e("Check", snapshot.getKey());
                for (DataSnapshot data : snapshot.getChildren())
                {
                    Log.e("Xét", data.child("maHKNH").getValue(String.class) + "  " + data.child("maGV").getValue(String.class));
                    if (data.child("maGV").getValue(String.class).equals(key_user) &&
                            data.child("maHKNH").getValue(String.class).equals(mahknh))
                    {
                        callback.onMaBaoCaoNamCallBack(data.getKey(), data.child("tongSoDeThi").getValue(Integer.class),
                                data.child("tongSoBaiCham").getValue(Integer.class));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    String key = db_baocaonam.push().getKey();
                    BAOCAONAM bcn = new BAOCAONAM(key, mahknh, key_user, 0, 0);
                    db_baocaonam.child(key).setValue(bcn);
                    callback.onMaBaoCaoNamCallBack(key, 0, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private void GetMaCTBCN(DatabaseReference db_ctbcn , String mabcn, String mamon, OnCTBCNAMCallBack callback)
    {
        db_ctbcn.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("maBCNam").getValue(String.class).equals(mabcn) && data.child("maMH").getValue(String.class).equals(mamon)) {
                        callback.onCTBCNAMCallBack(data.getKey(), data.child("soLuongDeThi").getValue(Integer.class),
                                data.child("tileDeThi").getValue(Integer.class),
                                data.child("soLuongBaiCham").getValue(Integer.class),
                                data.child("tileBaiCham").getValue(Integer.class));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    String key = db_ctbcn.push().getKey();
                    CTBCNAM ctbcn = new CTBCNAM(mabcn, mamon, 0, 0, 0, 0);
                    db_ctbcn.child(key).setValue(ctbcn);
                    callback.onCTBCNAMCallBack(key, 0, 0, 0, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }
    interface OnMaBaoCaoNamCallBack
    {
        void onMaBaoCaoNamCallBack(String mabaocaonam, int tongsodethi , int tongsobaicham);
    }
    interface OnCTBCNAMCallBack
    {
        void onCTBCNAMCallBack(String key_ctbcnam, int soluongdethi, int tiledethi , int soluongbaicham , int tilebaicham);
    }

    private void GetListMonHoc2()
    {
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_baocaonam = FirebaseDatabase.getInstance().getReference("BAOCAONAM");
        DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
        String[] namSplit = namhoc.split("/");
        String nam1 = namSplit[0];
        String nam2 = namSplit[1];
        db_hknh.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    String mahk = data.getKey();
                    if(String.valueOf(data.child("nam1").getValue(Integer.class)).equals(nam1) &&
                            String.valueOf(data.child("nam2").getValue(Integer.class)).equals(nam2))
                    {
                        db_dethi.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for(DataSnapshot data : snapshot.getChildren())
                                {
                                    String mamonhoc_dethi = data.child("maMH").getValue(String.class);
                                    if(data.child("maGV").getValue(String.class).equals(key_user) &&
                                            data.child("maHKNH").getValue(String.class).equals(mahk))
                                    {
                                        db_baocaonam.addValueEventListener(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error)
                                            {

                                            }
                                        });
                                        GetMaBCNAM(db_baocaonam, mahk, new OnMaBaoCaoNamCallBack()
                                        {
                                            int _tongsodethi;
                                            @Override
                                            public void onMaBaoCaoNamCallBack(String mabaocaonam, int tongsodethi, int tongsobaicham)
                                            {

                                                GetMaCTBCN(db_ctbcn , mabaocaonam, mamonhoc_dethi, new OnCTBCNAMCallBack()
                                                {
                                                    @Override
                                                    public void onCTBCNAMCallBack(String key_ctbcnam, int soluongdethi, int tiledethi, int soluongbaicham, int tilebaicham)
                                                    {
                                                        _tongsodethi = _tongsodethi + 1;
                                                        int _soluongdethi = soluongdethi + 1;
                                                        int _tiledethi = _soluongdethi * 100 / _tongsodethi;
                                                        db_ctbcn.child(key_ctbcnam).child("soLuongDeThi").setValue(_soluongdethi);
                                                        db_ctbcn.child(key_ctbcnam).child("tileDeThi").setValue(_tiledethi);
                                                        db_baocaonam.child(mabaocaonam).child("tongSoDeThi").setValue(_tongsodethi);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error)
                            {

                            }
                        });

                        db_lop.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for(DataSnapshot data : snapshot.getChildren())
                                {
                                    String mamonlop = data.child("maMH").getValue(String.class);
                                    String magvcham = data.child("maGVCham").getValue(String.class);
                                    int siso = data.child("siSo").getValue(Integer.class);
                                    String mahknh_lop = data.child("maHKNH").getValue(String.class);
                                    if(magvcham.equals(key_user) && mahknh_lop.equals(mahk))
                                    {
                                        DatabaseReference db_baocaonam = FirebaseDatabase.getInstance().getReference("BAOCAONAM");
                                        DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
                                        GetMaBCNAM(db_baocaonam, mahknh_lop, new OnMaBaoCaoNamCallBack()
                                        {
                                            int _tongsobaicham;
                                            @Override
                                            public void onMaBaoCaoNamCallBack(String mabaocaonam, int tongsodethi, int tongsobaicham)
                                            {
                                                GetMaCTBCN(db_ctbcn ,mabaocaonam, mamonlop, new OnCTBCNAMCallBack()
                                                {
                                                    @Override
                                                    public void onCTBCNAMCallBack(String key_ctbcnam, int soluongdethi, int tiledethi, int soluongbaicham, int tilebaicham)
                                                    {

                                                        _tongsobaicham = _tongsobaicham + siso;
                                                        int _soluongbaicham = soluongbaicham + siso;
                                                        int _tilebaicham = _soluongbaicham * 100 / _tongsobaicham;
                                                        db_ctbcn.child(key_ctbcnam).child("soLuongBaiCham").setValue(_soluongbaicham);
                                                        db_ctbcn.child(key_ctbcnam).child("tileBaiCham").setValue(_tilebaicham);
                                                        db_baocaonam.child(mabaocaonam).child("tongSoBaiCham").setValue(_tongsobaicham);
                                                    }
                                                });
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
}