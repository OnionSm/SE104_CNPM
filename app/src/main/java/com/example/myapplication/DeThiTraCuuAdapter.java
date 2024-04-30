package com.example.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DeThiTraCuuAdapter extends ArrayAdapter<dethitracuuitem>
{
    Activity context;
    ArrayList<dethitracuuitem> mylist;

    public DeThiTraCuuAdapter(Activity context,int idlayout,ArrayList<dethitracuuitem> mylist)
    {
        super(context, idlayout, mylist);
        this.context = context;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.activity_tra_cuu_de_thi_list_view, null);

        dethitracuuitem dethi = mylist.get(position);

        TextView mon_hoc =  convertView.findViewById(R.id.cs_lv_ten_mon);
        TextView ngay =  convertView.findViewById(R.id.cs_lv_ngay);
        TextView ma_de =  convertView.findViewById(R.id.cs_lv_ma_de);



        mon_hoc.setText(dethi.getMon_hoc());
        ngay.setText(dethi.getNgay());
        ma_de.setText(dethi.getMa_de());

        return convertView;
    }
}
