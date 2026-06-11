# Kuafor Otomasyonu — Kurulum Rehberi

## On Gereksinimler

1. **Java JDK 17+** — https://adoptium.net
2. **Maven 3.8+** — https://maven.apache.org/download
3. **MySQL 8.0+** — https://dev.mysql.com/downloads/installer/

---

## Adim 1 — Veritabani Kurulumu

MySQL komut satirinda asagidaki komutu calistir:

```bash
mysql -u root -p < src/main/resources/sql/init.sql
```

Ya da MySQL'e girerek:

```sql
source /tam/yol/KuaforOtomasyon/src/main/resources/sql/init.sql
```

Bu komut `kuafor_db` veritabanini ve gerekli tablolari olusturur.

---

## Adim 2 — Veritabani Baglanti Ayarlari

`src/main/resources/database.properties.example` dosyasini kopyala:

```bash
cp src/main/resources/database.properties.example src/main/resources/database.properties
```

Sonra `database.properties` dosyasini ac ve kendi MySQL sifrenle duzenle:

```properties
db.url=jdbc:mysql://localhost:3306/kuafor_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Europe/Istanbul&allowPublicKeyRetrieval=true&useSSL=false
db.user=root
db.password=MYSQL_SIFRENIZI_BURAYA_YAZIN
```

---

## Adim 3 — Build ve Calistirma

```bash
cd KuaforOtomasyon

# Bagimliliklar indir + derle
mvn clean package -DskipTests

# Calistir
java -Dfile.encoding=UTF-8 -jar target/KuaforOtomasyon-1.0.0.jar
```

---

## Sik Sorunlar

### MySQL baglantisi kurulamiyor
- MySQL servisinin calistigini dogrulayin
- `database.properties` sifre ve port kontrol edin
- `localhost` yerine `127.0.0.1` deneyin

### Turkce karakter bozulmasi
- `java -Dfile.encoding=UTF-8 -jar ...` ile baslatin

---

## Proje Yapisi

```
KuaforOtomasyon/
├── pom.xml
├── KURULUM.md
└── src/main/
    ├── java/com/kuafor/
    │   ├── Main.java                ← Giris noktasi
    │   ├── database/                ← DB baglanti yonetimi
    │   ├── model/                   ← Entity siniflari
    │   ├── dao/                     ← Veritabani erisim katmani
    │   ├── service/                 ← Is mantigi katmani
    │   ├── ui/                      ← Swing UI bilesenleri
    │   └── util/                    ← Yardimci siniflar
    └── resources/
        ├── database.properties.example  ← Ornek baglanti ayarlari (bunu kopyala)
        ├── logback.xml
        └── sql/init.sql             ← DB baslatma scripti
```
