package com.kuafor.ui.components;

import com.kuafor.ui.theme.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class TableHelper {

    private TableHelper() {}

    public static void stilUygula(JTable table) {
        table.setFont(AppTheme.FONT_TABLE);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(AppTheme.TABLE_SELECTED);
        table.setSelectionForeground(AppTheme.TEXT_PRIMARY);
        table.setBackground(AppTheme.SURFACE);
        table.setForeground(AppTheme.TEXT_PRIMARY);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(AppTheme.FONT_LABEL);
        header.setBackground(AppTheme.TABLE_HEADER_BG);
        header.setForeground(AppTheme.TEXT_SECONDARY);
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        tbl, value, isSelected, hasFocus, row, col);
                lbl.setOpaque(true);
                lbl.setBorder(new EmptyBorder(0, 8, 0, 8));
                lbl.setForeground(AppTheme.TEXT_PRIMARY);
                if (isSelected) {
                    lbl.setBackground(AppTheme.TABLE_SELECTED);
                } else {
                    lbl.setBackground(row % 2 == 0 ? AppTheme.TABLE_ROW_EVEN : AppTheme.TABLE_ROW_ODD);
                }
                return lbl;
            }
        });
    }
}
