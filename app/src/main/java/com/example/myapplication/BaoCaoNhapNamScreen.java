package com.example.myapplication;

import static org.apache.poi.ss.formula.ConditionalFormattingEvaluator.getRef;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

    private String ma_bcn_hk1;
    private String ma_bcn_hk2;
    private ArrayList<CTBCNAM> list_ctbcn;
    private ArrayList<BAOCAONAM> list_bcnam;

    private ArrayList<CTBCNAM> list_chitietbaocaonam;
    private boolean check;

    private EditText nam1_edt;
    private EditText nam2_edt;

    private ArrayList<MONHOC> list_mon_db;
    SessionManager sessionManager;
    String user_account;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bao_cao_nhap_nam_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(getApplicationContext());
        user_account = sessionManager.getUsername();

        check = true;
        list_ctbcn = new ArrayList<>();
        list_bcnam = new ArrayList<>();
        list_mon_db = new ArrayList<>();
        nam1_edt = findViewById(R.id.otp_input_1);
        nam2_edt = findViewById(R.id.otp_input_2);


        ImageButton quay_lai_trang_chu = findViewById(R.id.tao_de_thi_icon_back);
        quay_lai_trang_chu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent quay_lai_trang_chu_intent = new Intent(BaoCaoNhapNamScreen.this, MainScreenNew.class);
                startActivity(quay_lai_trang_chu_intent);
                finish();
            }
        });

        ImageButton xembaocao = findViewById(R.id.xem_bao_cao_button);
        xembaocao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String nam1_text = nam1_edt.getText().toString();
                String nam2_text = nam2_edt.getText().toString();
                /*if (!"2023".equals(nam1_text) || !"2024".equals(nam2_text)) {
                    Toast.makeText(BaoCaoNhapNamScreen.this, "Không có dữ liệu cho năm học này", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                namhoc = nam1_text + "/" + nam2_text;
                if (!TextUtils.isEmpty(nam1_text) && !TextUtils.isEmpty(nam2_text))
                {
                    try
                    {
                        int nam1_value = Integer.parseInt(nam1_text);
                        int nam2_value = Integer.parseInt(nam2_text);

                        if (nam2_value == nam1_value + 1)
                        {
                            Log.e("Check condition", "OK TRUE");
                            GetUserLogin(new OnCompleteData()
                            {
                                @Override
                                public void onCompleteData(ArrayList<CTBCNAM> list_ctbcn, ArrayList<BAOCAONAM> list_bcn)
                                {
                                    if (check)
                                    {
                                        DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
                                        DatabaseReference db_bcn = FirebaseDatabase.getInstance().getReference("BAOCAONAM");
                                        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
                                        db_bcn.addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                for (int i = 0; i < list_bcn.size(); i++)
                                                {
                                                    boolean check = false;
                                                    for (DataSnapshot data : snapshot.getChildren())
                                                    {
                                                        if ((list_bcn.get(i).getMaHKNH().equals(data.child("maBCNam").getValue(String.class)) &&
                                                                (list_bcn.get(i).getMaGV().equals(data.child("maGV").getValue(String.class))))) {
                                                            check = true;
                                                            Map<String, Object> map = new HashMap<>();
                                                            map.put("maBCNam", list_bcn.get(i).getMaBCNam());
                                                            map.put("maHKNH", list_bcn.get(i).getMaHKNH());
                                                            map.put(" maGV", list_bcn.get(i).getMaGV());
                                                            map.put("tongSoDeThi", list_bcn.get(i).getTongSoDeThi());
                                                            map.put("tongSoBaiCham", list_bcn.get(i).getTongSoBaiCham());
                                                            db_bcn.child(data.getKey()).updateChildren(map);
                                                        }

                                                    }
                                                    if (check == false)
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
                                                for (int i = 0; i < list_ctbcn.size(); i++)
                                                {
                                                    boolean check = false;
                                                    for (DataSnapshot data : snapshot.getChildren())
                                                    {
                                                        if ((list_ctbcn.get(i).getMaBCNam().equals(data.child("maBCNam").getValue(String.class))) &&
                                                                (list_ctbcn.get(i).getMaMH().equals(data.child("maMH").getValue(String.class))))
                                                        {
                                                            check = true;
                                                            Map<String, Object> map = new HashMap<>();
                                                            map.put("maBCNam", list_ctbcn.get(i).getMaBCNam());
                                                            map.put("maMH", list_ctbcn.get(i).getMaMH());
                                                            map.put("soLuongDeThi", list_ctbcn.get(i).getSoLuongDeThi());
                                                            map.put("tileDeThi", list_ctbcn.get(i).getTileDeThi());
                                                            map.put("soLuongBaiCham", list_ctbcn.get(i).getSoLuongBaiCham());
                                                            map.put("tileBaiCham", list_ctbcn.get(i).getTileBaiCham());
                                                            db_ctbcn.child(data.getKey()).updateChildren(map);
                                                        }
                                                    }
                                                    if (check == false)
                                                    {
                                                        String key = db_ctbcn.push().getKey();
                                                        db_ctbcn.child(key).setValue(list_ctbcn.get(i));
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        check = false;
                                        int tongsodethi = 0;
                                        int tongsobaicham = 0;
                                        for (int i = 0; i < list_bcn.size(); i++)
                                        {
                                            tongsodethi += list_bcn.get(i).getTongSoDeThi();
                                            tongsobaicham += list_bcn.get(i).getTongSoBaiCham();
                                        }
                                        ArrayList<baocaomonhocitem> list_monhoc = new ArrayList<>();
                                        for (int i = 0; i < list_ctbcn.size(); i++)
                                        {
                                            boolean check_monhoc = false;
                                            for (int j = 0; j < list_monhoc.size(); j++)
                                            {
                                                if (list_monhoc.get(j).getTenmon().equals(list_ctbcn.get(i).getMaMH()))
                                                {
                                                    list_monhoc.get(j).setSoluongdethi(list_monhoc.get(j).getSoluongdethi() + list_ctbcn.get(i).getSoLuongDeThi());
                                                    list_monhoc.get(j).setSoluongbaicham(list_monhoc.get(j).getSoluongbaicham() + list_ctbcn.get(i).getSoLuongBaiCham());
                                                    check_monhoc = true;
                                                }
                                            }
                                            if (check_monhoc == false) {
                                                baocaomonhocitem monhoc = new baocaomonhocitem(list_ctbcn.get(i).getMaMH(),
                                                        list_ctbcn.get(i).getSoLuongDeThi(),
                                                        list_ctbcn.get(i).getSoLuongBaiCham(), 0, 0);
                                                list_monhoc.add(monhoc);
                                            }
                                        }


                                        for (int i = 0; i < list_monhoc.size(); i++)
                                        {
                                            for (int j = 0; j<list_mon_db.size();j++)
                                            {
                                                if (list_monhoc.get(i).getTenmon().equals(list_mon_db.get(j).getMaMH()))
                                                {
                                                    list_monhoc.get(i).setTenmon(list_mon_db.get(j).getTenMH());
                                                    break;
                                                }
                                            }
                                        }
                                        for (int i = 0; i < list_monhoc.size(); i++)
                                        {
                                            int tiledethi = list_monhoc.get(i).getSoluongdethi() * 100 / tongsodethi;
                                            int tilebaicham = list_monhoc.get(i).getSoluongbaicham() * 100 / tongsobaicham;
                                            list_monhoc.get(i).setTiledethi(tiledethi);
                                            list_monhoc.get(i).setTilebaicham(tilebaicham);
                                        }
                                        Intent intent = new Intent(BaoCaoNhapNamScreen.this, BaoCaoNamScreen.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("tongsodethi", tongsodethi);
                                        bundle.putInt("tongsobaicham", tongsobaicham);
                                        bundle.putString("nam1", nam1_text);
                                        bundle.putString("nam2", nam2_text);
                                        bundle.putParcelableArrayList("data_list", list_monhoc);
                                        intent.putExtra("data", bundle);
                                        Log.e("Check Activity", "READY TO START");
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(BaoCaoNhapNamScreen.this,"Năm học không hợp lệ",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        // Handle the case where input is not a valid integer
                        // Show an error message or handle accordingly
                    }
                }
                else
                {
                    Toast.makeText(BaoCaoNhapNamScreen.this, "Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void GetUserLogin(OnCompleteData callback)
    {
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_baocaonam = FirebaseDatabase.getInstance().getReference("BAOCAONAM");
        DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");


        String[] namSplit = namhoc.split("/");
        String nam1 = namSplit[0];
        String nam2 = namSplit[1];
        db_hknh.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(nam1!= null && !nam1.isEmpty() && nam2 != null && !nam2.isEmpty())
                {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (String.valueOf(data.child("nam1").getValue(Integer.class)).equals(nam1) &&
                                String.valueOf(data.child("nam2").getValue(Integer.class)).equals(nam2)) {
                            if (String.valueOf(data.child("hocKy").getValue(Integer.class)).equals("1")) {
                                mahk1 = data.getKey();
                            } else {
                                mahk2 = data.getKey();
                            }
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
                    if(data.child("maGV").getValue(String.class).equals(user_account))
                    {
                        if(data.child("maHKNH").getValue(String.class).equals(mahk1))
                        {
                            ma_bcn_hk1 = data.getKey();
                            BAOCAONAM bcn = new BAOCAONAM(ma_bcn_hk1,mahk1,user_account,0,0);
                            list_bcnam.add(bcn);
                            check_hk1 = 1;
                        }
                        else if(data.child("maHKNH").getValue(String.class).equals(mahk2))
                        {
                            ma_bcn_hk2 = data.getKey();
                            BAOCAONAM bcn = new BAOCAONAM(ma_bcn_hk2,mahk2,user_account,0,0);
                            list_bcnam.add(bcn);
                            check_hk2 = 1;
                        }
                    }
                }
                if(check_hk1 == 0)
                {
                    ma_bcn_hk1 = db_baocaonam.push().getKey();
                    BAOCAONAM bcn = new BAOCAONAM(ma_bcn_hk1,mahk1,user_account,0,0);
                    list_bcnam.add(bcn);
                }
                if(check_hk2 == 0)
                {
                    ma_bcn_hk2 = db_baocaonam.push().getKey();
                    BAOCAONAM bcn = new BAOCAONAM(ma_bcn_hk2,mahk2,user_account,0,0);
                    list_bcnam.add(bcn);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
        db_monhoc.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    //MONHOC mh = new MONHOC(data.getKey(),data.child("tenMH").getValue(String.class),"");
                    MONHOC mh = data.getValue(MONHOC.class);
                    list_mon_db.add(mh);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

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
                    if(data.child("maGV").getValue(String.class).equals(user_account))
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
                    if(magvcham.equals(user_account))
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