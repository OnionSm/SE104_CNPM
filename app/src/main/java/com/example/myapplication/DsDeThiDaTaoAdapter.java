package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DsDeThiDaTaoAdapter extends RecyclerView.Adapter<DsDeThiDaTaoAdapter.DsDeThiViewHoder>
{
    private ArrayList<dethidataoitem> mylist;
    private DsDeThiClickCallBack callback;
    interface DsDeThiClickCallBack
    {
        void dsDeThiCallBack(dethidataoitem dethi);
    }

    public DsDeThiDaTaoAdapter(ArrayList<dethidataoitem> mylist, DsDeThiClickCallBack callback)
    {
        this.mylist = mylist;
        this.callback = callback;
    }

    @NonNull
    @Override
    public DsDeThiDaTaoAdapter.DsDeThiViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ds_de_thi_da_tao_item,parent,false);
        return new DsDeThiDaTaoAdapter.DsDeThiViewHoder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull DsDeThiDaTaoAdapter.DsDeThiViewHoder holder, int position)
    {
        dethidataoitem dethi = mylist.get(position);
        if(dethi == null)
        {
            return;
        }
        holder.stt.setText(dethi.getStt());
        holder.monhoc.setText(dethi.getTenmon());
        holder.hocky.setText("Học kỳ: " + dethi.getHocky());
        holder.namhoc.setText("Năm học: " +dethi.getNamhoc());
        holder.made.setText(dethi.getMade());
        holder.thoiluong.setText(dethi.getThoiluong() + " phút");
        if(dethi.getNgaytao().equals(""))
        {
            holder.ngaytao.setText("Chưa có ngày thi");
        }
        else
        {
            holder.ngaytao.setText(dethi.getNgaytao());
        }
        holder.layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.dsDeThiCallBack(dethi);
            }
        });

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



    public class DsDeThiViewHoder extends RecyclerView.ViewHolder
    {
        private TextView stt;
        private TextView monhoc;
        private TextView hocky;

        private TextView namhoc;

        private TextView made;
        private TextView thoiluong;
        private TextView ngaytao;
        private ConstraintLayout layout;

        public DsDeThiViewHoder(@NonNull View itemView)
        {
            super(itemView);
            stt =  itemView.findViewById(R.id.custom_list_view_stt);
            monhoc =  itemView.findViewById(R.id.custom_list_view_ten_mon);
            hocky =  itemView.findViewById(R.id.custom_list_view_hoc_ky);
            namhoc =  itemView.findViewById(R.id.custom_list_view_nam_hoc);
            made =  itemView.findViewById(R.id.custom_list_view_ma_de);
            thoiluong =  itemView.findViewById(R.id.custom_list_view_thoi_luong);
            ngaytao =  itemView.findViewById(R.id.custom_list_view_ngay_tao);
            layout = itemView.findViewById(R.id.main);
        }
    }

}
