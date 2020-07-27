package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Кофе", 500));
            mealRestController.create(new Meal(LocalDateTime.of(2020, Month.APRIL, 12, 17, 0), "Пицца", 1500));
            mealRestController.create(new Meal(LocalDateTime.of(2020, Month.AUGUST, 15, 21, 30), "Пельмени", 1000));
            final List<MealTo> filteredByDate = mealRestController.getFilteredByDate(LocalTime.of(9, 20), LocalTime.of(22, 50),
                    LocalDate.of(2020, 1, 1), LocalDate.of(2020, 3, 25));

            for (MealTo meal : filteredByDate) {
                System.out.println(meal);
            }
        }
    }
}
