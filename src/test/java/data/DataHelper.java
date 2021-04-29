package data;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

public class DataHelper {

    private DataHelper() {
    }

    public static String activeCard() {
        return "4444 4444 4444 4441";
    }

    public static String rejectedCard() {
        return "4444 4444 4444 4442";
    }

    public static String invalidCard() {
        return "4444 4444 4444";
    }

    public static String notExistCard() {
        return "1111 1111 1111 1111";
    }

    public static String generateByFakerRandomCVC() {
        Faker faker = new Faker();
        return String.valueOf(faker.number().randomNumber(3, true));
    }

    public static String invalidCVC() {
        return "98";
    }

    public static String generateByFakerName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return new String(
                faker.name().firstName() + " " + faker.name().lastName()
        );
    }

    public static String actualYear() {
        LocalDate date = LocalDate.now();
        int year = date.getYear() - 2000;
        return String.valueOf(year);
    }

    public static String lastYear() {
        LocalDate date = LocalDate.now();
        int lastYear = date.getYear() - 2001;
        return String.valueOf(lastYear);
    }

    public static String nextYear() {
        LocalDate date = LocalDate.now();
        int nextYear = date.getYear() - 1999;
        return String.valueOf(nextYear);
    }

    public static String actualMonth() {
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        return String.format("%02d", month);
    }

    public static String lastMonth() {
        LocalDate date = LocalDate.now();
        int lastMonth = date.getMonthValue() - 1;
        return String.format("%02d", lastMonth);
    }

    public static String randomMonth() {
        Random random = new Random();
        int month = random.nextInt(12) + 1;
        return String.format("%02d", month);
    }

    public static String invalidNullMonth() {
        return "00";
    }

    public static String invalidNotExistMonth() {
        LocalDate date = LocalDate.now();
        int invalidMonth = date.getMonthValue() + 12;
        return String.format("%02d", invalidMonth);
    }
}

