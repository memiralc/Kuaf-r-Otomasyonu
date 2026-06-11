package com.kuafor.model;

import java.time.LocalDateTime;

public class Musteri {
    private int id;
    private String ad;
    private String soyad;
    private String telefon;
    private String email;
    private String notlar;
    private LocalDateTime kayitTarihi;

    public Musteri() {}

    public Musteri(String ad, String soyad, String telefon, String email) {
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNotlar() { return notlar; }
    public void setNotlar(String notlar) { this.notlar = notlar; }

    public LocalDateTime getKayitTarihi() { return kayitTarihi; }
    public void setKayitTarihi(LocalDateTime kayitTarihi) { this.kayitTarihi = kayitTarihi; }

    public String getAdSoyad() { return ad + " " + soyad; }

    @Override
    public String toString() {
        return ad + " " + soyad + (telefon != null ? " (" + telefon + ")" : "");
    }
}
