package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DeThiTraCuuAdapter extends RecyclerView.Adapter<DeThiTraCuuAdapter.DeThiViewHoder> implements Filterable
{

    private ArrayList<dethitracuuitem> mylist;
    private ArrayList<dethitracuuitem> mylist_old;
    private ArrayList<dethitracuuitem> mylist_filtered;
    private DeThiTraCuuClickCallBack callback;
    interface DeThiTraCuuClickCallBack
    {
        void deThiTraCuuCallBack(dethitracuuitem dethi);
    }
    public void SetListData(ArrayList<dethitracuuitem> data)
    {
        mylist_filtered = data;
    }

    public DeThiTraCuuAdapter(ArrayList<dethitracuuitem> mylist , DeThiTraCuuClickCallBack callback)
    {
        this.mylist = mylist;
        this.mylist_old = mylist;
        this.callback = callback;
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
        if(dethi.getNgay().equals(""))
        {
            holder.ngay_tao.setText("Chưa có ngày thi");
        }
        else
        {
            holder.ngay_tao.setText(dethi.getNgay());
        }
        holder.ten_mon.setText(dethi.getMon_hoc());
        holder.thoi_luong.setText(dethi.getThoiluong() + " phút");
        holder.layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.deThiTraCuuCallBack(dethi);
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

    public class DeThiViewHoder extends RecyclerView.ViewHolder
    {
        private TextView ma_de;
        private TextView ten_mon;

        private TextView ngay_tao;
        private TextView thoi_luong;
        private ConstraintLayout layout;
        public DeThiViewHoder(@NonNull View itemView)
        {
            super(itemView);
            ten_mon =  itemView.findViewById(R.id.cs_lv_ten_mon);
            ngay_tao =  itemView.findViewById(R.id.cs_lv_ngay_thi);
            ma_de =  itemView.findViewById(R.id.cs_lv_ma_de);
            thoi_luong = itemView.findViewById(R.id.cs_lv_thoi_luong);
            layout = itemView.findViewById(R.id.tc_de_thi_custom_lv);
        }
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

    public Filter getFilterMaDe()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                ArrayList<dethitracuuitem> list = new ArrayList<>();
                String str_search = constraint.toString().trim();
                if(str_search.isEmpty())
                {
                    mylist = mylist_old;
                }
                else
                {
                    for(dethitracuuitem dethi : mylist_old)
                    {
                        if(dethi.getMa_de().toLowerCase().contains(str_search.toLowerCase()))
                        {
                            list.add(dethi);
                        }
                    }
                    mylist = list;

                }
                mylist_filtered = mylist;
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
    public Filter getFilterHocKy()
    {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        DatabaseReference db_hknh = FirebaseDatabase.getInstance().getReference("HKNH");
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                ArrayList<dethitracuuitem> list = new ArrayList<>();
                String str_search = constraint.toString().trim();
                if(str_search.isEmpty())
                {
                    mylist = mylist_filtered;
                }
                else
                {
                    for(dethitracuuitem dethi : mylist_filtered)
                    {
                        db_dethi.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                String mahocky = snapshot.child(dethi.getMa_de()).child("maHKNH").getValue(String.class);
                                db_hknh.addValueEventListener(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot)
                                    {
                                        String hocky = String.valueOf(snapshot.child(mahocky).child("hocKy").getValue(Integer.class));
                                        if(hocky.toLowerCase().contains(str_search.toLowerCase()))
                                        {
                                            list.add(dethi);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    mylist = list;
                }
                mylist_filtered = mylist;
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

    public Filter getFilterMonHoc()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                ArrayList<dethitracuuitem> list = new ArrayList<>();
                String str_search = constraint.toString().trim();
                Log.e("Check môn", str_search);
                if(str_search.isEmpty())
                {
                    mylist = mylist_filtered;
                }
                else
                {
                    for(dethitracuuitem dethi : mylist_filtered)
                    {
                        Log.e("Tên môn", dethi.getMon_hoc());
                        if(dethi.getMon_hoc().toLowerCase().equals(str_search.toLowerCase()))
                        {
                            list.add(dethi);
                        }
                    }
                    mylist = list;
                }
                mylist_filtered = mylist;
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


    public Filter getFilterThoiLuong()
    {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                ArrayList<dethitracuuitem> list = new ArrayList<>();
                String str_search = constraint.toString().trim();
                if(str_search.isEmpty())
                {
                    mylist = mylist_filtered;
                }
                else
                {
                    for(dethitracuuitem dethi : mylist_filtered)
                    {
                        db_dethi.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                String thoiluong = String.valueOf(snapshot.child(dethi.getMa_de()).child("thoiLuong").getValue(Integer.class));
                                if(thoiluong.equals(str_search.toLowerCase()))
                                {
                                    list.add(dethi);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error)
                            {

                            }
                        });
                        if(dethi.getMon_hoc().toLowerCase().contains(str_search.toLowerCase()))
                        {
                            list.add(dethi);
                        }
                    }
                    mylist = list;
                }
                mylist_filtered = mylist;
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

    public Filter getFilterNgayThi()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                String str_search = constraint.toString().trim();
                ArrayList<dethitracuuitem> list = new ArrayList<>();
                Log.e("String search", str_search);
                for(dethitracuuitem dethi : mylist_filtered)
                {
                    if(dethi.getNgay().equals(str_search.toLowerCase()))
                    {
                        list.add(dethi);
                    }
                }
                mylist = list;
                mylist_filtered = mylist;
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

    public Filter getFilterNam()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                ArrayList<dethitracuuitem> list = new ArrayList<>();
                String str_search = constraint.toString().trim();
                if(str_search.isEmpty())
                {
                    mylist = mylist_old;
                }
                else
                {
                    for(dethitracuuitem dethi : mylist_filtered)
                    {
                        String nam = dethi.getNgay().split("/")[2];
                        Log.e("năm", nam);
                        if(dethi.getNgay().toLowerCase().equals(str_search.toLowerCase()))
                        {
                            list.add(dethi);
                        }
                    }
                    mylist = list;
                }
                mylist_filtered = mylist;
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

