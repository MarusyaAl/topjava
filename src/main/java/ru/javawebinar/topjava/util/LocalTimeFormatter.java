package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(
            String text,
            Locale locale) throws ParseException {
        if (text.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(text, formatter);
    }

    @Override
    public String print(
            LocalTime time, Locale locale) {
        if (time==null){
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
}
