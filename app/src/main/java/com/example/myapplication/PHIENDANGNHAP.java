package com.example.myapplication;

public class PHIENDANGNHAP
{
    String account;
    String password;

    public PHIENDANGNHAP(String account, String password)
    {
        this.account = account;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "PHIENDANGNHAP{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
