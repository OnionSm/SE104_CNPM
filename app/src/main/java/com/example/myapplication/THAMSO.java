package com.example.myapplication;

public class THAMSO
{
    private String tenThamSo;
    private int giaTri;

    public THAMSO(String tenThamSo, int giaTri) {
        this.tenThamSo = tenThamSo;
        this.giaTri = giaTri;
    }

    public String getTenThamSo() {
        return tenThamSo;
    }

    public void setTenThamSo(String tenThamSo) {
        this.tenThamSo = tenThamSo;
    }

    public int getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(int giaTri) {
        this.giaTri = giaTri;
    }

    @Override
    public String toString() {
        return "com.example.myapplication.THAMSO{" +
                "tenThamSo='" + tenThamSo + '\'' +
                ", giaTri=" + giaTri +
                '}';
    }
}
