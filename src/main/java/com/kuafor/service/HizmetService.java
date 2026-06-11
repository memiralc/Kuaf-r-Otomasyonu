package com.kuafor.service;

import com.kuafor.dao.HizmetDAO;
import com.kuafor.model.Hizmet;

import java.sql.SQLException;
import java.util.List;

public class HizmetService {

    private final HizmetDAO dao = new HizmetDAO();

    public int hizmetEkle(Hizmet h) throws Exception {
        dogrula(h);
        return dao.ekle(h);
    }

    public void hizmetGuncelle(Hizmet h) throws Exception {
        dogrula(h);
        dao.guncelle(h);
    }

    public void hizmetSil(int id) throws SQLException {
        dao.sil(id);
    }

    public List<Hizmet> tumHizmetler() throws SQLException {
        return dao.tumunuGetir();
    }

    public List<Hizmet> aktifHizmetler() throws SQLException {
        return dao.aktifHizmetleriGetir();
    }

    private void dogrula(Hizmet h) throws Exception {
        if (h.getIsim() == null || h.getIsim().isBlank())
            throw new Exception("Hizmet adi bos olamaz.");
        if (h.getSureDakika() <= 0)
            throw new Exception("Hizmet suresi 0'dan buyuk olmalidir.");
        if (h.getFiyat() <= 0)
            throw new Exception("Hizmet fiyati 0'dan buyuk olmalidir.");
    }
}
