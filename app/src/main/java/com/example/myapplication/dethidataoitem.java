package com.example.myapplication;

public class dethidataoitem
{
    private String stt;
    private String made;
    private String tenmon;
    private String hocky;
    private String namhoc;
    private String thoiluong;
    private String ngaytao;

    public dethidataoitem(String stt, String made, String tenmon, String hocky, String namhoc, String thoiluong, String ngaytao)
    {
        this.stt = stt;
        this.made = made;
        this.tenmon = tenmon;
        this.hocky = hocky;
        this.namhoc = namhoc;
        this.thoiluong = thoiluong;
        this.ngaytao = ngaytao;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
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

    public String getThoiluong() {
        return thoiluong;
    }

    public void setThoiluong(String thoiluong) {
        this.thoiluong = thoiluong;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }
}
