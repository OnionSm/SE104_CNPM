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
import java.util.HashMap;
import java.util.Map;

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

    private ArrayList<CTBCNAM> list_chitietbaocaonam;
    private boolean check;


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
        check = true;
        list_ctbcn = new ArrayList<>();
        list_bcnam = new ArrayList<>();
        namhoc = "2023/2024";
        GetUserLogin(new OnCompleteData()
        {
            @Override
            public void onCompleteData(ArrayList<CTBCNAM> list_ctbcn , ArrayList<BAOCAONAM> list_bcn)
            {
                if(check)
                {
                    for(int i =0 ;i< list_ctbcn.size();i++)
                    {
                        Log.e("CTBCN",String.valueOf(i)+ " " + list_ctbcn.get(i).getMaBCNam() + " "
                                + list_ctbcn.get(i).getMaMH()+ " "
                                + String.valueOf(list_ctbcn.get(i).getSoLuongDeThi()) + " "
                                + String.valueOf(list_ctbcn.get(i).getSoLuongBaiCham()));
                    }
                    for(int i =0 ;i< list_bcn.size();i++)
                    {
                        Log.e("BCN",String.valueOf(i)+ " " + list_bcn.get(i).getMaBCNam() + " " +
                                list_bcn.get(i).getMaHKNH() + "" + list_bcn.get(i).getMaGV() + " " +
                                list_bcn.get(i).getTongSoDeThi() + " " + list_bcn.get(i).getTongSoBaiCham());
                    }
                    DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
                    DatabaseReference db_bcn = FirebaseDatabase.getInstance().getReference("BAOCAONAM");
                    db_bcn.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            for(int i = 0 ; i< list_bcn.size(); i++)
                            {
                                boolean check = false;
                                for(DataSnapshot data : snapshot.getChildren())
                                {
                                    if((list_bcn.get(i).getMaHKNH().equals(data.child("maBCNam").getValue(String.class)) &&
                                            (list_bcn.get(i).getMaGV().equals(data.child("maGV").getValue(String.class)))))
                                    {
                                        check = true;
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("maBCNam",list_bcn.get(i).getMaBCNam());
                                        map.put("maHKNH", list_bcn.get(i).getMaHKNH());
                                        map.put(" maGV",list_bcn.get(i).getMaGV());
                                        map.put("tongSoDeThi",list_bcn.get(i).getTongSoDeThi());
                                        map.put("tongSoBaiCham",list_bcn.get(i).getTongSoBaiCham());
                                        db_bcn.child(data.getKey()).updateChildren(map);
                                    }

                                }
                                if(check==false)
                                {
                                    db_bcn.child(list_bcn.get(i).getMaBCNam()).setValue(list_bcn.get(i));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });

                    db_ctbcn.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            for(int i = 0; i<list_ctbcn.size();i++)
                            {
                                boolean check = false;
                                for(DataSnapshot data : snapshot.getChildren())
                                {
                                    if((list_ctbcn.get(i).getMaBCNam().equals(data.child("maBCNam").getValue(String.class))) &&
                                            (list_ctbcn.get(i).getMaMH().equals(data.child("maMH").getValue(String.class))))
                                    {
                                        check = true;
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("maBCNam", list_ctbcn.get(i).getMaBCNam());
                                        map.put("maMH",list_ctbcn.get(i).getMaMH());
                                        map.put("soLuongDeThi",list_ctbcn.get(i).getSoLuongDeThi());
                                        map.put("tileDeThi",list_ctbcn.get(i).getTileDeThi());
                                        map.put("soLuongBaiCham", list_ctbcn.get(i).getSoLuongBaiCham());
                                        map.put("tileBaiCham",list_ctbcn.get(i).getTileBaiCham());
                                        db_ctbcn.child(data.getKey()).updateChildren(map);
                                    }
                                }
                                if(check==false)
                                {
                                    String key = db_ctbcn.push().getKey();
                                    db_ctbcn.child(key).setValue(list_ctbcn.get(i));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });

                   /* for(int i = 0 ; i< list_bcn.size(); i++)
                    {
                        db_bcn.child(list_bcn.get(i).getMaBCNam()).setValue(list_bcn.get(i));
                    }
                    for(int i = 0 ; i < list_ctbcn.size(); i++)
                    {
                        String key = db_ctbcn.push().getKey();
                        db_ctbcn.child(key).setValue(list_ctbcn.get(i));
                    }*/
                    check = false;
                    /*DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
                    for(int i = 0; i<list_ctbcn.size();i++)
                    {
                        String key = db_ctbcn.push().getKey();
                        db_ctbcn.child(key).setValue(list_ctbcn.get(i));
                    }*/
                }
            }
        });
    }

    private void GetUserLogin(OnCompleteData callback)
    {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_baocaonam = FirebaseDatabase.getInstance().getReference("BAOCAONAM");
        DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
        db_pdn.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                key_user = snapshot.child("account").getValue(String.class);
                Log.e("User2", key_user);

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
                            if(String.valueOf(data.child("nam1").getValue(Integer.class)).equals(nam1) &&
                                    String.valueOf(data.child("nam2").getValue(Integer.class)).equals(nam2))
                            {
                                if(String.valueOf(data.child("hocKy").getValue(Integer.class)).equals("1"))
                                {
                                    mahk1 = data.getKey();
                                }
                                else
                                {
                                    mahk2 = data.getKey();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
                db_baocaonam.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        int check_hk1 = 0;
                        int check_hk2 = 0;
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            if(data.child("maGV").getValue(String.class).equals(key_user))
                            {
                                if(data.child("maHKNH").getValue(String.class).equals(mahk1))
                                {
                                    ma_bcn_hk1 = data.getKey();
                                    BAOCAONAM bcn = data.getValue(BAOCAONAM.class);
                                    list_bcnam.add(bcn);
                                    check_hk1 = 1;
                                }
                                else if(data.child("maHKNH").getValue(String.class).equals(mahk2))
                                {
                                    ma_bcn_hk2 = data.getKey();
                                    BAOCAONAM bcn  = data.getValue(BAOCAONAM.class);
                                    list_bcnam.add(bcn);
                                    check_hk2 = 1;
                                }
                            }
                        }
                        if(check_hk1 == 0)
                        {
                            ma_bcn_hk1 = db_baocaonam.push().getKey();
                            BAOCAONAM bcn = new BAOCAONAM(ma_bcn_hk1,mahk1,key_user,0,0);
                            list_bcnam.add(bcn);
                        }
                        if(check_hk2 == 0)
                        {
                            ma_bcn_hk2 = db_baocaonam.push().getKey();
                            BAOCAONAM bcn = new BAOCAONAM(ma_bcn_hk2,mahk2,key_user,0,0);
                            list_bcnam.add(bcn);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                db_dethi.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            String mamonhoc_dethi = data.child("maMH").getValue(String.class);

                            //Xét xem đề thi đó có thuộc giáo viên đang đăng nhập hay không
                            if(data.child("maGV").getValue(String.class).equals(key_user))
                            {
                                //Xét các đề thuộc học kì 1
                                if(data.child("maHKNH").getValue(String.class).equals(mahk1))
                                {
                                    int pos_bcn = GetMaBCNAM(ma_bcn_hk1);
                                    int pos_ctbcn = GetMaCTBCN(ma_bcn_hk1,mamonhoc_dethi);
                                    int tongsodethi = list_bcnam.get(pos_bcn).getTongSoDeThi() + 1;
                                    int soluongdethi = list_ctbcn.get(pos_ctbcn).getSoLuongDeThi()+ 1;
                                    int tiledethi = soluongdethi * 100 / tongsodethi;
                                    list_bcnam.get(pos_bcn).setTongSoDeThi(tongsodethi);
                                    list_ctbcn.get(pos_ctbcn).setSoLuongDeThi(soluongdethi);
                                    list_ctbcn.get(pos_ctbcn).setTileDeThi(tiledethi);
                                }
                                //Xét các đề thuộc học kì 2
                                else if(data.child("maHKNH").getValue(String.class).equals(mahk2))
                                {
                                    int pos_bcn = GetMaBCNAM(ma_bcn_hk2);
                                    int pos_ctbcn = GetMaCTBCN(ma_bcn_hk2,mamonhoc_dethi);
                                    int tongsodethi = list_bcnam.get(pos_bcn).getTongSoDeThi() + 1;
                                    int soluongdethi = list_ctbcn.get(pos_ctbcn).getSoLuongDeThi()+ 1;
                                    int tiledethi = soluongdethi * 100 / tongsodethi;
                                    list_bcnam.get(pos_bcn).setTongSoDeThi(tongsodethi);
                                    list_ctbcn.get(pos_ctbcn).setSoLuongDeThi(soluongdethi);
                                    list_ctbcn.get(pos_ctbcn).setTileDeThi(tiledethi);
                                }
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
                            if(magvcham.equals(key_user))
                            {
                                if(mahknh_lop.equals(mahk1))
                                {
                                    int pos_bcn = GetMaBCNAM(ma_bcn_hk1);
                                    int pos_ctbcn = GetMaCTBCN(ma_bcn_hk1,mamonlop);
                                    int tongsobaicham = list_bcnam.get(pos_bcn).getTongSoBaiCham() + siso;
                                    int soluongbaicham = list_ctbcn.get(pos_ctbcn).getSoLuongBaiCham() + siso;
                                    int tilebaicham = soluongbaicham * 100 / tongsobaicham;
                                    list_bcnam.get(pos_bcn).setTongSoBaiCham(tongsobaicham);
                                    list_ctbcn.get(pos_ctbcn).setSoLuongBaiCham(soluongbaicham);
                                    list_ctbcn.get(pos_ctbcn).setTileBaiCham(tilebaicham);
                                }
                                else if(mahknh_lop.equals(mahk1))
                                {
                                    int pos_bcn = GetMaBCNAM(ma_bcn_hk2);
                                    int pos_ctbcn = GetMaCTBCN(ma_bcn_hk2,mamonlop);
                                    int tongsobaicham = list_bcnam.get(pos_bcn).getTongSoBaiCham() + siso;
                                    int soluongbaicham = list_ctbcn.get(pos_ctbcn).getSoLuongBaiCham() + siso;
                                    int tilebaicham = soluongbaicham * 100 / tongsobaicham;
                                    list_bcnam.get(pos_bcn).setTongSoBaiCham(tongsobaicham);
                                    list_ctbcn.get(pos_ctbcn).setSoLuongBaiCham(soluongbaicham);
                                    list_ctbcn.get(pos_ctbcn).setTileBaiCham(tilebaicham);
                                }

                            }
                            count ++;
                            if(count >= size)
                            {
                                count = 0;
                                callback.onCompleteData(list_ctbcn,list_bcnam);
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
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private int GetMaBCNAM(String ma_bcn_hk1)
    {
        for (int i = 0; i< list_bcnam.size();i++)
        {
            if(list_bcnam.get(i).getMaBCNam().equals(ma_bcn_hk1))
            {
                return i;
            }
        }
        return -1;
    }

    private int GetMaCTBCN(String mabcn, String mamon)
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
        void onCompleteData(ArrayList<CTBCNAM> list_ctbcn , ArrayList<BAOCAONAM> list_bcn);
    }
}