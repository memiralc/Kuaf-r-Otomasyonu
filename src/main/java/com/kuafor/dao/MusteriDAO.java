package com.kuafor.dao;

import com.kuafor.database.DatabaseConnection;
import com.kuafor.model.Musteri;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusteriDAO {

    public int ekle(Musteri m) throws SQLException {
        String sql = "INSERT INTO musteriler (ad, soyad, telefon, email, notlar) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getAd());
            ps.setString(2, m.getSoyad());
            ps.setString(3, m.getTelefon());
            ps.setString(4, m.getEmail());
            ps.setString(5, m.getNotlar());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public void guncelle(Musteri m) throws SQLException {
        String sql = "UPDATE musteriler SET ad=?, soyad=?, telefon=?, email=?, notlar=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getAd());
            ps.setString(2, m.getSoyad());
            ps.setString(3, m.getTelefon());
            ps.setString(4, m.getEmail());
            ps.setString(5, m.getNotlar());
            ps.setInt(6, m.getId());
            ps.executeUpdate();
        }
    }

    public void sil(int id) throws SQLException {
        String sql = "DELETE FROM musteriler WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Musteri> tumunuGetir() throws SQLException {
        String sql = "SELECT * FROM musteriler ORDER BY ad, soyad";
        List<Musteri> liste = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapRow(rs));
        }
        return liste;
    }

    public List<Musteri> aramaYap(String aramaMetni) throws SQLException {
        String sql = """
            SELECT * FROM musteriler
            WHERE ad LIKE ? OR soyad LIKE ? OR telefon LIKE ? OR email LIKE ?
            ORDER BY ad, soyad
            """;
        String pattern = "%" + aramaMetni + "%";
        List<Musteri> liste = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(mapRow(rs));
            }
        }
        return liste;
    }

    public int toplamSayisi() throws SQLException {
        String sql = "SELECT COUNT(*) FROM musteriler";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Musteri mapRow(ResultSet rs) throws SQLException {
        Musteri m = new Musteri();
        m.setId(rs.getInt("id"));
        m.setAd(rs.getString("ad"));
        m.setSoyad(rs.getString("soyad"));
        m.setTelefon(rs.getString("telefon"));
        m.setEmail(rs.getString("email"));
        m.setNotlar(rs.getString("notlar"));
        Timestamp ts = rs.getTimestamp("kayit_tarihi");
        if (ts != null) m.setKayitTarihi(ts.toLocalDateTime());
        return m;
    }
}
