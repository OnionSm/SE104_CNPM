package com.example.myapplication;

public class baocaomonhocitem
{
    private String  tenmon;
    private int soluongdethi;

    private int soluongbaicham;

    private float tiledethi;

    private float tilebaicham;



    public baocaomonhocitem(String tenmon, int soluongdethi, int soluongbaicham, float tiledethi, float tilebaicham)
    {
        this.tenmon = tenmon;
        this.soluongdethi = soluongdethi;
        this.soluongbaicham = soluongbaicham;
        this.tiledethi = tiledethi;
        this.tilebaicham = tilebaicham;
    }

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public int getSoluongdethi() {
        return soluongdethi;
    }

    public void setSoluongdethi(int soluongdethi) {
        this.soluongdethi = soluongdethi;
    }

    public int getSoluongbaicham() {
        return soluongbaicham;
    }

    public void setSoluongbaicham(int soluongbaicham) {
        this.soluongbaicham = soluongbaicham;
    }

    public float getTiledethi() {
        return tiledethi;
    }

    public void setTiledethi(float tiledethi) {
        this.tiledethi = tiledethi;
    }

    public float getTilebaicham() {
        return tilebaicham;
    }

    public void setTilebaicham(float tilebaicham) {
        this.tilebaicham = tilebaicham;
    }
}
