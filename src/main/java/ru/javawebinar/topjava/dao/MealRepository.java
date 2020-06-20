package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;


public interface MealRepository {

    Meal save(Meal meal);

    void delete(int id);

    Meal getMeal(int id);

    Collection<Meal> getAll();

}
