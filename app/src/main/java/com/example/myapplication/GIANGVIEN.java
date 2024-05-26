package com.example.myapplication;




public class GIANGVIEN 
{
    public String gioiTinhGV;
    public String hoTenGV;
    public String maGV;
    public String maNhomND;
    public String matKhau;
    public String ngSinhGV;
    public String sdt;
    public GIANGVIEN()
    {

    }
    public GIANGVIEN(String gioiTinhGV, String hoTenGV, String maGV, String maNhomND, String matKhau, String ngSinhGV, String sdt) {
        this.gioiTinhGV = gioiTinhGV;
        this.hoTenGV = hoTenGV;
        this.maGV = maGV;
        this.maNhomND = maNhomND;
        this.matKhau = matKhau;
        this.ngSinhGV = ngSinhGV;
        this.sdt = sdt;
    }

    public String getGioiTinhGV() 
    {
        return gioiTinhGV;
    }

    public String getHoTenGV() 
    {
        return hoTenGV;
    }

    public String getMaGV() {
        return maGV;
    }

    public String getMaNhomND() {
        return maNhomND;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getNgSinhGV() {
        return ngSinhGV;
    }

    public String getSdt() {
        return sdt;
    }

    public void setGioiTinhGV(String gioiTinhGV) {
        this.gioiTinhGV = gioiTinhGV;
    }



    public void setHoTenGV(String hoTenGV) 
    {
        this.hoTenGV = hoTenGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public void setMaNhomND(String maNhomND) {
        this.maNhomND = maNhomND;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public void setNgSinhGV(String ngSinhGV) {
        this.ngSinhGV = ngSinhGV;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    @Override
    public String toString() 
    {
        return "GIANGVIEN{" +
                "gioiTinhGV='" + gioiTinhGV + '\'' +
                ", hoTenGV='" + hoTenGV + '\'' +
                ", maGV='" + maGV + '\'' +
                ", maNhomND='" + maNhomND + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", ngSinhGV='" + ngSinhGV + '\'' +
                ", sdt='" + sdt + '\'' +
                '}';
    }
}





