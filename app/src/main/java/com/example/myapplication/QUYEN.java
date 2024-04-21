package com.example.myapplication;

public class QUYEN
{
    private String maQuyen;
    private String tenQuyen;

    public QUYEN(String maQuyen, String tenQuyen) {
        this.maQuyen = maQuyen;
        this.tenQuyen = tenQuyen;
    }

    public String getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }

    @Override
    public String toString() {
        return "com.example.myapplication.QUYEN{" +
                "maQuyen='" + maQuyen + '\'' +
                ", tenQuyen='" + tenQuyen + '\'' +
                '}';
    }
}
