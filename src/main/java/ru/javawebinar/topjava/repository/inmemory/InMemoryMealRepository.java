package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        for (Meal MEAL : MealsUtil.MEALS) {
            save(MEAL, SecurityUtil.authUserId());
        }
    }

    public Map<Integer, Meal> getRepository() {
        return repository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (checkUser(userId)) {
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (checkUser(userId)) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (checkUser(userId))
            return repository.get(id);
        else return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        if (repository.isEmpty()) {
            throw new NullPointerException("repository is empty");
        }
        Comparator<Meal> dateTimeComparator
                = Comparator.comparing(Meal::getDateTime);
        List<Meal> result = new ArrayList<>(repository.values());
        result.sort(dateTimeComparator);
        return result;
    }

    @Override
    public List<Meal> getFilteredByDate(LocalDate starDate, LocalDateTime startTime, LocalDate endDate, LocalDateTime endTime) {
        return repository.values().stream()
                .filter(meal -> meal.getDateTime().isAfter(startTime) && meal.getDateTime().isBefore(endTime) && meal.getDate().isAfter(starDate) && meal.getDate().isBefore(endDate))
                .collect(Collectors.toList());
    }

    private boolean checkUser(int mealId) {
        if (repository.get(mealId).getUserId() == null) {
            return false;
        }
        return SecurityUtil.authUserId() == repository.get(mealId).getUserId() && mealId != 0;
    }
}

