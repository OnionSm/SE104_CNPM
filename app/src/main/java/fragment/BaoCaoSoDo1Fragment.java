package fragment;

import android.content.Context;
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
import android.widget.TextView;

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

    private BaoCaoNamScreen activity;

    RecyclerView bao_cao_rcv;
    BaoCaoNamAdapter adapter;
    private ArrayList<baocaomonhocitem> mylist;
    private ArrayList<baocaomonhocitem> mylist2;

    TextView bcnam_text;
    TextView sldt;
    TextView slbc;
    int tongsodethi;
    int tongsobaicham;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public BaoCaoSoDo1Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
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
        namhoc = activity.AccessDataNam();
        mylist = activity.AccessDataList();
        tongsodethi = activity.AccessDataTongSoDeThi();
        tongsobaicham = activity.AccessDataTongSoBaiCham();
        if(namhoc != null)
        {
            Log.e("namhoc", namhoc);
        }



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bao_cao_so_do1, container, false);
        bcnam_text = view.findViewById(R.id.bao_cao_nam_text);
        sldt = view.findViewById(R.id.bao_cao_nam_sldt_text);
        slbc = view.findViewById(R.id.bao_cao_nam_slbc_text);
        bcnam_text.setText("Báo cáo năm học " + namhoc.split("/")[0] + "-" + namhoc.split("/")[1]);
        sldt.setText("Tổng số đề thi: " + String.valueOf(tongsodethi));
        slbc.setText("Tổng số bài chấm: " + String.valueOf(tongsobaicham));




        if(mylist != null)
        {
            for(int i = 0 ; i<mylist.size();i++)
            {
                Log.e("giá trị list",mylist.get(i).getTenmon()+" " +
                        String.valueOf(mylist.get(i).getSoluongdethi()) + " " +
                        String.valueOf(mylist.get(i).getSoluongbaicham()) + " " +
                        String.valueOf(mylist.get(i).getTiledethi()) + " " +
                        String.valueOf(mylist.get(i).getTilebaicham()));
            }
        }
        GetRecycleViewMonHoc(view);

        ArrayList<String> list_mon = new ArrayList();
        ArrayList<Integer> list_sldt = new ArrayList<>();
        for(int i = 0; i<mylist.size();i++)
        {
            list_mon.add(mylist.get(i).getTenmon());
            list_sldt.add(mylist.get(i).getSoluongdethi());
        }

        Bundle result = new Bundle();
        result.putSerializable("list_mon", mylist);
        getParentFragmentManager().setFragmentResult("data", result);
        getParentFragmentManager().setFragmentResult("data1", result);

        return view;
    }

    private void GetRecycleViewMonHoc(View view)
    {
        bao_cao_rcv = (RecyclerView)view.findViewById(R.id.bao_cao_tao_de_thi_rcv);
        LinearLayoutManager ln_layout_manager = new LinearLayoutManager(activity);
        bao_cao_rcv.setLayoutManager(ln_layout_manager);
        adapter = new BaoCaoNamAdapter(mylist);
        bao_cao_rcv.setAdapter(adapter);

        DividerItemDecoration item_decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.divider_nhap_diem, null);
        item_decoration.setDrawable(drawable);
        bao_cao_rcv.addItemDecoration(item_decoration);
    }


}