package com.example.myapplication;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BaoCaoNamAdapter extends RecyclerView.Adapter<BaoCaoNamAdapter.MonHocViewHolder>
{
    ArrayList<baocaomonhocitem> mylist;

    public BaoCaoNamAdapter(ArrayList<baocaomonhocitem> mylist)
    {
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public BaoCaoNamAdapter.MonHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bao_cao_recycle_item,parent,false);
        return new BaoCaoNamAdapter.MonHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonHocViewHolder holder, int position)
    {
        baocaomonhocitem monhoc = mylist.get(position);
        if(monhoc==null)
        {
            return;
        }
        holder.ten_mon_hoc.setText(monhoc.getTenmon());
        holder.sl_de_thi.setText(monhoc.getSoluongdethi());
        holder.sl_bai_cham.setText(monhoc.getSoluongbaicham());

        final CountDownTimer countDownTimer1 = new CountDownTimer(500, 5)
        {
            int current_progress = 0;
            @Override
            public void onTick(long millisUntilFinished)
            {
                current_progress = Math.round(current_progress + (monhoc.getTiledethi() / 100));
                holder.progress_bar_1.setProgress(current_progress);
                holder.progress_bar_1.setMax(Math.round(monhoc.getTiledethi()));
            }

            @Override
            public void onFinish()
            {

            }
        };
        final CountDownTimer countDownTimer2 = new CountDownTimer(500, 5)
        {
            int current_progress = 0;
            @Override
            public void onTick(long millisUntilFinished)
            {
                current_progress = Math.round(current_progress + (monhoc.getTilebaicham() / 100));
                holder.progress_bar_2.setProgress(current_progress);
                holder.progress_bar_2.setMax(Math.round(monhoc.getTilebaicham()));
            }

            @Override
            public void onFinish()
            {

            }
        };
        countDownTimer1.start();
        countDownTimer2.start();
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

    public class MonHocViewHolder extends RecyclerView.ViewHolder
    {
        private TextView ten_mon_hoc;
        private TextView sl_de_thi;
        private TextView sl_bai_cham;
        private ProgressBar progress_bar_1;
        private ProgressBar progress_bar_2;

        public MonHocViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ten_mon_hoc = itemView.findViewById(R.id.ten_mon);
            sl_de_thi = itemView.findViewById(R.id.so_luong_de_thi);
            sl_bai_cham = itemView.findViewById(R.id.so_luong_bai_cham);
            progress_bar_1 = itemView.findViewById(R.id.progressBar);
            progress_bar_2 = itemView.findViewById(R.id.progressBar2);
        }
    }
}
