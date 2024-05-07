package fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.GIANGVIEN;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HoSoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HoSoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HoSoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HoSoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HoSoFragment newInstance(String param1, String param2) {
        HoSoFragment fragment = new HoSoFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ho_so, container, false);
        SetProfileData(view);
        return view;
    }
    private void SetProfileData(View view)
    {
        TextView ho_so_ten = view.findViewById(R.id.profile_ten_text);
        TextView ho_so_ngaysinh = view.findViewById(R.id.profile_ngay_sinh_text);
        TextView ho_so_gioitinh = view.findViewById(R.id.profile_gioi_tinh_text);
        TextView ho_so_email = view.findViewById(R.id.profile_email_text);
        TextView ho_so_sdt = view.findViewById(R.id.profile_sdt_text);
        TextView ho_so_diachi = view.findViewById(R.id.profile_dia_chi_text);
        CircleImageView ho_so_userimage = view.findViewById(R.id.ho_so_user_image);

        DatabaseReference db_gv = FirebaseDatabase.getInstance().getReference("GIANGVIEN");
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        DatabaseReference db_userimage = FirebaseDatabase.getInstance().getReference("USERIMAGE");

        db_pdn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String account = snapshot.child("account").getValue(String.class);
                db_gv.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        GIANGVIEN giangvien = snapshot.child(account).getValue(GIANGVIEN.class);
                        ho_so_ten.setText(giangvien.getHoTenGV());
                        ho_so_ngaysinh.setText(giangvien.getNgSinhGV());
                        ho_so_gioitinh.setText(giangvien.getGioiTinhGV());
                        ho_so_email.setText(account + "@gm.uit.edu.vn");
                        ho_so_sdt.setText("");
                        ho_so_diachi.setText("TP. Hồ Chí Minh");String filepath = "user_image_22520375";

                        db_userimage.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                String file_image = snapshot.child(account).child("fileImage").getValue(String.class);
                                Context context = getContext();;
                                String packageName = context.getPackageName();
                                int resourceId = context.getResources().getIdentifier(file_image, "drawable", packageName);
                                ho_so_userimage.setImageResource(resourceId);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}