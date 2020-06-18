package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealDao {

    Meal save(Meal meal);

    void delete(int id);

    Meal getMeal(int id);

    List<MealTo> getAll();

}
