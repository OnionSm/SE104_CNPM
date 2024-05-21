package fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.baocaomonhocitem;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaoCaoSoDo3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaoCaoSoDo3Fragment extends Fragment
{

    ArrayList<baocaomonhocitem> list_mon;
    ArrayList<Integer> list_sldt;
    PieChart pieChart;
    int tongsobaicham;

    String list_ma_mau[] = {"#70FB4570","#70F0E1DE","#70F7C9B6","#70549BAD","#7083BDC0","#70F9C7E4"};
    CircularProgressIndicator circularProgress;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BaoCaoSoDo3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BaoCaoSoDo3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaoCaoSoDo3Fragment newInstance(String param1, String param2) {
        BaoCaoSoDo3Fragment fragment = new BaoCaoSoDo3Fragment();
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
        View view = inflater.inflate(R.layout.fragment_bao_cao_so_do3, container, false);
        // Inflate the layout for this fragment
        pieChart = view.findViewById(R.id.sldt_piechart);
        list_mon = new ArrayList<>();

        getParentFragmentManager().setFragmentResultListener("data1", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                list_mon = (ArrayList<baocaomonhocitem>) result.getSerializable("list_mon");
                updateData();
            }
        });
        return view;
    }
    private void updateData()
    {
        tongsobaicham = 0;
        for (int i = 0; i < list_mon.size(); i++)
        {
            tongsobaicham += list_mon.get(i).getSoluongbaicham();
        }
        insertionSort(list_mon);
        setupPieChart();
    }

    private void setupPieChart()
    {
        List<PieEntry> pieEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < list_mon.size(); i++)
        {
            pieEntries.add(new PieEntry(list_mon.get(i).getSoluongbaicham(),list_mon.get(i).getTenmon()));
            colors.add(Color.parseColor(list_ma_mau[i]));
            count += list_mon.get(i).getSoluongbaicham();
            if(i==4)
            {
                break;
            }
        }
        if(list_mon.size()>=6)
        {
            pieEntries.add(new PieEntry(tongsobaicham - count, "Khác"));
            colors.add(Color.parseColor(list_ma_mau[5]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, "");

        // Tạo danh sách màu tùy chỉnh


        // Thêm nhiều màu tùy thích vào đây

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);  // Set kích thước chữ cho các giá trị trên biểu đồ
        data.setValueTextColor(Color.BLACK);  // Set màu chữ nếu cần

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.setCenterText(String.valueOf(tongsobaicham) + "\n");
        pieChart.setCenterTextSize(20f);  // Set kích thước chữ cho Center Text
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelTextSize(25f);  // Set kích thước chữ cho các nhãn mục
        pieChart.setHoleRadius(65f);
        pieChart.setContentDescription("Biểu đồ môn học");

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12f);  // Set kích thước chữ cho chú thích
        legend.setFormSize(10f);
        legend.setFormToTextSpace(1f);
        legend.setWordWrapEnabled(true);  // Cho phép chú thích hiển thị trên nhiều hàng
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    }


    public void insertionSort(ArrayList<baocaomonhocitem> list)
    {
        int n = list.size();
        for (int i = 1; i < n; ++i) {
            baocaomonhocitem key = list.get(i);
            int j = i - 1;

            // Di chuyển các phần tử của list[0..i-1], lớn hơn key, đến vị trí cao hơn
            while (j >= 0 && list.get(j).getSoluongdethi() < key.getSoluongdethi())
            {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
    }
}