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

public class CustomListViewBase extends ArrayAdapter<cauhoiitem>
{
    Activity context;
    ArrayList<cauhoiitem> mylist;


    public CustomListViewBase(Activity context,int idlayout,ArrayList<cauhoiitem> mylist)
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
        convertView = inflater.inflate(R.layout.activity_custom_list_view, null);

        TextView stt_txt = convertView.findViewById(R.id.custom_list_view_stt);
        TextView mon_hoc_txt =  convertView.findViewById(R.id.custom_list_view_ten_mon);
        TextView mo_ta_txt = convertView.findViewById(R.id.custom_list_view_de_thi);
        TextView do_kho_txt = convertView.findViewById(R.id.custom_list_view_do_kho);
        TextView ngay_tao_txt =  convertView.findViewById(R.id.custom_list_view_ngay_tao);
        cauhoiitem cauhoi  = mylist.get(position);

        stt_txt.setText(cauhoi.getStt());
        mon_hoc_txt.setText(cauhoi.getMon_hoc());
        mo_ta_txt.setText(cauhoi.getMo_ta());
        do_kho_txt.setText(cauhoi.getDo_kho());
        ngay_tao_txt.setText(cauhoi.getNgay_tao());

        return convertView;
    }
}
