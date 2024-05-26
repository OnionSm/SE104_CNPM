package fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.BaoCaoNhapNamScreen;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrangChuFragment extends Fragment {

    MainScreenNew activity;
    ImageButton cauhoi;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public TrangChuFragment() {
        // Required empty public constructor
    }

    public static TrangChuFragment newInstance(String param1, String param2) {
        TrangChuFragment fragment = new TrangChuFragment();
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

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                CircleImageView user_image = getView().findViewById(R.id.trang_chu_user_image);
                Glide.with(this).load(imageUri).into(user_image);
                addToStorage(imageUri);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);
        activity = (MainScreenNew) getActivity();
        cauhoi = view.findViewById(R.id.trang_chu_cau_hoi_button);
        cauhoi.setOnClickListener(v -> {
            startActivity(new Intent(activity, CauHoiScreen.class));
            requireActivity().finishAffinity();
        });

        ImageButton dethi = view.findViewById(R.id.trang_chu_de_thi_button);
        dethi.setOnClickListener(v -> {
            Intent de_thi_intent = new Intent(activity, DeThiScreen.class);
            startActivity(de_thi_intent);
            requireActivity().finishAffinity();
        });

        ImageButton tra_cuu = view.findViewById(R.id.trang_chu_tra_cuu_button);
        tra_cuu.setOnClickListener(v -> {
            startActivity(new Intent(activity, TraCuuScreen.class));
            requireActivity().finishAffinity();
        });

        ImageButton cham_diem = view.findViewById(R.id.trang_chu_cham_diem_button);
        cham_diem.setOnClickListener(v -> {
            startActivity(new Intent(activity, ChamDiemScreen.class));
            requireActivity().finishAffinity();
        });

        ImageButton bao_cao = view.findViewById(R.id.trang_chu_bao_cao_button);
        bao_cao.setOnClickListener(v -> {
            startActivity(new Intent(activity, BaoCaoNhapNamScreen.class));
        });

        SetUserImage(view);

        return view;
    }

    private void SetUserImage(View view) {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        DatabaseReference db_userimage = FirebaseDatabase.getInstance().getReference("USERIMAGE");
        DatabaseReference db_gv = FirebaseDatabase.getInstance().getReference("GIANGVIEN");
        CircleImageView user_image = view.findViewById(R.id.trang_chu_user_image);
        TextView user_name = view.findViewById(R.id.trang_chu_ten_user);
        db_pdn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String taikhoan = snapshot.child("account").getValue(String.class);
                db_userimage.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String file_image = snapshot.child(taikhoan).child("fileImage").getValue(String.class);
                        if (file_image != null) {
                            Glide.with(TrangChuFragment.this).load(file_image).into(user_image);
                        }
                        db_gv.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

    public void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void addToStorage(Uri imageUri) {
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        db_pdn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String userAccount = snapshot.child("account").getValue(String.class);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("USERIMAGE");
                final StorageReference imageName = storageReference.child(userAccount + "_" + UUID.randomUUID().toString());
                imageName.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    imageName.getDownloadUrl().addOnSuccessListener(uri -> {
                        DatabaseReference db_userimage = FirebaseDatabase.getInstance().getReference("USERIMAGE");
                        db_userimage.child(userAccount).child("fileImage").setValue(uri.toString());
                    });
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
