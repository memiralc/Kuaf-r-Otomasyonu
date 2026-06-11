package com.kuafor.model;

public enum RandevuDurum {
    BEKLIYOR("Bekliyor"),
    TAMAMLANDI("Tamamlandi"),
    IPTAL("Iptal");

    private final String goruntu;

    RandevuDurum(String goruntu) {
        this.goruntu = goruntu;
    }

    public String getGoruntu() {
        return goruntu;
    }

    @Override
    public String toString() {
        return goruntu;
    }
}
