package com.example.myapplication;

public class CAUHOI {
    private String maCH;
    private String maDoKho;
    private String noiDung;
    private String maMH;
    private String maGV;

    public CAUHOI(String maCH, String maDoKho, String noiDung, String maMH, String maGV) {
        this.maCH = maCH;
        this.maDoKho = maDoKho;
        this.noiDung = noiDung;
        this.maMH = maMH;
        this.maGV = maGV;
    }

    public String getMaCH() {
        return maCH;
    }

    public void setMaCH(String maCH) {
        this.maCH = maCH;
    }

    public String getMaDoKho() {
        return maDoKho;
    }

    public void setMaDoKho(String maDoKho) {
        this.maDoKho = maDoKho;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
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
        return "com.example.myapplication.CAUHOI{" +
                "maCH='" + maCH + '\'' +
                ", maDoKho='" + maDoKho + '\'' +
                ", noiDung='" + noiDung + '\'' +
                ", maMH='" + maMH + '\'' +
                ", maGV='" + maGV + '\'' +
                '}';
    }
}
