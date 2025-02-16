package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> save(m, m.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }

        AtomicBoolean result = new AtomicBoolean(false);
        mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (oldMeal.getUserId().equals(userId)) {
                result.set(true);
                return meal;
            } else {
                return oldMeal;
            }
        });
        return result.get() ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        AtomicBoolean result = new AtomicBoolean(false);
        return mealsMap.computeIfPresent(id, (mealId, meal) -> {
            if (meal.getUserId() == userId) {
                result.set(true);
                return null;
            } else {
                return meal;
            }
        }) == null && result.get();
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = mealsMap.get(id);
        return (meal != null && meal.getUserId() == userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealsMap.values().stream()
                .filter(m -> m.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return mealsMap.values().stream()
                .filter(meal -> meal.getUserId().equals(userId)
                        && isBetweenHalfOpen(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

