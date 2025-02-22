package ru.javawebinar.topjava.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest extends TestCase {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID_USER, USER_ID);
        service.delete(MEAL_ID_ADMIN, ADMIN_ID);

        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_USER, USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_ADMIN, ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_USER, NOT_FOUND));
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, NOT_FOUND));
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_USER, ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_ADMIN, USER_ID));
    }

    @Test
    public void get() {
        Meal mealUser = service.get(MEAL_ID_USER, USER_ID);
        Meal mealAdmin = service.get(MEAL_ID_ADMIN, ADMIN_ID);

        assertMatch(mealUser, MealTestData.MEAL_USER);
        assertMatch(mealAdmin, MealTestData.MEAL_ADMIN);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_USER, NOT_FOUND));
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, NOT_FOUND));
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_USER, ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_ADMIN, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actual = service.getBetweenInclusive(START_DATE, END_DATE, USER_ID);
        List<Meal> actualStarDate = service.getBetweenInclusive(START_DATE, null, USER_ID);
        List<Meal> actualEndDate = service.getBetweenInclusive(null, END_DATE, USER_ID);
        List<Meal> actualStartDateAdmin = service.getBetweenInclusive(START_DATE_ADMIN, null, ADMIN_ID);
        List<Meal> expected = MealTestData.getBetweenInclusive(START_DATE, END_DATE, USER_ID);
        List<Meal> expectedStarDate = MealTestData.getBetweenInclusive(START_DATE, null, USER_ID);
        List<Meal> expectedEndDate = MealTestData.getBetweenInclusive(null, END_DATE, USER_ID);
        List<Meal> expectedStartDateAdmin = MealTestData.getBetweenInclusive(START_DATE_ADMIN, null, ADMIN_ID);

        assertMatch(actual, expected);
        assertMatch(actualStarDate, expectedStarDate);
        assertMatch(actualEndDate, expectedEndDate);
        assertMatch(actualStartDateAdmin, expectedStartDateAdmin);
    }

    @Test
    public void update() {
        Meal updateUser = getUpdated(USER_ID);
        Meal updateAdmin = getUpdated(ADMIN_ID);
        service.update(updateUser, USER_ID);
        service.update(updateAdmin, ADMIN_ID);

        assertMatch(service.get(MEAL_ID_USER, USER_ID), getUpdated(USER_ID));
        assertMatch(service.get(MEAL_ID_ADMIN, ADMIN_ID), getUpdated(ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> allUser = service.getAll(USER_ID);
        List<Meal> allAdmin = service.getAll(ADMIN_ID);

        assertMatch(allUser, getMeals(USER_ID));
        assertMatch(allAdmin, getMeals(ADMIN_ID));
    }

}