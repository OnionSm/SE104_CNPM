package fragment;

import static com.google.common.reflect.Reflection.getPackageName;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.CauHoiScreen;
import com.example.myapplication.ChamDiemScreen;
import com.example.myapplication.DeThiScreen;
import com.example.myapplication.MainScreenNew;
import com.example.myapplication.R;
import com.example.myapplication.TraCuuScreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrangChuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrangChuFragment extends Fragment
{
    MainScreenNew activity;
    ImageButton cauhoi;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrangChuFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrangChuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrangChuFragment newInstance(String param1, String param2)
    {
        TrangChuFragment fragment = new TrangChuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);
        activity = (MainScreenNew)getActivity();
        cauhoi = (ImageButton)view.findViewById(R.id.trang_chu_cau_hoi_button);
        cauhoi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(activity, CauHoiScreen.class));
                requireActivity().finishAffinity();
            }
        });
        ImageButton dethi = view.findViewById(R.id.trang_chu_de_thi_button);
        dethi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent de_thi_intent = new Intent(activity, DeThiScreen.class);
                startActivity(de_thi_intent);
                requireActivity().finishAffinity();
            }
        });

        ImageButton tra_cuu = view.findViewById(R.id.trang_chu_tra_cuu_button);
        tra_cuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(activity, TraCuuScreen.class));
                requireActivity().finishAffinity();
            }
        });

        ImageButton cham_diem = view.findViewById(R.id.trang_chu_cham_diem_button);
        cham_diem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(activity, ChamDiemScreen.class));
                requireActivity().finishAffinity();
            }
        });


        SetUserImage(view);



        return view;
    }
    private void SetUserImage(View view)
    {

        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        DatabaseReference db_userimage = FirebaseDatabase.getInstance().getReference("USERIMAGE");
        DatabaseReference db_gv = FirebaseDatabase.getInstance().getReference("GIANGVIEN");
        CircleImageView user_image = view.findViewById(R.id.trang_chu_user_image);
        TextView user_name = view.findViewById(R.id.trang_chu_ten_user);
        db_pdn.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String taikhoan = snapshot.child("account").getValue(String.class);
                db_userimage.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String file_image = snapshot.child(taikhoan).child("fileImage").getValue(String.class);
                        Context context = getContext();;
                        String packageName = context.getPackageName();
                        int resourceId = context.getResources().getIdentifier(file_image, "drawable", packageName);
                        user_image.setImageResource(resourceId);
                        db_gv.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                String tengv = snapshot.child(taikhoan).child("hoTenGV").getValue(String.class);
                                user_name.setText(tengv);
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