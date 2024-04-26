package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CauHoiAdapter extends ArrayAdapter<monhocitem>
{
    Context context;
    ArrayList<monhocitem> mylist;
    LayoutInflater inflater;

    public CauHoiAdapter(@Nullable Context context,int idlayout, ArrayList<monhocitem> mylist)
    {
        super(context, idlayout, mylist);
        this.context = context;
        this.mylist = mylist;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        View rowView = inflater.inflate(R.layout.mon_hoc_item_drop, null, true);
        monhocitem monhoc  = mylist.get(position);
        TextView mon_hoc_text_view = convertView.findViewById(R.id.custom_spiner_mon_hoc);
        mon_hoc_text_view.setText(monhoc.getTen_mon_hoc());
        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        if(convertView == null)
            convertView = inflater.inflate(R.layout.mon_hoc_item_drop, parent, false);

        monhocitem monhoc  = mylist.get(position);
        TextView mon_hoc_text_view = convertView.findViewById(R.id.custom_spiner_mon_hoc);
        mon_hoc_text_view.setText(monhoc.getTen_mon_hoc());
        return convertView;
    }
}
