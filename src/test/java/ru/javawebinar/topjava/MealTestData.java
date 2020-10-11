package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;


import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int USER_ID = UserTestData.getUserId();
    public static final int ANOTHER_USER_ID = 100009;
    public static final int MEAL_ID_4 = 100005;
    public static final int MEAL_ID_1 = 100002;
    public static final int MEAL_ID_2 = 100003;
    public static final int MEAL_ID_3 = 100004;
    public static final int MEAL_ID_5 = 100006;
    public static final int MEAL_ID_6 = 100007;
    public static final int MEAL_ID_7 = 100008;

    public static final Meal MEAL_1 = new Meal(MEAL_ID_2, LocalDateTime.of(2020, 9, 8, 11, 21, 33), "Спагетти", 200);
    public static final Meal MEAL_2 = new Meal(MEAL_ID_3, LocalDateTime.of(2020, 9, 8, 12, 22, 33), "Тольятелле", 600);
    public static final Meal MEAL_3 = new Meal(MEAL_ID_4, LocalDateTime.of(2020, 9, 8, 13, 25, 33), "Тортик", 950);
    public static final Meal MEAL_4 = new Meal(MEAL_ID_5, LocalDateTime.of(2020, 9, 8, 14, 29, 33), "Гречка", 250);
    public static final Meal MEAL_5 = new Meal(MEAL_ID_6, LocalDateTime.of(2020, 1, 30, 11, 10, 0), "Банан", 235);
    public static final Meal MEAL_6 = new Meal(MEAL_ID_6, LocalDateTime.of(2020, 4, 18, 12, 0, 0), "Макароны с котлетой", 420);
    public static final Meal MEAL_7 = new Meal(MEAL_ID_7, LocalDateTime.of(2020, 6, 8, 13, 35, 0), "Сосиски с горошком", 380);


    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2021, 1, 1, 11, 15, 15), "Каша пшенная", 500);
    }

    public static Meal getUpdated() {
        Meal updated = MEAL_1;
        updated.setCalories(7000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
        //   assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    private static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual);
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
