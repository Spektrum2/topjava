package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Repository {
    List<Meal> findAll();

    Meal findById(Integer id);

    Meal create(Meal meal);

    Meal update(Meal updateMeal);

    Meal deleteById(Integer id);
}
