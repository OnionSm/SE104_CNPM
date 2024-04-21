public class CTBCNAM
{
    private String maBCNam;
    private String maMH;
    private int soLuongDeThi;
    private int tileDeThi;
    private int soLuongBaiCham;
    private int tileBaiCham;

    public CTBCNAM(String maBCNam, String maMH, int soLuongDeThi, int tileDeThi, int soLuongBaiCham, int tileBaiCham) {
        this.maBCNam = maBCNam;
        this.maMH = maMH;
        this.soLuongDeThi = soLuongDeThi;
        this.tileDeThi = tileDeThi;
        this.soLuongBaiCham = soLuongBaiCham;
        this.tileBaiCham = tileBaiCham;
    }

    public String getMaBCNam() {
        return maBCNam;
    }

    public void setMaBCNam(String maBCNam) {
        this.maBCNam = maBCNam;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public int getSoLuongDeThi() {
        return soLuongDeThi;
    }

    public void setSoLuongDeThi(int soLuongDeThi) {
        this.soLuongDeThi = soLuongDeThi;
    }

    public int getTileDeThi() {
        return tileDeThi;
    }

    public void setTileDeThi(int tileDeThi) {
        this.tileDeThi = tileDeThi;
    }

    public int getSoLuongBaiCham() {
        return soLuongBaiCham;
    }

    public void setSoLuongBaiCham(int soLuongBaiCham) {
        this.soLuongBaiCham = soLuongBaiCham;
    }

    public int getTileBaiCham() {
        return tileBaiCham;
    }

    public void setTileBaiCham(int tileBaiCham) {
        this.tileBaiCham = tileBaiCham;
    }

    @Override
    public String toString() {
        return "CTBCNAM{" +
                "maBCNam='" + maBCNam + '\'' +
                ", maMH='" + maMH + '\'' +
                ", soLuongDeThi=" + soLuongDeThi +
                ", tileDeThi=" + tileDeThi +
                ", soLuongBaiCham=" + soLuongBaiCham +
                ", tileBaiCham=" + tileBaiCham +
                '}';
    }
}
