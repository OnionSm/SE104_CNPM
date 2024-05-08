package com.example.myapplication;

public class UserImage
{
    private String magv;
    private String fileImage;

    public UserImage(String magv, String fileImage)
    {
        this.magv = magv;
        this.fileImage = fileImage;
    }

    public String getMagv()
    {
        return magv;
    }

    public void setMagv(String magv)
    {
        this.magv = magv;
    }

    public String getFileImage()
    {
        return fileImage;
    }

    public void setFileImage(String fileImage)
    {
        this.fileImage = fileImage;
    }

}
