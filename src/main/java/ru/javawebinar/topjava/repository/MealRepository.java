package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Meal get(int id, int userid);

    List<Meal> getAll();

    List<Meal> getFilteredByDate(LocalDate starDate, LocalDateTime startTime, LocalDate endDate, LocalDateTime endTim);

}
