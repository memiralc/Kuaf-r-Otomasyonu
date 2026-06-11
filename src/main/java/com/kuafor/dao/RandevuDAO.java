package com.kuafor.dao;

import com.kuafor.database.DatabaseConnection;
import com.kuafor.model.Randevu;
import com.kuafor.model.RandevuDurum;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RandevuDAO {

    public int ekle(Randevu r) throws SQLException {
        String sql = """
            INSERT INTO randevular
              (musteri_id, calisan_id, hizmet_id, randevu_tarihi, bitis_tarihi, durum, notlar)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getMusteriId());
            ps.setInt(2, r.getCalisanId());
            ps.setInt(3, r.getHizmetId());
            ps.setTimestamp(4, Timestamp.valueOf(r.getRandevuTarihi()));
            ps.setTimestamp(5, Timestamp.valueOf(r.getBitisTarihi()));
            ps.setString(6, r.getDurum().name());
            ps.setString(7, r.getNotlar());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public void guncelle(Randevu r) throws SQLException {
        String sql = """
            UPDATE randevular SET musteri_id=?, calisan_id=?, hizmet_id=?,
            randevu_tarihi=?, bitis_tarihi=?, durum=?, notlar=? WHERE id=?
            """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getMusteriId());
            ps.setInt(2, r.getCalisanId());
            ps.setInt(3, r.getHizmetId());
            ps.setTimestamp(4, Timestamp.valueOf(r.getRandevuTarihi()));
            ps.setTimestamp(5, Timestamp.valueOf(r.getBitisTarihi()));
            ps.setString(6, r.getDurum().name());
            ps.setString(7, r.getNotlar());
            ps.setInt(8, r.getId());
            ps.executeUpdate();
        }
    }

    public List<Randevu> tumunuGetir() throws SQLException {
        String sql = """
            SELECT r.*,
                   CONCAT(m.ad, ' ', m.soyad) AS musteri_ad,
                   CONCAT(c.ad, ' ', c.soyad) AS calisan_ad,
                   h.isim AS hizmet_isim,
                   h.fiyat AS hizmet_fiyat
            FROM randevular r
            JOIN musteriler m ON r.musteri_id = m.id
            JOIN calisanlar c ON r.calisan_id = c.id
            JOIN hizmetler  h ON r.hizmet_id  = h.id
            ORDER BY r.randevu_tarihi DESC
            """;
        return executeQuery(sql);
    }

    public List<Randevu> tariheFiltreEt(LocalDate tarih) throws SQLException {
        String sql = """
            SELECT r.*,
                   CONCAT(m.ad, ' ', m.soyad) AS musteri_ad,
                   CONCAT(c.ad, ' ', c.soyad) AS calisan_ad,
                   h.isim AS hizmet_isim,
                   h.fiyat AS hizmet_fiyat
            FROM randevular r
            JOIN musteriler m ON r.musteri_id = m.id
            JOIN calisanlar c ON r.calisan_id = c.id
            JOIN hizmetler  h ON r.hizmet_id  = h.id
            WHERE DATE(r.randevu_tarihi) = ?
            ORDER BY r.randevu_tarihi ASC
            """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tarih));
            return mapResultSet(ps.executeQuery());
        }
    }

    public List<Randevu> bugunRandevulari() throws SQLException {
        return tariheFiltreEt(LocalDate.now());
    }

    public int bugunRandevuSayisi() throws SQLException {
        String sql = "SELECT COUNT(*) FROM randevular WHERE DATE(randevu_tarihi) = CURDATE() AND durum != 'IPTAL'";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int bugunTamamlananSayisi() throws SQLException {
        String sql = "SELECT COUNT(*) FROM randevular WHERE DATE(randevu_tarihi) = CURDATE() AND durum = 'TAMAMLANDI'";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public double haftaninCirosu() throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(h.fiyat), 0)
            FROM randevular r
            JOIN hizmetler h ON r.hizmet_id = h.id
            WHERE r.randevu_tarihi >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
              AND r.durum = 'TAMAMLANDI'
            """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    public boolean cakismaVarMi(int calisanId, LocalDateTime baslangic,
                                 LocalDateTime bitis, int haricRandevuId) throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM randevular
            WHERE calisan_id = ?
              AND id != ?
              AND durum != 'IPTAL'
              AND randevu_tarihi < ?
              AND bitis_tarihi  > ?
            """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, calisanId);
            ps.setInt(2, haricRandevuId);
            ps.setTimestamp(3, Timestamp.valueOf(bitis));
            ps.setTimestamp(4, Timestamp.valueOf(baslangic));
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public void durumGuncelle(int id, RandevuDurum durum) throws SQLException {
        String sql = "UPDATE randevular SET durum = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, durum.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void sil(int id) throws SQLException {
        String sql = "DELETE FROM randevular WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private List<Randevu> executeQuery(String sql) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return mapResultSet(ps.executeQuery());
        }
    }

    private List<Randevu> mapResultSet(ResultSet rs) throws SQLException {
        List<Randevu> liste = new ArrayList<>();
        while (rs.next()) {
            Randevu r = new Randevu();
            r.setId(rs.getInt("id"));
            r.setMusteriId(rs.getInt("musteri_id"));
            r.setCalisanId(rs.getInt("calisan_id"));
            r.setHizmetId(rs.getInt("hizmet_id"));
            r.setRandevuTarihi(rs.getTimestamp("randevu_tarihi").toLocalDateTime());
            r.setBitisTarihi(rs.getTimestamp("bitis_tarihi").toLocalDateTime());
            r.setDurum(RandevuDurum.valueOf(rs.getString("durum")));
            r.setNotlar(rs.getString("notlar"));
            r.setMusteriAdSoyad(rs.getString("musteri_ad"));
            r.setCalisanAdSoyad(rs.getString("calisan_ad"));
            r.setHizmetIsim(rs.getString("hizmet_isim"));
            r.setHizmetFiyat(rs.getDouble("hizmet_fiyat"));
            liste.add(r);
        }
        return liste;
    }
}
