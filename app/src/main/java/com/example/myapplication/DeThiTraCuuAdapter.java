package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
                String str_search = constraint.toString();
                if(str_search.isEmpty())
                {
                    mylist = mylist_filtered;
                }
                else
                {
                    ArrayList<dethitracuuitem> list = new ArrayList<>();
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
                                        String hocky = snapshot.child(mahocky).child("hocKy").getValue(String.class);
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
                String str_search = constraint.toString();
                if(str_search.isEmpty())
                {
                    mylist = mylist_filtered;
                }
                else
                {
                    ArrayList<dethitracuuitem> list = new ArrayList<>();
                    for(dethitracuuitem dethi : mylist_filtered)
                    {
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


    public Filter getFilterThoiLuong()
    {
        DatabaseReference db_dethi = FirebaseDatabase.getInstance().getReference("DETHI");
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                String str_search = constraint.toString();
                if(str_search.isEmpty())
                {
                    mylist = mylist_filtered;
                }
                else
                {
                    ArrayList<dethitracuuitem> list = new ArrayList<>();
                    for(dethitracuuitem dethi : mylist_filtered)
                    {
                        db_dethi.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                String thoiluong = snapshot.child(dethi.getMa_de()).child("thoiLuong").getValue(String.class);
                                if(thoiluong.toLowerCase().contains(str_search.toLowerCase()))
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
                String str_search = constraint.toString();
                if(str_search.isEmpty())
                {
                    mylist = mylist_filtered;
                }
                else
                {
                    ArrayList<dethitracuuitem> list = new ArrayList<>();
                    for(dethitracuuitem dethi : mylist_filtered)
                    {
                        if(dethi.getNgay().toLowerCase().contains(str_search.toLowerCase()))
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

    public Filter getFilterNam()
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
                    for(dethitracuuitem dethi : mylist_filtered)
                    {
                        String nam = dethi.getNgay().split("/")[2];
                        Log.e("năm", nam);
                        if(dethi.getNgay().toLowerCase().contains(str_search.toLowerCase()))
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

