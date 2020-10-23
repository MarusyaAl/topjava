package ru.javawebinar.topjava.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(ref);
            em.persist(meal);
            return meal;
        } else {
            int idMeal = meal.getId();
            Meal mealFromDB = em.getReference(Meal.class, idMeal);
            if (mealFromDB.getUser().getId() == userId) {
                meal.setUser(ref);
                return em.merge(meal);
            }
            throw new NotFoundException("user is not the own of the meal");
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        boolean deletedTrueOrFalse = em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
        return deletedTrueOrFalse;
      /*
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;*/
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        int idThisUser;
        try {
            idThisUser = meal.getUser().getId();
        } catch (Exception e) {
            throw new NotFoundException("meal with userId not was found");
        }
        if (userId == idThisUser) {
            return meal;
        }
        throw new NotFoundException("meal with userId not was found");
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> userMealList = new ArrayList<>();
        List<Meal> resultList = em.createNamedQuery(Meal.ALL_SORTED, Meal.class).getResultList();
        for (Meal meal : resultList) {
            if (meal.getUser().getId().equals(userId)) {
                userMealList.add(meal);
            }
        }
        return userMealList;
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BW_HALF_OPEN, Meal.class)
                .setParameter("start_date_time", startDateTime)
                .setParameter("end_date_time", endDateTime)
                .setParameter("user_id", userId)
                .getResultList();
    }
}