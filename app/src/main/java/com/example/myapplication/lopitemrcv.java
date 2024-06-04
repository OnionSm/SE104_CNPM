package com.example.myapplication;

public class lopitemrcv
{
    private String maLop;
    private String tenLop;
    private String maHKNH;
    private String hocky;
    private String namhoc;
    private int siSo;
    private String maMH;
    private String maGVCham;

    public lopitemrcv(String maLop, String tenLop, String maHKNH, String hocky, String namhoc, int siSo, String maMH, String maGVCham) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maHKNH = maHKNH;
        this.hocky = hocky;
        this.namhoc = namhoc;
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

    public String getHocky() {
        return hocky;
    }

    public void setHocky(String hocky) {
        this.hocky = hocky;
    }

    public String getNamhoc() {
        return namhoc;
    }

    public void setNamhoc(String namhoc) {
        this.namhoc = namhoc;
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
}
