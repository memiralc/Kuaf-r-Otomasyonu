package com.kuafor.ui;

import com.kuafor.ui.panels.*;
import com.kuafor.ui.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel   = new JPanel(cardLayout);
    private JButton aktifButon;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Kuafor Otomasyonu v1.0");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 780);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);

        contentPanel.setBackground(AppTheme.BACKGROUND);
        contentPanel.add(new DashboardPanel(),    "dashboard");
        contentPanel.add(new RandevuPanel(),      "randevu");
        contentPanel.add(new RandevuListePanel(), "randevuListe");
        contentPanel.add(new MusteriPanel(),      "musteri");
        contentPanel.add(new CalisanPanel(),      "calisan");
        contentPanel.add(new HizmetPanel(),       "hizmet");
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "dashboard");
    }

    // ────────────────────────────────────────────────────────────
    //  Sidebar
    // ────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        // Dış sarmalayıcı: BorderLayout ile tam kontrol
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(AppTheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER));

        // ── Logo ──────────────────────────────────────────────
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(26, 0, 26, 0));

        JPanel logoDot = new JPanel();
        logoDot.setPreferredSize(new Dimension(5, 30));
        logoDot.setBackground(AppTheme.ACCENT);
        logoPanel.add(logoDot);

        JLabel logo = new JLabel("Kuafor Pro");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(AppTheme.TEXT_PRIMARY);
        logoPanel.add(logo);

        // ── Menü içeriği ──────────────────────────────────────
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        menuPanel.add(makeCatLabel("ANA MENU"));
        JButton btnDashboard  = makeNavBtn("Dashboard",       "dashboard",   menuPanel);
        JButton btnRandevu    = makeNavBtn("Randevu Olustur", "randevu",     menuPanel);
        makeNavBtn("Randevu Listesi", "randevuListe", menuPanel);

        menuPanel.add(Box.createVerticalStrut(4));
        menuPanel.add(makeSep());
        menuPanel.add(makeCatLabel("YONETIM"));

        makeNavBtn("Musteriler", "musteri", menuPanel);
        makeNavBtn("Calisanlar", "calisan", menuPanel);
        makeNavBtn("Hizmetler",  "hizmet",  menuPanel);

        // ── Alt bilgi ─────────────────────────────────────────
        JLabel version = new JLabel("v1.0.0  —  Kuafor Otomasyonu");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        version.setForeground(AppTheme.TEXT_MUTED);
        version.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));
        footer.add(version, BorderLayout.WEST);

        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(makeSep(), BorderLayout.CENTER); // separator altında
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(footer, BorderLayout.SOUTH);

        // Düzeltme: CENTER'a sadece menüyü koy, NORTH'a logo + sep
        sidebar.removeAll();
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(logoPanel, BorderLayout.CENTER);
        top.add(makeSep(), BorderLayout.SOUTH);

        sidebar.add(top, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(footer, BorderLayout.SOUTH);

        aktifButon = btnDashboard;
        setAktif(btnDashboard, true);

        return sidebar;
    }

    private JPanel makeSep() {
        JPanel s = new JPanel();
        s.setBackground(AppTheme.BORDER);
        s.setPreferredSize(new Dimension(0, 1));
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    private JLabel makeCatLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lbl.setForeground(AppTheme.TEXT_MUTED);
        // Sabit yükseklik, tam genişlik
        lbl.setPreferredSize(new Dimension(Short.MAX_VALUE, 32));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        lbl.setAlignmentX(0f);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 20, 4, 20));
        return lbl;
    }

    private JButton makeNavBtn(String label, String cardKey, JPanel parent) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                if (this == aktifButon || getBackground().equals(AppTheme.SIDEBAR_HOVER)) {
                    g2.setColor(AppTheme.ACCENT);
                    g2.fillRect(0, 10, 3, getHeight() - 20);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        // Genişliği BoxLayout'un doldurmasına izin ver
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        btn.setPreferredSize(new Dimension(280, 54));
        btn.setMinimumSize(new Dimension(0, 54));
        btn.setAlignmentX(0f);

        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(AppTheme.TEXT_SECONDARY);
        btn.setBackground(AppTheme.SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setHorizontalTextPosition(SwingConstants.LEFT);
        btn.setIconTextGap(0);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 12));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn != aktifButon) {
                    btn.setBackground(AppTheme.SIDEBAR_HOVER);
                    btn.setForeground(AppTheme.TEXT_PRIMARY);
                    btn.repaint();
                }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (btn != aktifButon) {
                    btn.setBackground(AppTheme.SIDEBAR_BG);
                    btn.setForeground(AppTheme.TEXT_SECONDARY);
                    btn.repaint();
                }
            }
        });

        btn.addActionListener(e -> {
            if (aktifButon != null) setAktif(aktifButon, false);
            aktifButon = btn;
            setAktif(btn, true);
            cardLayout.show(contentPanel, cardKey);
        });

        parent.add(btn);
        return btn;
    }

    private void setAktif(JButton btn, boolean aktif) {
        if (aktif) {
            btn.setBackground(new Color(0x7C, 0x3A, 0xED, 30));
            btn.setForeground(AppTheme.ACCENT_LIGHT);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        } else {
            btn.setBackground(AppTheme.SIDEBAR_BG);
            btn.setForeground(AppTheme.TEXT_SECONDARY);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        }
        btn.repaint();
    }
}
