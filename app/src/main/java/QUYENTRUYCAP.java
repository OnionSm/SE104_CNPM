public class QUYENTRUYCAP
{
    private String maNhomND;
    private String maQuyen;

    public QUYENTRUYCAP(String maNhomND, String maQuyen) {
        this.maNhomND = maNhomND;
        this.maQuyen = maQuyen;
    }

    public String getMaNhomND() {
        return maNhomND;
    }

    public void setMaNhomND(String maNhomND) {
        this.maNhomND = maNhomND;
    }

    public String getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }

    @Override
    public String toString() {
        return "QUYENTRUYCAP{" +
                "maNhomND='" + maNhomND + '\'' +
                ", maQuyen='" + maQuyen + '\'' +
                '}';
    }
}
