package fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myapplication.CauHoiScreen;
import com.example.myapplication.DeThiScreen;
import com.example.myapplication.MainScreenNew;
import com.example.myapplication.R;
import com.example.myapplication.TraCuuScreen;

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

    public TrangChuFragment() {
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
            }
        });

        ImageButton tra_cuu = view.findViewById(R.id.trang_chu_tra_cuu_button);
        tra_cuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, TraCuuScreen.class));
            }
        });

        return view;
    }
}