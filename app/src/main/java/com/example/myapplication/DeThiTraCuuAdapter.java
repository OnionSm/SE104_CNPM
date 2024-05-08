package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class DeThiTraCuuAdapter extends RecyclerView.Adapter<DeThiTraCuuAdapter.DeThiViewHoder> implements Filterable
{

    private ArrayList<dethitracuuitem> mylist;
    private ArrayList<dethitracuuitem> mylist_old;

    public DeThiTraCuuAdapter(ArrayList<dethitracuuitem> mylist)
    {
        this.mylist = mylist;
        this.mylist_old = mylist;
    }

    @NonNull
    @Override
    public DeThiViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_tra_cuu_de_thi_list_view,parent,false);
        return new DeThiViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeThiViewHoder holder, int position)
    {
        dethitracuuitem dethi = mylist.get(position);
        if(dethi == null)
        {
            return;
        }
        holder.ma_de.setText(dethi.getMa_de());
        holder.ngay_tao.setText(dethi.getNgay());
        holder.ten_mon.setText(dethi.getMon_hoc());
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



    public class DeThiViewHoder extends RecyclerView.ViewHolder
    {
        private TextView ma_de;
        private TextView ten_mon;

        private TextView ngay_tao;
        public DeThiViewHoder(@NonNull View itemView)
        {
            super(itemView);
            ten_mon =  itemView.findViewById(R.id.cs_lv_ten_mon);
            ngay_tao =  itemView.findViewById(R.id.cs_lv_ngay);
            ma_de =  itemView.findViewById(R.id.cs_lv_ma_de);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
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
                    ArrayList<dethitracuuitem> list = new ArrayList<>();
                    for(dethitracuuitem dethi : mylist_old)
                    {
                        if(dethi.getMa_de().toLowerCase().contains(str_search.toLowerCase()))
                        {
                            list.add(dethi);
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
                mylist = (ArrayList<dethitracuuitem>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

