package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.javawebinar.topjava.util.MealsUtil.CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class JavaMealRepository implements MealRepository {
    private final List<Meal> meals = new CopyOnWriteArrayList<>(Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    ));

    @Override
    public List<MealTo> findAll() {
        return filteredByStreams(new ArrayList<>(meals), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
    }

    @Override
    public Optional<Meal> findById(long id) {
        return meals.stream().filter(m -> m.getId() == id).findFirst();
    }

    @Override
    public void save(Meal meal) {
        meals.add(meal);
    }

    @Override
    public void update(Meal updateMeal) {
        findById(updateMeal.getId()).ifPresent(m -> {
            m.setDateTime(updateMeal.getDateTime());
            m.setDescription(updateMeal.getDescription());
            m.setCalories(updateMeal.getCalories());
        });
    }

    @Override
    public void deleteById(long id) {
        meals.removeIf(m -> m.getId() == id);
    }

    @Override
    public void delete(Meal meal) {
        meals.remove(meal);
    }
}
