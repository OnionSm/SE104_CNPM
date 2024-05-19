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
    private ArrayList<CTBCNAM> list_ctbcn;
    private ArrayList<BAOCAONAM> list_bcnam;


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
        list_ctbcn = new ArrayList<>();
        list_bcnam = new ArrayList<>();
        namhoc = "2023/2024";
        GetUserLogin(new OnCompleteData()
        {
            @Override
            public void onCompleteData(ArrayList<CTBCNAM> list_ctbcn)
            {
                for(int i =0 ;i< list_ctbcn.size();i++)
                {
                    Log.e("CTBCN",String.valueOf(i)+ " " + list_ctbcn.get(i).getMaBCNam() + " "
                            + list_ctbcn.get(i).getMaMH()+ " "
                            + String.valueOf(list_ctbcn.get(i).getSoLuongDeThi()) + " "
                            + String.valueOf(list_ctbcn.get(i).getSoLuongBaiCham()));
                }
                /*DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
                for(int i = 0; i<list_ctbcn.size();i++)
                {
                    String key = db_ctbcn.push().getKey();
                    db_ctbcn.child(key).setValue(list_ctbcn.get(i));
                }*/
            }
        });
    }

    private void GetUserLogin(OnCompleteData callback)
    {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        db_pdn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                key_user = snapshot.child("account").getValue(String.class);
                Log.e("User2", key_user);
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
                                                int pos_bc = GetMaBCNAM(db_baocaonam,mahk);
                                                String mabcnam = list_bcnam.get(pos_bc).getMaBCNam();
                                                int pos_ctbcn = GetMaCTBCN(db_ctbcn,mabcnam,mamonhoc_dethi);
                                                int tongsodethi = list_bcnam.get(pos_bc).getTongSoDeThi() + 1;
                                                int soluongdethi = list_ctbcn.get(pos_ctbcn).getSoLuongDeThi() + 1;
                                                int tiledethi = soluongdethi * 100 / tongsodethi;
                                                list_bcnam.get(pos_bc).setTongSoDeThi(tongsodethi);
                                                list_ctbcn.get(pos_ctbcn).setSoLuongDeThi(soluongdethi);
                                                list_ctbcn.get(pos_ctbcn).setTileDeThi(tiledethi);
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
                                        int size = (int)snapshot.getChildrenCount();
                                        int count = 0;
                                        for(DataSnapshot data : snapshot.getChildren())
                                        {
                                            String mamonlop = data.child("maMH").getValue(String.class);
                                            String magvcham = data.child("maGVCham").getValue(String.class);
                                            int siso = data.child("siSo").getValue(Integer.class);
                                            String mahknh_lop = data.child("maHKNH").getValue(String.class);
                                            if(magvcham.equals(key_user) && mahknh_lop.equals(mahk))
                                            {
                                                int pos_bc = GetMaBCNAM(db_baocaonam,mahk);
                                                String mabcnam = list_bcnam.get(pos_bc).getMaBCNam();
                                                int pos_ctbcn = GetMaCTBCN(db_ctbcn,mabcnam,mamonlop);
                                                int tongsobaicham = list_bcnam.get(pos_bc).getTongSoBaiCham() + siso;
                                                int soluongbaicham = list_ctbcn.get(pos_ctbcn).getSoLuongBaiCham() + siso;
                                                int tilebaicham = soluongbaicham * 100 / tongsobaicham;
                                                list_bcnam.get(pos_bc).setTongSoBaiCham(tongsobaicham);
                                                list_ctbcn.get(pos_ctbcn).setSoLuongBaiCham(soluongbaicham);
                                                list_ctbcn.get(pos_ctbcn).setTileBaiCham(tilebaicham);
                                            }
                                            count ++;
                                            if(count == size)
                                            {
                                                callback.onCompleteData(list_ctbcn);
                                                return;
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
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private int GetMaBCNAM(DatabaseReference db_baocaonam , String mahknh)
    {

        for (int i = 0; i< list_bcnam.size();i++)
        {
            if(list_bcnam.get(i).getMaGV().equals(key_user) && list_bcnam.get(i).getMaHKNH().equals(mahknh))
            {
                return i;
            }
        }
        String key = db_baocaonam.push().getKey().toString();
        BAOCAONAM bc = new BAOCAONAM(key,mahknh,key_user,0,0);
        list_bcnam.add(bc);
        return list_bcnam.size() - 1;
    }

    private int GetMaCTBCN(DatabaseReference db_ctbcn , String mabcn, String mamon)
    {
        for(int i = 0;i<list_ctbcn.size();i++)
        {
            if(list_ctbcn.get(i).getMaBCNam().equals(mabcn) && list_ctbcn.get(i).getMaMH().equals(mamon))
            {
                return i;
            }
        }
        CTBCNAM ct = new CTBCNAM(mabcn,mamon,0,0,0,0);
        list_ctbcn.add(ct);
        return list_ctbcn.size() - 1;
    }
    interface OnCompleteData
    {
        void onCompleteData(ArrayList<CTBCNAM> list_ctbcn);
    }

}