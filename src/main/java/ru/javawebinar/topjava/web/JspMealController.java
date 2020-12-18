package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@Controller
public class JspMealController {
    @Autowired
    private MealService service;

    @GetMapping("/meals")
    public String getMeals(Model model) {
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), caloriesPerDay));
        return "meals";
    }

    @GetMapping("/meals/delete")
    public String deleteMeal(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int mealId = Integer.parseInt(request.getParameter("id"));
        service.delete(mealId, userId);
        return "redirect:/meals";
    }

    @GetMapping("/meals/update")
    public String updateMeal(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        int mealId = Integer.parseInt(request.getParameter("id"));
        Meal meal = service.get(mealId, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("meals/editMeal")
    public String editMeal(HttpServletRequest request) {
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
    public String createMeal(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("meals/filter")
    public String filter(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        service.getBetweenInclusive(startDate, endDate, userId);
        model.addAttribute("meals",  service.getBetweenInclusive(startDate, endDate, userId));
        return "meals";
    }
}
