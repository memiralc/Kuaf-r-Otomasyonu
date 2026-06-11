package com.kuafor.util;

import javax.swing.*;
import java.awt.*;

public final class MessageUtil {

    private MessageUtil() {}

    public static void basariGoster(Component parent, String mesaj) {
        JOptionPane.showMessageDialog(parent, mesaj, "Başarılı", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void uyariGoster(Component parent, String mesaj) {
        JOptionPane.showMessageDialog(parent, mesaj, "Uyarı", JOptionPane.WARNING_MESSAGE);
    }

    public static void hataGoster(Component parent, String mesaj) {
        JOptionPane.showMessageDialog(parent, mesaj, "Hata", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean onayAl(Component parent, String mesaj) {
        int sonuc = JOptionPane.showConfirmDialog(parent, mesaj, "Onay",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return sonuc == JOptionPane.YES_OPTION;
    }
}
