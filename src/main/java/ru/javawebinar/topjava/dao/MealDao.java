package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

public class MealDao {
    public void addMeal(MealTo meal) {

    }

    public void deleteMeal(int id) {
        MealsUtil.getMealsTo().remove(getMealById(id));

    }

    public void updateMeal(MealTo meal) {

    }

    public MealTo getMealById(int id) {

        return null;
    }

    public List<MealTo> getAllMeals() {
        return MealsUtil.getMealsTo();
    }
}
