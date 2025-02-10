package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepository implements Repository {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final ConcurrentMap<Integer, Meal> meals = new ConcurrentHashMap<>();

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal findById(Integer id) {
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
        Meal meal = findById(updateMeal.getId());
        meal.setDateTime(updateMeal.getDateTime());
        meal.setDescription(updateMeal.getDescription());
        meal.setCalories(updateMeal.getCalories());
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal deleteById(Integer id) {
        return meals.remove(id);
    }
}
