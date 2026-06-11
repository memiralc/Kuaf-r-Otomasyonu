package com.kuafor.ui.panels;

import com.kuafor.model.Hizmet;
import com.kuafor.service.HizmetService;
import com.kuafor.ui.components.TableHelper;
import com.kuafor.ui.theme.AppTheme;
import com.kuafor.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HizmetPanel extends JPanel {

    private final HizmetService hizmetService = new HizmetService();

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblSayisi;
    private List<Hizmet> mevcutListe;

    private static final String[] KOLONLAR = {"#", "Hizmet Adi", "Sure (dk)", "Fiyat (TL)", "Aciklama", "Durum"};

    public HizmetPanel() {
        setLayout(new BorderLayout(0, 12));
        setBackground(AppTheme.BACKGROUND);
        setBorder(new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                                  AppTheme.PADDING_LG, AppTheme.PADDING_LG));
        buildUI();
        hizmetleriYukle();
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

        JLabel title = new JLabel("Hizmet Yonetimi");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        panel.add(title, BorderLayout.WEST);

        lblSayisi = new JLabel("");
        lblSayisi.setFont(AppTheme.FONT_SMALL);
        lblSayisi.setForeground(AppTheme.TEXT_MUTED);
        JPanel sag = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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

        int[] widths = {40, 160, 90, 100, 220, 80};
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

        JButton btnEkle    = buildActionButton("Yeni Hizmet", AppTheme.ACCENT, 160);
        JButton btnDuzenle = buildActionButton("Duzenle",     AppTheme.WARNING, 130);
        JButton btnSil     = buildActionButton("Sil",         AppTheme.DANGER, 110);
        JButton btnYenile  = buildActionButton("Yenile",      AppTheme.SIDEBAR_HOVER, 110);

        btnEkle.addActionListener(e -> hizmetDialogAc(null));
        btnDuzenle.addActionListener(e -> seciliHizmetiDuzenle());
        btnSil.addActionListener(e -> seciliHizmetiSil());
        btnYenile.addActionListener(e -> hizmetleriYukle());

        panel.add(btnEkle);
        panel.add(btnDuzenle);
        panel.add(Box.createHorizontalStrut(8));
        panel.add(btnSil);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(btnYenile);
        return panel;
    }

    private void hizmetleriYukle() {
        try {
            mevcutListe = hizmetService.tumHizmetler();
            tabloDoldur(mevcutListe);
        } catch (Exception e) { MessageUtil.hataGoster(this, "Hizmetler yuklenemedi: " + e.getMessage()); }
    }

    private void tabloDoldur(List<Hizmet> liste) {
        tableModel.setRowCount(0);
        for (int i = 0; i < liste.size(); i++) {
            Hizmet h = liste.get(i);
            tableModel.addRow(new Object[]{
                i + 1, h.getIsim(), h.getSureDakika(),
                String.format("%.2f", h.getFiyat()),
                h.getAciklama() != null ? h.getAciklama() : "",
                h.isAktif() ? "Aktif" : "Pasif"
            });
        }
        lblSayisi.setText(liste.size() + " hizmet");
    }

    private void seciliHizmetiDuzenle() {
        int secilen = table.getSelectedRow();
        if (secilen < 0) { MessageUtil.uyariGoster(this, "Lutfen duzenlenecek hizmeti seciniz."); return; }
        hizmetDialogAc(mevcutListe.get(secilen));
    }

    private void seciliHizmetiSil() {
        int secilen = table.getSelectedRow();
        if (secilen < 0) { MessageUtil.uyariGoster(this, "Lutfen silinecek hizmeti seciniz."); return; }
        Hizmet h = mevcutListe.get(secilen);
        if (MessageUtil.onayAl(this, "\"" + h.getIsim() + "\" hizmetini silmek istiyor musunuz?")) {
            try {
                hizmetService.hizmetSil(h.getId());
                hizmetleriYukle();
                MessageUtil.basariGoster(this, "Hizmet silindi.");
            } catch (Exception e) { MessageUtil.hataGoster(this, "Silinemedi: " + e.getMessage()); }
        }
    }

    private void hizmetDialogAc(Hizmet hizmet) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                hizmet == null ? "Yeni Hizmet" : "Hizmeti Duzenle",
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(430, 300);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.SURFACE);
        form.setBorder(new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                                       AppTheme.PADDING_LG, AppTheme.PADDING_LG));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtIsim     = MusteriPanel.createField(hizmet != null ? hizmet.getIsim()     : "");
        JTextField txtSure     = MusteriPanel.createField(hizmet != null ? String.valueOf(hizmet.getSureDakika()) : "30");
        JTextField txtFiyat    = MusteriPanel.createField(hizmet != null ? String.valueOf(hizmet.getFiyat())      : "");
        JTextField txtAciklama = MusteriPanel.createField(hizmet != null ? hizmet.getAciklama() : "");
        JCheckBox  chkAktif    = new JCheckBox("Aktif");
        chkAktif.setSelected(hizmet == null || hizmet.isAktif());
        chkAktif.setFont(AppTheme.FONT_INPUT);
        chkAktif.setOpaque(false);
        chkAktif.setForeground(AppTheme.TEXT_PRIMARY);

        MusteriPanel.addRow(form, gbc, 0, "Hizmet Adi *:", txtIsim);
        MusteriPanel.addRow(form, gbc, 1, "Sure (dk) *:",  txtSure);
        MusteriPanel.addRow(form, gbc, 2, "Fiyat (TL) *:", txtFiyat);
        MusteriPanel.addRow(form, gbc, 3, "Aciklama:",     txtAciklama);
        MusteriPanel.addRow(form, gbc, 4, "Durum:",        chkAktif);

        JPanel btnPanel = MusteriPanel.buildDialogButtons(dialog, () -> {
            try {
                Hizmet yeni = hizmet != null ? hizmet : new Hizmet();
                yeni.setIsim(txtIsim.getText().trim());
                yeni.setSureDakika(Integer.parseInt(txtSure.getText().trim()));
                yeni.setFiyat(Double.parseDouble(txtFiyat.getText().trim().replace(",", ".")));
                yeni.setAciklama(txtAciklama.getText().trim().isEmpty() ? null : txtAciklama.getText().trim());
                yeni.setAktif(chkAktif.isSelected());
                if (hizmet == null) hizmetService.hizmetEkle(yeni);
                else hizmetService.hizmetGuncelle(yeni);
                dialog.dispose();
                hizmetleriYukle();
            } catch (NumberFormatException ex) {
                MessageUtil.hataGoster(dialog, "Sure ve fiyat gecerli sayi olmalidir.");
            } catch (Exception ex) { MessageUtil.hataGoster(dialog, ex.getMessage()); }
        }, hizmet == null ? "Kaydet" : "Guncelle");

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
