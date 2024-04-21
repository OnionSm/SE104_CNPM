package com.example.myapplication;

public class BAOCAONAM
{
    private String maBCNam;
    private String maHKNH;
    private String maGV;
    private int tongSoDeThi;
    private int tongSoBaiCham;

    public BAOCAONAM(String maBCNam, String maHKNH, String maGV, int tongSoDeThi, int tongSoBaiCham) {
        this.maBCNam = maBCNam;
        this.maHKNH = maHKNH;
        this.maGV = maGV;
        this.tongSoDeThi = tongSoDeThi;
        this.tongSoBaiCham = tongSoBaiCham;
    }

    public String getMaBCNam() {
        return maBCNam;
    }

    public void setMaBCNam(String maBCNam) {
        this.maBCNam = maBCNam;
    }

    public String getMaHKNH() {
        return maHKNH;
    }

    public void setMaHKNH(String maHKNH) {
        this.maHKNH = maHKNH;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public int getTongSoDeThi() {
        return tongSoDeThi;
    }

    public void setTongSoDeThi(int tongSoDeThi) {
        this.tongSoDeThi = tongSoDeThi;
    }

    public int getTongSoBaiCham() {
        return tongSoBaiCham;
    }

    public void setTongSoBaiCham(int tongSoBaiCham) {
        this.tongSoBaiCham = tongSoBaiCham;
    }

    @Override
    public String toString() {
        return "com.example.myapplication.BAOCAONAM{" +
                "maBCNam='" + maBCNam + '\'' +
                ", maHKNH='" + maHKNH + '\'' +
                ", maGV='" + maGV + '\'' +
                ", tongSoDeThi=" + tongSoDeThi +
                ", tongSoBaiCham=" + tongSoBaiCham +
                '}';
    }
}
