package com.example.myapplication;

public class CAUHOI {
    private String maCH;
    private String maDoKho;
    private String noiDung;
    private String maMH;
    private String maGV;

    private String ngaytao;

    public CAUHOI(String maCH, String maDoKho, String noiDung, String maMH, String maGV, String ngaytao)
    {
        this.maCH = maCH;
        this.maDoKho = maDoKho;
        this.noiDung = noiDung;
        this.maMH = maMH;
        this.maGV = maGV;
        this.ngaytao = ngaytao;
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

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }

    @Override
    public String toString() {
        return "CAUHOI{" +
                "maCH='" + maCH + '\'' +
                ", maDoKho='" + maDoKho + '\'' +
                ", noiDung='" + noiDung + '\'' +
                ", maMH='" + maMH + '\'' +
                ", maGV='" + maGV + '\'' +
                ", ngaytao='" + ngaytao + '\'' +
                '}';
    }
}
