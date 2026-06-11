package com.kuafor.model;

public class Hizmet {
    private int id;
    private String isim;
    private int sureDakika;
    private double fiyat;
    private String aciklama;
    private boolean aktif;

    public Hizmet() {}

    public Hizmet(String isim, int sureDakika, double fiyat) {
        this.isim = isim;
        this.sureDakika = sureDakika;
        this.fiyat = fiyat;
        this.aktif = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIsim() { return isim; }
    public void setIsim(String isim) { this.isim = isim; }

    public int getSureDakika() { return sureDakika; }
    public void setSureDakika(int sureDakika) { this.sureDakika = sureDakika; }

    public double getFiyat() { return fiyat; }
    public void setFiyat(double fiyat) { this.fiyat = fiyat; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    @Override
    public String toString() {
        return isim + " (" + sureDakika + " dk - " + String.format("%.2f ₺", fiyat) + ")";
    }
}
