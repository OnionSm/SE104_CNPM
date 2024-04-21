import java.util.Date;

public class GIANGVIEN
{
    private String maGV;
    private String hoTenGV;
    private Date ngSinhGV;
    private String gioiTinhGV;
    private String matKhau;
    private String maNhomND;

    public GIANGVIEN(String maGV, String hoTenGV, Date ngSinhGV, String gioiTinhGV, String matKhau, String maNhomND) {
        this.maGV = maGV;
        this.hoTenGV = hoTenGV;
        this.ngSinhGV = ngSinhGV;
        this.gioiTinhGV = gioiTinhGV;
        this.matKhau = matKhau;
        this.maNhomND = maNhomND;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getHoTenGV() {
        return hoTenGV;
    }

    public void setHoTenGV(String hoTenGV) {
        this.hoTenGV = hoTenGV;
    }

    public Date getNgSinhGV() {
        return ngSinhGV;
    }

    public void setNgSinhGV(Date ngSinhGV) {
        this.ngSinhGV = ngSinhGV;
    }

    public String getGioiTinhGV() {
        return gioiTinhGV;
    }

    public void setGioiTinhGV(String gioiTinhGV) {
        this.gioiTinhGV = gioiTinhGV;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getMaNhomND() {
        return maNhomND;
    }

    public void setMaNhomND(String maNhomND) {
        this.maNhomND = maNhomND;
    }

    @Override
    public String toString() {
        return "GIANGVIEN{" +
                "maGV='" + maGV + '\'' +
                ", hoTenGV='" + hoTenGV + '\'' +
                ", ngSinhGV=" + ngSinhGV +
                ", gioiTinhGV='" + gioiTinhGV + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", maNhomND='" + maNhomND + '\'' +
                '}';
    }
}
