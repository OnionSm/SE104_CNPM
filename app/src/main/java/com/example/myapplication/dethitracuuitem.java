package com.example.myapplication;

public class dethitracuuitem
{
    private String mon_hoc;
    private String ngay;
    private String ma_de;

    public dethitracuuitem(String mon_hoc, String ngay, String ma_de) {
        this.mon_hoc = mon_hoc;
        this.ngay = ngay;
        this.ma_de = ma_de;
    }

    public String getMon_hoc() {
        return mon_hoc;
    }

    public void setMon_hoc(String mon_hoc) {
        this.mon_hoc = mon_hoc;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getMa_de() {
        return ma_de;
    }

    public void setMa_de(String ma_de) {
        this.ma_de = ma_de;
    }
}
