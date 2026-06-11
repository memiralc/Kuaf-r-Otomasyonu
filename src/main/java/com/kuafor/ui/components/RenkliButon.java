package com.kuafor.ui.components;

import com.kuafor.ui.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RenkliButon extends JButton {

    private final Color normalRenk;
    private final Color hoverRenk;

    public RenkliButon(String metin, Color renk) {
        super(metin);
        this.normalRenk = renk;
        this.hoverRenk = renk.brighter();

        setFont(AppTheme.FONT_BUTTON);
        setBackground(normalRenk);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(150, 38));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverRenk);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalRenk);
            }
        });
    }
}
