import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;

/*
  Bu sınıf uygulamanın giriş noktasıdır.
  Tüm menü işlemleri, dosya okuma/yazma ve raporlama burada yönetilir.
*/
public class Main {

    /*
      Veri Yapısı: HashMap
      Öğrenci numarası ile (Key) öğrenci nesnesine (Value) hızlı erişim sağlar.
    */
    private Map<String, Ogrenci> ogrenciKayitlari = new HashMap<>();

    /*
      Veri Yapısı: ArrayList
      Sisteme tanımlanan dersleri tutar.
      Artık dosyadan okunan dersler buraya otomatik eklenmektedir.
    */
    private List<Ders> dersListesi = new ArrayList<>();

    public static void main(String[] args) {
        Main sistem = new Main();
        sistem.baslat();
    }

    public void baslat() {
        /*
          GENERIC SINIF TESTİ (Madde 5.1)
          Hocanın istediği <T> yapısının çalıştığını göstermek için açılışta test ediyoruz.
        */
        System.out.println("-> Generic 'Depo' sınıfı test ediliyor...");
        Depo<String> mesajDeposu = new Depo<>();
        mesajDeposu.ekle("Generic yapı başarıyla çalıştı!");
        System.out.println("-> Sonuç: " + mesajDeposu.getir(0));

        /*
           BAŞLANGIÇ YÜKLEMESİ
           Program açılır açılmaz veriler dosyadan okunur.
           NOT: Dersler de bu aşamada otomatik olarak listeye eklenir.
        */
        System.out.println("Sistem başlatılıyor, veriler dosyadan yükleniyor...");
        verileriDosyadanYukle(false); // Açılışta detay gösterme (false)

        Scanner sc = new Scanner(System.in);
        int secim;

        /* SONSUZ DÖNGÜ: Kullanıcı 0 diyene kadar menü döner */
        while (true) {
            menuGoster();
            System.out.print("Seçiminiz : ");

            if (sc.hasNextInt()) {
                secim = sc.nextInt();
                sc.nextLine(); // Buffer temizle

                switch (secim) {
                    case 1: yeniOgrenciEkle(sc); break;
                    case 2:
                        System.out.println("Dosya tekrar taranıyor...");
                        verileriDosyadanYukle(true); // Manuel güncellemede detay göster (true)
                        break;
                    case 3: transkriptGoster(sc); break;
                    case 4: ogrenciBilgileriniGoster(sc); break;
                    case 5: dersEkle(sc); break;
                    case 6: enBasariliOgrenciyiBul(); break;
                    case 7: dersleriListele(); break;
                    case 8: ogrenciListesiniDosyayaYaz(); break;
                    case 0:
                        System.out.println("Sistem Kapatılıyor...");
                        return;
                    default:
                        System.out.println("Hata! Geçersiz seçenek.");
                        break;
                }
            } else {
                sc.nextLine();
                System.out.println("Lütfen sayısal bir değer giriniz.");
            }
        }
    }

    private void menuGoster() {
        System.out.println("\n--- ÖĞRENCİ BİLGİ SİSTEMİ ---");
        System.out.println("1. Yeni Öğrenci Ekleme (Elle)");
        System.out.println("2. Dosyayı Tekrar Oku/Güncelle");
        System.out.println("3. Transkript Göster");
        System.out.println("4. Öğrenci Bilgilerini Göster");
        System.out.println("5. Ders Ekleme");
        System.out.println("6. En Başarılı Öğrenciyi Bul");
        System.out.println("7. Kayıtlı Dersleri Listele");
        System.out.println("8. Kayıtlı Öğrenci Listesini Dosyaya Yaz");
        System.out.println("0. Çıkış");
    }

