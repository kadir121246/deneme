/*
  POJO (Plain Old Java Object)
  Sadece veri tutmak ve taşımak için kullanılan basit sınıftır.
*/
public class Ders {
    private String dersKodu;
    private String ad;
    private int kredi;

    public Ders(String dersKodu, String ad, int kredi) {
        this.dersKodu = dersKodu;
        this.ad = ad;
        this.kredi = kredi;
    }

    public String getAd() { return ad; }
    public String getDersKodu() { return dersKodu; }
    public int getKredi() { return kredi; }
}