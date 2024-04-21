package com.example.myapplication;

public class LOP
{
    private String maLop;
    private String tenLop;
    private String maHKNH;
    private int siSo;
    private String maMH;
    private String maGVCham;

    public LOP(String maLop, String tenLop, String maHKNH, int siSo, String maMH, String maGVCham) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maHKNH = maHKNH;
        this.siSo = siSo;
        this.maMH = maMH;
        this.maGVCham = maGVCham;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getMaHKNH() {
        return maHKNH;
    }

    public void setMaHKNH(String maHKNH) {
        this.maHKNH = maHKNH;
    }

    public int getSiSo() {
        return siSo;
    }

    public void setSiSo(int siSo) {
        this.siSo = siSo;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getMaGVCham() {
        return maGVCham;
    }

    public void setMaGVCham(String maGVCham) {
        this.maGVCham = maGVCham;
    }

    @Override
    public String toString() {
        return "com.example.myapplication.LOP{" +
                "maLop='" + maLop + '\'' +
                ", tenLop='" + tenLop + '\'' +
                ", maHKNH='" + maHKNH + '\'' +
                ", siSo=" + siSo +
                ", maMH='" + maMH + '\'' +
                ", maGVCham='" + maGVCham + '\'' +
                '}';
    }
}
