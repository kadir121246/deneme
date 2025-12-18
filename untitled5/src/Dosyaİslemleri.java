import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
  UTILITY CLASS (Yardımcı Sınıf - Madde 1.1)
  İçindeki metotlar 'static' tanımlanmıştır, böylece nesne üretmeden
  DosyaIslemleri.metotAdi() şeklinde doğrudan çağrılabilirler.
*/
public class Dosyaİslemleri {

    /* Dosyadan satır satır veri okuyan metot (Scanner kullanımı) */
    public static List<String> notDosyasiniOku(String dosyaYolu) throws FileNotFoundException {
        List<String> satirlar = new ArrayList<>();

        /* Try-with-resources yapısı: Scanner işi bitince otomatik kapanır */
        try (Scanner scanner = new Scanner(new File(dosyaYolu))) {
            while (scanner.hasNextLine()) {
                satirlar.add(scanner.nextLine());
            }
        }
        return satirlar;
    }

    /* Verileri CSV formatında dosyaya yazan metot (PrintWriter kullanımı) */
    public static void ogrenciListesiniYaz(String dosyaYolu, Map<String, Ogrenci> ogrenciKayitlari) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(dosyaYolu)) {
            writer.println("OGR_NO,AD,SOYAD,GIRIS_YILI,GANO");
            for (Ogrenci ogrenci : ogrenciKayitlari.values()) {
                writer.printf("%s,%s,%s,%d,%.2f\n",
                        ogrenci.getOgrenciNo(),
                        ogrenci.getAd(),
                        ogrenci.getSoyad(),
                        ogrenci.getGirisYili(),
                        ogrenci.getGano()
                );
            }
        }
    }

    public static void dosyaVarliğiniKontrolEtVeOlustur(String dosyaYolu) {
        File dosya = new File(dosyaYolu);
        if (!dosya.exists()) {
            try {
                dosya.createNewFile();
            } catch (IOException e) {
                System.err.println("Dosya oluşturma hatası.");
            }
        }
    }
}