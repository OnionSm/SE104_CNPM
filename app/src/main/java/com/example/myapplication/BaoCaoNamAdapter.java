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

import org.apache.poi.ss.formula.functions.T;

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
    public void onBindViewHolder(@NonNull BaoCaoNamAdapter.MonHocViewHolder holder, int position) {
        baocaomonhocitem monhoc = mylist.get(position);
        if (monhoc == null)
        {
            return;
        }
        holder.ten_mon_hoc.setText(monhoc.getTenmon());
        holder.sl_de_thi.setText("Số lượng đề thi: " + String.valueOf(monhoc.getSoluongdethi()));
        holder.sl_bai_cham.setText("Số lượng bài chấm: " + String.valueOf(monhoc.getSoluongbaicham()));
        holder.tldt.setText(String.valueOf(monhoc.getTiledethi()) + "%");
        holder.tlbc.setText(String.valueOf(monhoc.getTilebaicham()) + "%");

        // Rotate the progress bars to start from 12 o'clock
        holder.progress_bar_1.setRotation(-90);
        holder.progress_bar_2.setRotation(-90);

        // Set the max value for the progress bars
        holder.progress_bar_1.setMax(100); // Assuming the progress is percentage-based
        holder.progress_bar_2.setMax(100);

        // Initialize and start the countdown timer for progress_bar_1
        final CountDownTimer countDownTimer1 = new CountDownTimer(500, 5)
        {
            int current_progress1 = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                current_progress1 += (monhoc.getTiledethi() - current_progress1) / ((int) millisUntilFinished / 5 + 1);
                holder.progress_bar_1.setProgress(current_progress1);
            }

            @Override
            public void onFinish() {
                holder.progress_bar_1.setProgress(monhoc.getTiledethi());
            }
        };

        // Initialize and start the countdown timer for progress_bar_2
        final CountDownTimer countDownTimer2 = new CountDownTimer(500, 5) {
            int current_progress2 = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                current_progress2 += (monhoc.getTilebaicham() - current_progress2) / ((int) millisUntilFinished / 5 + 1);
                holder.progress_bar_2.setProgress(current_progress2);
            }

            @Override
            public void onFinish() {
                holder.progress_bar_2.setProgress(monhoc.getTilebaicham());
            }
        };

        countDownTimer1.start();
        countDownTimer2.start();
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

    public class MonHocViewHolder extends RecyclerView.ViewHolder
    {
        private TextView ten_mon_hoc;
        private TextView sl_de_thi;
        private TextView sl_bai_cham;
        private ProgressBar progress_bar_1;
        private ProgressBar progress_bar_2;
        private TextView tldt;
        private TextView tlbc;

        public MonHocViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ten_mon_hoc = itemView.findViewById(R.id.bao_cao_ten_mon);
            sl_de_thi = itemView.findViewById(R.id.bao_cao_so_luong_de_thi);
            sl_bai_cham = itemView.findViewById(R.id.bao_cao_so_luong_bai_cham);
            progress_bar_1 = itemView.findViewById(R.id.progressBar);
            progress_bar_2 = itemView.findViewById(R.id.progressBar2);
            tldt = itemView.findViewById(R.id.ti_le_de_thi);
            tlbc = itemView.findViewById(R.id.ti_le_bai_cham);
        }
    }
}
