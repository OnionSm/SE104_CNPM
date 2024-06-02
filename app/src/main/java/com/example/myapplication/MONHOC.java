package com.example.myapplication;

public class MONHOC
{
    private String maMH;
    private String tenMH;
    private String moTaMH;
    public MONHOC()
    {

    }

    public MONHOC(String maMH, String tenMH, String moTaMH) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.moTaMH = moTaMH;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public String getMoTaMH() {
        return moTaMH;
    }

    public void setMoTaMH(String moTaMH) {
        this.moTaMH = moTaMH;
    }

    @Override
    public String toString() {
        return "com.example.myapplication.MONHOC{" +
                "maMH='" + maMH + '\'' +
                ", tenMH='" + tenMH + '\'' +
                ", moTaMH='" + moTaMH + '\'' +
                '}';
    }
}
