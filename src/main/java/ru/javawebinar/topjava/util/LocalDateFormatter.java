package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {


    @Override
    public LocalDate parse(
            String text, Locale locale) throws ParseException {
        if (text.isEmpty()) {
            return null;
        }
        Locale localeRu = new Locale("ru");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(localeRu);
        return LocalDate.parse(text, formatter);

    }

    @Override
    public String print(LocalDate date, Locale locale) {
        if (date == null) {
            return "";
        }
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
}
