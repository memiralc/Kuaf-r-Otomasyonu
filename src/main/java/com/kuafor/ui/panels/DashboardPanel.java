package com.kuafor.ui.panels;

import com.kuafor.model.Randevu;
import com.kuafor.service.MusteriService;
import com.kuafor.service.RandevuService;
import com.kuafor.ui.components.StatKart;
import com.kuafor.ui.components.TableHelper;
import com.kuafor.ui.theme.AppTheme;
import com.kuafor.util.DateUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final RandevuService randevuService = new RandevuService();
    private final MusteriService musteriService = new MusteriService();

    private StatKart kartBugunRandevu;
    private StatKart kartToplamMusteri;
    private StatKart kartHaftaCiro;
    private StatKart kartTamamlanan;
    private DefaultTableModel yaklasanModel;

    public DashboardPanel() {
        setLayout(new BorderLayout(0, AppTheme.PADDING_MD));
        setBackground(AppTheme.BACKGROUND);
        setBorder(new EmptyBorder(AppTheme.PADDING_LG, AppTheme.PADDING_LG,
                                  AppTheme.PADDING_LG, AppTheme.PADDING_LG));
        buildUI();
        veriYukle();
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);
        add(buildKartlar(), BorderLayout.CENTER);

        JPanel alt = new JPanel(new BorderLayout(0, AppTheme.PADDING_SM));
        alt.setOpaque(false);
        alt.add(buildYaklasanBaslik(), BorderLayout.NORTH);
        alt.add(buildYaklasanTablo(), BorderLayout.CENTER);
        add(alt, BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel title = new JLabel("Dashboard");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        panel.add(title, BorderLayout.WEST);

        JLabel tarih = new JLabel("Bugun: " + LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        tarih.setFont(AppTheme.FONT_SUBTITLE);
        tarih.setForeground(AppTheme.TEXT_MUTED);
        panel.add(tarih, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildKartlar() {
        kartBugunRandevu  = new StatKart("Bugunku Randevu",    "—", AppTheme.ACCENT);
        kartToplamMusteri = new StatKart("Toplam Musteri",     "—", AppTheme.SUCCESS);
        kartHaftaCiro     = new StatKart("Bu Hafta Ciro",      "—", AppTheme.WARNING);
        kartTamamlanan    = new StatKart("Tamamlanan (Bugun)", "—", new Color(0x06, 0xB6, 0xD4));

        JPanel panel = new JPanel(new GridLayout(1, 4, AppTheme.PADDING_MD, 0));
        panel.setOpaque(false);
        panel.add(kartBugunRandevu);
        panel.add(kartToplamMusteri);
        panel.add(kartHaftaCiro);
        panel.add(kartTamamlanan);
        panel.setPreferredSize(new Dimension(0, 120));
        return panel;
    }

    private JLabel buildYaklasanBaslik() {
        JLabel lbl = new JLabel("Yaklasan Randevular (Bugun)");
        lbl.setFont(AppTheme.FONT_LABEL);
        lbl.setForeground(AppTheme.TEXT_SECONDARY);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));
        return lbl;
    }

    private JScrollPane buildYaklasanTablo() {
        String[] kolonlar = {"Saat", "Musteri", "Calisan", "Hizmet", "Fiyat", "Durum"};
        yaklasanModel = new DefaultTableModel(kolonlar, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tablo = new JTable(yaklasanModel);
        TableHelper.stilUygula(tablo);

        int[] widths = {70, 160, 130, 120, 90, 110};
        for (int i = 0; i < widths.length; i++)
            tablo.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(tablo);
        scroll.setBackground(AppTheme.SURFACE);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER, 1, true));
        scroll.getViewport().setBackground(AppTheme.SURFACE);
        scroll.setPreferredSize(new Dimension(0, 220));
        return scroll;
    }

    private void veriYukle() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            int bugunRandevu, toplamMusteri, tamamlanan;
            double ciro;
            List<Randevu> bugunListe;

            @Override
            protected Void doInBackground() throws Exception {
                bugunRandevu  = randevuService.bugunRandevuSayisi();
                toplamMusteri = musteriService.toplamSayisi();
                tamamlanan    = randevuService.bugunTamamlananSayisi();
                ciro          = randevuService.haftaninCirosu();
                bugunListe    = randevuService.bugunRandevulari();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    kartBugunRandevu.setDeger(String.valueOf(bugunRandevu));
                    kartToplamMusteri.setDeger(String.valueOf(toplamMusteri));
                    kartTamamlanan.setDeger(String.valueOf(tamamlanan));
                    kartHaftaCiro.setDeger(String.format("%.0f TL", ciro));

                    yaklasanModel.setRowCount(0);
                    for (Randevu r : bugunListe) {
                        yaklasanModel.addRow(new Object[]{
                            DateUtil.formatSaat(r.getRandevuTarihi()),
                            r.getMusteriAdSoyad(),
                            r.getCalisanAdSoyad(),
                            r.getHizmetIsim(),
                            String.format("%.2f TL", r.getHizmetFiyat()),
                            r.getDurum().getGoruntu()
                        });
                    }
                } catch (Exception ignored) {}
            }
        };
        worker.execute();
    }
}
