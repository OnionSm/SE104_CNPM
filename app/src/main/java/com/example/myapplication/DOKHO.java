package com.example.myapplication;

public class DOKHO {
    private String maDoKho;
    private String tenDoKho;

    public DOKHO(String maDoKho, String tenDoKho) {
        this.maDoKho = maDoKho;
        this.tenDoKho = tenDoKho;
    }

    public String getMaDoKho() {
        return maDoKho;
    }

    public void setMaDoKho(String maDoKho) {
        this.maDoKho = maDoKho;
    }

    public String getTenDoKho() {
        return tenDoKho;
    }

    public void setTenDoKho(String tenDoKho) {
        this.tenDoKho = tenDoKho;
    }

    @Override
    public String toString() {
        return "com.example.myapplication.DOKHO{" +
                "maDoKho='" + maDoKho + '\'' +
                ", tenDoKho='" + tenDoKho + '\'' +
                '}';
    }
}
