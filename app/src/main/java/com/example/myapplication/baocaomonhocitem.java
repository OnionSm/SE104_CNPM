package com.example.myapplication;

public class baocaomonhocitem
{
    private String  tenmon;
    private int soluongdethi;

    private int soluongbaicham;

    private int tiledetho;

    private int tilebaicham;

    public baocaomonhocitem(String tenmon, int soluongdethi, int soluongbaicham, int tiledetho, int tilebaicham)
    {
        this.tenmon = tenmon;
        this.soluongdethi = soluongdethi;
        this.soluongbaicham = soluongbaicham;
        this.tiledetho = tiledetho;
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

    public int getTiledetho() {
        return tiledetho;
    }

    public void setTiledetho(int tiledetho) {
        this.tiledetho = tiledetho;
    }

    public int getTilebaicham() {
        return tilebaicham;
    }

    public void setTilebaicham(int tilebaicham) {
        this.tilebaicham = tilebaicham;
    }
}
