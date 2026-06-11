package com.kuafor.util;

public final class ValidationUtil {

    private ValidationUtil() {}

    public static boolean bos(String deger) {
        return deger == null || deger.isBlank();
    }

    public static boolean gecerliTelefon(String telefon) {
        return telefon != null && telefon.matches("^[0-9 \\-+()]{7,15}$");
    }

    public static boolean gecerliEmail(String email) {
        return email != null && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean pozitifSayi(double sayi) {
        return sayi > 0;
    }
}
