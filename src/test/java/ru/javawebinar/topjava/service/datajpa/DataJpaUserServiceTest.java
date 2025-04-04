package ru.javawebinar.topjava.service.datajpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    void getWithMeals() {
        User actual = service.getWithMeals(ADMIN_ID);
        USER_WITH_MEALS_MATCHER.assertMatch(actual, admin);
    }

    @Test
    void getWithMealsNotFound() {
        Assertions.assertThrows(NotFoundException.class,
                () -> service.getWithMeals(NOT_FOUND));
    }

    @Test
    void enable() {
        service.enable(USER_ID, false);
        User userEnabled = new User(user);
        userEnabled.setEnabled(false);
        USER_MATCHER.assertMatch(service.get(USER_ID), userEnabled);

    }
}