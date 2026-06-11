package com.kuafor.service;

import com.kuafor.dao.RandevuDAO;
import com.kuafor.model.Randevu;
import com.kuafor.model.RandevuDurum;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RandevuService {

    private final RandevuDAO dao = new RandevuDAO();

    public int randevuOlustur(Randevu r) throws Exception {
        dogrula(r);
        if (dao.cakismaVarMi(r.getCalisanId(), r.getRandevuTarihi(), r.getBitisTarihi(), 0))
            throw new Exception("Secilen calisanin bu saatte baska bir randevusu var!");
        return dao.ekle(r);
    }

    public void randevuGuncelle(Randevu r) throws Exception {
        dogrula(r);
        if (dao.cakismaVarMi(r.getCalisanId(), r.getRandevuTarihi(), r.getBitisTarihi(), r.getId()))
            throw new Exception("Secilen calisanin bu saatte baska bir randevusu var!");
        dao.guncelle(r);
    }

    public List<Randevu> tumRandevular() throws SQLException {
        return dao.tumunuGetir();
    }

    public List<Randevu> gunlukRandevular(LocalDate tarih) throws SQLException {
        return dao.tariheFiltreEt(tarih);
    }

    public List<Randevu> bugunRandevulari() throws SQLException {
        return dao.bugunRandevulari();
    }

    public void durumGuncelle(int id, RandevuDurum durum) throws SQLException {
        dao.durumGuncelle(id, durum);
    }

    public void randevuIptalEt(int id) throws SQLException {
        dao.durumGuncelle(id, RandevuDurum.IPTAL);
    }

    public void randevuSil(int id) throws SQLException {
        dao.sil(id);
    }

    public int bugunRandevuSayisi() throws SQLException {
        return dao.bugunRandevuSayisi();
    }

    public int bugunTamamlananSayisi() throws SQLException {
        return dao.bugunTamamlananSayisi();
    }

    public double haftaninCirosu() throws SQLException {
        return dao.haftaninCirosu();
    }

    private void dogrula(Randevu r) throws Exception {
        if (r.getMusteriId() <= 0)
            throw new Exception("Lutfen bir musteri seciniz.");
        if (r.getCalisanId() <= 0)
            throw new Exception("Lutfen bir calisan seciniz.");
        if (r.getHizmetId() <= 0)
            throw new Exception("Lutfen bir hizmet seciniz.");
        if (r.getRandevuTarihi() == null)
            throw new Exception("Lutfen randevu tarihi ve saatini seciniz.");
        if (r.getRandevuTarihi().isBefore(LocalDateTime.now()))
            throw new Exception("Gecmis bir tarih icin randevu olusturulamaz.");
    }
}
