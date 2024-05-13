package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my_interface.IClickCauHoiItemListener;

public class TaoDeThiAdapter extends RecyclerView.Adapter<TaoDeThiAdapter.CauHoiViewHolder> implements Filterable
{
    private ArrayList<taodethicauhoiitem> mylist;
    private ArrayList<taodethicauhoiitem> mylist_old;

    private IClickCauHoiItemListener iClickCauHoiItemListener;

    public TaoDeThiAdapter(ArrayList<taodethicauhoiitem> mylist, IClickCauHoiItemListener iClickCauHoiItemListener)
    {
        this.mylist = mylist;
        this.mylist_old = mylist;
        this.iClickCauHoiItemListener = iClickCauHoiItemListener;
    }

    @NonNull
    @Override
    public TaoDeThiAdapter.CauHoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_tao_cau_hoi_cau_hoi_item,parent,false);
        return new TaoDeThiAdapter.CauHoiViewHolder(view);
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
        holder.layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                iClickCauHoiItemListener.onClickItemCauHoi(cauhoi);
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

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                String str_search = constraint.toString();
                if(str_search.isEmpty())
                {
                    mylist = mylist_old;
                }
                else
                {
                    ArrayList<taodethicauhoiitem> list = new ArrayList<>();
                    for(taodethicauhoiitem cauhoi : mylist_old)
                    {
                        if(cauhoi.getNoidung().toLowerCase().contains(str_search.toLowerCase()))
                        {
                            list.add(cauhoi);
                        }
                    }
                    mylist = list;
                }
                FilterResults filter_results = new FilterResults();
                filter_results.values = mylist;
                return filter_results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                mylist = (ArrayList<taodethicauhoiitem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class CauHoiViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mach;
        private TextView nd;
        private ConstraintLayout layout;
        public CauHoiViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mach = itemView.findViewById(R.id.ma_cau_hoi);
            nd = itemView.findViewById(R.id.noi_dung_cau_hoi);
            layout = itemView.findViewById(R.id.taocauhoiitem);
        }
    }
}