    /* 1. YENİ ÖĞRENCİ EKLEME */
    private void yeniOgrenciEkle(Scanner sc) {
        System.out.print("Öğrenci No: ");
        String no = sc.nextLine();

        if (ogrenciKayitlari.containsKey(no)) {
            System.out.println("UYARI: Bu numaraya sahip bir öğrenci zaten var.");
            return;
        }

        System.out.print("Ad: ");
        String ad = sc.nextLine();
        System.out.print("Soyad: ");
        String soyad = sc.nextLine();
        System.out.print("Giriş Yılı: ");
        int yil = sc.nextInt();
        sc.nextLine();

        try {
            Ogrenci yeniOgrenci = new Ogrenci(no, ad, soyad, yil);
            ogrenciKayitlari.put(no, yeniOgrenci);
            System.out.println(yeniOgrenci.tamAdGetir() + " sisteme başarıyla kaydedildi.");

            // Interface kullanımı (Log)
            yeniOgrenci.logKaydiOlustur("Kullanıcı sisteme elle eklendi.");

        } catch (GecersizVeriException e) {
            System.err.println("Ekleme başarısız: " + e.getMessage());
        }
    }

    /* 2. DOSYADAN OKUMA VE GÜNCELLEME (Otomatik Ders Ekleme Özellikli) */
    public void verileriDosyadanYukle(boolean detayGoster) {
        String dosyaAdi = "not_kayitlari.txt";
        int basariliNotSayisi = 0;
        int yeniEklenenOgrenciSayisi = 0;

        try {
            List<String> notSatirlari = Dosyaİslemleri.notDosyasiniOku(dosyaAdi);

            for (String satir : notSatirlari) {
                if (satir.startsWith("Öğrenci No")) continue;

                String[] veriler = satir.split(",");

                if (veriler.length < 5) continue;

                String ogrenciNo = veriler[0].trim();
                String dersAdi = veriler[1].trim();

                try {
                    int vize = Integer.parseInt(veriler[2].trim());
                    int finalNotu = Integer.parseInt(veriler[3].trim());
                    double akts = Double.parseDouble(veriler[4].trim());

                    /* OTOMATİK DERS KAYDI (Yeni Özellik)
                       Dosyada gördüğümüz ders, sistemde kayıtlı değilse listeye ekliyoruz.
                       Böylece 'Dersleri Listele' dediğimizde bu dersler görünür.
                    */
                    boolean dersVarMi = false;
                    for (Ders d : dersListesi) {
                        if (d.getAd().equalsIgnoreCase(dersAdi)) {
                            dersVarMi = true;
                            break;
                        }
                    }
                    if (!dersVarMi) {
                        // Dosyadan gelen dersi sisteme tanımla (Kodu ve Adı aynı yapıldı)
                        dersListesi.add(new Ders(dersAdi, dersAdi, (int)akts));
                    }

                    /* Öğrenci İşlemleri */
                    Ogrenci ogrenci = ogrenciKayitlari.get(ogrenciNo);

                    if (ogrenci == null) {
                        try {
                            ogrenci = new Ogrenci(ogrenciNo, "Otomatik", "Kayıt", 2024);
                            ogrenciKayitlari.put(ogrenciNo, ogrenci);
                            yeniEklenenOgrenciSayisi++;
                        } catch (GecersizVeriException e) {
                            continue;
                        }
                    }

                    /* Mükerrer Not Önleme (Update Mantığı) */
                    ogrenci.getNotListesi().removeIf(n -> n.getDersAdi().equalsIgnoreCase(dersAdi));

                    /* Polimorfizm ile Not Oluşturma */
                    NotKaydi eklenecekNot;
                    if (dersAdi.toUpperCase().contains("PROJE") || dersAdi.toUpperCase().contains("SERTİFİKA")) {
                        eklenecekNot = new SertifikaNotu(dersAdi, vize, finalNotu, akts);
                    } else {
                        eklenecekNot = new LisansNotu(dersAdi, vize, finalNotu, akts);
                    }

                    ogrenci.notEkle(eklenecekNot);

                    if(detayGoster) {
                        ogrenci.logKaydiOlustur(dersAdi + " notu güncellendi/eklendi.");
                    }
                    basariliNotSayisi++;

                } catch (NumberFormatException nfe) {
                    if (detayGoster) System.out.println("Hatalı sayı formatı: " + satir);
                }
            }

            System.out.println("✅ Yükleme/Güncelleme Tamamlandı.");
            if(detayGoster) {
                System.out.println("   -> " + yeniEklenenOgrenciSayisi + " yeni öğrenci eklendi.");
                System.out.println("   -> " + basariliNotSayisi + " not işlendi.");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Hata: Dosya bulunamadı (" + dosyaAdi + ")");
        } catch (GecersizVeriException e) {
            System.err.println("Veri hatası: " + e.getMessage());
        }
    }

    /* 3. TRANSKRİPT GÖSTER */
    private void transkriptGoster(Scanner sc) {
        System.out.print("Transkripti görüntülenecek öğrenci no: ");
        String no = sc.nextLine();
        Ogrenci ogrenci = ogrenciKayitlari.get(no);

        if (ogrenci != null) {
            System.out.println(ogrenci.transkriptOlustur());
        } else {
            System.out.println("Hata! Öğrenci bulunamadı.");
        }
    }

    /* 4. ÖĞRENCİ BİLGİLERİ  */
    private void ogrenciBilgileriniGoster(Scanner sc) {
        System.out.print("Bilgileri istenen Öğrenci No: ");
        String no = sc.nextLine();
        Ogrenci ogr = ogrenciKayitlari.get(no);

        if (ogr != null) {
            // Listelenecek not yoksa uyarı ver
            if (ogr.getNotListesi().isEmpty()) {
                System.out.println("Bu öğrenciye ait ders kaydı bulunmamaktadır.");
            } else {
                // Her dersi istenilen formatta alt alta yazdır
                for (NotKaydi not : ogr.getNotListesi()) {
                    System.out.println(
                            "\nno=" + ogr.getOgrenciNo() +
                                    " \ndersi=" + not.getDersAdi() +
                                    "\nvize not=" + not.getVizeNotu() +
                                    "\nfinal notu=" + not.getFinalNotu()
                    );
                }
            }
        } else {
            System.out.println("❌ Öğrenci bulunamadı.");
        }
    }

    /* 5. DERS EKLEME */
    private void dersEkle(Scanner sc) {
        System.out.print("Ders Kodu: ");
        String kod = sc.nextLine();
        System.out.print("Ders Adı: ");
        String ad = sc.nextLine();
        System.out.print("Ders Kredisi: ");
        try {
            int kredi = sc.nextInt();
            sc.nextLine();
            dersListesi.add(new Ders(kod, ad, kredi));
            System.out.println("✅ Ders eklendi.");
        } catch (Exception e) {
            System.out.println("Hatalı giriş.");
            sc.nextLine();
        }
    }

    /* 6. EN BAŞARILI ÖĞRENCİ */
    private void enBasariliOgrenciyiBul() {
        if (ogrenciKayitlari.isEmpty()) {
            System.out.println("Sistemde kayıtlı öğrenci yok.");
            return;
        }

        Ogrenci enBasarili = null;
        double enYuksekGano = -1.0;

        for (Ogrenci ogr : ogrenciKayitlari.values()) {
            if (ogr.getGano() > enYuksekGano) {
                enYuksekGano = ogr.getGano();
                enBasarili = ogr;
            }
        }

        if (enBasarili != null) {
            System.out.println("En başarılı öğrenci " + enBasarili.getOgrenciNo() + " nolu öğrenci ve notu=" + String.format("%.2f", enYuksekGano));
        } else {
            System.out.println("Hesaplanabilir notu olan öğrenci bulunamadı.");
        }
    }

    /* 7. DERSLERİ LİSTELE (Dosyadan Okunanlar Dahil) */
    private void dersleriListele() {
        System.out.println("\n--- DERS LİSTESİ ---");
        if (dersListesi.isEmpty()) {
            System.out.println("Sistemde kayıtlı ders bulunmamaktadır.");
        } else {
            /* Listeyi döngüyle gezip ekrana basıyoruz */
            for (Ders d : dersListesi) {
                System.out.println("- " + d.getAd() + " (" + d.getKredi() + " AKTS)");
            }
        }
    }

    /* 8. DOSYAYA YAZMA */
    private void ogrenciListesiniDosyayaYaz() {
        try {
            Dosyaİslemleri.ogrenciListesiniYaz("ogrenci_raporu.csv", ogrenciKayitlari);
            System.out.println("✅ Öğrenci listesi dosyaya kaydedildi.");
        } catch (FileNotFoundException e) {
            System.out.println("❌ Dosya yazma hatası.");
        }
    }
}