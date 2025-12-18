import java.util.ArrayList;
import java.util.List;

/*
  GENERIC SINIF (Madde 5.1)
  <T> parametresi sayesinde bu sınıf her türden veriyi saklayabilir.
  Type Safety (Tip Güvenliği) ve Code Reusability (Kod Tekrar Kullanımı) sağlar.
*/
public class Depo<T> {
    private List<T> esyalar = new ArrayList<>();

    public void ekle(T nesne) {
        esyalar.add(nesne);
    }

    public T getir(int index) {
        return esyalar.get(index);
    }
}