package com.kuafor.ui.panels;

import com.github.lgooddatepicker.components.DatePicker;
import com.kuafor.model.Randevu;
import com.kuafor.model.RandevuDurum;
import com.kuafor.service.RandevuService;
import com.kuafor.ui.components.TableHelper;
import com.kuafor.ui.theme.AppTheme;
import com.kuafor.util.DateUtil;
import com.kuafor.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class RandevuListePanel extends JPanel {

    private final RandevuService randevuService = new RandevuService();

    private JTable table;
    private DefaultTableModel tableModel;
    private DatePicker datePicker;
    private JLabel lblToplamSayisi;
    private List<Randevu> mevcutListe;

    private static final String[] KOLONLAR = {
        "#", "Musteri", "Calisan", "Hizmet",
        "Baslangic", "Bitis", "Sure", "Fiyat", "Durum", "Not"
    };

    public RandevuListePanel() {
        setLayout(new BorderLayout(0, 12));
        setBackground(AppTheme.BACKGROUND);
        setBorder(new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                                  AppTheme.PADDING_LG, AppTheme.PADDING_LG));
        buildUI();
        randevulariYukle();
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

        JLabel title = new JLabel("Randevu Listesi");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        panel.add(title, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterPanel.setOpaque(false);

        JLabel lblTarih = new JLabel("Tarih:");
        lblTarih.setFont(AppTheme.FONT_LABEL);
        lblTarih.setForeground(AppTheme.TEXT_SECONDARY);

        datePicker = new DatePicker();
        datePicker.setPreferredSize(new Dimension(160, 32));

        JButton btnFiltrele = buildActionButton("Filtrele", AppTheme.ACCENT, 120);
        btnFiltrele.addActionListener(e -> tarihFiltre());

        JButton btnTumu = buildActionButton("Tumunu Goster", AppTheme.SIDEBAR_HOVER, 150);
        btnTumu.addActionListener(e -> randevulariYukle());

        lblToplamSayisi = new JLabel("0 randevu");
        lblToplamSayisi.setFont(AppTheme.FONT_SMALL);
        lblToplamSayisi.setForeground(AppTheme.TEXT_MUTED);

        filterPanel.add(lblTarih);
        filterPanel.add(datePicker);
        filterPanel.add(btnFiltrele);
        filterPanel.add(btnTumu);
        filterPanel.add(Box.createHorizontalStrut(12));
        filterPanel.add(lblToplamSayisi);
        panel.add(filterPanel, BorderLayout.EAST);
        return panel;
    }

    private JScrollPane buildTablo() {
        tableModel = new DefaultTableModel(KOLONLAR, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        TableHelper.stilUygula(table);

        int[] widths = {40, 140, 120, 110, 120, 100, 60, 90, 110, 120};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.getColumnModel().getColumn(8).setCellRenderer(durumRenderer());

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

        JButton btnTamamlandi = buildActionButton("Tamamlandi", AppTheme.SUCCESS, 150);
        JButton btnIptal      = buildActionButton("Iptal Et",   AppTheme.WARNING, 130);
        JButton btnSil        = buildActionButton("Sil",        AppTheme.DANGER,  110);
        JButton btnYenile     = buildActionButton("Yenile",     AppTheme.SIDEBAR_HOVER, 110);

        btnTamamlandi.addActionListener(e -> durumDegistir(RandevuDurum.TAMAMLANDI));
        btnIptal.addActionListener(e -> durumDegistir(RandevuDurum.IPTAL));
        btnSil.addActionListener(e -> randevuSil());
        btnYenile.addActionListener(e -> randevulariYukle());

        panel.add(btnTamamlandi);
        panel.add(btnIptal);
        panel.add(Box.createHorizontalStrut(8));
        panel.add(btnSil);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(btnYenile);
        return panel;
    }

    private void randevulariYukle() {
        SwingWorker<List<Randevu>, Void> worker = new SwingWorker<>() {
            @Override protected List<Randevu> doInBackground() throws Exception {
                return randevuService.tumRandevular();
            }
            @Override protected void done() {
                try { mevcutListe = get(); tabloDoldur(mevcutListe); }
                catch (Exception e) { MessageUtil.hataGoster(RandevuListePanel.this, e.getMessage()); }
            }
        };
        worker.execute();
    }

    private void tarihFiltre() {
        if (datePicker.getDate() == null) {
            MessageUtil.uyariGoster(this, "Lutfen filtre icin bir tarih seciniz.");
            return;
        }
        SwingWorker<List<Randevu>, Void> worker = new SwingWorker<>() {
            @Override protected List<Randevu> doInBackground() throws Exception {
                return randevuService.gunlukRandevular(datePicker.getDate());
            }
            @Override protected void done() {
                try { mevcutListe = get(); tabloDoldur(mevcutListe); }
                catch (Exception e) { MessageUtil.hataGoster(RandevuListePanel.this, e.getMessage()); }
            }
        };
        worker.execute();
    }

    private void tabloDoldur(List<Randevu> liste) {
        tableModel.setRowCount(0);
        for (int i = 0; i < liste.size(); i++) {
            Randevu r = liste.get(i);
            long dakika = java.time.Duration.between(
                r.getRandevuTarihi(), r.getBitisTarihi()).toMinutes();
            tableModel.addRow(new Object[]{
                i + 1,
                r.getMusteriAdSoyad(),
                r.getCalisanAdSoyad(),
                r.getHizmetIsim(),
                DateUtil.formatTarihSaat(r.getRandevuTarihi()),
                DateUtil.formatTarihSaat(r.getBitisTarihi()),
                dakika + " dk",
                String.format("%.2f TL", r.getHizmetFiyat()),
                r.getDurum().getGoruntu(),
                r.getNotlar() != null ? r.getNotlar() : ""
            });
        }
        lblToplamSayisi.setText(liste.size() + " randevu");
    }

    private void durumDegistir(RandevuDurum yeniDurum) {
        int secilen = table.getSelectedRow();
        if (secilen < 0) { MessageUtil.uyariGoster(this, "Lutfen bir randevu seciniz."); return; }
        try {
            randevuService.durumGuncelle(mevcutListe.get(secilen).getId(), yeniDurum);
            randevulariYukle();
            MessageUtil.basariGoster(this, "Randevu durumu guncellendi.");
        } catch (Exception e) { MessageUtil.hataGoster(this, e.getMessage()); }
    }

    private void randevuSil() {
        int secilen = table.getSelectedRow();
        if (secilen < 0) { MessageUtil.uyariGoster(this, "Lutfen silinecek randevuyu seciniz."); return; }
        if (MessageUtil.onayAl(this, "Bu randevuyu kalici olarak silmek istediginize emin misiniz?")) {
            try {
                randevuService.randevuSil(mevcutListe.get(secilen).getId());
                randevulariYukle();
                MessageUtil.basariGoster(this, "Randevu silindi.");
            } catch (Exception e) { MessageUtil.hataGoster(this, e.getMessage()); }
        }
    }

    private TableCellRenderer durumRenderer() {
        return (tbl, value, isSelected, hasFocus, row, col) -> {
            JLabel lbl = new JLabel(value != null ? value.toString() : "");
            lbl.setOpaque(true);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setFont(AppTheme.FONT_LABEL);
            lbl.setBorder(new EmptyBorder(2, 8, 2, 8));
            String durum = value != null ? value.toString() : "";
            switch (durum) {
                case "Bekliyor"    -> { lbl.setBackground(new Color(0xF5, 0x9E, 0x0B, 55)); lbl.setForeground(AppTheme.WARNING); }
                case "Tamamlandi"  -> { lbl.setBackground(new Color(0x10, 0xB9, 0x81, 55)); lbl.setForeground(AppTheme.SUCCESS); }
                case "Iptal"       -> { lbl.setBackground(new Color(0xEF, 0x44, 0x44, 55)); lbl.setForeground(AppTheme.DANGER); }
                default           -> { lbl.setBackground(AppTheme.SURFACE); lbl.setForeground(AppTheme.TEXT_MUTED); }
            }
            if (isSelected) lbl.setBackground(AppTheme.TABLE_SELECTED);
            return lbl;
        };
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
