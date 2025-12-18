/*
  POLİMORFİZM ÖRNEĞİ
  Bu sınıf da bir NotKaydi'dır ama hesaplama mantığı tamamen farklıdır.
  Normal derslerde 4.0, 3.5 gibi notlar varken burada "Geçti/Kaldı" veya "Belge Hakkı" vardır.
*/
public class SertifikaNotu extends NotKaydi {

    public SertifikaNotu(String dersAdi, int vizeNotu, int finalNotu, double akts) throws GecersizVeriException {
        super(dersAdi, vizeNotu, finalNotu, akts);
    }

    /* Farklı davranış: 80 puan altı 0 sayılır, üstü tam puan sayılır.
      Bu, "Çok Biçimlilik" ilkesinin canlı örneğidir.
    */
    @Override
    public double gpaPuanıHesapla(double yuzlukOrtalama) {
        if (yuzlukOrtalama >= 80) {
            return 4.0;
        }
        return 0.0;
    }

    /* Harf notu yerine özel mesaj döndürmek için metodu eziyoruz. */
    @Override
    public String harfNotuGetir() {
        double ortalama = yuzlukOrtalamaHesapla();
        if (ortalama >= 80) {
            return "BELGE HAKKEDİLDİ";
        } else {
            return "BAŞARISIZ (Belge Yok)";
        }
    }
}