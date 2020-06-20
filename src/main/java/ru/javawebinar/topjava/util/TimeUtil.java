package ru.javawebinar.topjava.util;

import java.time.LocalTime;

class TimeUtil {
    static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }
}
