package com.example.myapplication;

public class dethitracuuitem
{
    private String mon_hoc;
    private String magv;
    private String mahknh;
    private String hocky;
    private String nam1;
    private String nam2;
    private String ngay;
    private String ma_de;
    private String thoiluong;

    public dethitracuuitem(String mon_hoc, String magv, String mahknh, String hocky, String nam1, String nam2, String ngay, String ma_de, String thoiluong) {
        this.mon_hoc = mon_hoc;
        this.magv = magv;
        this.mahknh = mahknh;
        this.hocky = hocky;
        this.nam1 = nam1;
        this.nam2 = nam2;
        this.ngay = ngay;
        this.ma_de = ma_de;
        this.thoiluong = thoiluong;
    }

    public String getMon_hoc()
    {
        return mon_hoc;
    }

    public void setMon_hoc(String mon_hoc)
    {
        this.mon_hoc = mon_hoc;
    }

    public String getMagv()
    {
        return magv;
    }

    public void setMagv(String magv)
    {
        this.magv = magv;
    }

    public String getMahknh()
    {
        return mahknh;
    }

    public void setMahknh(String mahknh)
    {
        this.mahknh = mahknh;
    }

    public String getHocky()
    {
        return hocky;
    }

    public void setHocky(String hocky)
    {
        this.hocky = hocky;
    }

    public String getNam1()
    {
        return nam1;
    }

    public void setNam1(String nam1)
    {
        this.nam1 = nam1;
    }

    public String getNam2()
    {
        return nam2;
    }

    public void setNam2(String nam2)
    {
        this.nam2 = nam2;
    }

    public String getNgay()
    {
        return ngay;
    }

    public void setNgay(String ngay)
    {
        this.ngay = ngay;
    }

    public String getMa_de()
    {
        return ma_de;
    }

    public void setMa_de(String ma_de)
    {
        this.ma_de = ma_de;
    }

    public String getThoiluong()
    {
        return thoiluong;
    }

    public void setThoiluong(String thoiluong)
    {
        this.thoiluong = thoiluong;
    }

    @Override
    public String toString() {
        return "dethitracuuitem{" +
                "mon_hoc='" + mon_hoc + '\'' +
                ", magv='" + magv + '\'' +
                ", mahknh='" + mahknh + '\'' +
                ", hocky='" + hocky + '\'' +
                ", nam1='" + nam1 + '\'' +
                ", nam2='" + nam2 + '\'' +
                ", ngay='" + ngay + '\'' +
                ", ma_de='" + ma_de + '\'' +
                ", thoiluong='" + thoiluong + '\'' +
                '}';
    }
}
