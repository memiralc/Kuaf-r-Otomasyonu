package com.kuafor.model;

import java.time.LocalDateTime;

public class Calisan {
    private int id;
    private String ad;
    private String soyad;
    private String telefon;
    private String uzmanlik;
    private boolean aktif;
    private LocalDateTime kayitTarihi;

    public Calisan() {}

    public Calisan(String ad, String soyad, String telefon, String uzmanlik) {
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
        this.uzmanlik = uzmanlik;
        this.aktif = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getUzmanlik() { return uzmanlik; }
    public void setUzmanlik(String uzmanlik) { this.uzmanlik = uzmanlik; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public LocalDateTime getKayitTarihi() { return kayitTarihi; }
    public void setKayitTarihi(LocalDateTime kayitTarihi) { this.kayitTarihi = kayitTarihi; }

    public String getAdSoyad() { return ad + " " + soyad; }

    @Override
    public String toString() {
        return ad + " " + soyad;
    }
}
