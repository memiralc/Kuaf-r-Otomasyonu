package com.kuafor.dao;

import com.kuafor.database.DatabaseConnection;
import com.kuafor.model.Calisan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalisanDAO {

    public int ekle(Calisan c) throws SQLException {
        String sql = "INSERT INTO calisanlar (ad, soyad, telefon, uzmanlik, aktif) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getAd());
            ps.setString(2, c.getSoyad());
            ps.setString(3, c.getTelefon());
            ps.setString(4, c.getUzmanlik());
            ps.setBoolean(5, c.isAktif());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public void guncelle(Calisan c) throws SQLException {
        String sql = "UPDATE calisanlar SET ad=?, soyad=?, telefon=?, uzmanlik=?, aktif=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getAd());
            ps.setString(2, c.getSoyad());
            ps.setString(3, c.getTelefon());
            ps.setString(4, c.getUzmanlik());
            ps.setBoolean(5, c.isAktif());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    public void sil(int id) throws SQLException {
        String sql = "DELETE FROM calisanlar WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Calisan> tumunuGetir() throws SQLException {
        String sql = "SELECT * FROM calisanlar ORDER BY ad, soyad";
        List<Calisan> liste = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapRow(rs));
        }
        return liste;
    }

    public List<Calisan> aktifCalisanlariGetir() throws SQLException {
        String sql = "SELECT * FROM calisanlar WHERE aktif=1 ORDER BY ad, soyad";
        List<Calisan> liste = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapRow(rs));
        }
        return liste;
    }

    private Calisan mapRow(ResultSet rs) throws SQLException {
        Calisan c = new Calisan();
        c.setId(rs.getInt("id"));
        c.setAd(rs.getString("ad"));
        c.setSoyad(rs.getString("soyad"));
        c.setTelefon(rs.getString("telefon"));
        c.setUzmanlik(rs.getString("uzmanlik"));
        c.setAktif(rs.getBoolean("aktif"));
        Timestamp ts = rs.getTimestamp("kayit_tarihi");
        if (ts != null) c.setKayitTarihi(ts.toLocalDateTime());
        return c;
    }
}
