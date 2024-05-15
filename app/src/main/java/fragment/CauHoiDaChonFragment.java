package fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.myapplication.R;
import com.example.myapplication.TaoDeThi;
import com.example.myapplication.TaoDeThi2Adapter;
import com.example.myapplication.TaoDeThiAdapter;
import com.example.myapplication.taodethicauhoiitem;

import java.util.ArrayList;

import my_interface.IClickCauHoiItemListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CauHoiDaChonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CauHoiDaChonFragment extends Fragment
{

    ArrayList<taodethicauhoiitem> list_cau_hoi_duoc_chon;

    RecyclerView cau_hoi_da_tao_thi_rcv;

    TaoDeThi activity;

    TaoDeThi2Adapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CauHoiDaChonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CauHoiDaChonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CauHoiDaChonFragment newInstance(String param1, String param2) {
        CauHoiDaChonFragment fragment = new CauHoiDaChonFragment();
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
        list_cau_hoi_duoc_chon = new ArrayList<>();
        activity = (TaoDeThi)getActivity();
        View view = inflater.inflate(R.layout.fragment_cau_hoi_da_chon, container, false);
        getParentFragmentManager().setFragmentResultListener("data", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                list_cau_hoi_duoc_chon = (ArrayList<taodethicauhoiitem>) result.getSerializable("list_duoc_chon");
                Log.e("Số phần tử ", String.valueOf(list_cau_hoi_duoc_chon.size()));
                GetListCauHoi(view);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
    private void GetListCauHoi(View view)
    {
        Log.e("Số phần tử ", String.valueOf(list_cau_hoi_duoc_chon.size()));
        cau_hoi_da_tao_thi_rcv = (RecyclerView)view.findViewById(R.id.cau_hoi_da_tao_thi_rcv);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(activity);
        cau_hoi_da_tao_thi_rcv.setLayoutManager(ln_layout_manager);
        adapter = new TaoDeThi2Adapter(list_cau_hoi_duoc_chon);
        cau_hoi_da_tao_thi_rcv.setAdapter(adapter);

        DividerItemDecoration item_decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.divider_nhap_diem, null);
        item_decoration.setDrawable(drawable);
        cau_hoi_da_tao_thi_rcv.addItemDecoration(item_decoration);

    }
}