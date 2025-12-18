/*
  CONCRETE CLASS (Somut Sınıf)
  NotKaydi sınıfını miras alır ve normal dersler için GPA hesaplamasını uygular.
*/
public class LisansNotu extends NotKaydi {

    public LisansNotu(String dersAdi, int vizeNotu, int finalNotu, double akts) throws GecersizVeriException {
        super(dersAdi, vizeNotu, finalNotu, akts);
    }

    /*
      OVERRIDE (Ezme)
      Üst sınıftaki soyut metodu, Lisans kurallarına göre dolduruyoruz.
    */
    @Override
    public double gpaPuanıHesapla(double yuzlukOrtalama) {
        /* ÖSYM Katsayı Sistemi */
        int basamak = (int) (yuzlukOrtalama / 10);
        switch (basamak) {
            case 10:
            case 9:  return 4.0;
            case 8:  return 3.5;
            case 7:  return (yuzlukOrtalama >= 75) ? 3.0 : 2.5;
            case 6:  return 2.0;
            case 5:  return 1.5;
            case 4:  return 1.0;
            case 3:  return 0.5;
            default: return 0.0;
        }
    }
}