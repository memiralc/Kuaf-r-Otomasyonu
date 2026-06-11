-- Kuaför Otomasyonu Veritabanı Başlangıç Scripti
-- MySQL 8.0+ için hazırlanmıştır

CREATE DATABASE IF NOT EXISTS kuafor_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_turkish_ci;

USE kuafor_db;

-- Müşteriler tablosu
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- Çalışanlar tablosu
CREATE TABLE IF NOT EXISTS calisanlar (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    ad           VARCHAR(50)  NOT NULL,
    soyad        VARCHAR(50)  NOT NULL,
    telefon      VARCHAR(15),
    uzmanlik     VARCHAR(100),
    aktif        TINYINT(1)   DEFAULT 1,
    kayit_tarihi DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- Hizmetler tablosu
CREATE TABLE IF NOT EXISTS hizmetler (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    isim         VARCHAR(100) NOT NULL,
    sure_dakika  INT          NOT NULL DEFAULT 30,
    fiyat        DECIMAL(8,2) NOT NULL,
    aciklama     TEXT,
    aktif        TINYINT(1)   DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- Randevular tablosu
CREATE TABLE IF NOT EXISTS randevular (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    musteri_id        INT     NOT NULL,
    calisan_id        INT     NOT NULL,
    hizmet_id         INT     NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

-- Örnek hizmet verileri
INSERT IGNORE INTO hizmetler (isim, sure_dakika, fiyat) VALUES
  ('Saç Kesimi',   30,  150.00),
  ('Sakal Tıraşı', 20,  100.00),
  ('Saç + Sakal',  50,  220.00),
  ('Boya',         90,  350.00),
  ('Röfle',       120,  450.00);

-- Örnek çalışan verileri
INSERT IGNORE INTO calisanlar (ad, soyad, uzmanlik) VALUES
  ('Mehmet', 'Yılmaz', 'Saç Kesimi & Boya'),
  ('Ali',    'Kaya',   'Erkek Tıraşı'),
  ('Ayşe',   'Demir',  'Boya & Röfle');

-- Örnek müşteri verileri
INSERT IGNORE INTO musteriler (ad, soyad, telefon, email) VALUES
  ('Ahmet', 'Yılmaz', '0555 111 22 33', 'ahmet@email.com'),
  ('Fatma', 'Kaya',   '0555 444 55 66', 'fatma@email.com'),
  ('Can',   'Demir',  '0555 777 88 99', 'can@email.com');
