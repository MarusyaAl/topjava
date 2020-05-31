package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    private static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<UserMealWithExcess>();
        List<UserMeal> userMealBetweenHours = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();

        for (UserMeal meal : meals) {
            Integer newCalories = 0;
            LocalDate date = meal.getDateTime().toLocalDate();
            if (caloriesPerDayMap.containsKey(date)) {
                newCalories = caloriesPerDayMap.get(meal.getDateTime().toLocalDate());
            }
            caloriesPerDayMap.put(date, newCalories + meal.getCalories());

            LocalTime localDateTime = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(localDateTime, startTime, endTime)) {
                userMealBetweenHours.add(meal);
            }
        }

        for (UserMeal meal : userMealBetweenHours) {
            boolean excees = findExcees(meal, caloriesPerDayMap, caloriesPerDay);
            userMealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excees));
        }
        return userMealWithExcesses;
    }

    private static boolean findExcees(UserMeal meal, Map<LocalDate, Integer> caloriesPerDayMap, int caloriesPerDay) {
        Integer calories = caloriesPerDayMap.get(meal.getDateTime().toLocalDate());
        return calories > caloriesPerDay;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> totalCaloriesPerDay = meals.stream().collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(x -> TimeUtil.isBetweenHalfOpen(x.getDateTime().toLocalTime(), startTime, endTime))
                .map((x) -> new UserMealWithExcess(x.getDateTime(), x.getDescription(), x.getCalories(), totalCaloriesPerDay.get(x.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
