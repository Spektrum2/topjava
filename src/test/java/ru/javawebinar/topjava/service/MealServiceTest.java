package ru.javawebinar.topjava.service;

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

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

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
                service.create(new Meal(userMeal.getDateTime(), "Завтрак", 500), USER_ID));
    }

    @Test
    public void deleteUserMeal() {
        service.delete(USER_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteAdminMeal() {
        service.delete(ADMIN_MEAL_ID, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID, ADMIN_ID));
    }


    @Test
    public void deleteMealNotFoundForUser() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteMealNotFoundForAdmin() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void deleteMealByDifferentUser() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void deleteMealByDifferentAdmin() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void getUserMeal() {
        Meal mealUser = service.get(USER_MEAL_ID, USER_ID);
        assertMatch(mealUser, MealTestData.userMeal);
    }

    @Test
    public void getAdminMeal() {
        Meal mealAdmin = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(mealAdmin, MealTestData.adminMeal);
    }


    @Test
    public void getMealNotFoundForUser() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getMealNotFoundForAdmin() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getMealByDifferentUser() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getMealByDifferentAdmin() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusiveForUser() {
        List<Meal> actual = service.getBetweenInclusive(START_DATE, END_DATE, USER_ID);
        assertMatch(actual, userMeal7, userMeal6, userMeal5, userMeal4, userMeal3);
    }

    @Test
    public void getBetweenInclusiveForAdmin() {
        List<Meal> actual = service.getBetweenInclusive(ADMIN_START_DATE, ADMIN_END_DATE, ADMIN_ID);
        assertMatch(actual, adminMeal6, adminMeal5, adminMeal4, adminMeal3);
    }

    @Test
    public void getBetweenInclusiveWithEndDateOnlyForUser() {
        List<Meal> actual = service.getBetweenInclusive(null, TO_DATE, USER_ID);
        assertMatch(actual, userMeal2, userMeal1, userMeal);
    }

    @Test
    public void getBetweenInclusiveWithStartDateOnlyForAdmin() {
        List<Meal> actual = service.getBetweenInclusive(ADMIN_END_DATE, null, ADMIN_ID);
        assertMatch(actual, adminMeal6);
    }

    @Test
    public void updateUserMeal() {
        Meal updateUser = getUpdated(USER_ID);
        service.update(updateUser, USER_ID);
        assertMatch(service.get(USER_MEAL_ID, USER_ID), getUpdated(USER_ID));
    }

    @Test
    public void updateAdminMeal() {
        Meal updateAdmin = getUpdated(ADMIN_ID);
        service.update(updateAdmin, ADMIN_ID);
        assertMatch(service.get(ADMIN_MEAL_ID, ADMIN_ID), getUpdated(ADMIN_ID));
    }

    @Test
    public void updateMealNotFoundForUser() {
        Meal updateUser = getUpdated(USER_ID);
        assertThrows(NotFoundException.class, () -> service.update(updateUser, ADMIN_ID));
    }

    @Test
    public void updateMealNotFoundForAdmin() {
        Meal updateAdmin = getUpdated(ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.update(updateAdmin, USER_ID));
    }

    @Test
    public void getAllUserMeals() {
        List<Meal> allUser = service.getAll(USER_ID);
        assertMatch(allUser, userMeal7, userMeal6, userMeal5, userMeal4, userMeal3, userMeal2, userMeal1, userMeal);
    }

    @Test
    public void getAllAdminMeals() {
        List<Meal> allAdmin = service.getAll(ADMIN_ID);
        assertMatch(allAdmin, adminMeal6, adminMeal5, adminMeal4, adminMeal3, adminMeal2, adminMeal1, adminMeal);
    }

}