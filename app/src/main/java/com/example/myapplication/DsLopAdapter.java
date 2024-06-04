package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DsLopAdapter extends RecyclerView.Adapter<DsLopAdapter.LopViewHolder>
{
    private ArrayList<lopitemrcv> mylist;
    private DanhSachLopScreen.OnClickLopHocListener callback;


    public DsLopAdapter(ArrayList<lopitemrcv> mylist, DanhSachLopScreen.OnClickLopHocListener callback)
    {
        this.mylist = mylist;
        this.callback = callback;
    }

    @NonNull
    @Override
    public LopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.them_lop_item_rcv, parent, false);
        return new DsLopAdapter.LopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LopViewHolder holder, int position)
    {
        lopitemrcv lop = mylist.get(position);
        if(lop == null)
        {
            return;
        }
        holder.mamh.setText(lop.getMaMH());
        holder.malop.setText(lop.getMaLop());
        holder.tenlop.setText("Tên lớp: "+ lop.getTenLop());
        holder.hocky.setText("Học kỳ: " +lop.getHocky());
        holder.namhoc.setText("Năm học: " +lop.getNamhoc());
        holder.siso.setText("Sỉ số: "+lop.getSiSo());
        holder.magvcham.setText("Giáo viên chấm: "+lop.getMaGVCham());
        holder.layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.onClickCallBack(lop);
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

    public class LopViewHolder extends RecyclerView.ViewHolder
    {
        private TextView malop;
        private TextView tenlop;
        private TextView hocky;
        private TextView namhoc;
        private TextView siso;
        private TextView magvcham;
        private TextView mamh;
        private ConstraintLayout layout;

        public LopViewHolder(@NonNull View itemView)
        {
            super(itemView);
            malop = itemView.findViewById(R.id.malop);
            tenlop = itemView.findViewById(R.id.tenlop);
            hocky = itemView.findViewById(R.id.hocky);
            namhoc = itemView.findViewById(R.id.namhoc);
            siso = itemView.findViewById(R.id.siso);
            magvcham = itemView.findViewById(R.id.magvcham);
            mamh = itemView.findViewById(R.id.mamonhoc);
            layout = itemView.findViewById(R.id.lop_item);
        }
    }
}

