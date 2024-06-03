package com.example.myapplication;

public class DETHICAUHOI
{
    private String key;
    private String maDT;
    private String maCH;

    public DETHICAUHOI(String key, String maDT, String maCH)
    {
        this.key = key;
        this.maDT = maDT;
        this.maCH = maCH;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMaDT() {
        return maDT;
    }

    public void setMaDT(String maDT) {
        this.maDT = maDT;
    }

    public String getMaCH() {
        return maCH;
    }

    public void setMaCH(String maCH) {
        this.maCH = maCH;
    }

    @Override
    public String toString()
    {
        return "DETHICAUHOI{" +
                "key='" + key + '\'' +
                ", maDT='" + maDT + '\'' +
                ", maCH='" + maCH + '\'' +
                '}';
    }
}
