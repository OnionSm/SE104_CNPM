package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        // sẽ sửa khi có màn hình

       /* View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_list_view,parent,false);
        return new DsCauHoiDaTaoAdapter.DsViewHoder(view);*/

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietLopAdapter.DsCTViewHolder holder, int position)
    {
        // sẽ sửa khi có màn hình

        /*cauhoiitem cauhoi = mylist.get(position);
        if(cauhoi == null)
        {
            return;
        }
        holder.stt.setText(cauhoi.getStt());
        holder.monhoc.setText(cauhoi.getMon_hoc());
        holder.noidung.setText(cauhoi.getMo_ta());
        holder.dokho.setText(cauhoi.getDo_kho());
        holder.ngaytao.setText(cauhoi.getNgay_tao());*/
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

         // sẽ sửa sau khi có màn hình

         /*private TextView stt;
         private TextView monhoc;
         private TextView noidung;

         private TextView dokho;

         private TextView ngaytao;*/
         public DsCTViewHolder(@NonNull View itemView)
         {
             super(itemView);

             // sẽ sửa sau khi có màn hình

             /*stt =  itemView.findViewById(R.id.custom_list_view_stt);
             monhoc =  itemView.findViewById(R.id.custom_list_view_ten_mon);
             noidung =  itemView.findViewById(R.id.custom_list_view_de_thi);
             dokho =  itemView.findViewById(R.id.custom_list_view_do_kho);
             ngaytao =  itemView.findViewById(R.id.custom_list_view_ngay_tao);*/
         }
     }
}
