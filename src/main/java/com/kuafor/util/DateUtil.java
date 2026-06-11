package com.kuafor.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    public static final DateTimeFormatter FMT_TARIH_SAAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static final DateTimeFormatter FMT_TARIH      = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter FMT_SAAT       = DateTimeFormatter.ofPattern("HH:mm");

    private DateUtil() {}

    public static String formatTarihSaat(LocalDateTime dt) {
        return dt != null ? dt.format(FMT_TARIH_SAAT) : "";
    }

    public static String formatTarih(LocalDate d) {
        return d != null ? d.format(FMT_TARIH) : "";
    }

    public static String formatSaat(LocalDateTime dt) {
        return dt != null ? dt.format(FMT_SAAT) : "";
    }
}
