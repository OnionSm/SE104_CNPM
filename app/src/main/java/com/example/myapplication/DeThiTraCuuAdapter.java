package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeThiTraCuuAdapter extends RecyclerView.Adapter<DeThiTraCuuAdapter.DeThiViewHolder> implements Filterable {

    private List<dethitracuuitem> mylist;
    private List<dethitracuuitem> mylistOld;
    private List<dethitracuuitem> my_list_main;
    private DeThiTraCuuClickCallBack callback;

    interface DeThiTraCuuClickCallBack {
        void deThiTraCuuCallBack(dethitracuuitem dethi);
    }

    public DeThiTraCuuAdapter(List<dethitracuuitem> mylist, DeThiTraCuuClickCallBack callback) {
        this.mylist = mylist;
        this.mylistOld = mylist;
        this.my_list_main = mylist;
        this.callback = callback;
    }

    @NonNull
    @Override
    public DeThiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_tra_cuu_de_thi_list_view, parent, false);
        return new DeThiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeThiViewHolder holder, int position) {
        dethitracuuitem dethi = mylist.get(position);
        if (dethi == null) {
            return;
        }
        holder.maDe.setText(dethi.getMa_de());
        holder.ngayTao.setText(dethi.getNgay().isEmpty() ? "Chưa có ngày thi" : dethi.getNgay());
        holder.tenMon.setText(dethi.getMon_hoc());
        holder.thoiLuong.setText(dethi.getThoiluong() + " phút");
        holder.layout.setOnClickListener(v -> callback.deThiTraCuuCallBack(dethi));
    }

    @Override
    public int getItemCount() {
        return mylist != null ? mylist.size() : 0;
    }

    public static class DeThiViewHolder extends RecyclerView.ViewHolder {
        private TextView maDe;
        private TextView tenMon;
        private TextView ngayTao;
        private TextView thoiLuong;
        private ConstraintLayout layout;

        public DeThiViewHolder(@NonNull View itemView) {
            super(itemView);
            tenMon = itemView.findViewById(R.id.cs_lv_ten_mon);
            ngayTao = itemView.findViewById(R.id.cs_lv_ngay_thi);
            maDe = itemView.findViewById(R.id.cs_lv_ma_de);
            thoiLuong = itemView.findViewById(R.id.cs_lv_thoi_luong);
            layout = itemView.findViewById(R.id.tc_de_thi_custom_lv);
        }
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty())
                {
                    mylist = mylistOld;
                } else {
                    List<dethitracuuitem> filteredList = new ArrayList<>();
                    for (dethitracuuitem dethi : mylistOld)
                    {
                        if (dethi.getMa_de().toLowerCase().contains(strSearch.toLowerCase()))
                        {
                            filteredList.add(dethi);
                        }
                    }
                    mylist = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mylist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mylist = (ArrayList<dethitracuuitem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public Filter getFilterByAttribute(String attribute, String value) {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<dethitracuuitem> filteredList = new ArrayList<>();
                for (dethitracuuitem dethi : mylistOld) {
                    switch (attribute) {
                        case "maDe":
                            if (dethi.getMa_de().toLowerCase().contains(value.toLowerCase())) {
                                filteredList.add(dethi);
                            }
                            break;
                        case "hocKy":
                            if (dethi.getHocky().toLowerCase().contains(value.toLowerCase())) {
                                filteredList.add(dethi);
                            }
                            break;
                        case "monHoc":
                            if (dethi.getMon_hoc().toLowerCase().contains(value.toLowerCase())) {
                                filteredList.add(dethi);
                            }
                            break;
                        case "thoiLuong":
                            if (dethi.getThoiluong().equals(value)) {
                                filteredList.add(dethi);
                            }
                            break;
                        case "ngayThi":
                            if (dethi.getNgay().equals(value)) {
                                filteredList.add(dethi);
                            }
                            break;
                        case "nam":
                            String year = dethi.getNgay().split("/")[2];
                            if (year.equals(value)) {
                                filteredList.add(dethi);
                            }
                            break;
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mylist = (ArrayList<dethitracuuitem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void getFilterByMultipleAttributes(String maDe, String hocKy, String monHoc, String thoiLuong, String ngayThi, String nam)
    {
        Log.e("Đã vào filter", "OK");
        List<dethitracuuitem> filteredList = new ArrayList<>();
        for(int i = 0; i < my_list_main.size(); i++)
        {
            Log.e("MY LIST MAIN",my_list_main.get(i).toString());
        }
        for (dethitracuuitem dethi : my_list_main)
        {
            boolean matches = true;
            Log.e("DETHI",dethi.toString());
            if (maDe != null && !maDe.isEmpty() && !dethi.getMa_de().toLowerCase().equals(maDe.toLowerCase()))
            {
                Log.e("check", "1");
                matches = false;
            }
            if (hocKy != null && !hocKy.isEmpty() && !dethi.getHocky().toLowerCase().equals(hocKy.toLowerCase()))
            {
                Log.e("check", "2");
                matches = false;
            }
            if (monHoc != null && !monHoc.isEmpty() && !dethi.getMon_hoc().toLowerCase().equals(monHoc.toLowerCase()))
            {
                Log.e("check", "3");
                matches = false;
            }
            if (thoiLuong != null && !thoiLuong.isEmpty() && !dethi.getThoiluong().equals(thoiLuong))
            {
                Log.e("check", "4");
                matches = false;
            }
            if (ngayThi != null && !ngayThi.isEmpty() && !dethi.getNgay().equals(ngayThi))
            {
                Log.e("check", "5");
                matches = false;
            }
            if (nam != null && !nam.isEmpty())
            {
                if (!dethi.getNam1().equals(nam) && !dethi.getNam2().equals(nam))
                {
                    Log.e("check", "6");
                    matches = false;
                }
            }
            Log.e("------------","---------------------------------------------------------");
            if (matches)
            {
                filteredList.add(dethi);
            }
        }
        Log.e("Số phần tử", String.valueOf(filteredList.size()));
        for(int i = 0; i < filteredList.size(); i++)
        {
            Log.e("FILTERLIST",filteredList.get(i).toString());
        }
        mylistOld = filteredList;
        mylist =  filteredList;
        notifyDataSetChanged();
    }

}
