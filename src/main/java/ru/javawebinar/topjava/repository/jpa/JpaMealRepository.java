package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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
            Meal mealFromDB = em.find(Meal.class, idMeal);
            if (mealFromDB == null) {
                throw new NotFoundException("this meal is not exist");
            }
            else if (mealFromDB.getUser().getId() == userId) {
                meal.setUser(ref);
                return em.merge(meal);
            }
            throw new NotFoundException("user is not the own of the meal");
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        boolean deleted = em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
        return deleted;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal != null) {
            int idThisUser = meal.getUser().getId();
            if (userId == idThisUser) {
                return meal;
            }
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
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