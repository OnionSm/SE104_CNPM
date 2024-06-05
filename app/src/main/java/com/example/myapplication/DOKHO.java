package com.example.myapplication;

public class DOKHO {
    private String maDoKho;
    private String TenDK;
    public DOKHO()
    {

    }

    public DOKHO(String maDoKho, String tenDK) {
        this.maDoKho = maDoKho;
        TenDK = tenDK;
    }

    public String getMaDoKho() {
        return maDoKho;
    }

    public void setMaDoKho(String maDoKho) {
        this.maDoKho = maDoKho;
    }

    public String getTenDK() {
        return TenDK;
    }

    public void setTenDK(String tenDK) {
        TenDK = tenDK;
    }
}
