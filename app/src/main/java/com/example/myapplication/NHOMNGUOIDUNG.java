package com.example.myapplication;

public class NHOMNGUOIDUNG
{
    private String maNhomND;
    private String tenNhomND;

    public NHOMNGUOIDUNG(String maNhomND, String tenNhomND)
    {
        this.maNhomND = maNhomND;
        this.tenNhomND = tenNhomND;
    }

    public String getMaNhomND() {
        return maNhomND;
    }

    public void setMaNhomND(String maNhomND) {
        this.maNhomND = maNhomND;
    }

    public String getTenNhomND() {
        return tenNhomND;
    }

    public void setTenNhomND(String tenNhomND) {
        this.tenNhomND = tenNhomND;
    }

    @Override
    public String toString() {
        return "com.example.myapplication.NHOMNGUOIDUNG{" +
                "maNhomND='" + maNhomND + '\'' +
                ", tenNhomND='" + tenNhomND + '\'' +
                '}';
    }
}
