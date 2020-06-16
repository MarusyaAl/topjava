package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealDaoCommon {


    Meal save(Meal meal);

    void deleteMeal(int id);

    Meal getMealById(int id);

    List<MealTo> getAllMeals();

}
