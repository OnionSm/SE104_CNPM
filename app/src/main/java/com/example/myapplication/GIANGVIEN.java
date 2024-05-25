package com.example.myapplication;



public class GIANGVIEN
{
    private String maGV;
    private String hoTenGV;
    private String ngSinhGV;
    private String gioiTinhGV;
    private String email;
    private String sdt;
    private String matKhau;
    private String maNhomND;

    public GIANGVIEN()
    {

    }

    public GIANGVIEN(String maGV, String hoTenGV, String ngSinhGV, String gioiTinhGV, String email, String sdt, String matKhau, String maNhomND) {
        this.maGV = maGV;
        this.hoTenGV = hoTenGV;
        this.ngSinhGV = ngSinhGV;
        this.gioiTinhGV = gioiTinhGV;
        this.email = email;
        this.sdt = sdt;
        this.matKhau = matKhau;
        this.maNhomND = maNhomND;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getHoTenGV() {
        return hoTenGV;
    }

    public void setHoTenGV(String hoTenGV) {
        this.hoTenGV = hoTenGV;
    }

    public String getNgSinhGV() {
        return ngSinhGV;
    }

    public void setNgSinhGV(String ngSinhGV) {
        this.ngSinhGV = ngSinhGV;
    }

    public String getGioiTinhGV() {
        return gioiTinhGV;
    }

    public void setGioiTinhGV(String gioiTinhGV) {
        this.gioiTinhGV = gioiTinhGV;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getMaNhomND() {
        return maNhomND;
    }

    public void setMaNhomND(String maNhomND) {
        this.maNhomND = maNhomND;
    }

    @Override
    public String toString() {
        return "GIANGVIEN{" +
                "maGV='" + maGV + '\'' +
                ", hoTenGV='" + hoTenGV + '\'' +
                ", ngSinhGV='" + ngSinhGV + '\'' +
                ", gioiTinhGV='" + gioiTinhGV + '\'' +
                ", email='" + email + '\'' +
                ", sdt='" + sdt + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", maNhomND='" + maNhomND + '\'' +
                '}';
    }
}
