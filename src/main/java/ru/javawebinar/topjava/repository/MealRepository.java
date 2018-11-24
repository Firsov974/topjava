package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal, int userId);

    // false если еда чужая
    boolean delete(int id, int userId);

    // null если еда чужая
    Meal get(int id, int userId);

    List<Meal> getAll(int userId);

    List<Meal> getBetweenDates(LocalDateTime startDate, LocalDateTime endDate, int userId);

}
