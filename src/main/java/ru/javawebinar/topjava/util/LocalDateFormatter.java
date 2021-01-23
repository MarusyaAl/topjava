package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<Date> {
    private String pattern;

    public LocalDateFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String print(Date localDate, Locale locale) {
        if (localDate == null){
            return "";
        }
        return getDateFormat(locale).format(localDate);
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0){
            return null;
        }
        return getDateFormat(locale).parse(text);
    }

    protected DateFormat getDateFormat(Locale locale) {
        DateFormat dateFormat = new SimpleDateFormat(this.pattern, locale);
   //     dateFormat.setLenient(false);
        return dateFormat;
    }

}
