package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DsDoKhoAdapter extends RecyclerView.Adapter<DsDoKhoAdapter.DoKhoViewHolder>
{
    private ArrayList<DOKHO> mylist;

    public DsDoKhoAdapter(ArrayList<DOKHO> mylist)
    {
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public DoKhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.do_kho_item_rcv, parent, false);
        return new DsDoKhoAdapter.DoKhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoKhoViewHolder holder, int position)
    {
        DOKHO dokho = mylist.get(position);
        if(dokho == null)
        {
            return;
        }
        holder.madokho_tv.setText("Mã độ khó: "+dokho.getMaDoKho());
        holder.tendokho_tv.setText("Tên độ khó: "+ dokho.getTenDK());
    }

    @Override
    public int getItemCount()
    {
        if(mylist == null)
        {
            return 0;
        }
        return mylist.size();
    }

    public class DoKhoViewHolder extends RecyclerView.ViewHolder
    {
        private TextView madokho_tv;
        private TextView tendokho_tv;

        public DoKhoViewHolder(@NonNull View itemView)
        {
            super(itemView);
            madokho_tv = itemView.findViewById(R.id.ma_do_kho);
            tendokho_tv = itemView.findViewById(R.id.ten_do_kho);

        }
    }
}
