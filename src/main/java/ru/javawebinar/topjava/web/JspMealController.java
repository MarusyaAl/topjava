package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@Controller
public class JspMealController {

    @Autowired
    private MealService service;

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @GetMapping("/meals")
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        log.info("getAll for user {}", userId);
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), caloriesPerDay));
        return "meals";
    }

    @GetMapping("/meals/delete")
    public String delete(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int mealId = Integer.parseInt(request.getParameter("id"));
        log.info("delete meal {} for user {}", mealId, userId);
        service.delete(mealId, userId);
        return "redirect:/meals";
    }

    @GetMapping("/meals/update")
    public String update(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        int mealId = Integer.parseInt(request.getParameter("id"));
        Meal meal = service.get(mealId, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("meals/editMeal")
    public String edit(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int mealId;
        Meal meal;
        String mealIdString = request.getParameter("id");
        if (!mealIdString.equals("")) {
            mealId = Integer.parseInt(mealIdString);
            meal = service.get(mealId, userId);
        } else {
            meal = new Meal();
        }
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        meal.setDescription(description);
        meal.setCalories(calories);
        meal.setDateTime(localDateTime);
        service.update(meal, userId);
        return "redirect:/meals";
    }

    @GetMapping("meals/add")
    public String create(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("meals/getBetween")
    public String filter(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        service.getBetweenInclusive(startDate, endDate, userId);

        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);

        model.addAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }
}
