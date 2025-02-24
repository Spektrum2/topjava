package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID = START_SEQ + 3;
    public static final int ADMIN_MEAL_ID = START_SEQ + 11;
    public static final int NOT_FOUND = 10;

    public static final LocalDate TO_DATE = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate START_DATE = LocalDate.of(2020, Month.JANUARY, 31);
    public static final LocalDate END_DATE = LocalDate.of(2020, Month.FEBRUARY, 1);
    public static final LocalDate ADMIN_START_DATE = LocalDate.of(2020, Month.JANUARY, 29);
    public static final LocalDate ADMIN_END_DATE = LocalDate.of(2020, Month.JANUARY, 30);

    public static final Meal userMeal = new Meal(USER_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal userMeal1 = new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal userMeal2 = new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal userMeal3 = new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal userMeal4 = new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal userMeal5 = new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal userMeal6 = new Meal(START_SEQ + 9, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    public static final Meal userMeal7 = new Meal(START_SEQ + 10, LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 0), "Завтрак", 500);

    public static final Meal adminMeal = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 28, 10, 0), "Завтрак - Яичница", 600);
    public static final Meal adminMeal1 = new Meal(START_SEQ + 12, LocalDateTime.of(2020, Month.JANUARY, 28, 13, 0), "Обед - Окрошка", 500);
    public static final Meal adminMeal2 = new Meal(START_SEQ + 13, LocalDateTime.of(2020, Month.JANUARY, 28, 20, 0), "Ужин - Паста", 1000);
    public static final Meal adminMeal3 = new Meal(START_SEQ + 14, LocalDateTime.of(2020, Month.JANUARY, 29, 10, 0), "Завтрак - Омлет", 500);
    public static final Meal adminMeal4 = new Meal(START_SEQ + 15, LocalDateTime.of(2020, Month.JANUARY, 29, 13, 0), "Обед - Борщ", 500);
    public static final Meal adminMeal5 = new Meal(START_SEQ + 16, LocalDateTime.of(2020, Month.JANUARY, 29, 20, 0), "Ужин - Пицца", 1000);
    public static final Meal adminMeal6 = new Meal(START_SEQ + 17, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин - Пицца", 1000);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2024, Month.MARCH, 13, 15, 0), "lunch", 300);
    }

    public static Meal getUpdated(int userId) {
        Meal updated = userId == USER_ID ? new Meal(userMeal) : new Meal(adminMeal);
        updated.setDateTime(LocalDateTime.of(2024, Month.APRIL, 5, 6, 0));
        updated.setDescription("dinner");
        updated.setCalories(100);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}
