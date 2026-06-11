package com.kuafor.service;

import com.kuafor.dao.MusteriDAO;
import com.kuafor.model.Musteri;

import java.sql.SQLException;
import java.util.List;

public class MusteriService {

    private final MusteriDAO dao = new MusteriDAO();

    public int musteriEkle(Musteri m) throws Exception {
        dogrula(m);
        return dao.ekle(m);
    }

    public void musteriGuncelle(Musteri m) throws Exception {
        dogrula(m);
        dao.guncelle(m);
    }

    public void musteriSil(int id) throws SQLException {
        dao.sil(id);
    }

    public List<Musteri> tumMusteriler() throws SQLException {
        return dao.tumunuGetir();
    }

    public List<Musteri> aramaYap(String metin) throws SQLException {
        return dao.aramaYap(metin);
    }

    public int toplamSayisi() throws SQLException {
        return dao.toplamSayisi();
    }

    private void dogrula(Musteri m) throws Exception {
        if (m.getAd() == null || m.getAd().isBlank())
            throw new Exception("Musteri adi bos olamaz.");
        if (m.getSoyad() == null || m.getSoyad().isBlank())
            throw new Exception("Musteri soyadi bos olamaz.");
        if (m.getTelefon() != null && !m.getTelefon().isBlank()
                && !m.getTelefon().matches("^[0-9 \\-+()]{7,15}$"))
            throw new Exception("Gecersiz telefon numarasi formati.");
    }
}
