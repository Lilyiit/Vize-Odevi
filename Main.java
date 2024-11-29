import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Temel sınıf
abstract class BaseEntity {
    protected String id;
    protected String name;

    public BaseEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract void bilgiGoster();
}

// Film sınıfı
class Film {
    private String ad;
    private int sure;
    private String tur;

    public Film(String ad, int sure, String tur) {
        this.ad = ad;
        this.sure = sure;
        this.tur = tur;
    }

    public void bilgiGoster() {
        System.out.println("Film Adı: " + ad + ", Süre: " + sure + " dakika, Tür: " + tur);
    }

    public String getAd() {
        return ad;
    }
}

// Salon sınıfı
class Salon extends BaseEntity {
    private List<Film> filmler;
    private boolean[] koltuklar;

    public Salon(String id, String name, int koltukSayisi) {
        super(id, name);
        this.filmler = new ArrayList<>();
        this.koltuklar = new boolean[koltukSayisi]; // Tüm koltuklar başlangıçta boş
    }

    public void filmEkle(Film film) {
        filmler.add(film);
    }

    public List<Film> getFilmler() {
        return filmler;
    }

    public boolean koltukSec(int koltukNo) {
        if (koltukNo < 0 || koltukNo >= koltuklar.length) {
            System.out.println("Geçersiz koltuk numarası.");
            return false;
        }
        if (koltuklar[koltukNo]) {
            System.out.println("Bu koltuk zaten dolu.");
            return false;
        }
        koltuklar[koltukNo] = true;
        System.out.println("Koltuk başarıyla rezerve edildi!");
        return true;
    }

    public void bosKoltuklariGoster() {
        System.out.print("Boş Koltuklar: ");
        for (int i = 0; i < koltuklar.length; i++) {
            if (!koltuklar[i]) {
                System.out.print((i + 1) + " ");
            }
        }
        System.out.println();
    }

    @Override
    public void bilgiGoster() {
        System.out.println("Salon Adı: " + name + " (ID: " + id + ")");
        System.out.println("Gösterilen Filmler:");
        for (Film film : filmler) {
            film.bilgiGoster();
        }
        bosKoltuklariGoster();
    }
}

// Ana sınıf (Main)
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Salon> salonlar = new ArrayList<>();

        // Örnek salonlar ve filmler
        Salon salon1 = new Salon("1", "Büyük Salon", 10);
        Salon salon2 = new Salon("2", "Küçük Salon", 5);
        salonlar.add(salon1);
        salonlar.add(salon2);

        // Filmleri ekle
        salon1.filmEkle(new Film("The Shawshank Redemption", 142, "Dram"));
        salon1.filmEkle(new Film("The Godfather", 175, "Suç/Dram"));
        salon2.filmEkle(new Film("Inception", 148, "Bilim Kurgu/Aksiyon"));
        salon2.filmEkle(new Film("Pulp Fiction", 154, "Suç/Dram"));

        while (true) {
            System.out.println("\n=================== Sinema Yönetim Sistemi ===================");
            System.out.println("1. Salon Bilgisi Göster (Film ve koltuk durumu)");
            System.out.println("2. Salon Seç, Film Seç ve Koltuk Rezervasyonu Yap");
            System.out.println("3. Çıkış");
            System.out.println("=============================================================");
            System.out.print("Lütfen bir işlem seçiniz: ");
            int secim = scanner.nextInt();
            scanner.nextLine();

            switch (secim) {
                case 1:
                    System.out.println("\n[Salon ve Film Bilgisi]");
                    for (Salon salon : salonlar) {
                        System.out.println("-------------------------------------------------------------");
                        salon.bilgiGoster();
                        System.out.println("-------------------------------------------------------------");
                    }
                    break;

                case 2:
                    System.out.println("\n[Salon Seçimi]");
                    System.out.print("Lütfen Salon ID'sini giriniz: ");
                    String secilenSalonId = scanner.nextLine();

                    salonlar.stream()
                            .filter(salon -> salon.id.equals(secilenSalonId))
                            .findFirst()
                            .ifPresentOrElse(salon -> {
                                System.out.println("Salon: " + salon.name);
                                System.out.println("\nBu salondaki filmler:");
                                List<Film> filmler = salon.getFilmler();
                                for (int i = 0; i < filmler.size(); i++) {
                                    System.out.println((i + 1) + ". " + filmler.get(i).getAd());
                                }

                                System.out.print("Lütfen izlemek istediğiniz filmin numarasını seçiniz: ");
                                int filmSecimi = scanner.nextInt();
                                scanner.nextLine();

                                if (filmSecimi > 0 && filmSecimi <= filmler.size()) {
                                    System.out.println("Seçtiğiniz Film: " + filmler.get(filmSecimi - 1).getAd());
                                    salon.bosKoltuklariGoster();
                                    System.out.print("Lütfen rezervasyon yapmak istediğiniz koltuk numarasını giriniz: ");
                                    int koltukNo = scanner.nextInt() - 1;
                                    scanner.nextLine();

                                    if (salon.koltukSec(koltukNo)) {
                                        System.out.println("Rezervasyon başarıyla tamamlandı.");
                                    }
                                } else {
                                    System.out.println("Geçersiz film seçimi.");
                                }
                            }, () -> System.out.println("Hata: Belirtilen ID'ye sahip bir salon bulunamadı."));
                    break;

                case 3:
                    System.out.println("\nSistemden çıkış yapılıyor... Teşekkür ederiz!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Hata: Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        }
    }
}
