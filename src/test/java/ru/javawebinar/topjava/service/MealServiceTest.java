package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.jdbc.JdbcMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.USER_ID;
import static ru.javawebinar.topjava.MealTestData.assertMatch;

import static ru.javawebinar.topjava.MealTestData.getNew;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Autowired
    private JdbcMealRepository jdbcMealRepository;

    @Test
    public void create() throws Exception {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MEAL_ID_4, USER_ID);
        assertMatch(meal, MEAL_4);
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL_ID_4, USER_ID);
        assertNull(repository.get(MEAL_ID_4, USER_ID));
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> all = service.getAll(USER_ID);
        MealTestData.assertMatch(all, MEAL_4, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID_3, USER_ID), updated);
    }

    @Test
    public void deleteAnotherUserMeal() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_4, ANOTHER_USER_ID));
    }

    @Test
    public void getAnotherUserMeal() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_4, ANOTHER_USER_ID));
    }

    @Test
    public void updateAnotherUserMeal() throws Exception {
        Meal updated = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ANOTHER_USER_ID));
    }

}