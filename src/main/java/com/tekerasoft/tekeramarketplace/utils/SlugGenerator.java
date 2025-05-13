package com.tekerasoft.tekeramarketplace.utils;

import java.util.Locale;
import java.util.Random;

public class SlugGenerator {
    public static String generateSlug(String input) {
        String slug = input.toLowerCase(Locale.forLanguageTag("en"));

        // Türkçe karakterleri dönüştürme
        slug = slug
                .replace("ç", "c")
                .replace("ğ", "g")
                .replace("ı", "i")
                .replace("ö", "o")
                .replace("ş", "s")
                .replace("ü", "u")
                .replace("Ç", "c")
                .replace("Ğ", "g")
                .replace("I", "i")
                .replace("Ö", "o")
                .replace("Ş", "s")
                .replace("Ü", "u");

        slug = slug.replaceAll("[^a-z0-9\\s-]", "");

        slug = slug.replaceAll("\\s+", "-");

        slug = slug.replaceAll("-+", "-");

        slug = slug.trim();

        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000); // 10000 ile 99999 arasında rastgele sayı
        slug += "-" + randomNumber;

        return slug;
    }
}
