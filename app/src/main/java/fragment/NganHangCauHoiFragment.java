package fragment;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.TaoDeThi;
import com.example.myapplication.TaoDeThiAdapter;
import com.example.myapplication.ThemCauHoi;
import com.example.myapplication.TranslateAnimationUtil;
import com.example.myapplication.taodethicauhoiitem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import my_interface.IClickCauHoiItemListener;
import my_interface.IPassingData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NganHangCauHoiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NganHangCauHoiFragment extends Fragment
{
    TaoDeThi activity;
    RecyclerView tao_de_thi_rcv;

    TaoDeThiAdapter adapter;

    SearchView searchview;

    ArrayList<taodethicauhoiitem> mylist;

    ArrayList<taodethicauhoiitem> list_cau_hoi_duoc_chon;
    private int socautd;


    String monhoc;


    String mamonhoc;

    ImageButton themcauhoi;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NganHangCauHoiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NganHangCauHoiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NganHangCauHoiFragment newInstance(String param1, String param2)
    {
        NganHangCauHoiFragment fragment = new NganHangCauHoiFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        activity = (TaoDeThi)getActivity();
        monhoc = activity.AccessData();

        View view = inflater.inflate(R.layout.fragment_ngan_hang_cau_hoi, container, false);
        themcauhoi = view.findViewById(R.id.them_cau_hoi_button);

        GetMaMH();
        TriggerDeThi();
        // Inflate the layout for this fragment
        GetDataCauHoiFromFireBase(view);
        GetListCauHoi(view);


        themcauhoi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, ThemCauHoi.class);
                startActivity(intent);
            }
        });


        return view;
    }




    private void GetDataCauHoiFromFireBase(View view)
    {
        DatabaseReference db_cauhoi = FirebaseDatabase.getInstance().getReference("CAUHOI");
        mylist = new ArrayList<>();

        db_cauhoi.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {

                    if(data.child("maMH").getValue(String.class).equals(mamonhoc))
                    {

                        String mach = data.child("maCH").getValue(String.class);
                        String noidung = data.child("noiDung").getValue(String.class);
                        String madokho = data.child("maDoKho").getValue(String.class);
                        mylist.add(new taodethicauhoiitem(mach, madokho, noidung));
                        adapter.notifyDataSetChanged();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    private void GetListCauHoi(View view)
    {
        list_cau_hoi_duoc_chon = new ArrayList<>();
        tao_de_thi_rcv = (RecyclerView)view.findViewById(R.id.tao_de_thi_rcv);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(activity);
        tao_de_thi_rcv.setLayoutManager(ln_layout_manager);
        adapter = new TaoDeThiAdapter(mylist, new IClickCauHoiItemListener()
        {

            @Override
            public void onClickItemCauHoi(taodethicauhoiitem cauhoi)
            {

                for(taodethicauhoiitem item : list_cau_hoi_duoc_chon)
                {
                    if(item.getMacauhoi() == cauhoi.getMacauhoi())
                    {
                        Log.e("Trùng câu hỏi" , "Trùng câu hỏi");
                        return;
                    }
                }
                if(list_cau_hoi_duoc_chon.size() < socautd)
                {
                    list_cau_hoi_duoc_chon.add(cauhoi);
                    Bundle result = new Bundle();
                    result.putSerializable("list_duoc_chon", list_cau_hoi_duoc_chon);
                    getParentFragmentManager().setFragmentResult("data", result);
                }
                else
                {
                    Toast.makeText(activity, "Đã đạt số lượng câu hỏi tối đa", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tao_de_thi_rcv.setAdapter(adapter);
        tao_de_thi_rcv.setOnTouchListener(new TranslateAnimationUtil(activity, themcauhoi));

        DividerItemDecoration item_decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.divider_nhap_diem, null);
        item_decoration.setDrawable(drawable);
        tao_de_thi_rcv.addItemDecoration(item_decoration);

        searchview = (SearchView)view.findViewById(R.id.tao_de_thi_search_view);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void TriggerDeThi()
    {
        DatabaseReference db_thamso = FirebaseDatabase.getInstance().getReference("THAMSO");
        db_thamso.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                socautd = snapshot.child("socautd").child("giaTri").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        });
    }
    private void GetMaMH()
    {
        monhoc = monhoc.toLowerCase();
        DatabaseReference db_monhoc = FirebaseDatabase.getInstance().getReference("MONHOC");
        db_monhoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    String tenmh = data.child("tenMH").getValue(String.class).toLowerCase();
                    if(tenmh.equals(monhoc))
                    {
                        mamonhoc = data.getKey().toString();
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
}