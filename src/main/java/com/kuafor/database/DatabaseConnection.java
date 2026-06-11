package com.kuafor.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    private DatabaseConnection() {}

    public static void init() {
        try (InputStream is = DatabaseConnection.class
                .getResourceAsStream("/database.properties")) {
            Properties props = new Properties();
            props.load(is);

            String url = props.getProperty("db.url");
            // Gerekli parametreler URL'de yoksa otomatik ekle
            if (!url.contains("?")) url += "?";
            if (!url.contains("serverTimezone")) url += "&serverTimezone=UTC";
            if (!url.contains("useSSL")) url += "&useSSL=false";
            if (!url.contains("allowPublicKeyRetrieval")) url += "&allowPublicKeyRetrieval=true";

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30_000);
            config.setIdleTimeout(600_000);
            config.setMaxLifetime(1_800_000);

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Veritabanı bağlantısı başlatılamadı!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DatabaseConnection henüz init() edilmedi.");
        }
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
