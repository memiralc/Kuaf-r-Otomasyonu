package com.kuafor.ui.panels;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.kuafor.model.*;
import com.kuafor.service.*;
import com.kuafor.ui.theme.AppTheme;
import com.kuafor.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class RandevuPanel extends JPanel {

    private final RandevuService randevuService = new RandevuService();
    private final MusteriService musteriService = new MusteriService();
    private final CalisanService calisanService = new CalisanService();
    private final HizmetService  hizmetService  = new HizmetService();

    private JComboBox<String> cbMusteri;
    private JComboBox<String> cbCalisan;
    private JComboBox<String> cbHizmet;
    private DateTimePicker    dateTimePicker;
    private JTextArea         txtNot;
    private JLabel            lblSure;
    private JLabel            lblFiyat;

    private List<Musteri> musteriList;
    private List<Calisan> calisanList;
    private List<Hizmet>  hizmetList;

    public RandevuPanel() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);
        setBorder(new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                                  AppTheme.PADDING_LG, AppTheme.PADDING_LG));
        buildUI();
        veriYukle();
    }

    private void buildUI() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        JLabel title = new JLabel("Yeni Randevu Olustur");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(AppTheme.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
            new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                            AppTheme.PADDING_LG, AppTheme.PADDING_LG)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        addFormLabel(card, gbc, row, "Musteri:");
        cbMusteri = buildComboBox();
        addFormField(card, gbc, row++, cbMusteri);

        JButton btnYeniMusteri = buildLinkButton("+ Yeni musteri ekle");
        btnYeniMusteri.addActionListener(e ->
            MusteriPanel.musteriDialogAc(this, null, this::veriYukle));
        gbc.gridx = 1; gbc.gridy = row++;
        card.add(btnYeniMusteri, gbc);

        addFormLabel(card, gbc, row, "Calisan:");
        cbCalisan = buildComboBox();
        addFormField(card, gbc, row++, cbCalisan);

        addFormLabel(card, gbc, row, "Hizmet:");
        cbHizmet = buildComboBox();
        cbHizmet.addActionListener(e -> hizmetSecildi());
        addFormField(card, gbc, row++, cbHizmet);

        JPanel hizmetBilgi = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hizmetBilgi.setOpaque(false);
        lblSure  = buildInfoLabel("Sure: —");
        lblFiyat = buildInfoLabel("Fiyat: —");
        hizmetBilgi.add(lblSure);
        hizmetBilgi.add(Box.createHorizontalStrut(24));
        hizmetBilgi.add(lblFiyat);
        gbc.gridx = 1; gbc.gridy = row++;
        card.add(hizmetBilgi, gbc);

        addFormLabel(card, gbc, row, "Tarih ve Saat:");
        dateTimePicker = new DateTimePicker();
        dateTimePicker.getDatePicker().setPreferredSize(new Dimension(160, 36));
        dateTimePicker.getTimePicker().setPreferredSize(new Dimension(100, 36));
        addFormField(card, gbc, row++, dateTimePicker);

        addFormLabel(card, gbc, row, "Not:");
        txtNot = new JTextArea(3, 28);
        txtNot.setFont(AppTheme.FONT_INPUT);
        txtNot.setLineWrap(true);
        txtNot.setWrapStyleWord(true);
        JScrollPane notScroll = new JScrollPane(txtNot);
        notScroll.setPreferredSize(new Dimension(320, 70));
        addFormField(card, gbc, row++, notScroll);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.insets = new Insets(20, 8, 8, 8);
        JButton btnKaydet = buildPrimaryButton("Randevu Olustur");
        btnKaydet.addActionListener(e -> randevuKaydet());
        card.add(btnKaydet, gbc);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(card);
        add(centerWrapper, BorderLayout.CENTER);
    }

    public void veriYukle() {
        try {
            musteriList = musteriService.tumMusteriler();
            cbMusteri.removeAllItems();
            cbMusteri.addItem("— Seciniz —");
            for (Musteri m : musteriList)
                cbMusteri.addItem(m.getAd() + " " + m.getSoyad() + " (" + m.getTelefon() + ")");

            calisanList = calisanService.aktifCalisanlar();
            cbCalisan.removeAllItems();
            cbCalisan.addItem("— Seciniz —");
            for (Calisan c : calisanList)
                cbCalisan.addItem(c.getAd() + " " + c.getSoyad());

            hizmetList = hizmetService.aktifHizmetler();
            cbHizmet.removeAllItems();
            cbHizmet.addItem("— Seciniz —");
            for (Hizmet h : hizmetList)
                cbHizmet.addItem(h.getIsim());
        } catch (Exception e) {
            MessageUtil.hataGoster(this, "Veriler yuklenemedi: " + e.getMessage());
        }
    }

    private void hizmetSecildi() {
        int idx = cbHizmet.getSelectedIndex() - 1;
        if (idx >= 0 && hizmetList != null && idx < hizmetList.size()) {
            Hizmet h = hizmetList.get(idx);
            lblSure.setText("Sure: " + h.getSureDakika() + " dk");
            lblFiyat.setText("Fiyat: " + String.format("%.2f TL", h.getFiyat()));
        } else {
            lblSure.setText("Sure: —");
            lblFiyat.setText("Fiyat: —");
        }
    }

    private void randevuKaydet() {
        try {
            int musteriIdx = cbMusteri.getSelectedIndex() - 1;
            int calisanIdx = cbCalisan.getSelectedIndex() - 1;
            int hizmetIdx  = cbHizmet.getSelectedIndex() - 1;

            if (musteriIdx < 0 || calisanIdx < 0 || hizmetIdx < 0) {
                MessageUtil.uyariGoster(this, "Lutfen tum alanlari doldurunuz.");
                return;
            }

            LocalDateTime baslangic = dateTimePicker.getDateTimeStrict();
            if (baslangic == null) {
                MessageUtil.uyariGoster(this, "Lutfen gecerli bir tarih ve saat seciniz.");
                return;
            }

            Hizmet hizmet = hizmetList.get(hizmetIdx);
            LocalDateTime bitis = baslangic.plusMinutes(hizmet.getSureDakika());

            Randevu r = new Randevu(
                musteriList.get(musteriIdx).getId(),
                calisanList.get(calisanIdx).getId(),
                hizmet.getId(),
                baslangic,
                bitis
            );
            r.setNotlar(txtNot.getText().trim());

            randevuService.randevuOlustur(r);
            MessageUtil.basariGoster(this, "Randevu basariyla olusturuldu.");
            formuTemizle();
        } catch (Exception e) {
            MessageUtil.hataGoster(this, e.getMessage());
        }
    }

    private void formuTemizle() {
        cbMusteri.setSelectedIndex(0);
        cbCalisan.setSelectedIndex(0);
        cbHizmet.setSelectedIndex(0);
        dateTimePicker.clear();
        txtNot.setText("");
        lblSure.setText("Sure: —");
        lblFiyat.setText("Fiyat: —");
    }

    private void addFormLabel(JPanel panel, GridBagConstraints gbc, int row, String text) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lbl = new JLabel(text);
        lbl.setFont(AppTheme.FONT_LABEL);
        lbl.setForeground(AppTheme.TEXT_SECONDARY);
        panel.add(lbl, gbc);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, JComponent field) {
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JComboBox<String> buildComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setFont(AppTheme.FONT_INPUT);
        cb.setPreferredSize(new Dimension(320, 36));
        return cb;
    }

    private JButton buildPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(AppTheme.ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(240, 48));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton buildLinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(AppTheme.FONT_SMALL);
        btn.setForeground(AppTheme.ACCENT_LIGHT);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel buildInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(AppTheme.FONT_SMALL);
        lbl.setForeground(AppTheme.ACCENT_LIGHT);
        return lbl;
    }
}
