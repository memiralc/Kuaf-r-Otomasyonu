package com.kuafor.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void kontrol() {
        try (Connection con = DatabaseConnection.getConnection()) {
            DatabaseMetaData meta = con.getMetaData();
            if (!tabloVarMi(meta, "musteriler")) {
                tabloOlustur(con);
            }
        } catch (Exception e) {
            throw new RuntimeException("Veritabani baslatma hatasi: " + e.getMessage(), e);
        }
    }

    private static boolean tabloVarMi(DatabaseMetaData meta, String tablo) {
        try (ResultSet rs = meta.getTables(null, null, tablo, new String[]{"TABLE"})) {
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    private static void tabloOlustur(Connection con) throws Exception {
        try (Statement st = con.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS musteriler (
                    id           INT AUTO_INCREMENT PRIMARY KEY,
                    ad           VARCHAR(50)  NOT NULL,
                    soyad        VARCHAR(50)  NOT NULL,
                    telefon      VARCHAR(15)  UNIQUE,
                    email        VARCHAR(100),
                    notlar       TEXT,
                    kayit_tarihi DATETIME DEFAULT CURRENT_TIMESTAMP,
                    INDEX idx_telefon  (telefon),
                    INDEX idx_ad_soyad (ad, soyad)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci
                """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS calisanlar (
                    id           INT AUTO_INCREMENT PRIMARY KEY,
                    ad           VARCHAR(50)  NOT NULL,
                    soyad        VARCHAR(50)  NOT NULL,
                    telefon      VARCHAR(15),
                    uzmanlik     VARCHAR(100),
                    aktif        TINYINT(1)   DEFAULT 1,
                    kayit_tarihi DATETIME DEFAULT CURRENT_TIMESTAMP
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci
                """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS hizmetler (
                    id           INT AUTO_INCREMENT PRIMARY KEY,
                    isim         VARCHAR(100) NOT NULL,
                    sure_dakika  INT          NOT NULL DEFAULT 30,
                    fiyat        DECIMAL(8,2) NOT NULL,
                    aciklama     TEXT,
                    aktif        TINYINT(1)   DEFAULT 1
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci
                """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS randevular (
                    id                INT AUTO_INCREMENT PRIMARY KEY,
                    musteri_id        INT      NOT NULL,
                    calisan_id        INT      NOT NULL,
                    hizmet_id         INT      NOT NULL,
                    randevu_tarihi    DATETIME NOT NULL,
                    bitis_tarihi      DATETIME NOT NULL,
                    durum             ENUM('BEKLIYOR','TAMAMLANDI','IPTAL') DEFAULT 'BEKLIYOR',
                    notlar            TEXT,
                    olusturma_tarihi  DATETIME DEFAULT CURRENT_TIMESTAMP,
                    guncelleme_tarihi DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (musteri_id) REFERENCES musteriler(id) ON DELETE RESTRICT,
                    FOREIGN KEY (calisan_id) REFERENCES calisanlar(id) ON DELETE RESTRICT,
                    FOREIGN KEY (hizmet_id)  REFERENCES hizmetler(id)  ON DELETE RESTRICT,
                    INDEX idx_randevu_tarihi (randevu_tarihi),
                    INDEX idx_calisan_tarih  (calisan_id, randevu_tarihi),
                    INDEX idx_durum          (durum)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci
                """);
        }
    }
}
