package fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.CAUHOI;
import com.example.myapplication.R;
import com.example.myapplication.SuaDeThiScreen;
import com.example.myapplication.TaoDeThiAdapter4;
import com.example.myapplication.taodethicauhoiitem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import my_interface.IPassingData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuaCauHoiDaChonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuaCauHoiDaChonFragment extends Fragment
{
    ArrayList<taodethicauhoiitem> list_cau_hoi_duoc_chon;

    RecyclerView cau_hoi_da_tao_thi_rcv;

    SuaDeThiScreen activity;

    TaoDeThiAdapter4 adapter;
    IPassingData passdata_interface;
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        passdata_interface = (IPassingData) context;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SuaCauHoiDaChonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuaCauHoiDaChonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuaCauHoiDaChonFragment newInstance(String param1, String param2) {
        SuaCauHoiDaChonFragment fragment = new SuaCauHoiDaChonFragment();
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
        list_cau_hoi_duoc_chon = new ArrayList<>();
        activity = (SuaDeThiScreen)getActivity();
        View view = inflater.inflate(R.layout.fragment_sua_cau_hoi_da_chon, container, false);
        getParentFragmentManager().setFragmentResultListener("data", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {

                list_cau_hoi_duoc_chon = (ArrayList<taodethicauhoiitem>) result.getSerializable("list_duoc_chon");
                Log.e("Số phần tử ", String.valueOf(list_cau_hoi_duoc_chon.size()));
                GetListCauHoi(view);
                sendDataToActivity(list_cau_hoi_duoc_chon);
                adapter.notifyDataSetChanged();

            }
        });

        return view;
    }
    private void GetListCauHoi(View view)
    {
        Log.e("Số phần tử ", String.valueOf(list_cau_hoi_duoc_chon.size()));
        cau_hoi_da_tao_thi_rcv = (RecyclerView)view.findViewById(R.id.cau_hoi_da_tao_thi_rcv);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(activity);
        cau_hoi_da_tao_thi_rcv.setLayoutManager(ln_layout_manager);
        adapter = new TaoDeThiAdapter4(list_cau_hoi_duoc_chon, new TaoDeThiAdapter4.IClickUpdateCauHoiDaChon()
        {
            @Override
            public void UpdateCauHoi(taodethicauhoiitem cauhoi)
            {
                UpdateCauHoiToDataBase(cauhoi);
            }
        }, new TaoDeThiAdapter4.IClickDeleteCauHoiDaChon()
        {
            @Override
            public void DeleteCauHoi(taodethicauhoiitem cauhoi)
            {
                DeleteCauHoiFromList(cauhoi);
            }
        });
        cau_hoi_da_tao_thi_rcv.setAdapter(adapter);

        DividerItemDecoration item_decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.divider_nhap_diem3, null);
        item_decoration.setDrawable(drawable);
        cau_hoi_da_tao_thi_rcv.addItemDecoration(item_decoration);
    }


    private void UpdateCauHoiToDataBase(taodethicauhoiitem cauhoi)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_sua_cau_hoi_pop_up);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams window_attributes = window.getAttributes();
        window_attributes.gravity = Gravity.CENTER;
        window.setAttributes(window_attributes);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        EditText noidung_edt = dialog.findViewById(R.id.noi_dung_cau_hoi_edt);
        EditText dokho_edt = dialog.findViewById(R.id.do_kho_edt);
        ImageButton thay_doi_button = dialog.findViewById(R.id.thay_doi_cau_hoi_button);

        thay_doi_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference db_cauhoi = FirebaseDatabase.getInstance().getReference("CAUHOI");
                DatabaseReference db_dokho = FirebaseDatabase.getInstance().getReference("DOKHO");
                String dokho = dokho_edt.getText().toString();
                String noidung = noidung_edt.getText().toString();
                db_cauhoi.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        CAUHOI cauhoi_full = snapshot.child(cauhoi.getMacauhoi()).getValue(CAUHOI.class);
                        cauhoi_full.setNoiDung(noidung);
                        db_dokho.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for (DataSnapshot data : snapshot.getChildren())
                                {
                                    Log.e("tên độ khó", data.child("TenDK").getValue(String.class).toLowerCase());
                                    if (data.child("TenDK").getValue(String.class).toLowerCase().equals(dokho.toLowerCase())) {
                                        String madokho = data.getKey().toString();
                                        cauhoi_full.setMaDoKho(madokho);
                                        String key = db_cauhoi.push().getKey();
                                        cauhoi.setMacauhoi(key);
                                        cauhoi.setNoidung(noidung);
                                        cauhoi_full.setMaCH(key);
                                        db_cauhoi.child(key).setValue(cauhoi_full);
                                        adapter.notifyDataSetChanged();
                                        sendDataToActivity(list_cau_hoi_duoc_chon);
                                        dialog.dismiss();
                                        break;
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

                    }
                });
            }
        });
    }

    private void DeleteCauHoiFromList(taodethicauhoiitem cauhoi)
    {
        list_cau_hoi_duoc_chon.remove(cauhoi);
        sendDataToActivity(list_cau_hoi_duoc_chon);
        adapter.notifyDataSetChanged();
    }

    private void sendDataToActivity(ArrayList<taodethicauhoiitem> list_cau_hoi)
    {
        passdata_interface.PassData(list_cau_hoi);
    }
}