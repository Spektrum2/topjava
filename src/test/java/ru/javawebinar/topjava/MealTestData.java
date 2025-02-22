package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfDayOrMin;
import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfNextDayOrMax;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int MEAL_ID_USER = START_SEQ + 3;
    public static final int MEAL_ID_ADMIN = START_SEQ + 10;
    public static final int NOT_FOUND = 10;

    public static final LocalDate START_DATE = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate END_DATE = LocalDate.of(2020, Month.JANUARY, 31);
    public static final LocalDate START_DATE_ADMIN = LocalDate.of(2020, Month.JANUARY, 28);


    public static final Meal MEAL_USER = new Meal(MEAL_ID_USER, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_ADMIN = new Meal(MEAL_ID_ADMIN, LocalDateTime.of(2020, Month.JANUARY, 29, 10, 0), "Завтрак - Омлет", 500);

    public static final List<Meal> MEALS_USER = Arrays.asList(
            new Meal(100003, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(100005, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(100006, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(100007, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(100008, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(100009, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    public static final List<Meal> MEALS_ADMIN = Arrays.asList(
            new Meal(100010, LocalDateTime.of(2020, Month.JANUARY, 29, 10, 0), "Завтрак - Омлет", 500),
            new Meal(100011, LocalDateTime.of(2020, Month.JANUARY, 29, 13, 0), "Обед - Борщ", 500),
            new Meal(100012, LocalDateTime.of(2020, Month.JANUARY, 29, 20, 0), "Ужин - Пицца", 1000),
            new Meal(100013, LocalDateTime.of(2020, Month.JANUARY, 28, 10, 0), "Завтрак - Яичница", 600),
            new Meal(100014, LocalDateTime.of(2020, Month.JANUARY, 28, 13, 0), "Обед - Окрошка", 500),
            new Meal(100015, LocalDateTime.of(2020, Month.JANUARY, 28, 20, 0), "Ужин - Паста", 1000)
    );

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2024, Month.MARCH, 13, 15, 0), "lunch", 300);
    }

    public static Meal getUpdated(int userId) {
        Meal updated = userId == USER_ID ? new Meal(MEAL_USER) : new Meal(MEAL_ADMIN);
        updated.setDateTime(LocalDateTime.of(2024, Month.APRIL, 5, 6, 0));
        updated.setDescription("dinner");
        updated.setCalories(100);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static List<Meal> getMeals(int userId) {
        return filter(meal -> true, userId);
    }

    public static List<Meal> getBetweenInclusive(LocalDate startDate,
                                                 LocalDate endDate, int userId) {

        return filter(meal -> Util.isBetweenHalfOpen(meal.getDateTime(), atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate)), userId);
    }

    public static List<Meal> filter(Predicate<Meal> filter, int userId) {
        if (userId == USER_ID) {
            return MEALS_USER.stream()
                    .filter(filter)
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        } else {
            return MEALS_ADMIN.stream()
                    .filter(filter)
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }

    }
}
