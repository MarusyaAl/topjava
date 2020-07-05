package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private MealService service;

    public List<Meal> getAll() {
        return service.getAll();
    }


    public Meal get(int id) {
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        return service.create(meal, authUserId());
    }


    public void delete(int id) {
        service.delete(id, authUserId());
    }


    public void update(Meal meal) {
        service.update(meal, authUserId());
    }


    public List<Meal> getFilteredByDate(LocalDate starDate, LocalDateTime startTime, LocalDate endDate, LocalDateTime endTime) {
        return service.getFilteredByDate(starDate, startTime, endDate, endTime);
    }
}