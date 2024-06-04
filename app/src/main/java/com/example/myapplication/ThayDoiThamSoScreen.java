package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThayDoiThamSoScreen extends AppCompatActivity {

    private EditText diem_toi_thieu_edt, diem_toi_da_edt, so_cau_toi_da_edt, thoi_luong_toi_thieu_edt, thoi_luong_toi_da_edt;
    private ImageButton dtt_update, dtt_ok, dtd_update, dtd_ok, sctd_update, sctd_ok, tltt_update, tltt_ok, tltd_update, tltd_ok;
    private DatabaseReference db_thamso, db_chitietlop, db_dethi, db_dtch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thay_doi_tham_so_screen);

        initViews();
        setupEdgeToEdge();
        setupFirebase();
        setupOnClickListeners();
        GetDataThamSo();
        setupOnBackPressed();
    }

    private void initViews() {
        diem_toi_thieu_edt = findViewById(R.id.diem_toi_thieu_edt);
        diem_toi_da_edt = findViewById(R.id.diem_toi_da_edt);
        so_cau_toi_da_edt = findViewById(R.id.so_cau_toi_da_edt);
        thoi_luong_toi_thieu_edt = findViewById(R.id.thoi_luong_toi_thieu_edt);
        thoi_luong_toi_da_edt = findViewById(R.id.thoi_luong_toi_da_edt);

        dtt_update = findViewById(R.id.dtt_update);
        dtt_ok = findViewById(R.id.dtt_ok);
        dtd_update = findViewById(R.id.dtd_update);
        dtd_ok = findViewById(R.id.dtd_ok);
        sctd_update = findViewById(R.id.sctd_update);
        sctd_ok = findViewById(R.id.sctd_ok);
        tltt_update = findViewById(R.id.tltt_update);
        tltt_ok = findViewById(R.id.tltt_ok);
        tltd_update = findViewById(R.id.tltd_update);
        tltd_ok = findViewById(R.id.tltd_ok);
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.thay_doi_tham_so), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupFirebase() {
        db_thamso = FirebaseDatabase.getInstance().getReference("THAMSO");
        db_chitietlop = FirebaseDatabase.getInstance().getReference("CHITIETLOP");
        db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        db_dtch = FirebaseDatabase.getInstance().getReference("DETHICAUHOI");
    }

    private void setupOnClickListeners() {
        findViewById(R.id.thay_doi_tham_so_icon_back).setOnClickListener(v -> finish());

        setupEditButtonListeners(dtt_update, dtt_ok, diem_toi_thieu_edt, "diemtoithieu", this::validateMinScore);
        setupEditButtonListeners(dtd_update, dtd_ok, diem_toi_da_edt, "diemtoida", this::validateMaxScore);
        setupEditButtonListeners(sctd_update, sctd_ok, so_cau_toi_da_edt, "socautd", this::validateMaxQuestions);
        setupEditButtonListeners(tltt_update, tltt_ok, thoi_luong_toi_thieu_edt, "thoiluongthitoithieu", this::validateMinDuration);
        setupEditButtonListeners(tltd_update, tltd_ok, thoi_luong_toi_da_edt, "thoiluongthitoida", this::validateMaxDuration);
    }

    private void setupEditButtonListeners(ImageButton updateBtn, ImageButton okBtn, EditText editText, String dbField, ValidateParameter validate) {
        updateBtn.setOnClickListener(v -> {
            editText.setEnabled(true);
            updateBtn.setVisibility(View.GONE);
            okBtn.setVisibility(View.VISIBLE);
        });

        okBtn.setOnClickListener(v -> validate.validateParameter(dbField, editText, updateBtn, okBtn));
    }

    private void GetDataThamSo() {
        db_thamso.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setEditTextFromSnapshot(snapshot, "diemtoithieu", diem_toi_thieu_edt);
                setEditTextFromSnapshot(snapshot, "diemtoida", diem_toi_da_edt);
                setEditTextFromSnapshot(snapshot, "socautd", so_cau_toi_da_edt);
                setEditTextFromSnapshot(snapshot, "thoiluongthitoithieu", thoi_luong_toi_thieu_edt);
                setEditTextFromSnapshot(snapshot, "thoiluongthitoida", thoi_luong_toi_da_edt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setEditTextFromSnapshot(DataSnapshot snapshot, String field, EditText editText) {
        if (snapshot.child(field).exists()) {
            editText.setText(String.valueOf(snapshot.child(field).child("giaTri").getValue(Integer.class)));
        }
    }

    private void validateMinScore(String field, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        String newValue = editText.getText().toString();
        int newIntValue = Integer.parseInt(newValue);

        db_thamso.child(field).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (newValue.equals(String.valueOf(snapshot.child("giaTri").getValue(Integer.class)))) {
                    showToast("Giá trị này đã tồn tại");
                    resetEditState(editText, updateBtn, okBtn);
                } else {
                    validateClassDetailsForMinScore(newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void validateClassDetailsForMinScore(int newIntValue, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        db_chitietlop.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean isValid = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("diem").getValue(Integer.class) < newIntValue) {
                        showToast("Không thể thay đổi tham số");
                        isValid = false;
                        resetEditState(editText, updateBtn, okBtn);
                        break;
                    }
                }
                if (isValid) {
                    updateFirebaseValue("diemtoithieu", newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void validateMaxScore(String field, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        String newValue = editText.getText().toString();
        int newIntValue = Integer.parseInt(newValue);

        db_thamso.child(field).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (newValue.equals(String.valueOf(snapshot.child("giaTri").getValue(Integer.class)))) {
                    showToast("Giá trị này đã tồn tại");
                    resetEditState(editText, updateBtn, okBtn);
                } else {
                    validateClassDetailsForMaxScore(newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void validateClassDetailsForMaxScore(int newIntValue, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        db_chitietlop.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean isValid = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("diem").getValue(Integer.class) > newIntValue) {
                        showToast("Không thể thay đổi tham số");
                        isValid = false;
                        resetEditState(editText, updateBtn, okBtn);
                        break;
                    }
                }
                if (isValid) {
                    updateFirebaseValue("diemtoida", newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void validateMaxQuestions(String field, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        String newValue = editText.getText().toString();
        int newIntValue = Integer.parseInt(newValue);

        db_thamso.child(field).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (newValue.equals(String.valueOf(snapshot.child("giaTri").getValue(Integer.class)))) {
                    showToast("Giá trị này đã tồn tại");
                    resetEditState(editText, updateBtn, okBtn);
                } else {
                    validateExamDetailsForMaxQuestions(newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void validateExamDetailsForMaxQuestions(int newIntValue, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        db_dethi.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean isValid = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("soCau").getValue(Integer.class) > newIntValue) {
                        showToast("Không thể thay đổi tham số");
                        isValid = false;
                        resetEditState(editText, updateBtn, okBtn);
                        break;
                    }
                }
                if (isValid) {
                    updateFirebaseValue("socautd", newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void validateMinDuration(String field, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        String newValue = editText.getText().toString();
        int newIntValue = Integer.parseInt(newValue);

        db_thamso.child(field).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (newValue.equals(String.valueOf(snapshot.child("giaTri").getValue(Integer.class)))) {
                    showToast("Giá trị này đã tồn tại");
                    resetEditState(editText, updateBtn, okBtn);
                } else {
                    validateExamDetailsForMinDuration(newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void validateExamDetailsForMinDuration(int newIntValue, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        db_dtch.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean isValid = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("thoiGianLamBai").getValue(Integer.class) < newIntValue) {
                        showToast("Không thể thay đổi tham số");
                        isValid = false;
                        resetEditState(editText, updateBtn, okBtn);
                        break;
                    }
                }
                if (isValid) {
                    updateFirebaseValue("thoiluongthitoithieu", newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void validateMaxDuration(String field, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        String newValue = editText.getText().toString();
        int newIntValue = Integer.parseInt(newValue);

        db_thamso.child(field).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (newValue.equals(String.valueOf(snapshot.child("giaTri").getValue(Integer.class)))) {
                    showToast("Giá trị này đã tồn tại");
                    resetEditState(editText, updateBtn, okBtn);
                } else {
                    validateExamDetailsForMaxDuration(newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void validateExamDetailsForMaxDuration(int newIntValue, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        db_dtch.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean isValid = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("thoiGianLamBai").getValue(Integer.class) > newIntValue) {
                        showToast("Không thể thay đổi tham số");
                        isValid = false;
                        resetEditState(editText, updateBtn, okBtn);
                        break;
                    }
                }
                if (isValid) {
                    updateFirebaseValue("thoiluongthitoida", newIntValue, editText, updateBtn, okBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateFirebaseValue(String field, int newValue, EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        db_thamso.child(field).child("giaTri").setValue(newValue).addOnCompleteListener(task -> {
            showToast("Cập nhật thành công");
            resetEditState(editText, updateBtn, okBtn);
        }).addOnFailureListener(e -> showToast("Cập nhật thất bại"));
    }

    private void showToast(String message) {
        Toast.makeText(ThayDoiThamSoScreen.this, message, Toast.LENGTH_SHORT).show();
    }

    private void resetEditState(EditText editText, ImageButton updateBtn, ImageButton okBtn) {
        editText.setEnabled(false);
        updateBtn.setVisibility(View.VISIBLE);
        okBtn.setVisibility(View.GONE);
    }

    private void setupOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @FunctionalInterface
    interface ValidateParameter {
        void validateParameter(String field, EditText editText, ImageButton updateBtn, ImageButton okBtn);
    }
}
