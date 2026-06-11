package com.kuafor.model;

import java.time.LocalDateTime;

public class Randevu {
    private int id;
    private int musteriId;
    private int calisanId;
    private int hizmetId;
    private LocalDateTime randevuTarihi;
    private LocalDateTime bitisTarihi;
    private RandevuDurum durum;
    private String notlar;

    private String musteriAdSoyad;
    private String calisanAdSoyad;
    private String hizmetIsim;
    private double hizmetFiyat;

    public Randevu() {}

    public Randevu(int musteriId, int calisanId, int hizmetId,
                   LocalDateTime randevuTarihi, LocalDateTime bitisTarihi) {
        this.musteriId = musteriId;
        this.calisanId = calisanId;
        this.hizmetId = hizmetId;
        this.randevuTarihi = randevuTarihi;
        this.bitisTarihi = bitisTarihi;
        this.durum = RandevuDurum.BEKLIYOR;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMusteriId() { return musteriId; }
    public void setMusteriId(int musteriId) { this.musteriId = musteriId; }

    public int getCalisanId() { return calisanId; }
    public void setCalisanId(int calisanId) { this.calisanId = calisanId; }

    public int getHizmetId() { return hizmetId; }
    public void setHizmetId(int hizmetId) { this.hizmetId = hizmetId; }

    public LocalDateTime getRandevuTarihi() { return randevuTarihi; }
    public void setRandevuTarihi(LocalDateTime randevuTarihi) { this.randevuTarihi = randevuTarihi; }

    public LocalDateTime getBitisTarihi() { return bitisTarihi; }
    public void setBitisTarihi(LocalDateTime bitisTarihi) { this.bitisTarihi = bitisTarihi; }

    public RandevuDurum getDurum() { return durum; }
    public void setDurum(RandevuDurum durum) { this.durum = durum; }

    public String getNotlar() { return notlar; }
    public void setNotlar(String notlar) { this.notlar = notlar; }

    public String getMusteriAdSoyad() { return musteriAdSoyad; }
    public void setMusteriAdSoyad(String musteriAdSoyad) { this.musteriAdSoyad = musteriAdSoyad; }

    public String getCalisanAdSoyad() { return calisanAdSoyad; }
    public void setCalisanAdSoyad(String calisanAdSoyad) { this.calisanAdSoyad = calisanAdSoyad; }

    public String getHizmetIsim() { return hizmetIsim; }
    public void setHizmetIsim(String hizmetIsim) { this.hizmetIsim = hizmetIsim; }

    public double getHizmetFiyat() { return hizmetFiyat; }
    public void setHizmetFiyat(double hizmetFiyat) { this.hizmetFiyat = hizmetFiyat; }

    @Override
    public String toString() {
        return String.format("Randevu{id=%d, musteri='%s', tarih=%s, durum=%s}",
                id, musteriAdSoyad, randevuTarihi, durum);
    }
}
