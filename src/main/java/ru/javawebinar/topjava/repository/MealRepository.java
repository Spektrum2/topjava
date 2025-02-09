package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;
import java.util.Optional;

public interface MealRepository {
    List<MealTo> findAll();

    Optional<Meal> findById(long id);

    void save(Meal meal);

    void update(Meal updateMeal);

    void deleteById(long id);

    void delete(Meal meal);
}
