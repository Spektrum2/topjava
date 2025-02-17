package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;


@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        checkNotFound((repository.delete(id, userId)), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFound(repository.get(id, userId), id);
    }

    public List<MealTo> getAll(int userId, int authUserCaloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), authUserCaloriesPerDay);
    }

    public Meal update(Meal meal, int userId) {
        return checkNotFound(repository.save(meal, userId), meal.getId());
    }

    public List<MealTo> getFiltered(LocalDate startDate,
                                    LocalDate endDate,
                                    LocalTime startTime,
                                    LocalTime endTime,
                                    int authUserCaloriesPerDay,
                                    int userId) {
        return getFilteredTos(repository.getFilteredByDate(userId, startDate, endDate), authUserCaloriesPerDay, startTime, endTime);
    }


}