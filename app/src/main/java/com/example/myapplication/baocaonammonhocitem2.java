package com.example.myapplication;

public class baocaonammonhocitem2
{
    private String tenmon;
    private int soluongdethi;
    private int soluongbaicham;
    private int tiledethi;
    private int tilebaicham;

    public baocaonammonhocitem2(String tenmon, int soluongdethi, int soluongbaicham, int tiledethi, int tilebaicham) {
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

    public int getTiledethi() {
        return tiledethi;
    }

    public void setTiledethi(int tiledethi) {
        this.tiledethi = tiledethi;
    }

    public int getTilebaicham() {
        return tilebaicham;
    }

    public void setTilebaicham(int tilebaicham) {
        this.tilebaicham = tilebaicham;
    }
}
