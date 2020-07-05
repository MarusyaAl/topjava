package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealDateTimeComparator;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        for (Meal MEAL : MealsUtil.MEALS) {
            save(MEAL, SecurityUtil.authUserId());
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (checkUser(meal.getId())) {
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (checkUser(id)) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (checkUser(id))
            return repository.get(id);
        else return null;
    }

    @Override
    public List<Meal> getAll() {
        if (repository == null) {
            throw new NullPointerException("repository is empty");
        }
        List<Meal> result = (List<Meal>) repository.values();
        result.sort(new MealDateTimeComparator());
        return result;
    }

    @Override
    public List<Meal> getFilteredByDate(LocalDate starDate, LocalDateTime startTime, LocalDate endDate, LocalDateTime endTime) {
        List<Meal> result = new ArrayList<>();
        for (Meal meal : repository.values()) {
            if (meal.getDateTime().isAfter(startTime) && meal.getDateTime().isBefore(endTime) && meal.getDate().isAfter(starDate) && meal.getDate().isBefore(endDate)) {
                result.add(meal);
            }
        }
        return result;
    }

    private boolean checkUser(int id) {
        return SecurityUtil.authUserId() == repository.get(id).getUserId() && id != 0;
    }
}

