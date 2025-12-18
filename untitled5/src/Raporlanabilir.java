/*
  INTERFACE (Arayüz - Madde 4.3)
  Sınıfların hangi yeteneklere sahip olması gerektiğini belirten sözleşmedir.
  Ogrenci sınıfı bunu implemente ederek rapor oluşturma yeteneği kazanır.
*/
public interface Raporlanabilir {

    String transkriptOlustur();

    void raporaEkle(String icerik);

    boolean raporGonderildiMi();

    String raporOlustur();
}