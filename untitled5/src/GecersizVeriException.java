/*
  CUSTOM EXCEPTION (Özel Hata Sınıfı - Madde 7)
  Java'nın standart hataları yetmediğinde kendi iş kurallarımıza özel hatalar oluştururuz.
  Örneğin: Negatif not girildiğinde bu hatayı fırlatırız.
*/
public class GecersizVeriException extends RuntimeException {
    public GecersizVeriException(String message) {
        super(message);
    }
}