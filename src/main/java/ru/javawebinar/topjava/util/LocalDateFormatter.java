package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String PATTERN = "yyyy-MM-dd";

    @Override
    public LocalDate parse(
            String text, Locale locale) throws ParseException {
        if (text.isEmpty()) {
            return null;
        }
        //    Locale localeRu = new Locale("ru");
        //    formatter = formatter.withLocale(localeRu);
        return LocalDate.parse(text, FORMATTER);

    }

    @Override
    public String print(LocalDate date, Locale locale) {

        DateFormat df = new SimpleDateFormat(PATTERN);
        return df.format(date);
    }
}
