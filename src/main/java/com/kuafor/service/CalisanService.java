package com.kuafor.service;

import com.kuafor.dao.CalisanDAO;
import com.kuafor.model.Calisan;

import java.sql.SQLException;
import java.util.List;

public class CalisanService {

    private final CalisanDAO dao = new CalisanDAO();

    public int calisanEkle(Calisan c) throws Exception {
        dogrula(c);
        return dao.ekle(c);
    }

    public void calisanGuncelle(Calisan c) throws Exception {
        dogrula(c);
        dao.guncelle(c);
    }

    public void calisanSil(int id) throws SQLException {
        dao.sil(id);
    }

    public List<Calisan> tumCalisanlar() throws SQLException {
        return dao.tumunuGetir();
    }

    public List<Calisan> aktifCalisanlar() throws SQLException {
        return dao.aktifCalisanlariGetir();
    }

    private void dogrula(Calisan c) throws Exception {
        if (c.getAd() == null || c.getAd().isBlank())
            throw new Exception("Calisan adi bos olamaz.");
        if (c.getSoyad() == null || c.getSoyad().isBlank())
            throw new Exception("Calisan soyadi bos olamaz.");
    }
}
