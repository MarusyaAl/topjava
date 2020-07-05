package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Comparator;

public class MealDateTimeComparator implements Comparator<Meal> {
    @Override
    public int compare(Meal m1, Meal m2) {
        if (m1.getDateTime().equals(m2.getDateTime())) {
            return 0;
        }
        if (m1.getDateTime().isBefore(m2.getDateTime())) {
            return 1;
        } else {
            return -1;
        }
    }
}
