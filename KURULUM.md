# Kuaför Otomasyonu — Kurulum Rehberi

## ⚡ Hızlı Önizleme (Demo Modu — MySQL gerekmez)

Veritabanı kurmadan uygulamanın nasıl göründüğünü görmek için:

```bat
cd KuaforOtomasyon
mvn compile exec:java
```

Demo modunda:
- 5 müşteri, 3 çalışan, 5 hizmet ve 6 örnek randevu önceden yüklüdür
- Tüm CRUD işlemleri çalışır (ekleme, düzenleme, silme)
- Veriler bellekte tutulur; uygulama kapandığında sıfırlanır

---

## Ön Gereksinimler

1. **Java JDK 17+** → https://adoptium.net
2. **Maven 3.8+** → https://maven.apache.org/download
3. **MySQL 8.0+** → https://dev.mysql.com/downloads/installer/

---

## Adım 1 — MySQL Veritabanı Oluşturma

MySQL komut satırında (veya MySQL Workbench'te) aşağıdaki dosyayı çalıştırın:

```bat
mysql -u root -p < src\main\resources\sql\init.sql
```

Veya MySQL'e giriş yapıp:

```sql
source C:\KuaforOtomasyon\src\main\resources\sql\init.sql
```

---

## Adım 2 — Veritabanı Bağlantı Ayarları

`src\main\resources\database.properties` dosyasını düzenleyin:

```properties
db.url=jdbc:mysql://localhost:3306/kuafor_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Europe/Istanbul&allowPublicKeyRetrieval=true&useSSL=false
db.user=root
db.password=BURAYA_GERCEK_SIFREYI_YAZIN
```

---

## Adım 3 — Build ve Çalıştırma

```bat
cd KuaforOtomasyon

:: Bağımlılıkları indir ve derle
mvn clean package -DskipTests

:: Çalıştır
java -Dfile.encoding=UTF-8 -jar target\KuaforOtomasyon-1.0.0.jar
```

---

## Sık Karşılaşılan Sorunlar

### MySQL bağlantısı kurulamıyor
- MySQL servisinin çalıştığını doğrulayın: `Başlat > Hizmetler > MySQL80 > Başlat`
- `database.properties` dosyasındaki şifre ve port'u kontrol edin
- Güvenlik duvarında 3306 portunun açık olduğunu doğrulayın

### Türkçe karakter bozulması
- Uygulamayı şu şekilde başlatın: `java -Dfile.encoding=UTF-8 -jar ...`

### Communications link failure
- MySQL servisinin çalıştığından emin olun
- `localhost` yerine `127.0.0.1` deneyin

---

## Proje Yapısı

```
KuaforOtomasyon/
├── pom.xml                          ← Maven bağımlılıkları
└── src/main/
    ├── java/com/kuafor/
    │   ├── Main.java                ← Giriş noktası
    │   ├── database/                ← DB bağlantı yönetimi
    │   ├── model/                   ← Entity sınıfları
    │   ├── dao/                     ← Veritabanı erişim katmanı
    │   ├── service/                 ← İş mantığı katmanı
    │   ├── ui/                      ← Swing UI bileşenleri
    │   └── util/                    ← Yardımcı sınıflar
    └── resources/
        ├── database.properties      ← DB bağlantı ayarları
        ├── logback.xml              ← Log ayarları
        └── sql/init.sql             ← DB başlangıç scripti
```
