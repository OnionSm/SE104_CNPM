package fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.BaoCaoNamScreen;
import com.example.myapplication.R;
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
    private ArrayList<String> mamon;
    private ArrayList<String> tenmonhoc;
    private ArrayList<Integer> so_luong_de_thi;
    private ArrayList<Integer> so_luong_bai_cham;
    private String key_user;
    private BaoCaoNamScreen activity;

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
                             Bundle savedInstanceState) {
        activity = (BaoCaoNamScreen) getActivity();
        namhoc = activity.AccessData();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bao_cao_so_do1, container, false);

        mamon = new ArrayList<>();
        tenmonhoc = new ArrayList<>();
        so_luong_de_thi = new ArrayList<>();
        so_luong_bai_cham = new ArrayList<>();

        GetUserLogin();

        return view;
    }

    private void GetMaHK() {
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

                    if (nam1Value != null && nam2Value != null && nam1Value.equals(nam1) && nam2Value.equals(nam2)) {
                        if ("1".equals(hocKyValue)) {
                            mahk1 = data.getKey();
                            Log.e("Mã học kỳ 1", mahk1);
                        } else if ("2".equals(hocKyValue)) {
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

    private void GetSoLuongDeThi() {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        db_dethi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String mahkdt = data.child("maHKNH").getValue(String.class);
                    String id_mon = data.child("maMH").getValue(String.class);
                    String magv = data.child("maGV").getValue(String.class);

                    if (magv.equals(key_user) && (mahkdt.equals(mahk1) || mahkdt.equals(mahk2))) {
                        int check = FindTenMon(id_mon);
                        if (check != -1) {
                            so_luong_de_thi.set(check, so_luong_de_thi.get(check) + 1);
                        } else {
                            mamon.add(id_mon);
                            so_luong_de_thi.add(1);
                            so_luong_bai_cham.add(0);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private void GetSoLuongBaiCham() {
        DatabaseReference db_lop = FirebaseDatabase.getInstance().getReference("LOP");
        db_lop.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String lop_mahkdt = data.child("maHKNH").getValue(String.class);
                    String magvcham = data.child("maGVCham").getValue(String.class);
                    String id_mon = data.child("maMH").getValue(String.class);
                    int siso = data.child("siSo").getValue(Integer.class);

                    if (magvcham.equals(key_user) && (lop_mahkdt.equals(mahk1) || lop_mahkdt.equals(mahk2))) {
                        int check = FindTenMon(id_mon);
                        if (check != -1) {
                            so_luong_bai_cham.set(check, so_luong_bai_cham.get(check) + siso);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private int FindTenMon(String mon) {
        return mamon.indexOf(mon);
    }

    private void GetUserLogin() {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        db_pdn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

    private void GetTenMonHoc() {
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        db_monhoc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> monMap = new HashMap<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    monMap.put(data.getKey(), data.child("tenMH").getValue(String.class));
                }
                for (String mon : mamon) {
                    tenmonhoc.add(monMap.get(mon));
                }

                // Log all the details after fetching all necessary data
                for (int i = 0; i < mamon.size(); i++) {
                    Log.e("Báo cáo", mamon.get(i) + " " + tenmonhoc.get(i) + " " + so_luong_de_thi.get(i) + " " + so_luong_bai_cham.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }
}
