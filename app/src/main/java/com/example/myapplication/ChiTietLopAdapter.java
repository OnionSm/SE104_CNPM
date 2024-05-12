package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChiTietLopAdapter extends RecyclerView.Adapter<ChiTietLopAdapter.DsCTViewHolder>
{
    private ArrayList<CHITIETLOP> mylist;


    public ChiTietLopAdapter(ArrayList<CHITIETLOP> mylist)
    {
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public ChiTietLopAdapter.DsCTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chi_tiet_lop_recycle_view,parent,false);
        return new ChiTietLopAdapter.DsCTViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietLopAdapter.DsCTViewHolder holder, int position)
    {
        CHITIETLOP chitiet = mylist.get(position);
        if(chitiet == null)
        {
            return;
        }
        holder.masv.setText(chitiet.getMaSV());
        holder.hoten.setText(chitiet.getTenSV());
        holder.diem.setText(String.valueOf(chitiet.getDiem()));
        holder.diemchu.setText(chitiet.getDiemChu());
        holder.ghichu.setText(chitiet.getGhiChu());
    }

    @Override
    public int getItemCount()
    {
        if(mylist != null)
        {
            return mylist.size();
        }
        return 0;
    }

    public class DsCTViewHolder extends RecyclerView.ViewHolder
     {
        private EditText masv;
        private EditText hoten;
        private EditText diem;
        private EditText diemchu;
        private EditText ghichu;
         public DsCTViewHolder(@NonNull View itemView)
         {
             super(itemView);

             masv = itemView.findViewById(R.id.cham_thi_mssv_textview);
             hoten = itemView.findViewById(R.id.cham_thi_ho_va_ten_textview);
             diem = itemView.findViewById(R.id.cham_thi_diem_text);
             diemchu = itemView.findViewById(R.id.cham_thi_diem_chu_text);
             ghichu = itemView.findViewById(R.id.cham_thi_ghi_chu_text_view);
         }
     }
}
