package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        for (Meal meal : MealsUtil.MEALS) {
            save(meal, 1);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (checkUser(meal.getId(), userId)) {
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (checkUser(id, userId)) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (checkUser(id, userId))
            return repository.get(id);
        else return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Comparator<Meal> dateTimeComparator =
                Comparator.comparing(Meal::getDateTime);
        return filterByPredicate(userId, meal -> true, dateTimeComparator);
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter, Comparator<Meal> dateTimeComparator) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(filter)
                .sorted(dateTimeComparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFilteredByDate(LocalDate starDate, LocalDate endDate, int userId) {
        Comparator<Meal> dateTimeComparator =
                Comparator.comparing(Meal::getDateTime).reversed();
        return filterByPredicate(userId, meal -> DateTimeUtil.isBetweenDates(meal.getDate(), starDate, endDate), dateTimeComparator);
    }

    private boolean checkUser(int mealId, int userId) {
        if (repository.get(mealId) == null) {
            return false;
        }
        return userId == repository.get(mealId).getUserId();
    }
}

