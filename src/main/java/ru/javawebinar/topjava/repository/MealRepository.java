package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {

    List<Meal> findAll();

    Meal findById(int id);

    Meal create(Meal meal);

    Meal update(Meal updateMeal);

    void deleteById(int id);

}
