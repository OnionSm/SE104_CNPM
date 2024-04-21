public class CHITIETLOP
{
    private String maLop;
    private String maSV;
    private String tenSV;
    private int diem;
    private String diemChu;
    private String ghiChu;

    public CHITIETLOP(String maLop, String maSV, String tenSV, int diem, String diemChu, String ghiChu) {
        this.maLop = maLop;
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.diem = diem;
        this.diemChu = diemChu;
        this.ghiChu = ghiChu;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public int getDiem() {
        return diem;
    }

    public void setDiem(int diem) {
        this.diem = diem;
    }

    public String getDiemChu() {
        return diemChu;
    }

    public void setDiemChu(String diemChu) {
        this.diemChu = diemChu;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return "CHITIETLOP{" +
                "maLop='" + maLop + '\'' +
                ", maSV='" + maSV + '\'' +
                ", tenSV='" + tenSV + '\'' +
                ", diem=" + diem +
                ", diemChu='" + diemChu + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
