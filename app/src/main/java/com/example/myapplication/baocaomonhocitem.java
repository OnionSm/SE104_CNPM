package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class baocaomonhocitem implements Parcelable {
    private String tenmon;
    private int soluongdethi;
    private int soluongbaicham;
    private int tiledethi;
    private int tilebaicham;



    public baocaomonhocitem(String tenmon, int soluongdethi, int soluongbaicham, int tiledethi, int tilebaicham) {
        this.tenmon = tenmon;
        this.soluongdethi = soluongdethi;
        this.soluongbaicham = soluongbaicham;
        this.tiledethi = tiledethi;
        this.tilebaicham = tilebaicham;
    }

    protected baocaomonhocitem(Parcel in) {
        tenmon = in.readString();
        soluongdethi = in.readInt();
        soluongbaicham = in.readInt();
        tiledethi = in.readInt();
        tilebaicham = in.readInt();
    }

    public static final Creator<baocaomonhocitem> CREATOR = new Creator<baocaomonhocitem>() {
        @Override
        public baocaomonhocitem createFromParcel(Parcel in) {
            return new baocaomonhocitem(in);
        }

        @Override
        public baocaomonhocitem[] newArray(int size) {
            return new baocaomonhocitem[size];
        }
    };

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public int getSoluongdethi() {
        return soluongdethi;
    }

    public void setSoluongdethi(int soluongdethi) {
        this.soluongdethi = soluongdethi;
    }

    public int getSoluongbaicham() {
        return soluongbaicham;
    }

    public void setSoluongbaicham(int soluongbaicham) {
        this.soluongbaicham = soluongbaicham;
    }

    public int getTiledethi() {
        return tiledethi;
    }

    public void setTiledethi(int tiledethi) {
        this.tiledethi = tiledethi;
    }

    public int getTilebaicham() {
        return tilebaicham;
    }

    public void setTilebaicham(int tilebaicham) {
        this.tilebaicham = tilebaicham;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(tenmon);
        dest.writeInt(soluongdethi);
        dest.writeInt(soluongbaicham);
        dest.writeInt(tiledethi);
        dest.writeInt(tilebaicham);
    }
}
