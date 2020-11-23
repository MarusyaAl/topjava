package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.getId() == null) {
            User userOfMeal = crudUserRepository.getOne(userId);
            meal.setUser(userOfMeal);

            return crudRepository.save(meal);
        } else if (crudRepository.findById(meal.id(), userId) == null) {
            return null;
        }
       Meal mealFromBase = crudRepository.findById(meal.id(), userId);
        mealFromBase.setDescription(meal.getDescription());
        mealFromBase.setCalories(meal.getCalories());
        mealFromBase.setDateTime(meal.getDateTime());
        return crudRepository.save(mealFromBase);

     //   return crudRepository.update(meal.getDescription(), meal.getCalories(), meal.getDateTime(), meal.id());

    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findById(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
