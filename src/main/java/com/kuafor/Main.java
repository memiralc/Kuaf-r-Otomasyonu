package com.kuafor;

import com.formdev.flatlaf.FlatDarkLaf;
import com.kuafor.database.DatabaseConnection;
import com.kuafor.database.DatabaseInitializer;
import com.kuafor.ui.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        FlatDarkLaf.setup();

        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("TextComponent.arc", 6);

        try {
            DatabaseConnection.init();
            DatabaseInitializer.kontrol();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Veritabanina baglanılamadı!\n\n" + e.getMessage() +
                "\n\nLutfen MySQL'in calistigından ve\n" +
                "database.properties ayarlarının dogru oldugundan emin olunuz.",
                "Baglanti Hatasi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnection::shutdown));
    }
}
