package com.kuafor.ui.components;

import com.kuafor.ui.theme.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatKart extends JPanel {

    private final JLabel lblDeger;
    private final JLabel lblBaslik;

    public StatKart(String baslik, String deger, Color vurguRenk) {
        setLayout(new BorderLayout(0, 0));
        setBackground(AppTheme.SURFACE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
            new EmptyBorder(AppTheme.PADDING_MD, AppTheme.PADDING_MD,
                            AppTheme.PADDING_MD, AppTheme.PADDING_MD)
        ));

        // Üst renkli çizgi
        JPanel ust = new JPanel(new BorderLayout());
        ust.setOpaque(false);
        ust.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel lblBaslikUst = new JLabel(baslik.toUpperCase());
        lblBaslikUst.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lblBaslikUst.setForeground(AppTheme.TEXT_MUTED);
        ust.add(lblBaslikUst, BorderLayout.WEST);

        JPanel renkliBant = new JPanel();
        renkliBant.setPreferredSize(new Dimension(36, 4));
        renkliBant.setBackground(vurguRenk);
        renkliBant.setOpaque(true);
        renkliBant.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel bantSarici = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bantSarici.setOpaque(false);
        bantSarici.add(renkliBant);
        ust.add(bantSarici, BorderLayout.EAST);

        lblDeger = new JLabel(deger);
        lblDeger.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblDeger.setForeground(AppTheme.TEXT_PRIMARY);

        lblBaslik = new JLabel(baslik);
        lblBaslik.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBaslik.setForeground(AppTheme.TEXT_SECONDARY);
        lblBaslik.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        JPanel icerik = new JPanel();
        icerik.setLayout(new BoxLayout(icerik, BoxLayout.Y_AXIS));
        icerik.setOpaque(false);
        icerik.add(lblDeger);
        icerik.add(lblBaslik);

        // Alt renkli sol border efekti
        JPanel solBant = new JPanel();
        solBant.setPreferredSize(new Dimension(4, 0));
        solBant.setBackground(vurguRenk);
        solBant.setOpaque(true);

        add(solBant, BorderLayout.WEST);
        add(Box.createHorizontalStrut(12), BorderLayout.CENTER);

        JPanel sag = new JPanel(new BorderLayout(0, 6));
        sag.setOpaque(false);
        sag.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        sag.add(ust, BorderLayout.NORTH);
        sag.add(lblDeger, BorderLayout.CENTER);
        sag.add(lblBaslik, BorderLayout.SOUTH);
        add(sag, BorderLayout.CENTER);

        setPreferredSize(new Dimension(200, 110));
    }

    public void setDeger(String deger) {
        lblDeger.setText(deger);
    }
}
