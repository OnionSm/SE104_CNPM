package com.example.myapplication;

public class cauhoiitem
{
    private String stt;
    private String mon_hoc;
    private String mo_ta;
    private String do_kho;
    private String ngay_tao;

    public cauhoiitem(String stt, String mon_hoc, String mo_ta, String do_kho, String ngay_tao) {
        this.stt = stt;
        this.mon_hoc = mon_hoc;
        this.mo_ta = mo_ta;
        this.do_kho = do_kho;
        this.ngay_tao = ngay_tao;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getMon_hoc() {
        return mon_hoc;
    }

    public void setMon_hoc(String mon_hoc) {
        this.mon_hoc = mon_hoc;
    }

    public String getMo_ta() {
        return mo_ta;
    }

    public void setMo_ta(String mo_ta) {
        this.mo_ta = mo_ta;
    }

    public String getDo_kho() {
        return do_kho;
    }

    public void setDo_kho(String do_kho) {
        this.do_kho = do_kho;
    }

    public String getNgay_tao() {
        return ngay_tao;
    }

    public void setNgay_tao(String ngay_tao) {
        this.ngay_tao = ngay_tao;
    }



}
