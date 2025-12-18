/*
  Bu soyut sınıf, tüm not tipleri (Lisans, Sertifika) için temel yapıyı oluşturur.
  Template Method tasarım desenine benzer bir yapı kurar.
*/
public abstract class NotKaydi {
    private String dersAdi;
    private int vizeNotu;
    private int finalNotu;
    private double akts;

    /* Constructor 'protected' yapılarak sadece alt sınıfların çağırması sağlanır. */
    protected NotKaydi(String dersAdi, int vizeNotu, int finalNotu, double akts) throws GecersizVeriException {
        setDersAdi(dersAdi);
        setVizeNotu(vizeNotu);
        setFinalNotu(finalNotu);
        setAkts(akts);
    }

    /*
       Bu metod abstract'tır. Her not tipi GPA puanını farklı hesaplayabilir.
       Polimorfizmin temelini oluşturur.
    */
    public abstract double gpaPuanıHesapla(double yuzlukOrtalama);

    public double yuzlukOrtalamaHesapla() {
        return this.vizeNotu * 0.4 + this.finalNotu * 0.6;
    }

    /*
      ÖSYM UYUMLU HARF NOTU SİSTEMİ
      Ortalamayı hesaplar ve switch-case ile harf karşılığını bulur.
    */
    public String harfNotuGetir() {
        double ortalama = yuzlukOrtalamaHesapla();
        /* 10'a bölerek basamak değeri (9, 8, 7...) üzerinden switch kuruyoruz */
        int basamak = (int) (ortalama / 10);

        switch (basamak) {
            case 10:
            case 9:  return "AA";
            case 8:  return "BA";
            case 7:  return (ortalama >= 75) ? "BB" : "CB"; /* 70-74 arası CB, 75-79 arası BB */
            case 6:  return "CC";
            case 5:  return "DC";
            case 4:  return "DD";
            case 3:  return "FD";
            default: return "FF";
        }
    }

    public String getDersAdi() { return dersAdi; }
    public int getVizeNotu() { return vizeNotu; }
    public int getFinalNotu() { return finalNotu; }
    public double getAkts() { return akts; }

    public void setDersAdi(String dersAdi) {
        this.dersAdi = dersAdi;
    }

    public void setVizeNotu(int not) throws GecersizVeriException {
        if (not < 0 || not > 100) {
            throw new GecersizVeriException("Vize notu 0-100 aralığı dışında olamaz.");
        }
        this.vizeNotu = not;
    }

    public void setFinalNotu(int not) throws GecersizVeriException {
        if (not < 0 || not > 100) {
            throw new GecersizVeriException("Final notu 0-100 aralığı dışında olamaz.");
        }
        this.finalNotu = not;
    }

    public void setAkts(double akts) throws GecersizVeriException {
        if (akts < 1 || akts > 15) {
            throw new GecersizVeriException("AKTS değeri 1-15 aralığında olmalıdır.");
        }
        this.akts = akts;
    }
}