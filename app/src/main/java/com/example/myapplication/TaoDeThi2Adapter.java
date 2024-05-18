package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaoDeThi2Adapter extends RecyclerView.Adapter<TaoDeThi2Adapter.CauHoiViewHolder>
{
    private ArrayList<taodethicauhoiitem> mylist;
    private IClickUpdateCauHoiDaChon iclickupdate;
    private IClickDeleteCauHoiDaChon iclickdelete;
    public interface IClickUpdateCauHoiDaChon
    {
        void UpdateCauHoi(taodethicauhoiitem cauhoi);
    }
    public interface IClickDeleteCauHoiDaChon
    {
        void DeleteCauHoi(taodethicauhoiitem cauhoi);
    }


    public TaoDeThi2Adapter(ArrayList<taodethicauhoiitem> mylist, IClickUpdateCauHoiDaChon iclickupdate, IClickDeleteCauHoiDaChon iclickdelete)
    {
        this.mylist = mylist;
        this.iclickupdate = iclickupdate;
        this.iclickdelete = iclickdelete;
    }

    @NonNull
    @Override
    public TaoDeThi2Adapter.CauHoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_tao_de_thi_cau_hoi_da_tao,parent,false);
        return new TaoDeThi2Adapter.CauHoiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CauHoiViewHolder holder, int position)
    {
        taodethicauhoiitem cauhoi = mylist.get(position);
        if(cauhoi==null)
        {
            return;
        }
        holder.mach.setText("Mã câu hỏi: "+ cauhoi.getMacauhoi());
        holder.nd.setText(cauhoi.getNoidung());
        holder.update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                iclickupdate.UpdateCauHoi(cauhoi);
            }
        });
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickdelete.DeleteCauHoi(cauhoi);
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

    public class CauHoiViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mach;
        private TextView nd;
        private ImageButton update_button;
        private ImageButton delete_button;

        public CauHoiViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mach = itemView.findViewById(R.id.ma_cau_hoi);
            nd = itemView.findViewById(R.id.noi_dung_cau_hoi);
            update_button = itemView.findViewById(R.id.sua_button);
            delete_button = itemView.findViewById(R.id.xoa_button);
        }
    }
}
