package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChiTietDeThiAdapter extends RecyclerView.Adapter<ChiTietDeThiAdapter.DsDeThiViewHolder>
{
    private ArrayList<cauhoiitem> mylist;

    public ChiTietDeThiAdapter(ArrayList<cauhoiitem> mylist) {
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public ChiTietDeThiAdapter.DsDeThiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chi_tiet_cau_hoi_item, parent, false);
        return new ChiTietDeThiAdapter.DsDeThiViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ChiTietDeThiAdapter.DsDeThiViewHolder holder, int position) {
        cauhoiitem cauhoi = mylist.get(position);
        if (cauhoi == null) {
            return;
        }
        holder.stt.setText(cauhoi.getStt());
        holder.monhoc.setText(cauhoi.getMon_hoc());
        holder.noidung.setText(cauhoi.getMo_ta());
        holder.dokho.setText(cauhoi.getDo_kho());
        holder.ngaytao.setText(cauhoi.getNgay_tao());
    }

    @Override
    public int getItemCount() {
        if (mylist != null) {
            return mylist.size();
        }
        return 0;
    }

    public class DsDeThiViewHolder extends RecyclerView.ViewHolder
    {
        private TextView stt;
        private TextView monhoc;
        private TextView noidung;
        private TextView dokho;
        private TextView ngaytao;
        private ConstraintLayout layout;

        public DsDeThiViewHolder(@NonNull View itemView)
        {
            super(itemView);
            stt = itemView.findViewById(R.id.custom_list_view_stt);
            monhoc = itemView.findViewById(R.id.custom_list_view_ten_mon);
            noidung = itemView.findViewById(R.id.custom_list_view_de_thi);
            dokho = itemView.findViewById(R.id.custom_list_view_do_kho);
            ngaytao = itemView.findViewById(R.id.custom_list_view_ngay_tao);
            layout = itemView.findViewById(R.id.custom_list_view);

        }
    }
}
