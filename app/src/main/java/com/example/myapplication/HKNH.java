package com.example.myapplication;

import java.util.Date;

public class HKNH
{
    private String maHKNH;
    private int hocKy;
    private Date nam1;
    private Date nam2;

    public HKNH(String maHKNH, int hocKy, Date nam1, Date nam2) {
        this.maHKNH = maHKNH;
        this.hocKy = hocKy;
        this.nam1 = nam1;
        this.nam2 = nam2;
    }

    public String getMaHKNH() {
        return maHKNH;
    }

    public void setMaHKNH(String maHKNH) {
        this.maHKNH = maHKNH;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    public Date getNam1() {
        return nam1;
    }

    public void setNam1(Date nam1) {
        this.nam1 = nam1;
    }

    public Date getNam2() {
        return nam2;
    }

    public void setNam2(Date nam2) {
        this.nam2 = nam2;
    }

    @Override
    public String toString() {
        return "com.example.myapplication.HKNH{" +
                "maHKNH='" + maHKNH + '\'' +
                ", hocKy=" + hocKy +
                ", nam1=" + nam1 +
                ", nam2=" + nam2 +
                '}';
    }
}