package fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.GIANGVIEN;
import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class HoSoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Uri imageUri;
    private CircleImageView ho_so_userimage;
    private String userAccount;

    private String mParam1;
    private String mParam2;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public HoSoFragment() {
        // Required empty public constructor
    }

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

        // Khởi tạo imagePickerLauncher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                imageUri = result.getData().getData();
                Picasso.get().load(imageUri).into(ho_so_userimage);
                addToStorage(imageUri);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ho_so, container, false);
        ho_so_userimage = view.findViewById(R.id.ho_so_user_image);
        SetProfileData(view);
        ImageButton log_out = view.findViewById(R.id.log_out);
        log_out.setOnClickListener(v -> showPopupLogout());
        return view;
    }

    private void showPopupLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pop_up_dang_xuat, null);
        builder.setView(dialogView);
        Button button_dong_y = dialogView.findViewById(R.id.button_dong_y_dang_xuat);
        Button button_huy = dialogView.findViewById(R.id.button_huy_dang_xuat);
        AlertDialog dialog = builder.create();
        button_dong_y.setOnClickListener(v -> Logout());
        button_huy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void Logout() {
        Intent intent = new Intent(getActivity(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();    }

    private void SetProfileData(View view) {
        TextView ho_so_ten = view.findViewById(R.id.profile_ten_text);
        TextView ho_so_ngaysinh = view.findViewById(R.id.profile_ngay_sinh_text);
        TextView ho_so_gioitinh = view.findViewById(R.id.profile_gioi_tinh_text);
        TextView ho_so_email = view.findViewById(R.id.profile_email_text);
        TextView ho_so_sdt = view.findViewById(R.id.profile_sdt_text);
        TextView ho_so_diachi = view.findViewById(R.id.profile_dia_chi_text);
        ImageButton doi_anh_dai_dien = view.findViewById(R.id.doi_anh_dai_dien);

        DatabaseReference db_gv = FirebaseDatabase.getInstance().getReference("GIANGVIEN");
        DatabaseReference db_pdn = FirebaseDatabase.getInstance().getReference("PHIENDANGNHAP");
        DatabaseReference db_userimage = FirebaseDatabase.getInstance().getReference("USERIMAGE");

        doi_anh_dai_dien.setOnClickListener(v -> chooseImage());

        db_pdn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String account = snapshot.child("account").getValue(String.class);
                userAccount = account;
                db_gv.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        GIANGVIEN giangvien = snapshot.child(account).getValue(GIANGVIEN.class);
                        ho_so_ten.setText(giangvien.getHoTenGV());
                        ho_so_ngaysinh.setText(giangvien.getNgSinhGV());
                        ho_so_gioitinh.setText(giangvien.getGioiTinhGV());
                        ho_so_email.setText(account + "@gm.uit.edu.vn");
                        ho_so_sdt.setText("");
                        ho_so_diachi.setText("TP. Hồ Chí Minh");

                        db_userimage.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String file_image = snapshot.child(account).child("fileImage").getValue(String.class);
                                if (file_image != null) {
                                    Picasso.get().load(file_image).into(ho_so_userimage);
                                }
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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("USERIMAGE");
        final StorageReference imageName = storageReference.child(userAccount + "_" + UUID.randomUUID().toString());
        imageName.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            imageName.getDownloadUrl().addOnSuccessListener(uri -> {
                DatabaseReference db_userimage = FirebaseDatabase.getInstance().getReference("USERIMAGE");
                db_userimage.child(userAccount).child("fileImage").setValue(uri.toString());
            });
        });
    }
}
