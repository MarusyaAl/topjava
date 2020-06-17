package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao implements MealDaoCommon {

    private Map<Integer, Meal> mealRepository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm\t");

    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(counter.getAndIncrement());
            mealRepository.put(meal.getId(), meal);
        } else
            mealRepository.put(meal.getId(), meal);
        return meal;
    }

    public void deleteMeal(int id) {
        mealRepository.remove(id);
    }

    public Meal getMealById(int id) {
        return mealRepository.get(id);
    }

    public List<MealTo> getAllMeals() {
        return MealsUtil.filteredByStreams((mealRepository.values()), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
    }
}
