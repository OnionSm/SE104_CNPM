package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;

public class DsMonHocAdapter extends RecyclerView.Adapter<DsMonHocAdapter.MonHocViewHolder>
{
    private ArrayList<MONHOC> mylist;
    private MonHocScreen.OnClickMonHocCallback callback;

    public DsMonHocAdapter(ArrayList<MONHOC> mylist, MonHocScreen.OnClickMonHocCallback callback)
    {
        this.mylist = mylist;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MonHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mon_hoc_item_rcv, parent, false);
        return new DsMonHocAdapter.MonHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonHocViewHolder holder, int position)
    {
        MONHOC monhoc = mylist.get(position);
        if(monhoc == null)
        {
            return;
        }
        holder.monhoc_text.setText(monhoc.getTenMH());
        holder.mamon_text.setText(monhoc.getMaMH());
        holder.noi_dung_text.setText(monhoc.getMoTaMH());
        holder.layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.onClickMonHocCallback(monhoc);
            }
        });
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
        private TextView monhoc_text;
        private TextView mamon_text;
        private TextView noi_dung_text;
        private ConstraintLayout layout;

        public MonHocViewHolder(@NonNull View itemView)
        {
            super(itemView);
            monhoc_text = itemView.findViewById(R.id.custom_list_view_ten_mon);
            mamon_text = itemView.findViewById(R.id.custom_list_view_ma_mh);
            noi_dung_text = itemView.findViewById(R.id.custom_list_view_noi_dung);
            layout = itemView.findViewById(R.id.mon_hoc_rcv);
        }
    }

}
