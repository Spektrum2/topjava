package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaMealRepository implements MealRepository {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    public JavaMealRepository() {
        initializeTestData();
    }

    private void initializeTestData() {
        List<Meal> meals = Arrays.asList(
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        meals.forEach(this::create);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal findById(int id) {
        return meals.get(id);
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(counter.incrementAndGet());
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal update(Meal updateMeal) {
        return meals.replace(updateMeal.getId(), updateMeal);
    }

    @Override
    public void deleteById(int id) {
        meals.remove(id);
    }
}
