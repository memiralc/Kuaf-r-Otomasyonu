package com.kuafor.ui.panels;

import com.kuafor.model.Musteri;
import com.kuafor.service.MusteriService;
import com.kuafor.ui.components.TableHelper;
import com.kuafor.ui.theme.AppTheme;
import com.kuafor.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MusteriPanel extends JPanel {

    private final MusteriService musteriService = new MusteriService();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtArama;
    private JLabel lblSayisi;
    private List<Musteri> mevcutListe;

    private static final String[] KOLONLAR = {"#", "Ad", "Soyad", "Telefon", "E-posta", "Kayit Tarihi"};

    public MusteriPanel() {
        setLayout(new BorderLayout(0, 12));
        setBackground(AppTheme.BACKGROUND);
        setBorder(new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                                  AppTheme.PADDING_LG, AppTheme.PADDING_LG));
        buildUI();
        musterileriYukle();
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

        JLabel title = new JLabel("Musteri Yonetimi");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        panel.add(title, BorderLayout.WEST);

        JPanel aramaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        aramaPanel.setOpaque(false);

        txtArama = new JTextField(20);
        txtArama.setFont(AppTheme.FONT_INPUT);
        txtArama.setPreferredSize(new Dimension(220, 32));
        txtArama.putClientProperty("JTextField.placeholderText", "Musteri ara...");

        JButton btnAra  = buildActionButton("Ara",   AppTheme.ACCENT, 110);
        JButton btnTumu = buildActionButton("Tumu",  AppTheme.SIDEBAR_HOVER, 110);
        btnAra.addActionListener(e -> aramaYap());
        txtArama.addActionListener(e -> aramaYap());
        btnTumu.addActionListener(e -> musterileriYukle());

        lblSayisi = new JLabel("0 musteri");
        lblSayisi.setFont(AppTheme.FONT_SMALL);
        lblSayisi.setForeground(AppTheme.TEXT_MUTED);

        aramaPanel.add(new JLabel("Arama:") {{
            setFont(AppTheme.FONT_LABEL);
            setForeground(AppTheme.TEXT_SECONDARY);
        }});
        aramaPanel.add(txtArama);
        aramaPanel.add(btnAra);
        aramaPanel.add(btnTumu);
        aramaPanel.add(Box.createHorizontalStrut(12));
        aramaPanel.add(lblSayisi);
        panel.add(aramaPanel, BorderLayout.EAST);
        return panel;
    }

    private JScrollPane buildTablo() {
        tableModel = new DefaultTableModel(KOLONLAR, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        TableHelper.stilUygula(table);

        int[] widths = {40, 120, 120, 130, 180, 140};
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

        JButton btnEkle    = buildActionButton("Yeni Musteri", AppTheme.ACCENT, 160);
        JButton btnDuzenle = buildActionButton("Duzenle",      AppTheme.WARNING, 130);
        JButton btnSil     = buildActionButton("Sil",          AppTheme.DANGER, 110);
        JButton btnYenile  = buildActionButton("Yenile",       AppTheme.SIDEBAR_HOVER, 110);

        btnEkle.addActionListener(e -> musteriDialogAc(this, null, this::musterileriYukle));
        btnDuzenle.addActionListener(e -> seciliMusteriyiDuzenle());
        btnSil.addActionListener(e -> seciliMusteriyiSil());
        btnYenile.addActionListener(e -> musterileriYukle());

        panel.add(btnEkle);
        panel.add(btnDuzenle);
        panel.add(Box.createHorizontalStrut(8));
        panel.add(btnSil);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(btnYenile);
        return panel;
    }

    private void musterileriYukle() {
        try {
            mevcutListe = musteriService.tumMusteriler();
            tabloDoldur(mevcutListe);
        } catch (Exception e) {
            MessageUtil.hataGoster(this, "Musteriler yuklenemedi: " + e.getMessage());
        }
    }

    private void aramaYap() {
        String metin = txtArama.getText().trim();
        if (metin.isEmpty()) { musterileriYukle(); return; }
        try {
            mevcutListe = musteriService.aramaYap(metin);
            tabloDoldur(mevcutListe);
        } catch (Exception e) { MessageUtil.hataGoster(this, e.getMessage()); }
    }

    private void tabloDoldur(List<Musteri> liste) {
        tableModel.setRowCount(0);
        for (int i = 0; i < liste.size(); i++) {
            Musteri m = liste.get(i);
            tableModel.addRow(new Object[]{
                i + 1,
                m.getAd(),
                m.getSoyad(),
                m.getTelefon() != null ? m.getTelefon() : "",
                m.getEmail() != null ? m.getEmail() : "",
                m.getKayitTarihi() != null ? m.getKayitTarihi().toLocalDate().toString() : ""
            });
        }
        lblSayisi.setText(liste.size() + " musteri");
    }

    private void seciliMusteriyiDuzenle() {
        int secilen = table.getSelectedRow();
        if (secilen < 0) { MessageUtil.uyariGoster(this, "Lutfen duzenlenecek musteriyi seciniz."); return; }
        musteriDialogAc(this, mevcutListe.get(secilen), this::musterileriYukle);
    }

    private void seciliMusteriyiSil() {
        int secilen = table.getSelectedRow();
        if (secilen < 0) { MessageUtil.uyariGoster(this, "Lutfen silinecek musteriyi seciniz."); return; }
        Musteri m = mevcutListe.get(secilen);
        if (MessageUtil.onayAl(this, m.getAdSoyad() + " adli musteriyi silmek istiyor musunuz?")) {
            try {
                musteriService.musteriSil(m.getId());
                musterileriYukle();
                MessageUtil.basariGoster(this, "Musteri silindi.");
            } catch (Exception e) { MessageUtil.hataGoster(this, "Silinemedi: " + e.getMessage()); }
        }
    }

    public static void musteriDialogAc(Component parent, Musteri musteri, Runnable yenilemeCallback) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent),
                musteri == null ? "Yeni Musteri" : "Musteriyi Duzenle",
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(430, 360);
        dialog.setLocationRelativeTo(parent);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.SURFACE);
        form.setBorder(new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                                       AppTheme.PADDING_LG, AppTheme.PADDING_LG));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtAd     = createField(musteri != null ? musteri.getAd()      : "");
        JTextField txtSoyad  = createField(musteri != null ? musteri.getSoyad()   : "");
        JTextField txtTel    = createField(musteri != null ? musteri.getTelefon() : "");
        JTextField txtEmail  = createField(musteri != null ? musteri.getEmail()   : "");
        JTextArea  txtNotlar = new JTextArea(3, 20);
        txtNotlar.setFont(AppTheme.FONT_INPUT);
        txtNotlar.setLineWrap(true);
        if (musteri != null && musteri.getNotlar() != null) txtNotlar.setText(musteri.getNotlar());

        addRow(form, gbc, 0, "Ad *:",    txtAd);
        addRow(form, gbc, 1, "Soyad *:", txtSoyad);
        addRow(form, gbc, 2, "Telefon:", txtTel);
        addRow(form, gbc, 3, "E-posta:", txtEmail);
        addRow(form, gbc, 4, "Notlar:",  new JScrollPane(txtNotlar));

        JPanel btnPanel = buildDialogButtons(dialog, () -> {
            try {
                MusteriService svc = new MusteriService();
                Musteri yeni = musteri != null ? musteri : new Musteri();
                yeni.setAd(txtAd.getText().trim());
                yeni.setSoyad(txtSoyad.getText().trim());
                yeni.setTelefon(txtTel.getText().trim().isEmpty() ? null : txtTel.getText().trim());
                yeni.setEmail(txtEmail.getText().trim().isEmpty() ? null : txtEmail.getText().trim());
                yeni.setNotlar(txtNotlar.getText().trim().isEmpty() ? null : txtNotlar.getText().trim());
                if (musteri == null) svc.musteriEkle(yeni);
                else svc.musteriGuncelle(yeni);
                dialog.dispose();
                if (yenilemeCallback != null) yenilemeCallback.run();
            } catch (Exception ex) { MessageUtil.hataGoster(dialog, ex.getMessage()); }
        }, musteri == null ? "Kaydet" : "Guncelle");

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    static JTextField createField(String value) {
        JTextField f = new JTextField(value != null ? value : "");
        f.setFont(AppTheme.FONT_INPUT);
        f.setPreferredSize(new Dimension(240, 32));
        return f;
    }

    static void addRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(AppTheme.FONT_LABEL);
        lbl.setForeground(AppTheme.TEXT_SECONDARY);
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    static JPanel buildDialogButtons(JDialog dialog, Runnable onSave, String saveLabel) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        panel.setBackground(AppTheme.SURFACE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));

        JButton btnIptal  = new JButton("Iptal");
        btnIptal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIptal.setFocusPainted(false);
        btnIptal.setPreferredSize(new Dimension(100, 42));

        JButton btnKaydet = new JButton(saveLabel);
        btnKaydet.setBackground(AppTheme.ACCENT);
        btnKaydet.setForeground(Color.WHITE);
        btnKaydet.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnKaydet.setFocusPainted(false);
        btnKaydet.setBorderPainted(false);
        btnKaydet.setPreferredSize(new Dimension(120, 42));

        btnIptal.addActionListener(e -> dialog.dispose());
        btnKaydet.addActionListener(e -> onSave.run());

        panel.add(btnIptal);
        panel.add(btnKaydet);
        return panel;
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
