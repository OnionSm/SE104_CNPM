package fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.BAOCAONAM;
import com.example.myapplication.BaoCaoNamAdapter;
import com.example.myapplication.BaoCaoNamScreen;
import com.example.myapplication.CTBCNAM;
import com.example.myapplication.R;
import com.example.myapplication.TaoDeThiAdapter;
import com.example.myapplication.baocaomonhocitem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaoCaoSoDo1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaoCaoSoDo1Fragment extends Fragment {

    private String namhoc;
    private String mahk1;
    private String mahk2;
    private ArrayList<String> mamon_k1;
    private ArrayList<String> tenmonhoc_k1;
    private ArrayList<Integer> so_luong_de_thi_k1;
    private ArrayList<Integer> so_luong_bai_cham_k1;

    private ArrayList<String> mamon_k2;
    private ArrayList<String> tenmonhoc_k2;
    private ArrayList<Integer> so_luong_de_thi_k2;
    private ArrayList<Integer> so_luong_bai_cham_k2;
    private String key_user;
    private BaoCaoNamScreen activity;

    RecyclerView bao_cao_rcv;
    BaoCaoNamAdapter adapter;
    private ArrayList<baocaomonhocitem> mylist;

    private int tong_so_de_thi;

    private int tong_so_bai_cham;

    private ArrayList<String> list_ma_hk;
    private String ma_bcn_hk1;
    private String ma_bcn_hk2;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public BaoCaoSoDo1Fragment() {
        // Required empty public constructor
    }

    public static BaoCaoSoDo1Fragment newInstance(String param1, String param2) {
        BaoCaoSoDo1Fragment fragment = new BaoCaoSoDo1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        activity = (BaoCaoNamScreen) getActivity();
        namhoc = activity.AccessData();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bao_cao_so_do1, container, false);


        mamon_k1 = new ArrayList<>();
        tenmonhoc_k1 = new ArrayList<>();
        so_luong_de_thi_k1 = new ArrayList<>();
        so_luong_bai_cham_k1 = new ArrayList<>();

        mamon_k2 = new ArrayList<>();
        tenmonhoc_k2 = new ArrayList<>();
        so_luong_de_thi_k2 = new ArrayList<>();
        so_luong_bai_cham_k2 = new ArrayList<>();
        list_ma_hk = new ArrayList<>();

        GetUserLogin();
        //CountTongSoDeThiBaiCham();
        GetListItem();
        GetRecycleViewMonHoc(view);
        return view;
    }

    private void GetMaHK()
    {
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        String[] namSplit = namhoc.split("/");
        String nam1 = namSplit[0];
        String nam2 = namSplit[1];

        db_hknh.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String nam1Value = String.valueOf(data.child("nam1").getValue(Integer.class));
                    String nam2Value = String.valueOf(data.child("nam2").getValue(Integer.class));
                    String hocKyValue = String.valueOf(data.child("hocKy").getValue(Integer.class));

                    if (nam1Value != null && nam2Value != null && nam1Value.equals(nam1) && nam2Value.equals(nam2))
                    {
                        if ("1".equals(hocKyValue))
                        {
                            mahk1 = data.getKey();
                            Log.e("Mã học kỳ 1", mahk1);
                        } else if ("2".equals(hocKyValue))
                        {
                            mahk2 = data.getKey();
                            Log.e("Mã học kỳ 2", mahk2);
                        }
                    }
                }
                // Ensure the following methods are called after MaHK is retrieved
                GetSoLuongDeThi();
                GetSoLuongBaiCham();
                GetTenMonHoc();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private void GetSoLuongDeThi()
    {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        db_dethi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot data : snapshot.getChildren())
                {
                    String mahkdt = data.child("maHKNH").getValue(String.class);
                    String id_mon = data.child("maMH").getValue(String.class);
                    String magv = data.child("maGV").getValue(String.class);

                    if(magv.equals(key_user) && (mahkdt.equals(mahk1)))
                    {
                        int check = FindTenMon(id_mon, 1);
                        tong_so_de_thi += 1;
                        if (check != -1)
                        {
                            so_luong_de_thi_k1.set(check, so_luong_de_thi_k1.get(check) + 1);
                        }
                        else
                        {
                            mamon_k1.add(id_mon);
                            so_luong_de_thi_k1.add(1);
                            so_luong_bai_cham_k1.add(0);
                        }
                        Log.e("Tổng số đề thi", String.valueOf(tong_so_de_thi));
                        Log.e("Tổng số bài chấm", String.valueOf(tong_so_bai_cham));
                    }
                    else if(magv.equals(key_user) && (mahkdt.equals(mahk2)))
                    {
                        int check = FindTenMon(id_mon, 2);
                        tong_so_de_thi += 1;
                        if (check != -1)
                        {
                            so_luong_de_thi_k2.set(check, so_luong_de_thi_k2.get(check) + 1);
                        }
                        else
                        {
                            mamon_k2.add(id_mon);
                            so_luong_de_thi_k2.add(1);
                            so_luong_bai_cham_k2.add(0);
                        }
                        Log.e("Tổng số đề thi", String.valueOf(tong_so_de_thi));
                        Log.e("Tổng số bài chấm", String.valueOf(tong_so_bai_cham));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private void GetSoLuongBaiCham()
    {
        DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
        db_lop.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot data : snapshot.getChildren())
                {
                    String lop_mahkdt = data.child("maHKNH").getValue(String.class);
                    String magvcham = data.child("maGVCham").getValue(String.class);
                    String id_mon = data.child("maMH").getValue(String.class);
                    int siso = data.child("siSo").getValue(Integer.class);

                    if(magvcham.equals(key_user) && lop_mahkdt.equals(mahk1))
                    {
                        int check = FindTenMon(id_mon, 1);
                        tong_so_bai_cham += siso;
                        if (check != -1)
                        {
                            so_luong_bai_cham_k1.set(check, so_luong_bai_cham_k1.get(check) + siso);
                        }
                        Log.e("Tổng số đề thi", String.valueOf(tong_so_de_thi));
                        Log.e("Tổng số bài chấm", String.valueOf(tong_so_bai_cham));
                    }
                    else if(magvcham.equals(key_user) && lop_mahkdt.equals(mahk1))
                    {
                        int check = FindTenMon(id_mon, 2);
                        tong_so_de_thi += siso;
                        if (check != -1)
                        {
                            so_luong_bai_cham_k1.set(check, so_luong_bai_cham_k1.get(check) + siso);
                        }
                        Log.e("Tổng số đề thi", String.valueOf(tong_so_de_thi));
                        Log.e("Tổng số bài chấm", String.valueOf(tong_so_bai_cham));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private int FindTenMon(String mon, int ky)
    {
        if(ky == 1)
        {
            return mamon_k1.indexOf(mon);
        }
        else if(ky == 2)
        {
            return mamon_k2.indexOf(mon);
        }
        return -1;
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
                GetMaHK(); // Call GetMaHK after user login is retrieved
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private void GetTenMonHoc()
    {
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        db_monhoc.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Map<String, String> monMap = new HashMap<>();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    monMap.put(data.getKey(), data.child("tenMH").getValue(String.class));
                }
                for (String mon : mamon_k1)
                {
                    tenmonhoc_k1.add(monMap.get(mon));
                }
                for (String mon : mamon_k2)
                {
                    tenmonhoc_k2.add(monMap.get(mon));
                }

                // Log all the details after fetching all necessary data
                for (int i = 0; i < mamon_k1.size(); i++) {
                    Log.e("Báo cáo kỳ 1", mamon_k1.get(i) + " " + tenmonhoc_k1.get(i) + " " + so_luong_de_thi_k1.get(i) + " " + so_luong_bai_cham_k1.get(i));
                }
                for (int i = 0; i < mamon_k2.size(); i++)
                {
                    Log.e("Báo cáo kỳ 2", mamon_k2.get(i) + " " + tenmonhoc_k2.get(i) + " " + so_luong_de_thi_k2.get(i) + " " + so_luong_bai_cham_k2.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }
    private void GetRecycleViewMonHoc(View view)
    {
        bao_cao_rcv = (RecyclerView)view.findViewById(R.id.tao_de_thi_rcv);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(activity);
        bao_cao_rcv.setLayoutManager(ln_layout_manager);
        adapter = new BaoCaoNamAdapter(mylist);
        bao_cao_rcv.setAdapter(adapter);

        DividerItemDecoration item_decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.divider_nhap_diem, null);
        item_decoration.setDrawable(drawable);
        bao_cao_rcv.addItemDecoration(item_decoration);
    }

    private void CountTongSoDeThiBaiCham()
    {
        tong_so_de_thi = 0;
        tong_so_bai_cham = 0;
        for(int i = 0; i<so_luong_de_thi_k1.size();i++)
        {
            tong_so_de_thi += so_luong_de_thi_k1.get(i);
        }
        for(int i = 0; i<so_luong_de_thi_k2.size();i++)
        {
            tong_so_de_thi += so_luong_de_thi_k2.get(i);
        }
        for(int i = 0; i< so_luong_bai_cham_k1.size();i++)
        {
            tong_so_bai_cham += so_luong_bai_cham_k1.get(i);
        }
        for(int i = 0; i< so_luong_bai_cham_k2.size();i++)
        {
            tong_so_bai_cham += so_luong_bai_cham_k2.get(i);
        }
        Log.e("Tổng số đề thi", String.valueOf(tong_so_de_thi));
        Log.e("Tổng số bài chấm", String.valueOf(tong_so_bai_cham));
    }
    private void GetListItem()
    {
        int soluongdethi;
        int soluongbaicham;
        float tiledethi;
        float tilebaicham;
        for(int i = 0; i < tenmonhoc_k1.size(); i++)
        {
            int check = FindTenMon(tenmonhoc_k1.get(i));
            if(check != 1)
            {
                soluongdethi = mylist.get(check).getSoluongdethi() + so_luong_de_thi_k1.get(i);
                soluongbaicham = mylist.get(check).getSoluongbaicham() + so_luong_bai_cham_k1.get(i);
                tiledethi = soluongdethi/tong_so_de_thi;
                tilebaicham = soluongbaicham/tong_so_bai_cham;

                mylist.get(check).setSoluongdethi(soluongdethi);
                mylist.get(check).setSoluongbaicham(soluongbaicham);
                mylist.get(check).setTiledethi(tiledethi);
                mylist.get(check).setTilebaicham(tilebaicham);

            }
            else
            {
                soluongdethi = so_luong_de_thi_k1.get(i);
                soluongbaicham = so_luong_bai_cham_k1.get(i);
                tiledethi = soluongdethi/tong_so_de_thi;
                tilebaicham = soluongbaicham/tong_so_bai_cham;
                baocaomonhocitem monhoc = new baocaomonhocitem(tenmonhoc_k1.get(i), soluongdethi , soluongbaicham,  tiledethi, tilebaicham);
                mylist.add(monhoc);
            }
        }
        for(int i = 0; i < tenmonhoc_k2.size(); i++)
        {
            int check = FindTenMon(tenmonhoc_k2.get(i));
            if(check != 1)
            {
                soluongdethi = mylist.get(check).getSoluongdethi() + so_luong_de_thi_k2.get(i);
                soluongbaicham = mylist.get(check).getSoluongbaicham() + so_luong_bai_cham_k2.get(i);
                tiledethi = soluongdethi/tong_so_de_thi;
                tilebaicham = soluongbaicham/tong_so_bai_cham;

                mylist.get(check).setSoluongdethi(soluongdethi);
                mylist.get(check).setSoluongbaicham(soluongbaicham);
                mylist.get(check).setTiledethi(tiledethi);
                mylist.get(check).setTilebaicham(tilebaicham);
            }
            else
            {
                soluongdethi = so_luong_de_thi_k2.get(i);
                soluongbaicham = so_luong_bai_cham_k2.get(i);
                tiledethi = soluongdethi/tong_so_de_thi;
                tilebaicham = soluongbaicham/tong_so_bai_cham;
                baocaomonhocitem monhoc = new baocaomonhocitem(tenmonhoc_k2.get(i), soluongdethi , soluongbaicham,  tiledethi, tilebaicham);
                mylist.add(monhoc);
            }
        }

    }
    private int FindTenMon(String tenmon)
    {
        for(int i = 0; i< mylist.size(); i++)
        {
            if(mylist.get(i).getTenmon().equals(tenmon))
            {
                return i;
            }
        }
        return -1;
    }

    private void GetMonHocList()
    {
        DatabaseReference db_baocaonam = FirebaseDatabase.getInstance().getReference("BAOCAONAM");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
        DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
        String[] namSplit = namhoc.split("/");
        String nam1 = namSplit[0];
        String nam2 = namSplit[1];
        db_hknh.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot data : snapshot.getChildren())
                {
                    String nam1Value = String.valueOf(data.child("nam1").getValue(Integer.class));
                    String nam2Value = String.valueOf(data.child("nam2").getValue(Integer.class));
                    String hocKyValue = String.valueOf(data.child("hocKy").getValue(Integer.class));

                    if (nam1Value != null && nam2Value != null && nam1Value.equals(nam1) && nam2Value.equals(nam2))
                    {
                        if ("1".equals(hocKyValue))
                        {
                            mahk1 = data.getKey();
                            Log.e("Mã học kỳ 1", mahk1);
                            GetMaBCNAM(mahk1, new OnMaBaoCaoNamCallBack()
                            {
                                @Override
                                public void onMaBaoCaoNamCallBack(String mabaocaonam)
                                {
                                    ma_bcn_hk1 = mabaocaonam;
                                }
                            });
                        }
                        else if ("2".equals(hocKyValue))
                        {
                            mahk2 = data.getKey();
                            Log.e("Mã học kỳ 2", mahk2);
                            GetMaBCNAM(mahk2, new OnMaBaoCaoNamCallBack()
                            {
                                @Override
                                public void onMaBaoCaoNamCallBack(String mabaocaonam)
                                {
                                    ma_bcn_hk2 = mabaocaonam;
                                }
                            });
                        }
                    }
                }
                DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
                db_dethi.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            if (data.child("maGV").getValue(String.class).equals(key_user)) {
                                if (data.child("maHKNH").getValue(String.class).equals(mahk1)) {

                                    GetMaCTBCN(ma_bcn_hk1, data.child("maMH").getValue(String.class), new OnCTBCNAMCallBack() {
                                        int tongsodethi;
                                        int soluongdethi;
                                        int tiledethi;

                                        @Override
                                        public void onCTBCNAMCallBack(String key_ctbcnam) {

                                            db_baocaonam.child(ma_bcn_hk1).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    tongsodethi = snapshot.child("tongSoDeThi").getValue(Integer.class);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                                            db_ctbcn.child(key_ctbcnam).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    soluongdethi = snapshot.child("soLuongDeThi").getValue(Integer.class);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                                            soluongdethi++;
                                            tongsodethi++;
                                            tiledethi = soluongdethi * 100 / tongsodethi;
                                            db_ctbcn.child(key_ctbcnam).child("soLuongDeThi").setValue(soluongdethi);
                                            db_ctbcn.child(key_ctbcnam).child("tileDeThi").setValue(tiledethi);
                                            db_baocaonam.child(ma_bcn_hk1).child("tongSoDeThi").setValue(tongsodethi);
                                        }
                                    });
                                } else if (data.child("maHKNH").getValue(String.class).equals(mahk2)) {

                                    GetMaCTBCN(ma_bcn_hk2, data.child("maMH").getValue(String.class), new OnCTBCNAMCallBack() {
                                        int tongsodethi;
                                        int soluongdethi;
                                        int tiledethi;

                                        @Override
                                        public void onCTBCNAMCallBack(String key_ctbcnam) {

                                            db_baocaonam.child(ma_bcn_hk2).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    tongsodethi = snapshot.child("tongSoDeThi").getValue(Integer.class);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                                            db_ctbcn.child(key_ctbcnam).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    soluongdethi = snapshot.child("soLuongDeThi").getValue(Integer.class);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                                            soluongdethi++;
                                            tongsodethi++;
                                            tiledethi = soluongdethi * 100 / tongsodethi;
                                            db_ctbcn.child(key_ctbcnam).child("soLuongDeThi").setValue(soluongdethi);
                                            db_ctbcn.child(key_ctbcnam).child("tileDeThi").setValue(tiledethi);
                                            db_baocaonam.child(ma_bcn_hk2).child("tongSoDeThi").setValue(tongsodethi);
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                db_lop.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            if(data.child("maHKNH").getValue(String.class).equals(mahk1))
                            {

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });

    }
    private void GetMaBCNAM(String mahknh , OnMaBaoCaoNamCallBack callback)
    {
        DatabaseReference db_bcn = FirebaseDatabase.getInstance().getReference("BAOCAONAM");
        db_bcn.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    if(data.child("maHKNH").getValue(String.class).equals(mahknh) && data.child("maGV").getValue(String.class).equals(key_user))
                    {
                        callback.onMaBaoCaoNamCallBack(data.getKey());
                    }
                }
                String key = db_bcn.push().getKey();
                BAOCAONAM bcn = new BAOCAONAM(key,mahknh,key_user,0,0);
                db_bcn.child(key).setValue(bcn);
                callback.onMaBaoCaoNamCallBack(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void GetMaCTBCN(String mabcn , String mamon , OnCTBCNAMCallBack callback)
    {
        DatabaseReference db_ctbcn = FirebaseDatabase.getInstance().getReference("CTBCNAM");
        db_ctbcn.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    if(data.child("maBCNam").getValue(String.class).equals(mabcn) && data.child("maMH").getValue(String.class).equals(mamon))
                    {
                        callback.onCTBCNAMCallBack(data.getKey());
                    }
                }
                String key = db_ctbcn.push().getKey();
                CTBCNAM ctbcn = new CTBCNAM(mabcn,mamon,0,0,0,0);
                db_ctbcn.child(key).setValue(ctbcn);
                callback.onCTBCNAMCallBack(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    interface OnMaBaoCaoNamCallBack
    {
        void onMaBaoCaoNamCallBack(String mabaocaonam);
    }
    interface OnCTBCNAMCallBack
    {
        void onCTBCNAMCallBack(String key_ctbcnam);
    }
}