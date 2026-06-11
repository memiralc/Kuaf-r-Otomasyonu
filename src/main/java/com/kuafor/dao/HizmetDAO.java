package com.kuafor.dao;

import com.kuafor.database.DatabaseConnection;
import com.kuafor.model.Hizmet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HizmetDAO {

    public int ekle(Hizmet h) throws SQLException {
        String sql = "INSERT INTO hizmetler (isim, sure_dakika, fiyat, aciklama, aktif) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, h.getIsim());
            ps.setInt(2, h.getSureDakika());
            ps.setDouble(3, h.getFiyat());
            ps.setString(4, h.getAciklama());
            ps.setBoolean(5, h.isAktif());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public void guncelle(Hizmet h) throws SQLException {
        String sql = "UPDATE hizmetler SET isim=?, sure_dakika=?, fiyat=?, aciklama=?, aktif=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, h.getIsim());
            ps.setInt(2, h.getSureDakika());
            ps.setDouble(3, h.getFiyat());
            ps.setString(4, h.getAciklama());
            ps.setBoolean(5, h.isAktif());
            ps.setInt(6, h.getId());
            ps.executeUpdate();
        }
    }

    public void sil(int id) throws SQLException {
        String sql = "DELETE FROM hizmetler WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Hizmet> tumunuGetir() throws SQLException {
        String sql = "SELECT * FROM hizmetler ORDER BY isim";
        List<Hizmet> liste = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapRow(rs));
        }
        return liste;
    }

    public List<Hizmet> aktifHizmetleriGetir() throws SQLException {
        String sql = "SELECT * FROM hizmetler WHERE aktif=1 ORDER BY isim";
        List<Hizmet> liste = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapRow(rs));
        }
        return liste;
    }

    private Hizmet mapRow(ResultSet rs) throws SQLException {
        Hizmet h = new Hizmet();
        h.setId(rs.getInt("id"));
        h.setIsim(rs.getString("isim"));
        h.setSureDakika(rs.getInt("sure_dakika"));
        h.setFiyat(rs.getDouble("fiyat"));
        h.setAciklama(rs.getString("aciklama"));
        h.setAktif(rs.getBoolean("aktif"));
        return h;
    }
}
