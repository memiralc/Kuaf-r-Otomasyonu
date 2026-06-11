package com.kuafor.ui.panels;

import com.kuafor.model.Calisan;
import com.kuafor.service.CalisanService;
import com.kuafor.ui.components.TableHelper;
import com.kuafor.ui.theme.AppTheme;
import com.kuafor.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CalisanPanel extends JPanel {

    private final CalisanService calisanService = new CalisanService();

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblSayisi;
    private List<Calisan> mevcutListe;

    private static final String[] KOLONLAR = {"#", "Ad", "Soyad", "Telefon", "Uzmanlik", "Durum"};

    public CalisanPanel() {
        setLayout(new BorderLayout(0, 12));
        setBackground(AppTheme.BACKGROUND);
        setBorder(new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                                  AppTheme.PADDING_LG, AppTheme.PADDING_LG));
        buildUI();
        calisanlariYukle();
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTablo(), BorderLayout.CENTER);
        add(buildButonBar(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JLabel title = new JLabel("Calisan Yonetimi");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        panel.add(title, BorderLayout.WEST);

        lblSayisi = new JLabel("");
        lblSayisi.setFont(AppTheme.FONT_SMALL);
        lblSayisi.setForeground(AppTheme.TEXT_MUTED);
        JPanel sag = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        sag.setOpaque(false);
        sag.add(lblSayisi);
        panel.add(sag, BorderLayout.EAST);
        return panel;
    }

    private JScrollPane buildTablo() {
        tableModel = new DefaultTableModel(KOLONLAR, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        TableHelper.stilUygula(table);

        int[] widths = {40, 120, 120, 130, 200, 90};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(AppTheme.SURFACE);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER, 1, true));
        scroll.getViewport().setBackground(AppTheme.SURFACE);
        return scroll;
    }

    private JPanel buildButonBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JButton btnEkle    = buildActionButton("Yeni Calisan", AppTheme.ACCENT, 160);
        JButton btnDuzenle = buildActionButton("Duzenle",      AppTheme.WARNING, 130);
        JButton btnSil     = buildActionButton("Sil",          AppTheme.DANGER, 110);
        JButton btnYenile  = buildActionButton("Yenile",       AppTheme.SIDEBAR_HOVER, 110);

        btnEkle.addActionListener(e -> calisanDialogAc(null));
        btnDuzenle.addActionListener(e -> seciliCalisaniDuzenle());
        btnSil.addActionListener(e -> seciliCalisaniSil());
        btnYenile.addActionListener(e -> calisanlariYukle());

        panel.add(btnEkle);
        panel.add(btnDuzenle);
        panel.add(Box.createHorizontalStrut(8));
        panel.add(btnSil);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(btnYenile);
        return panel;
    }

    private void calisanlariYukle() {
        try {
            mevcutListe = calisanService.tumCalisanlar();
            tabloDoldur(mevcutListe);
        } catch (Exception e) { MessageUtil.hataGoster(this, "Calisanlar yuklenemedi: " + e.getMessage()); }
    }

    private void tabloDoldur(List<Calisan> liste) {
        tableModel.setRowCount(0);
        for (int i = 0; i < liste.size(); i++) {
            Calisan c = liste.get(i);
            tableModel.addRow(new Object[]{
                i + 1, c.getAd(), c.getSoyad(),
                c.getTelefon() != null ? c.getTelefon() : "",
                c.getUzmanlik() != null ? c.getUzmanlik() : "",
                c.isAktif() ? "Aktif" : "Pasif"
            });
        }
        lblSayisi.setText(liste.size() + " calisan");
    }

    private void seciliCalisaniDuzenle() {
        int secilen = table.getSelectedRow();
        if (secilen < 0) { MessageUtil.uyariGoster(this, "Lutfen duzenlenecek calisani seciniz."); return; }
        calisanDialogAc(mevcutListe.get(secilen));
    }

    private void seciliCalisaniSil() {
        int secilen = table.getSelectedRow();
        if (secilen < 0) { MessageUtil.uyariGoster(this, "Lutfen silinecek calisani seciniz."); return; }
        Calisan c = mevcutListe.get(secilen);
        if (MessageUtil.onayAl(this, c.getAdSoyad() + " adli calisani silmek istiyor musunuz?")) {
            try {
                calisanService.calisanSil(c.getId());
                calisanlariYukle();
                MessageUtil.basariGoster(this, "Calisan silindi.");
            } catch (Exception e) { MessageUtil.hataGoster(this, "Silinemedi: " + e.getMessage()); }
        }
    }

    private void calisanDialogAc(Calisan calisan) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                calisan == null ? "Yeni Calisan" : "Calisani Duzenle",
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(420, 310);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.SURFACE);
        form.setBorder(new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                                       AppTheme.PADDING_LG, AppTheme.PADDING_LG));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtAd       = MusteriPanel.createField(calisan != null ? calisan.getAd()       : "");
        JTextField txtSoyad    = MusteriPanel.createField(calisan != null ? calisan.getSoyad()    : "");
        JTextField txtTel      = MusteriPanel.createField(calisan != null ? calisan.getTelefon()  : "");
        JTextField txtUzmanlik = MusteriPanel.createField(calisan != null ? calisan.getUzmanlik() : "");
        JCheckBox  chkAktif    = new JCheckBox("Aktif");
        chkAktif.setSelected(calisan == null || calisan.isAktif());
        chkAktif.setFont(AppTheme.FONT_INPUT);
        chkAktif.setOpaque(false);
        chkAktif.setForeground(AppTheme.TEXT_PRIMARY);

        MusteriPanel.addRow(form, gbc, 0, "Ad *:",      txtAd);
        MusteriPanel.addRow(form, gbc, 1, "Soyad *:",   txtSoyad);
        MusteriPanel.addRow(form, gbc, 2, "Telefon:",   txtTel);
        MusteriPanel.addRow(form, gbc, 3, "Uzmanlik:",  txtUzmanlik);
        MusteriPanel.addRow(form, gbc, 4, "Durum:",     chkAktif);

        JPanel btnPanel = MusteriPanel.buildDialogButtons(dialog, () -> {
            try {
                Calisan yeni = calisan != null ? calisan : new Calisan();
                yeni.setAd(txtAd.getText().trim());
                yeni.setSoyad(txtSoyad.getText().trim());
                yeni.setTelefon(txtTel.getText().trim().isEmpty() ? null : txtTel.getText().trim());
                yeni.setUzmanlik(txtUzmanlik.getText().trim().isEmpty() ? null : txtUzmanlik.getText().trim());
                yeni.setAktif(chkAktif.isSelected());
                if (calisan == null) calisanService.calisanEkle(yeni);
                else calisanService.calisanGuncelle(yeni);
                dialog.dispose();
                calisanlariYukle();
            } catch (Exception ex) { MessageUtil.hataGoster(dialog, ex.getMessage()); }
        }, calisan == null ? "Kaydet" : "Guncelle");

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JButton buildActionButton(String text, Color bg, int width) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(width, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
