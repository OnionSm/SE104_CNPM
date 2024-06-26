package com.example.myapplication;

import java.util.Date;

public class DETHI
{
    private String maDT;
    private int thoiLuong;
    private String maHKNH;
    private String ngayThi;
    private String maMH;
    private String maGV;
    public DETHI()
    {

    }


    public DETHI(String maDT, int thoiLuong, String maHKNH, String ngayThi, String maMH, String maGV) {
        this.maDT = maDT;
        this.thoiLuong = thoiLuong;
        this.maHKNH = maHKNH;
        this.ngayThi = ngayThi;
        this.maMH = maMH;
        this.maGV = maGV;
    }

    public String getMaDT() {
        return maDT;
    }

    public void setMaDT(String maDT) {
        this.maDT = maDT;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getMaHKNH() {
        return maHKNH;
    }

    public void setMaHKNH(String maHKNH) {
        this.maHKNH = maHKNH;
    }

    public String getNgayThi() {
        return ngayThi;
    }

    public void setNgayThi(String ngayThi) {
        this.ngayThi = ngayThi;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    @Override
    public String toString() {
        return "com.example.myapplication.DETHI{" +
                "maDT='" + maDT + '\'' +
                ", thoiLuong=" + thoiLuong +
                ", maHKNH='" + maHKNH + '\'' +
                ", ngayThi=" + ngayThi +
                ", maMH='" + maMH + '\'' +
                ", maGV='" + maGV + '\'' +
                '}';
    }
}
