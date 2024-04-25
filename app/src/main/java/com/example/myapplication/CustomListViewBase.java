package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListViewBase extends BaseAdapter
{
    private Context context;
    private String mon_hoc[];
    private String mo_ta[];
    private String do_kho[];
    private String ngay_tao[];

    private LayoutInflater inflater;

    public CustomListViewBase(Context ctx, String mon_hoc[], String mota[], String do_kho[], String ngay_tao[])
    {
        this.context = ctx;
        this.mon_hoc = mon_hoc;
        this.mo_ta = mota;
        this.do_kho = do_kho;
        this.ngay_tao = ngay_tao;
        inflater = LayoutInflater.from(ctx);

    }

    @Override
    public int getCount()
    {
        return mon_hoc.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView  = inflater.inflate(R.layout.activity_custom_list_view, null);
        TextView stt_txt = (TextView) convertView.findViewById(R.id.custom_list_view_stt);
        TextView mon_hoc_txt = (TextView) convertView.findViewById(R.id.custom_list_view_ten_mon);
        TextView mo_ta_txt = (TextView) convertView.findViewById(R.id.custom_list_view_de_thi);
        TextView do_kho_txt = (TextView) convertView.findViewById(R.id.custom_list_view_do_kho);
        TextView ngay_tao_txt = (TextView) convertView.findViewById(R.id.custom_list_view_ngay_tao);
        stt_txt.setText(position+1);
        mon_hoc_txt.setText(mon_hoc[position]);
        mo_ta_txt.setText(mo_ta[position]);
        do_kho_txt.setText(do_kho[position]);
        ngay_tao_txt.setText(ngay_tao[position]);
        return convertView;
    }
}
