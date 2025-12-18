/*
  ABSTRACT SINIF (Soyut Sınıf - Madde 4.2)
  Bu sınıftan doğrudan nesne üretilemez, sadece kalıtım (extends) verilebilir.
  Ortak özellikleri (Ad, Soyad vb.) tutar.
*/
public abstract class Kullanici {
    /*
      ENCAPSULATION (Kapsülleme - Madde 3)
      Değişkenler private yapılarak dışarıdan doğrudan erişim engellenir.
    */
    private String ad;
    private String soyad;
    private int id;
    private String ePosta;

    /* Abstract Metotlar: Alt sınıflar bunları doldurmak ZORUNDADIR */
    public abstract String getKullaniciTuru();
    public abstract void bilgiGoster();

    public String tamAdGetir() {
        return this.ad + " " + this.soyad;
    }

    /* Getter ve Setter Metotları */
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }

    public void setAd(String ad) throws GecersizVeriException {
        if (ad == null || ad.trim().isEmpty()) {
            throw new GecersizVeriException("Ad alanı boş bırakılamaz.");
        }
        this.ad = ad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }
}