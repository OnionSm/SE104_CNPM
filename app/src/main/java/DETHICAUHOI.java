public class DETHICAUHOI
{
    private String maDT;
    private String maCH;

    public DETHICAUHOI(String maDT, String maCH) {
        this.maDT = maDT;
        this.maCH = maCH;
    }

    public String getMaDT() {
        return maDT;
    }

    public void setMaDT(String maDT) {
        this.maDT = maDT;
    }

    public String getMaCH() {
        return maCH;
    }

    public void setMaCH(String maCH) {
        this.maCH = maCH;
    }

    @Override
    public String toString() {
        return "DETHICAUHOI{" +
                "maDT='" + maDT + '\'' +
                ", maCH='" + maCH + '\'' +
                '}';
    }
}
