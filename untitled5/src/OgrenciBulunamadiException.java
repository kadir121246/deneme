/*
  CUSTOM EXCEPTION 2
  Aranan öğrenci map içinde bulunamadığında fırlatılır.
*/
public class OgrenciBulunamadiException extends RuntimeException {
    public OgrenciBulunamadiException(String message) {
        super(message);
    }
}